import java.util.Date;

public class Employee {

    private int empId;
    private String name;
    private byte empType;
    private String designation;
    private Date dob;
    private Date doj;
    private boolean isActive;
    private String mailId;
    private Integer managerId; // Using Integer to allow null values
    private Boolean alertForAll; // Using Boolean to allow null values, defaults to false
    private String password;

    public Employee() {

    }
    // Constructor with required fields
    public Employee(int empId, String name, byte empType, String designation,
                   Date dob, Date doj, boolean isActive, String mailId, String password) {
        this.empId = empId;
        this.name = name;
        this.empType = empType;
        this.designation = designation;
        this.dob = dob;
        this.doj = doj;
        this.isActive = isActive;
        this.mailId = mailId;
        this.password = password;
        this.alertForAll = false; // Default value
    }

    // Getters and Setters
    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getEmpType() {
        return empType;
    }

    public void setEmpType(byte empType) {
        this.empType = empType;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDoj() {
        return doj;
    }

    public void setDoj(Date doj) {
        this.doj = doj;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Boolean getAlertForAll() {
        return alertForAll;
    }

    public void setAlertForAll(Boolean alertForAll) {
        this.alertForAll = alertForAll;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
