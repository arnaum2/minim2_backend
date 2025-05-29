package edu.upc.dsa;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class GameManagerImpl implements GameManager {
    private static GameManager instance;
    private Map<String, Objeto> objetos;
    private Map<String, Usuario> usuarios;
    private Map<String, Usuario> usuariosm;
    private List<Issue> issues;
    private static final String USUARIOS_FILE = "usuarios.json";
    private static final String OBJETOS_FILE = "objetos.json";
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);

    public GameManagerImpl() {
        this.usuarios = new HashMap<>();
        this.usuariosm = new HashMap<>();
        this.objetos = new HashMap<>();
        this.issues = new LinkedList<>();
        cargarUsuarios();
        cargarObjetos();
        if (this.objetos.isEmpty()) {
            initTestObjects();
        }
    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManagerImpl();
        return instance;
    }

    public Usuario obtenerUsuario(String id) throws CredencialesIncorrectasException {
        Usuario u = null;
        if (usuarios.containsKey(id)) {
            u = usuarios.get(id);
        } else if (usuariosm.containsKey(id)) {
            u = usuariosm.get(id);
        }
        return u;
    }

    @Override
    public void addUsuario(String id, String name, String ape,String contra, String mail, String q, String a) throws UsuarioYaRegistradoException {
        logger.info("Registrando nuevo usuario: " + id + " / " + mail);

        if (usuarios.containsKey(id)) {
            throw new UsuarioYaRegistradoException("El USER ya está registrado");
        }

        if (usuariosm.containsKey(mail)) {
            throw new UsuarioYaRegistradoException("El MAIL ya está registrado");
        }

        Usuario nuevo = new Usuario(id, name, ape ,contra, mail,q,a);
        this.usuarios.put(id, nuevo);
        this.usuariosm.put(mail, nuevo);
        logger.info("Usuario registrado exitosamente");
    }

//    public void addUsuarioTest(String id, String name, String ape,String contra, String mail, String q, String a, int tarros, int flores, int mejor) throws UsuarioYaRegistradoException {
//        logger.info("Registrando nuevo usuario: " + id + " / " + mail);
//
//        if (usuarios.containsKey(id)) {
//            throw new UsuarioYaRegistradoException("El USER ya está registrado");
//        }
//
//        if (usuariosm.containsKey(mail)) {
//            throw new UsuarioYaRegistradoException("El MAIL ya está registrado");
//        }
//
//        Usuario nuevo = new Usuario(id, name, ape ,contra, mail,q,a, tarros, flores,mejor);
//        this.usuarios.put(id, nuevo);
//        this.usuariosm.put(mail, nuevo);
//        logger.info("Usuario registrado exitosamente");
//    }

    public void addUsuarioTest(String id, String name, String ape, String contra, String mail, String q, String a, int tarros, int flores, int mejor, int floreGold) throws UsuarioYaRegistradoException {
        logger.info("Registrando nuevo usuario: " + id + " / " + mail);

        if (usuarios.containsKey(id)) {
            throw new UsuarioYaRegistradoException("El USER ya está registrado");
        }

        if (usuariosm.containsKey(mail)) {
            throw new UsuarioYaRegistradoException("El MAIL ya está registrado");
        }

        Usuario nuevo = new Usuario(id, name, ape, contra, mail, q, a, tarros, flores, mejor);
        nuevo.setFloreGold(floreGold); // Inicializar floreGold
        this.usuarios.put(id, nuevo);
        this.usuariosm.put(mail, nuevo);
        logger.info("Usuario registrado exitosamente");
    }

    @Override
    public UsuarioEnviar login(String mailOId, String pswd) throws CredencialesIncorrectasException {
       logger.info("Iniciando login");
        Usuario u = obtenerUsuario(mailOId);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        if (!u.getPswd().equals(pswd)) {
            throw new CredencialesIncorrectasException("Contraseña incorrecta");
        }
        UsuarioEnviar usu = new UsuarioEnviar(u.getId(), u.getName(), u.getPswd(), u.getMail(), u.getPregunta(), u.getRespuesta(), u.getTarrosMiel(), u.getFlor(), u.getMejorPuntuacion(), u.getNumPartidas(), u.getFloreGold());
        return usu;
    }
    @Override
    public void addObjeto(Objeto objeto) {
        logger.info("Iniciando objeto");
        this.objetos.put(objeto.getId(), objeto);
        guardarObjetos();
    }

    @Override
    public Objeto findObjeto(String id) {
        return this.objetos.get(id);
    }

    @Override
    public DevolverCompra Comprar (Compra compra) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException { // va a ser un @PUT
        //Algo de que si idUsuari, mande una excepcion de que falta iniciar session, de quiero saber si
        // se ha registrado, mi idea esq en la web arriba a la derecha tengas un parametro con tu
        //id para almacenar la variable y poderla mandar en cada JSON
        Usuario u = this.usuarios.get(compra.getUsuarioId());
        if (u == null) {
            throw new UsuarioNoAutenticadoException("Usuario no encontrado");
        }

        Objeto o = objetos.get(compra.getObjeto()); // Asegúrate que "objetos" es un Map<String, Objeto>
        if (o == null) { // <--- Validación crítica
            throw new IllegalArgumentException("Objeto no existe");
        }

        if (o.getPrecio() > u.getTarrosMiel()) {
            throw new NoSuficientesTarrosException("Miel insuficiente");
        }
        u.setTarrosMiel(u.getTarrosMiel() - o.getPrecio());
        if(o.getTipo() == 1){ //arma
            u.UpdateArmas(o);
        }
        else if(o.getTipo() == 2){ //skin
            u.UpdateSkin(o);
        }
        guardarUsuarios();
        DevolverCompra y = new DevolverCompra(u.getTarrosMiel());
        System.out.println(y.getTarrosMiel());
        return y;
    }
