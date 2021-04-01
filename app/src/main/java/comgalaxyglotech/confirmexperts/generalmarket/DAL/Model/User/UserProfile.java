package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.User;

/**
 * Created by ELECTRON on 02/22/2019.
 */

public class UserProfile {
    public String fname;
    public String lname;
    public String email;
    public String country;
    public String phone;
    public String id;
    public UserProfile(String id, String fname, String lname, String email, String country, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.country = country;
        this.phone = phone;
        this.id=id;
    }

    public UserProfile() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
