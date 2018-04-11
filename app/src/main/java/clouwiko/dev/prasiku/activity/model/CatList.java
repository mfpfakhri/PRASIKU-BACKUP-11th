package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 11/04/2018.
 */

public class CatList {
    private String catProfilePhoto;
    private String catName;
    private String catReason;

    public CatList() {
    }

    public CatList(String catProfilePhoto, String catName, String catReason) {
        this.catProfilePhoto = catProfilePhoto;
        this.catName = catName;
        this.catReason = catReason;
    }

    public String getCatProfilePhoto() {
        return catProfilePhoto;
    }

    public void setCatProfilePhoto(String catProfilePhoto) {
        this.catProfilePhoto = catProfilePhoto;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatReason() {
        return catReason;
    }

    public void setCatReason(String catReason) {
        this.catReason = catReason;
    }

}
