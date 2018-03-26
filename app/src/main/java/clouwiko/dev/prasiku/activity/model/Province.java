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

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
