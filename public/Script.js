$(document).ready(function() { // espera a que todo este cargado ( el html)
    let usuarioActual = null;

    const mainHeader = $("#main-header");
    const userInfo = $("#user-info");

    const navAbout = $("#nav-about");
    const navLogin = $("#nav-login");
    const navSignup = $("#nav-signup");
    const navShop = $("#nav-shop");

    const sectionAbout = $("#about");
    const sectionLogin = $("#login");
    const sectionSignup = $("#signup");
    const sectionShop = $("#shop");

    const landing = $("#landing");
    const mainContent = $("#mainContent");

    <!--const ObjetoContainer = $("#armas-container");-->

    function setUpWeb(){
        landing.show();      // Se muestra la landing
        mainContent.hide(); // Se oculta el contenido principal
    }

    function updateNav() {
        if (usuarioActual) {
            $("#nav-signup").hide();
            $("#nav-shop").show(); //QUIERO HACER QUE CERRAR SESSION SE VAYA ARRIBA A LA DERECHA
            $("#nav-login").text("Cerrar sesión");
        } else {
            $("#nav-signup").show();
            $("#nav-shop").hide();
            $("#nav-login").text("Log in");
        }
    }

    function showSection(sectionId) { //Como hacemos una SPA cada vez que cliquemos a uno del nav
        $("#about, #login, #signup, #shop, #olvidadoContra,#terminos-condiciones").hide();//Vamos a tener que hacer hide de los <div>
        $("nav a").removeClass("active");
        $(sectionId).show();
    }

    function mostrarWeb (){
        mainContent.fadeIn(500);
        mainHeader.removeClass("header-portada").addClass("header-principal");
        updateNav();
    }

    $("#landing .overlay").one("click",(function() { // con un clik en el overlay se carga la web
        landing.slideUp(1000, mostrarWeb); //el landing tiene una funcion slideUP que lo que hace es que
    }));//aplica la funcion con un tiempo, que desaparece el contenedor y aplica la funcion.

    $("#nav-about").click(function (e) {
        e.preventDefault();
        showSection("#about");
        $(this).addClass("active");
    });

    $("#nav-login").click(function (e) {
        e.preventDefault();
        if (usuarioActual) {
            // Confirmación de cierre de sesión
            if (confirm("¿Estás seguro que quieres cerrar sesión?")) {
                usuarioActual = null;
                updateNav();
                showSection("#about");
                $("#nav-about").addClass("active");
                $("#user-info").fadeOut();
                alert("Sesión cerrada correctamente");
            }
        } else {
            showSection("#login");
            $(this).addClass("active"); //pinta de amarillo oscuro el nav-login en ele navegador
        }
    });

    $("#nav-signup").click(function (e) {
        e.preventDefault();
        showSection("#signup");
        $(this).addClass("active");
    });


    $("#link-to-signup").click(function (e) {
        e.preventDefault();
        $("#nav-signup").click();
    });

    $("#link-to-login").click(function (e) {
        e.preventDefault();
        $("#nav-login").click();
    });

    $("#Terminos-enlace").click(function(e){
        e.preventDefault();
        showSection("#terminos-condiciones");
    });

    $("#aceptarTerminos").click(function(e){
        e.preventDefault();
        var checkbox = $('#checkboxx');
        checkbox.prop('checked', !checkbox.prop('checked'));
        showSection("#signup");
    });

    $("#rechazarTerminos").click(function(e){
        e.preventDefault();
        showSection("#signup");
    });

    $("#form-login").submit(function (e) { //Si le da al boton submit del <div>
        e.preventDefault();

        const loginData = {
            idoname: $("#login-user").val(),
            pswd: $("#login-password").val()
        };

        $.ajax({
            url: "/dsaApp/usuarios/login",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(loginData),
            success: function (response) {
                usuarioActual = response; // usuarioActual es un contenedor de la respuesta, contiene
                // mail, id, nombre, etc
                alert("Login exitoso! Bienvenido, " + response.name);
                updateNav();
                showSection("#about");
                $("#num-tarros").text(response.tarrosMiel);
                $("#num-flores").text(response.flor);
                $("#num-flores-doradas").text(response.floreGold);

                $("#nav-about").addClass("active");
                $("#user-info").text("ID: " + response.id).fadeIn();
            },
            error: function (xhr) {
                alert("Error en el login: " + xhr.responseText);
            }
        });
    });

    $("#form-signup").submit(function (e) {
        e.preventDefault();//Para que no se actualice la web
        const signupData = { //definimos variables necesarias para la funcion ya que no son globales
            id: $("#signup-user").val(), // y las metemos en un objeto que lo transformaremos a JSON
            name: $("#signup-fullname").val(),
            apellidos:$("#signup-surname").val(),
            pswd: $("#signup-password").val(),
            mail: $("#signup-email").val(),
            pregunta:$("#security-question").val(),
            respuesta: $("#answer-securityquestion").val()
        };

        const p = $("#resignup-password").val();//miramos si las contraseñas coinciden
        if(signupData.pswd !== p){
            alert("Las contraseñas no coinciden");
            return;
        }
        const checkbox = $("#checkboxx");//miramos is ha aceptado los terminos o no
        if (!checkbox.prop("checked")) {
            $("#terminos-label").addClass("shake-border"); // Añadimos la classe para que el usuario vea
                                                                //en lo que se esta equivocando
            setTimeout(() => {
                $("#terminos-label").removeClass("shake-border");
            }, 1000);

            alert("Debes aceptar los términos y condiciones para registrarte");
            return;
        }

        $.ajax({
            url: "/dsaApp/usuarios/register",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(signupData),
            success: function (response) {
                alert("Registro exitoso! Ahora puedes iniciar sesión.");
                $("#nav-login").click(); // hacemos que se clique el boton del navegador del login

                //Rellenamos los campos del login con los daos que acaba de usar para el signup
                $("#login-user").val(signupData.id);
                $("#login-password").val(signupData.pswd);
                $("#login-boton-submit").focus(); //esto esta muy bien ya que deja el raton en el boton
            },
            error: function (xhr) {
                alert("Error en el registro: " + xhr.responseText);
            }
        });
    });

    $("#Olv-Contra").click(function(e){
        e.preventDefault();
        showSection("#olvidadoContra")
    });

    $("#enviar-usuario").click(function (e){
        e.preventDefault();
        const usuarioData = $("#olv-usuario").val();
        console.log(usuarioData);
        if (!usuarioData) {
            console.warn("El valor de usuarioData es nulo o vacío. Revisa el campo #olv-usuario.");
        } else {
            console.log("Se capturó correctamente el valor:", usuarioData);
        }

        $.ajax({
            url: "/dsaApp/usuarios/login/recordarContraseña",
            type: "GET",
            contentType: "application/json",
            data: { id: usuarioData }, // como solo tiene un valor lo enviamos de este formato
            success: function (response) {
                console.log("Respuesta recibida:", response);
                $("#nav-login").click();
                $("#enviar-usuario").hide();
                $("#pregunta-conseguida").text(response);
                $("#pregunta-conseguida").show();
                $("#respuesta-olv").show();
                $("#relogin").show();
            },
            error: function (xhr) {
                console.error("Entró en error del AJAX", xhr);
                console.log("Status:", xhr.status); // útil para depurar
                console.log("Respuesta texto:", xhr.responseText);

                // solo alerta si hay razón:
                if (xhr.status === 401) {
                    alert("Usuario no encontrado: " + xhr.responseText);
                } else {
                    alert("Error inesperado: " + xhr.status);
                }
            }
        });
    });

    $("#relogin").click(function(e){
        e.preventDefault();
        const usuarioData = {
            id: $("#olv-usuario").val(),
            respuesta: $("#respuesta-olv").val(),
        };
        $.ajax({
            url: "/dsaApp/usuarios/login/recuperarCuenta",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(usuarioData),
            success: function (response) {
            $("#usuario-a-cambiar").text(id);
            $("#olvidadoContra").hide();
            $("#update-password").show();
            },
            error: function (xhr) {
                alert("Respuesta Incorrecta - Vigilia con las Mayúsuclas " + xhr.responseText);
            }
        });
    });

    $("#olv-a-login").click(function(e){
        e.preventDefault();
        const usuarioData = {
            idoname: $("#usuario-a-cambiar").val(),
            pswd: $("#primera-contra").val(),
        };
        const pswd2 = $("#segunda-contra").val();
        if(usuarioData.pswd !== pswd2 ){
            alert("Las contraseñas no son iguales");
            return;
        }
        $.ajax({
            url: "/dsaApp/usuarios/login/cambiarContraseña",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(usuarioData),
            success: function (response) {
                $("#nav-login").click();

                $("#login-user").val(usuarioData.id);
                $("#login-password").val(signupData.pswd);
                $("#login-boton-submit").focus();


            },
            error: function (xhr) {
                alert(xhr.responseText);
            }
        });
    });

    $("#nav-shop").click(function (e) {
        e.preventDefault();
        showSection("#shop"); // Ocultara y mostrara la section de interes
        $(this).addClass("active");
        getTienda(); // En esta funcion tendra que hacer las peticiones de la listas de Tienda
    });
// El objetivo de esta funcion es hacer 4 petciones, las 2 primeras para tener los objetos que tiene YA comprados
// el usuario y poder en las 2 siguientes peticiones que seria para coger todos los datos de cada objeto, hacer que no
// Pueda comprarlo y cambie la class CSS y el texto del boton.
    function getTienda (){
        let listaArmasss = []
        let listaSkinsss = []
        const obtenerArmasCompradas = new Promise((resolve, reject) => {
            //Lo que se ha hecho ha sido crear una variable que cuando se cumpla estara resolve
            $.ajax({
                url: "/dsaApp/usuarios/tienda/" + usuarioActual.id + "/armas",
                type: "GET",
                contentType: "application/json",
                success: function (response) {
                    listaArmasss = response.consulta.map(objeto => objeto.id);
                    //En el foreach va en minusculas ya que aqui lo que hacemos es crear una variable,
                    //por cada objeto Json no le hacemos caso a que consulta sea de classe Objeto sbs,
                    //esto se llama objeto JSON o literal."consulta": [
                    //     {
                    //       "id": 1,
                    //       "nombre": "Espada",
                    //       "precio": 100
                    //     },
                    //     {
                    //       "id": 2,
                    //       "nombre": "Arco",
                    //       "precio": 150
                    //     }
                    //   ] Este seria el JSON y cada objeto esta entre un claudator (objeto JSON)
                    resolve();
                },
                error: function (xhr) {
                    resolve();
                }
            });
        });

        const obtenerSkinsCompradas = new Promise((resolve, reject) => {
            $.ajax({
                url: "/dsaApp/usuarios/tienda/" + usuarioActual.id + "/skins",
                type: "GET",
                contentType: "application/json",
                success: function (response) {
                    response.consulta.forEach(objeto => {
                        listaSkinsss = response.consulta.map(objeto => objeto.id);
                        //el .map sirve para recorrer toda la lista y seleccionar el valor que estas seleccionando
                        //Basicamente aqui estamos recorriendo toda la lista JSON y poniendo el listaSkinsss los
                        //objetos.id SOLO. Selecionas los atributos que quieres sbs.
                    })
                        resolve();
                },

                error: function (xhr) {
                    resolve();
                }
            });
        });

        Promise.all([obtenerArmasCompradas, obtenerSkinsCompradas]).then(() => {
//Una vez se cumplan las 2 hace lo del {}
            $.ajax({
                url: "/dsaApp/usuarios/tienda/armas",
                type: "GET",
                contentType: "application/json",
                success: function (response) {
                    const listaArmas = $("#listas-armas");
                    listaArmas.empty();
                    response.consulta.forEach(objeto => {
                        const comprado = objeto.id;
                        let li;
                        if (listaArmasss.includes(comprado)) {
                            li = $(`
                          <li>
                          <article>
                          <h4>${objeto.nombre}</h4>
                          <img src="img/${objeto.imagen}" alt="${objeto.nombre}" />
                          <h5>${objeto.precio} Tarros de Miel</h5>
                           <p>${objeto.descripcion}</p>
                            <button class="btn-ya-comprado" disabled>
                           Comprado
                           </button>
                           </article>
                          </li>
                     `);
                        } else {
                            li = $(`
                      <li>
                        <article>
                          <h4>${objeto.nombre}</h4>
                          <img src="img/${objeto.imagen}" alt="${objeto.nombre}" />
                          <h5>${objeto.precio} Tarros de Miel</h5>
                           <p>${objeto.descripcion}</p>
                           <button class= "btn-comprar" data-id="${objeto.id}" data-nombre="${objeto.nombre}">
                           Compra
                           </button>
                        </article>
                     </li>  
                     `);
                        }
                        listaArmas.append(li);
                    });
                },
                error: function (xhr) {
                    alert("Error al cargar armas: " + xhr.responseText);
                }
            });

            // SKINS
            $.ajax({
                url: "/dsaApp/usuarios/tienda/skins",
                type: "GET",
                contentType: "application/json",
                success: function (response) {
                    const listaSkins = $("#listas-skins");
                    listaSkins.empty();
                    response.consulta.forEach(objeto => {
                        const comprado = objeto.id;
                        let li;
                        if (listaSkinsss.includes(comprado)) {
                            li = $(`
                            <li>
                            <article>
                            <h4>${objeto.nombre}</h4>
                            <img src="img/${objeto.imagen}" alt="${objeto.nombre}" />
                            <h5>${objeto.precio} Tarros de Miel</h5>
                            <p>${objeto.descripcion}</p>
                            <button class="btn-ya-comprado" disabled>
                            Comprado
                            </button>
                            </article>
                          </li>
                         `);
                        } else {
                            li = $(`
                        <li>
                        <article>
                        <h4>${objeto.nombre}</h4>
                        <img src="img/${objeto.imagen}" alt="${objeto.nombre}" />
                        <h5>${objeto.precio} Tarros de Miel</h5>
                        <p>${objeto.descripcion}</p>
                        <button class="btn-comprar" data-id="${objeto.id}" data-nombre="${objeto.nombre}">
                            Compra
                        </button>
                    </article>
                    </li>
                    `);
                        }
                        listaSkins.append(li);
                    });
                },
                error: function (xhr) {
                    alert("Error al cargar skins: " + xhr.responseText);
                }
            });
        });
    }
    $(document).on("click", ".btn-compra", function () {
        e.preventDefault();
        const idUsuario = usuarioActual.id;
        const nombreObjeto = $(this).data("nombre");

        const jsonData = {
            usuarioId: idUsuario,
            objeto: nombreObjeto
        };

        $.ajax({
            url: "/dsaApp/usuarios/comprar",
            type: "PUT",
            data: JSON.stringify(jsonData),
            contentType: "application/json",
            success: function (response) {
                alert("Compra realizada con éxito");
                $("#num-tarros").text(response.tarrosMiel);
                // Cambiar el botón a apagado (sin recargar todo)
                $(this)
                    .removeClass("btn-compra")
                    .addClass("btn-compra-hecho")
                    .text("Comprado")
                    .attr("disabled", true);
            }.bind(this),
            error: function (xhr) {
                alert("Error al comprar: " + xhr.responseText);
            }
        });
    });

    $("#conversion").click(function (e) {
        e.preventDefault();
        const userId = usuarioActual.id; // o el valor que necesites
        $.ajax({
            url: "/dsaApp/"+ usuarioActual.id +"/conversion",
            type: "PUT",
            contentType: "application/json",
            success: function (response) {
                $("#num-tarros").text(response.tarrosMiel);
                $("#num-flores-doradas").text(0);
                $("#num-flores").text(response.flores);
            },
            error: function (xhr) {
                alert(xhr.responseText);
            }
        });
    });

});