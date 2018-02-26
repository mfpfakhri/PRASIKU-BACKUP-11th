package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 26/02/2018.
 */

public class Province {

    String provinceId;
    String provinceName;

    public Province(){

    }

    public Province(String provinceId, String provinceName) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }
}
