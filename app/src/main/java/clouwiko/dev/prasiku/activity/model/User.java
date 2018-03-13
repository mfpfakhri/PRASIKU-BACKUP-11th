package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 28/02/2018.
 */

public class User {
    private String userEmail;
    private String userId;
    private String userFname;
    private String userDob;
    private String userGender;
    private String userProfilePhoto;
    private String userCity;
    private String userPhone;
    private String userAddress;

    public User(String userEmail, String userId, String userFname, String userDob, String userGender, String userProfilePhoto, String userCity, String userPhone, String userAddress) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userFname = userFname;
        this.userDob = userDob;
        this.userGender = userGender;
        this.userProfilePhoto = userProfilePhoto;
        this.userCity = userCity;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
    }

    public User() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFname() {
        return userFname;
    }

    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    public String getUserDob() {
        return userDob;
    }

    public void setUserDob(String userDob) {
        this.userDob = userDob;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}
