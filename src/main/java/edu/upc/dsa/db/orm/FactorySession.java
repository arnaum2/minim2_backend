package edu.upc.dsa.db.orm;


import edu.upc.dsa.db.DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FactorySession {

    public static Session openSession() {
        Connection conn = getConnection();
        Session session = new SessionImpl(conn);
        return session;
    }



    public static Connection getConnection()  {
        String db = DBUtils.getDb();
        String host = DBUtils.getDbHost();
        String port = DBUtils.getDbPort();
        String user = DBUtils.getDbUser();
        String pass = DBUtils.getDbPasswd();


        Connection connection = null;
        try {
            //HE CAMBIAT AIXO, AQUI NOMES HI HAVIA : DriverManager.getConnection("jdbc:mariadb://"+host+":"+port+"/"+
            // NO HABIA EL CONNECTION =
           connection = DriverManager.getConnection("jdbc:mariadb://"+host+":"+port+"/"+
                    db+"?user="+user+"&password="+pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static Session openSession(String url, String user, String password) {
        Session session = null;
    //Basicamenete en vez de utilizar los DButils que vienen por defecto se usan los que le ponemos
        try {
            Connection conn = DriverManager.getConnection("jdbc:mariadb://" + url, user, password);
            session = new SessionImpl(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return session;
    }
}
