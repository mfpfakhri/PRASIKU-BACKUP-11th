package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 26/02/2018.
 */

public class City {
    private String cityId;
    private String cityName;
    private String cityProvince;

    public City() {

    }

    public City(String cityId, String cityName, String cityProvince) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityProvince = cityProvince;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityProvince() {
        return cityProvince;
    }

    public void setCityProvince(String cityProvince) {
        this.cityProvince = cityProvince;
    }
}
