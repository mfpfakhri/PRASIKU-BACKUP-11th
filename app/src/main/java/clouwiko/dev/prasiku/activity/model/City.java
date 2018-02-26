package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 26/02/2018.
 */

public class City {
    private String cityId;
    private String cityName;

    public City(){

    }

    public City(String cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }
}
