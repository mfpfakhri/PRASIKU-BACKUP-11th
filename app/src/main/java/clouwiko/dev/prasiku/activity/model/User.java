package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 28/02/2018.
 */

public class User {
    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userFname;
    private String userProvince;
    private String userCity;
    private String userAddress;

    public User(String userEmail, String userPassword, String userPhone, String userFname, String userProvince, String userCity, String userAddress) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userFname = userFname;
        this.userProvince = userProvince;
        this.userCity = userCity;
        this.userAddress = userAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserFname() {
        return userFname;
    }

    public String getUserProvince() {
        return userProvince;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserAddress() {
        return userAddress;
    }
}
