package edu.upc.dsa.db.orm;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.db.orm.util.ObjectHelper;
import edu.upc.dsa.db.orm.util.QueryHelper;

import java.sql.*;
import java.util.HashMap;
import java.util.List;


public class SessionImpl implements Session {
    private final Connection conn;

    public SessionImpl(Connection conn) {

        this.conn = conn;
    }

    public void save(Object entity) {

        // INSERT INTO Partida () ()
        String insertQuery = QueryHelper.createQueryINSERT(entity);
        // INSERT INTO User (ID, lastName, firstName, address, city) VALUES (0, ?, ?, ?,?), he hecho qe sea todo interrogantes

        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(insertQuery);
            int i = 1;

            for (String field: ObjectHelper.getFields(entity)) {
                Object _aux = ObjectHelper.getter(entity, field);
                pstm.setObject(i++, _aux);
            }

            pstm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void close() {

    }

    @Override
    public Object get(Class theClass, String filtro, Object ID) {
        Object o = null;
        try {
            String sql = QueryHelper.createQuerySELECT(theClass, filtro);  // En esta linea se hace la peticion sql con el Query Helper
            //pero sale con interrogantes los valores del WHERE, t lo buscamos por el atributo
            PreparedStatement pstm = conn.prepareStatement(sql);//Es un objeto de SQL que permite ejecutar las peticiones
            pstm.setObject(1, ID);  // sustituye ? por el ID

            ResultSet res = pstm.executeQuery();//ejecuta la queryx

            if (res.next()) { //mira si la primera file del res esta vacia o no
                o = theClass.getDeclaredConstructor().newInstance(); //crea un objeto vacio de la classe que se quiere obtener
                ResultSetMetaData rsmd = res.getMetaData();//te da info de cuantas columnas filas hay
                int numColumns = rsmd.getColumnCount();//basicamente para saber los atributos que tiene

                for (int i = 1; i <= numColumns; i++) {
                    String columnName = rsmd.getColumnName(i);//el nombre del atributo
                    int type = rsmd.getColumnType(i);
                    //System.out.println("type"+type);
                    Object value = res.getObject(i);//el valor de la celda
                    if (type== Types.DECIMAL) {
                        value = Double.valueOf(value.toString());
                    }

                    //if (type==java.sql.Types.B
                    ObjectHelper.setter(o, columnName, value);  // llama al setter del helper
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return o;
    }

    public void update(Object object) {

    }

    public void delete(Object object) {

    }

    public List<Object> findAll(Class theClass) {
        return null;
    }

    public List<Object> findAll(Class theClass, HashMap params) {
     /*   String theQuery = QueryHelper.createSelectFindAll(theClass, params);
        PreparedStatement pstm = null;
        pstm = conn.prepareStatement(theQuery);

        int i=1;
        for (Object value : params.values()) {
            pstm.setObject(i++, value );
        }
        //ResultSet rs = pstm.executeQuery();




        return result;
*/
     return null;
    }

    public List<Object> query(String query, Class theClass, HashMap params) {
        return null;
    }
}
