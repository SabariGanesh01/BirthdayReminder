import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/your_database_name";
    private final String username = "your_username";
    private final String password = ""; // Empty string for no password

    // Private constructor for singleton pattern
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Singleton pattern to get single instance of DBConnection.
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // Get connection object
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Get employees whose birthday falls on the specified date
     * @param period The date to check for birthdays
     * @return List of employees with birthdays on the given date
     */
    public List<Employee> getEmployeesByBirthday(Date period) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE DATE_FORMAT(dob, '%m-%d') = DATE_FORMAT(?, '%m-%d') AND is_active = 1";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) { // in prepared statement to prevent SQL injection
            stmt.setDate(1, new java.sql.Date(period.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = createEmployeeFromResult(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<Employee> getEmployeesByRangeOfBirthday(Date startDate, Date endDate) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE DATE_FORMAT(dob, '%m-%d') BETWEEN DATE_FORMAT(?, '%m-%d') AND DATE_FORMAT(?, '%m-%d') AND is_active = 1";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = createEmployeeFromResult(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    /**
     * Get employees who have alert_for_all enabled
     * @return List of employees with alert_for_all = true
     */
    public List<Employee> getEmployeesWithAlertForAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE alert_for_all = 1 AND is_active = 1";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = createEmployeeFromResult(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    /**
     * Get employees who have a reporting manager
     * @return List of employees with manager_id not null, ordered by manager_id
     */
    public List<Employee> getEmployeesWithSameReportingManager(int managerId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE manager_id IS ? AND is_active = 1";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, managerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = createEmployeeFromResult(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    /**
     * Helper method to create Employee object from ResultSet
     * @param rs ResultSet from database query
     * @return Employee object
     * @throws SQLException if database access error occurs
     */
    private Employee createEmployeeFromResult(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmpId(rs.getInt("emp_id"));
        employee.setName(rs.getString("name"));
        employee.setEmpType(rs.getByte("emp_type"));
        employee.setDesignation(rs.getString("designation"));
        employee.setDob(rs.getDate("dob"));
        employee.setDoj(rs.getDate("doj"));
        employee.setActive(rs.getBoolean("is_active"));
        employee.setMailId(rs.getString("mail_id"));

        // Handle nullable fields
        int managerId = rs.getInt("manager_id");
        if (!rs.wasNull()) {
            employee.setManagerId(managerId);
        }

        boolean alertForAll = rs.getBoolean("alert_for_all");
        if (!rs.wasNull()) {
            employee.setAlertForAll(alertForAll);
        }

        employee.setPassword(rs.getString("password"));

        return employee;
    }

    /**
     * Execute a custom query and return list of employees
     * @param query SQL query to execute
     * @param parameters Parameters for the prepared statement
     * @return List of employees
     */
    public List<Employee> executeQuery(String query, Object... parameters) {
        List<Employee> employees = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            // Set parameters
            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = createEmployeeFromResult(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
