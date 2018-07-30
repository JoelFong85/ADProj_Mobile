package Models;

public class Department {
    String departmentId;
    int headId;
    int tempHeadId;
    int departmentPin;
    String departmentName;
    String contactName;
    int phoneNumber;
    int faxNumber;

    public Department(String departmentId, int headId, int tempHeadId, int departmentPin, String departmentName, String contactName, int phoneNumber, int faxNumber) {
        this.departmentId = departmentId;
        this.headId = headId;
        this.tempHeadId = tempHeadId;
        this.departmentPin = departmentPin;
        this.departmentName = departmentName;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public int getTempHeadId() {
        return tempHeadId;
    }

    public void setTempHeadId(int tempHeadId) {
        this.tempHeadId = tempHeadId;
    }

    public int getDepartmentPin() {
        return departmentPin;
    }

    public void setDepartmentPin(int departmentPin) {
        this.departmentPin = departmentPin;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(int faxNumber) {
        this.faxNumber = faxNumber;
    }
}
