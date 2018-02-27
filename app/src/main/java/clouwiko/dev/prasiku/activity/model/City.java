package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 26/02/2018.
 */

public class City {
    private String cityId;
    private String cityName;
    private  String cityProvince;

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

    public String getCityName() {
        return cityName;
    }

    public String getCityProvince() {
        return cityProvince;
    }
}
