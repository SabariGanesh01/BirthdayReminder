import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BirthdayService {
    private final DBConnection dbConnection;

    public BirthdayService() {
        // Get database connection instance
        this.dbConnection = DBConnection.getInstance();
    }

    public List<Employee> getBirthdayList(Date period) {
        return dbConnection.getEmployeesByBirthday(period);
    }

    public List<Employee> getBirthdayListInRange(Date startDate, Date endDate) {
        return dbConnection.getEmployeesByRangeOfBirthday(startDate, endDate);
    }

    public List<Employee> getAlertForAll() {
        return dbConnection.getEmployeesWithAlertForAll();
    }

    public List<Employee> getSameReportingManagerEmployees(int managerId) {
        return dbConnection.getEmployeesWithSameReportingManager(managerId);
    }


    /**
     * Notify employees about today's birthdays
     */
    public void notifyEmployees(){
        List<Employee> birthdayListOfEmployees;
        birthdayListOfEmployees = this.getBirthdayList(java.sql.Date.valueOf(java.time.LocalDate.now()));

        // If no one has birthday today, no need to send notifications
        if (birthdayListOfEmployees.isEmpty()) {
            System.out.println("No birthdays today!");
            return;
        }

        // Create a set of all employees who should receive notification
        Set<Employee> employeesToNotify = new HashSet<>();

        // Extracting unique manager IDs from birthday employees
        Set<Integer> uniqueManagerIds = new HashSet<>();
        for (Employee employee : birthdayListOfEmployees) {
            Integer managerId = employee.getManagerId();
            if (managerId != null) {
                uniqueManagerIds.add(managerId);
            }
        }

        // Add employees under each manager and the managers themselves
        for (Integer managerId : uniqueManagerIds) {
            // Add employees under this manager
            List<Employee> employeesUnderManager = this.getSameReportingManagerEmployees(managerId);
            employeesToNotify.addAll(employeesUnderManager);

            // Add the manager himself
            Employee manager = getEmployeeById(managerId);
            if (manager != null) {
                employeesToNotify.add(manager);
            }
        }

        // Add all employees who have alert_for_all enabled
        List<Employee> alertForAllEmployees = this.getAlertForAll();
        employeesToNotify.addAll(alertForAllEmployees);

    }

    /**
     * Get employee details by ID
     * @param empId Employee ID
     * @return Employee object or null if not found
     */
    private Employee getEmployeeById(int empId) {
        // This method should be implemented in DBConnection class
        // For now, we'll add a placeholder implementation
        String query = "SELECT * FROM employees WHERE emp_id = ? AND is_active = 1";
        List<Employee> employees = dbConnection.executeQuery(query, empId);
        return employees.isEmpty() ? null : employees.get(0);
    }
}
