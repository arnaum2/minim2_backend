
/*package edu.upc.dsa.db.orm.dao;

import edu.upc.eetac.dsa.db.orm.FactorySession;
import edu.upc.eetac.dsa.db.orm.Session;
import edu.upc.eetac.dsa.db.orm.dao.IEmployeeDAO;
import edu.upc.eetac.dsa.db.orm.model.*;

import java.util.HashMap;
import java.util.List;

public class EmployeeDAOImpl implements IEmployeeDAO {


    public int addEmployee(String id, String name, String surname, double salary) {
        Session session = null;
        int employeeID = 0;
        try {
            session = FactorySession.openSession();
            Employee employee = new Employee(id, name, surname, "",salary);
            session.save(employee);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }

        return employeeID;
    }


    public Employee getEmployee(int employeeID) {
        Session session = null;
        Employee employee = null;
        try {
            session = FactorySession.openSession();
            employee = (Employee)session.get(Employee.class, "id", employeeID);//aunq no tiene campo ID ns a
                                                                                    //que campo se refiere
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }

        return employee;
    }


    public void updateEmployee(int employeeID, String name, String surname, double salary) {
        Employee employee  = this.getEmployee(employeeID);
        employee.setName(name);
        employee.setSurname(surname);
        employee.setSalary(salary);

        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(employee);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }
    }


    public void deleteEmployee(int employeeID) {
        Employee employee = this.getEmployee(employeeID);
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.delete(employee);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }

    }


    public List<Employee> getEmployees() {
        Session session = null;
        List<Employee> employeeList=null;
        try {
            session = FactorySession.openSession();
            employeeList = session.findAll(Employee.class);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }
        return employeeList;
    }


    public List<Employee> getEmployeeByDept(int deptID) {

        // SELECT e.name, d.name FROM Employees e, DEpt d WHERE e.deptId = d.ID AND e.edat>87 AND ........

//        Connection c =

        Session session = null;
        List<Employee> employeeList=null;
        try {
            session = FactorySession.openSession();


            HashMap<String, Integer> params = new HashMap<String, Integer>();
            params.put("deptID", deptID);

            employeeList = session.findAll(Employee.class, params);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }
        return employeeList;
    }

    /*

    public void customQuery(xxxx) {
        Session session = null;
        List<Employee> employeeList=null;
        try {
            session = FactorySession.openSession();
            Connection c = session.getConnection();
            c.createStatement("SELECT * ")

        }
*/

//}

