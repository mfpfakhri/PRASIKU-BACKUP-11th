package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 14/03/2018.
 */

public class Cat {
    private String catId;
    private String catProfilePhoto;
    private String catName;
    private String catDob;
    private String catGender;
    private String catDescription;
    private String catMedNote;
    private String catVaccStat;
    private String catSpayNeuterStat;
    private String catReason;

    public Cat() {
    }

    public Cat(String catId, String catProfilePhoto, String catName, String catDob, String catGender, String catDescription, String catMedNote, String catVaccStat, String catSpayNeuterStat, String catReason) {
        this.catId = catId;
        this.catProfilePhoto = catProfilePhoto;
        this.catName = catName;
        this.catDob = catDob;
        this.catGender = catGender;
        this.catDescription = catDescription;
        this.catMedNote = catMedNote;
        this.catVaccStat = catVaccStat;
        this.catSpayNeuterStat = catSpayNeuterStat;
        this.catReason = catReason;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
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

    public String getCatDob() {
        return catDob;
    }

    public void setCatDob(String catDob) {
        this.catDob = catDob;
    }

    public String getCatGender() {
        return catGender;
    }

    public void setCatGender(String catGender) {
        this.catGender = catGender;
    }

    public String getCatDescription() {
        return catDescription;
    }

    public void setCatDescription(String catDescription) {
        this.catDescription = catDescription;
    }

    public String getCatMedNote() {
        return catMedNote;
    }

    public void setCatMedNote(String catMedNote) {
        this.catMedNote = catMedNote;
    }

    public String getCatVaccStat() {
        return catVaccStat;
    }

    public void setCatVaccStat(String catVaccStat) {
        this.catVaccStat = catVaccStat;
    }

    public String getCatSpayNeuterStat() {
        return catSpayNeuterStat;
    }

    public void setCatSpayNeuterStat(String catSpayNeuterStat) {
        this.catSpayNeuterStat = catSpayNeuterStat;
    }

    public String getCatReason() {
        return catReason;
    }

    public void setCatReason(String catReason) {
        this.catReason = catReason;
    }
}