//    @Override
//    public void initTestUsers() throws UsuarioYaRegistradoException {
//        try {
//            this.addUsuarioTest("carlos2004", "Carlos","Gonzalez", "123", "carlos@gmail.com","Tu comida favorita?","Arroz", 1000, 106, 250 );
//            this.addUsuarioTest("MSC78", "Marc", "Lopez","321", "marc@gmail.com","Como se llamaba tu escuela de Primaria?" ,"Dali", 1500, 120, 500 );
//            this.addUsuarioTest("Test", "Dani", "Buenosdias","147", "dani@gmail.com","El nombre de tu familiar mas mayor?" ,"Teresa",2500, 151, 190 );
//            this.addObjeto(new Objeto("1", "Palo",200 ,1, "Un paaaaaaaaaaaaaalo","palo1.png"));
//            this.addObjeto(new Objeto("2", "Hacha",700, 1,"Un hacha asequible para todos pero mortal como ninguna, su especialidad: las telarañas" ,"hacha1.png"));
//            this.addObjeto(new Objeto("3", "Gorro Pirata", 1000, 2, "Para surcar los mares","gorropirata.png"));
//            this.addObjeto(new Objeto("4", "Gorro Patito", 1000, 2, "Para nadar mucho","gorropatito.png"));
//            this.addObjeto(new Objeto("5", "Mister Potato", 1000, 2, "Para ser feliz","misterpotato.png"));
//            this.addObjeto(new Objeto("7", "Espada",1150 ,1, "Un corte profundo que hiere a las arañas más poderosas","espada1.png"));
//            this.addObjeto(new Objeto("8", "Espada Real",1350 ,1, "De su corte se entera hasta la mismisima Anansi","espada2.png"));
//        } catch (UsuarioYaRegistradoException e) {
//            logger.warn("Usuario de prueba ya estaba registrado");
//        }
//    }

    @Override
    public void initTestUsers() throws UsuarioYaRegistradoException {
        try {
            // 1º user17 (1200)
            if (!usuarios.containsKey("user17"))
                this.addUsuarioTest("user17", "Max", "Power", "117", "max@gmail.com", "Color favorito?", "Rojo", 100, 10, 1200, 0);
            // 2º MSC78 (1100)
            if (!usuarios.containsKey("MSC78"))
                this.addUsuarioTest("MSC78", "Marc", "Lopez", "321", "marc@gmail.com", "Como se llamaba tu escuela de Primaria?", "Dali", 4, 0, 1100, 0);
            // 3º user16 (1000)
            if (!usuarios.containsKey("user16"))
                this.addUsuarioTest("user16", "Sara", "Ruiz", "116", "sara@gmail.com", "Animal favorito?", "Gato", 100, 10, 1000, 0);
            // 4º user15 (900)
            if (!usuarios.containsKey("user15"))
                this.addUsuarioTest("user15", "Luis", "Perez", "115", "luis@gmail.com", "Ciudad natal?", "Madrid", 100, 10, 900, 0);
            // 5º user14 (800)
            if (!usuarios.containsKey("user14"))
                this.addUsuarioTest("user14", "Ana", "Martinez", "114", "ana@gmail.com", "Color favorito?", "Azul", 100, 10, 800, 0);
            // 6º user13 (700)
            if (!usuarios.containsKey("user13"))
                this.addUsuarioTest("user13", "Javi", "Sanchez", "113", "javi@gmail.com", "Comida favorita?", "Pizza", 100, 10, 700, 0);
            // 7º user12 (600)
            if (!usuarios.containsKey("user12"))
                this.addUsuarioTest("user12", "Elena", "Diaz", "112", "elena@gmail.com", "Deporte favorito?", "Futbol", 100, 10, 600, 0);
            // 8º user11 (500)
            if (!usuarios.containsKey("user11"))
                this.addUsuarioTest("user11", "Pablo", "Gomez", "111", "pablo@gmail.com", "Libro favorito?", "1984", 100, 10, 500, 0);
            // 9º user10 (400)
            if (!usuarios.containsKey("user10"))
                this.addUsuarioTest("user10", "Lucia", "Fernandez", "110", "lucia@gmail.com", "Lugar soñado?", "Roma", 100, 10, 400, 0);
            // 10º user9 (300)
            if (!usuarios.containsKey("user9"))
                this.addUsuarioTest("user9", "Miguel", "Torres", "109", "miguel@gmail.com", "Serie favorita?", "Friends", 100, 10, 300, 0);
            // 11º Test (190)
            if (!usuarios.containsKey("Test"))
                this.addUsuarioTest("Test", "Dani", "Buenosdias", "147", "dani@gmail.com", "El nombre de tu familiar mas mayor?", "Teresa", 250, 151, 190, 15);
            // 12º user8 (180)
            if (!usuarios.containsKey("user8"))
                this.addUsuarioTest("user8", "Mario", "Santos", "108", "mario@gmail.com", "Color favorito?", "Verde", 100, 10, 180, 0);
            // 13º user7 (170)
            if (!usuarios.containsKey("user7"))
                this.addUsuarioTest("user7", "Laura", "Vega", "107", "laura@gmail.com", "Animal favorito?", "Perro", 100, 10, 170, 0);
            // 14º user6 (160)
            if (!usuarios.containsKey("user6"))
                this.addUsuarioTest("user6", "Raul", "Mendez", "106", "raul@gmail.com", "Ciudad natal?", "Sevilla", 100, 10, 160, 0);
            // 15º user5 (150)
            if (!usuarios.containsKey("user5"))
                this.addUsuarioTest("user5", "Carmen", "Gil", "105", "carmen@gmail.com", "Comida favorita?", "Pasta", 100, 10, 150, 0);
            // 16º user4 (140)
            if (!usuarios.containsKey("user4"))
                this.addUsuarioTest("user4", "Alba", "Romero", "104", "alba@gmail.com", "Deporte favorito?", "Tenis", 100, 10, 140, 0);
            // 17º user3 (130)
            if (!usuarios.containsKey("user3"))
                this.addUsuarioTest("user3", "Victor", "Navarro", "103", "victor@gmail.com", "Libro favorito?", "El Quijote", 100, 10, 130, 0);
            // 18º user2 (120)
            if (!usuarios.containsKey("user2"))
                this.addUsuarioTest("user2", "Paula", "Serrano", "102", "paula@gmail.com", "Lugar soñado?", "Tokio", 100, 10, 120, 0);

            // Asignar armas/skins a Test
            Usuario test = this.usuarios.get("Test");
            if (test != null) {
                if (this.objetos.get("1") != null) test.UpdateArmas(this.objetos.get("1"));
                if (this.objetos.get("2") != null) test.UpdateArmas(this.objetos.get("2"));
                if (this.objetos.get("7") != null) test.UpdateArmas(this.objetos.get("7"));
                if (this.objetos.get("8") != null) test.UpdateArmas(this.objetos.get("8"));
            }
            // Asignar skins a MSC78
            Usuario marc = this.usuarios.get("MSC78");
            if (marc != null) {
                if (this.objetos.get("4") != null) marc.UpdateSkin(this.objetos.get("4"));
                if (this.objetos.get("5") != null) marc.UpdateSkin(this.objetos.get("5"));
            }

            guardarUsuarios();
            logger.info("Usuarios de prueba inicializados.");
        } catch (UsuarioYaRegistradoException e) {
            logger.warn("Usuario de prueba ya estaba registrado");
        }
    }

    // Inicializa objetos de prueba solo si la tienda está vacía
    public void initTestObjects() {
        this.addObjeto(new Objeto("1", "Palo", 200, 1, "Un paaaaaaaaaaaaaalo", "palo1.png"));
        this.addObjeto(new Objeto("2", "Hacha", 700, 1, "Un hacha asequible para todos pero mortal como ninguna, su especialidad: las telarañas", "hacha1.png"));
        this.addObjeto(new Objeto("3", "Gorro Pirata", 1000, 2, "Para surcar los mares", "gorropirata.png"));
        this.addObjeto(new Objeto("4", "Gorro Patito", 1000, 2, "Para nadar mucho", "gorropatito.png"));
        this.addObjeto(new Objeto("5", "Mister Potato", 1000, 2, "Para ser feliz", "misterpotato.png"));
        this.addObjeto(new Objeto("7", "Espada", 1150, 1, "Un corte profundo que hiere a las arañas más poderosas", "espada1.png"));
        this.addObjeto(new Objeto("8", "Espada Real", 1350, 1, "De su corte se entera hasta la mismisima Anansi", "espada2.png"));
        logger.info("Objetos de prueba inicializados.");
    }



    @Override
    public ConsultaTienda findArmas() {

        HashMap<String, Objeto> armas = new HashMap<>();
        for (Objeto obj : this.objetos.values()) {
            if (obj.getTipo() == 1) { // tipo 1 = arma
                armas.put(obj.getId(), obj);
            }
        }
        List<Objeto> listaItems = new ArrayList<>(armas.values());
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }
    @Override
    public ConsultaTienda findSkins() {

        HashMap<String, Objeto> skins = new HashMap<>();
        for (Objeto obj : this.objetos.values()) {
            if (obj.getTipo() == 2) { // tipo 2 = arma
                skins.put(obj.getId(), obj);
            }
        }
        List<Objeto> listaItems = new ArrayList<>(skins.values());
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }

    @Override
    public String obtenerContra(String usuario) throws CredencialesIncorrectasException {

        logger.info("Obteniendo pregunta");
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }
        logger.info(u.getPregunta());
        return u.getPregunta();
    }

    @Override
    public Usuario relogin(String id, String respuesta) throws CredencialesIncorrectasException {
        Usuario u = obtenerUsuario(id);

        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        if (!u.getRespuesta().equals(respuesta)) {
            throw new CredencialesIncorrectasException("Respuesta incorrecta");
        }
        return u;


    }

    @Override
    public void CambiarContra(String usuario, String contra) throws CredencialesIncorrectasException {
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        u.setRespuesta(contra);

    }

    @Override
    public ConsultaTienda skinsUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos {
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }
        List<Objeto> listaItems = new ArrayList<>(u.getSkins().values());
        if (listaItems.isEmpty()) {
            throw new NoHayObjetos("No tienes skins");
        }
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }

    @Override
    public ConsultaTienda armasUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos {
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }
        List<Objeto> listaItems = new ArrayList<>(u.getArmas().values());
        if (listaItems.isEmpty()) {
            throw new NoHayObjetos("No tienes armas");
        }
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }

    @Override
    public void deleteUsuario(String id) throws UsuarioNoEncontradoException {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }
        String email = usuario.getMail();
        usuarios.remove(id);
        usuariosm.remove(email);
        logger.info("Usuario eliminado: " + id);
    }

    @Override
    public Intercambio intercambio (String usuario) throws CredencialesIncorrectasException, NoHayFlores {
        Usuario u = obtenerUsuario(usuario);
        System.out.println(u.getTarrosMiel() + "+" + u.getFlor()+ "+" + u.getFloreGold());
        int Tarros = 0;
        int FloresSobrantes = u.getFlor();
        if (u == null) {
            throw new CredencialesIncorrectasException("No esta registrado");
        }
        if((u.getFlor() < 30) && (u.getFloreGold() == 0)) {
            throw new NoHayFlores("No hay nada a convertir en Tarros, juega mas para conseguir mas!!");
        }
        while(FloresSobrantes >= 30){ // Va ahciendo restas de 30 n 30 i ba sumando Tarros pq cada 30 flores normales
            Tarros++;                // equivalen a 1 solo Tarro de Miel
            FloresSobrantes = FloresSobrantes - 30;
        }
        Tarros = Tarros + (u.getFloreGold()*50); // 1 Dorada = 50 Tarros
        u.setFlor(FloresSobrantes);
        u.setFloreGold(0);
        u.setTarrosMiel(u.getTarrosMiel() + Tarros);
        Intercambio i = new Intercambio(u.getTarrosMiel(), FloresSobrantes);
        System.out.println(u.getTarrosMiel() + "+" + u.getFlor()+ "+" + u.getFloreGold());
        return i;
    }
    
    public InfoList rankingConPosicion(String userId) {
        List<Usuario> allUsers = new ArrayList<>(usuarios.values());

        // Ordenar por mejor puntuación
        allUsers.sort((u1, u2) -> Integer.compare(u2.getMejorPuntuacion(), u1.getMejorPuntuacion()));

        // Obtener top 5
        List<Info> top5 = allUsers.stream()
                .limit(5)
                .map(u -> new Info(u.getId(), u.getMejorPuntuacion(), u.getNumPartidas()))
                .collect(Collectors.toList());

        // Buscar posición del usuario
        int posicion = -1;
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getId().equals(userId)) {
                posicion = i + 1; // Posiciones empiezan en 1
                break;
            }
        }

        // Si el usuario está en el top 5, no agregar duplicado
        if (posicion <= 5) {
            return new InfoList(top5, posicion);
        }

        // Si no está en el top 5, agregar al final para mostrar en móvil
        top5.add(new Info(userId,
                usuarios.get(userId).getMejorPuntuacion(),
                usuarios.get(userId).getNumPartidas()));

        return new InfoList(top5, posicion);
    }

    // Guardar usuarios en archivo
    public void guardarUsuarios() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(USUARIOS_FILE), this.usuarios);
        } catch (IOException e) {
            logger.error("Error guardando usuarios: " + e.getMessage());
        }
    }

    // Cargar usuarios desde archivo
    public void cargarUsuarios() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(USUARIOS_FILE);
            if (file.exists()) {
                this.usuarios = mapper.readValue(file, mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Usuario.class));
            }
        } catch (IOException e) {
            logger.error("Error cargando usuarios: " + e.getMessage());
        }
    }

    // Guardar objetos en archivo
    public void guardarObjetos() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(OBJETOS_FILE), this.objetos);
        } catch (IOException e) {
            logger.error("Error guardando objetos: " + e.getMessage());
        }
    }

    // Cargar objetos desde archivo
    public void cargarObjetos() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(OBJETOS_FILE);
            if (file.exists()) {
                this.objetos = mapper.readValue(file, mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Objeto.class));
            }
        } catch (IOException e) {
            logger.error("Error cargando objetos: " + e.getMessage());
        }
    }
    public Map<String, Usuario> getUsuarios() {
        return this.usuarios;
    }
    @Override
    public void addIssue(String date, String titol, String informer, String message) throws MissingDataException {

        if(date == "" || informer == "" || message == ""){
            throw new MissingDataException("Falten camps per completar");
        }
        else{
            Issue issue = new Issue(date, titol, informer, message);
            this.issues.add(issue);
        }
    }
    public List<Issue> llistaIssues() {
        return new ArrayList<>(issues);
    }
}
