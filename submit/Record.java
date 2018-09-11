import java.util.StringJoiner;

public class Record {
    private String name;
    private String ssn;
    private String homePhone;
    private String address;
    private String officePhone;
    private int age;
    private double gpa;

    public Record(String name, String ssn, String homePhone, String address, String officePhone, int age, double gpa){
        this.name = name;
        this.ssn = ssn;
        this.homePhone = homePhone;
        this.address = address;
        this.officePhone = officePhone;
        this.age = age;
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Record && obj.toString().equals(this.toString());
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add((getName()==null)?" ":getName())
                .add((getSsn()==null)?" ":getSsn())
                .add((getHomePhone()==null)?" ":getHomePhone())
                .add((getAddress()==null)?" ":getAddress())
                .add((getOfficePhone()==null)?" ":getOfficePhone()).add(getAge()+"").add(getGpa()+"");
        return joiner.toString();
    }
}
