package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 28/02/2018.
 */

public class User {
    private String userEmail;
    private String userUid;
    private String userFname;
    private String userDob;
    private String userGender;
    private String userProfilePhoto;
    private String userProvince;
    private String userCity;
    private Long userPhone;
    private String userAddress;
    private String userStatus;
    private String userCityStatus;

    public User() {
    }

    public User(String userEmail, String userUid, String userFname, String userDob, String userGender, String userProfilePhoto, String userProvince, String userCity, Long userPhone, String userAddress, String userStatus, String userCityStatus) {
        this.userEmail = userEmail;
        this.userUid = userUid;
        this.userFname = userFname;
        this.userDob = userDob;
        this.userGender = userGender;
        this.userProfilePhoto = userProfilePhoto;
        this.userProvince = userProvince;
        this.userCity = userCity;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userStatus = userStatus;
        this.userCityStatus = userCityStatus;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
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

    public String getUserProvince() {
        return userProvince;
    }

    public void setUserProvince(String userProvince) {
        this.userProvince = userProvince;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public Long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserCityStatus() {
        return userCityStatus;
    }

    public void setUserCityStatus(String userCityStatus) {
        this.userCityStatus = userCityStatus;
    }
}