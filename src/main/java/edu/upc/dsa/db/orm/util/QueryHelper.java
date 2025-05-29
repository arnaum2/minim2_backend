package edu.upc.dsa.db.orm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryHelper {

    public static String createQueryINSERT(Object entity) {

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" ");
        sb.append("(");

        String [] fields = edu.upc.dsa.db.orm.util.ObjectHelper.getFields(entity);

        int i = 0;
        for (String field: fields) {
            if (i<fields.length-1) sb.append(field).append(", ");
            else sb.append(field);
           i++;
        }
        sb.append(") VALUES (");

        i=0;
        for (String field: fields) {
            if (i<fields.length-1) sb.append("?").append(", ");
            else sb.append("?");
            i++;
        }
        sb.append(")");

        return sb.toString();
    }

    public static String createQuerySELECT(Class theClass, String atributo) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName());
        sb.append(" WHERE " + atributo + " = ?");

        return sb.toString();
    }
    public static String createQuerySELECTemail(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE email = ?");

        return sb.toString();
    }


    public static String createSelectFindAll(Class theClass, HashMap<String, String> params) {

        Set<Map.Entry<String, String>> set = params.entrySet();

        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass.getSimpleName()+" WHERE 1=1");
        for (String key: params.keySet()) {
            sb.append(" AND "+key+"=?");
        }


        return sb.toString();
    }
}
