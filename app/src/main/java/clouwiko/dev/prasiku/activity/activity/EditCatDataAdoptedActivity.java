package clouwiko.dev.prasiku.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;
import clouwiko.dev.prasiku.activity.model.Cat;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditCatDataAdoptedActivity extends AppCompatActivity {

    private MaterialSpinner spinnerAdoptionStatus;
    private Button btnDoneEditing;
    private DatabaseReference databaseCats, databaseAdoptions, databaseAdoptionsDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cat_data_adopted);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editcatadopted_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Adoption Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerAdoptionStatus = findViewById(R.id.editcatadopted_adoptionstatus);
        btnDoneEditing = findViewById(R.id.editcatadopted_done_button);

        String catId = getIntent().getStringExtra("cat_id");

        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catId);
        databaseCats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String adoptedstatus = dataSnapshot.child("catAdoptedStatus").getValue(String.class);

                switch (adoptedstatus) {
                    case "Available":
                        spinnerAdoptionStatus.setSelection(2);
                        break;
                    case "Adopted":
                        spinnerAdoptionStatus.setSelection(1);
                        break;
                    case "Not Available":
                        spinnerAdoptionStatus.setSelection(3);
                        break;
                    default:

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnDoneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adoptionStatusValidation();
                //Cat's Gender Validation
                int adoptionStatusSpinner = spinnerAdoptionStatus.getSelectedItemPosition();
                if (adoptionStatusSpinner == 0) {
                    Toast.makeText(getApplicationContext(), "Choose Adoption Status", Toast.LENGTH_SHORT).show();
                    return;
                } else if (adoptionStatusSpinner == 1) {
                    Toast.makeText(getApplicationContext(), "This Cat has been Adopted", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String cId = getIntent().getStringExtra("cat_id");
                    databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
                    databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String catidupdate = dataSnapshot.child("catId").getValue(String.class);
                            String ownerupdate = dataSnapshot.child("catOwnerId").getValue(String.class);
                            String photoupdate = dataSnapshot.child("catProfilePhoto").getValue(String.class);
                            String nameupdate = dataSnapshot.child("catName").getValue(String.class);
                            String dobupdate = dataSnapshot.child("catDob").getValue(String.class);
                            String genderupdate = dataSnapshot.child("catGender").getValue(String.class);
                            String descriptionupdate = dataSnapshot.child("catDescription").getValue(String.class);
                            String mednoteupdate = dataSnapshot.child("catMedNote").getValue(String.class);
                            String vaccstatupdate = dataSnapshot.child("catVaccStat").getValue(String.class);
                            String spayneuterstatupdate = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
                            String reasonupdate = dataSnapshot.child("catReason").getValue(String.class);
                            String catprovinceupdate = dataSnapshot.child("catProvince").getValue(String.class);
                            String catcityupdate = dataSnapshot.child("catCity").getValue(String.class);
                            String catdelete = dataSnapshot.child("catDeleteStatus").getValue(String.class);
                            String ownercatdelete = dataSnapshot.child("catOwnerDeleteStatus").getValue(String.class);
                            String catcitydeletestatus = dataSnapshot.child("catCityDeleteStatus").getValue(String.class);
                            String adoptedstatusupdate = spinnerAdoptionStatus.getSelectedItem().toString().trim();

                            updateCatData(catidupdate, ownerupdate, photoupdate, nameupdate, dobupdate, genderupdate, descriptionupdate, mednoteupdate, vaccstatupdate, spayneuterstatupdate, reasonupdate, catprovinceupdate, catcityupdate, adoptedstatusupdate, catdelete, ownercatdelete, catcitydeletestatus);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    String apponstatus = cId + "_Accepted";
                    databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions");
                    databaseAdoptions.orderByChild("adoptionCatIdApponStatus").equalTo(apponstatus).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String adoptionid = dataSnapshot.child("adoptionId").getValue(String.class);
                            String catid = dataSnapshot.child("adoptionCatId").getValue(String.class);
                            String owid = dataSnapshot.child("adoptionOwnerId").getValue(String.class);
                            String appid = dataSnapshot.child("adoptionApplicantId").getValue(String.class);
                            String appphone = dataSnapshot.child("adoptionApplicantPhone").getValue(String.class);
                            String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
                            String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
                            String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
                            String appnoanimal = dataSnapshot.child("adoptionApplicantNoAnimal").getValue(String.class);
                            String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
                            String apphousesize = dataSnapshot.child("adoptionApplicantHouseSize").getValue(String.class);
                            String appnopeople = dataSnapshot.child("adoptionApplicantNoPeople").getValue(String.class);
                            String appcatplace = dataSnapshot.child("adoptionApplicantCatPlace").getValue(String.class);
                            String appfampermission = dataSnapshot.child("adoptionApplicantFamPermission").getValue(String.class);
                            String appmove = dataSnapshot.child("adoptionApplicantMove").getValue(String.class);
                            String appmarriage = dataSnapshot.child("adoptionApplicantMarriage").getValue(String.class);
                            String appkids = dataSnapshot.child("adoptionApplicantKids").getValue(String.class);
                            String appfinancial = dataSnapshot.child("adoptionApplicantFinancial").getValue(String.class);
                            String apponstatus = "Rejected";
                            String catname = dataSnapshot.child("adoptionCatName").getValue(String.class);
                            String catphoto = dataSnapshot.child("adoptionCatPhoto").getValue(String.class);
                            String appname = dataSnapshot.child("adoptionApplicantName").getValue(String.class);
                            String appphoto = dataSnapshot.child("adoptionApplicantPhoto").getValue(String.class);
                            String owneridapponstatus = owid + "_" + apponstatus;
                            String catidapponstatus = catid + "_" + apponstatus;
                            String catdelete = dataSnapshot.child("adoptionDeleteStatus").getValue(String.class);
                            String ownercatdelete = dataSnapshot.child("adoptionOwnerDeleteStatus").getValue(String.class);

                            updateAcceptedAdoptionData(adoptionid, catid, owid, appid, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, appphoto, owneridapponstatus, catidapponstatus, catdelete, ownercatdelete);
                            Toast.makeText(getApplicationContext(), "Successfully changed cat adopt status", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    backToMainMenu();
                    finish();
                }
            }
        });
    }

    private boolean updateAcceptedAdoptionData(String adoptionId, String adoptionCatId, String adoptionOwnerId, String adoptionApplicantId, String adoptionApplicantPhone, String adoptionApplicantAddress, String adoptionApplicantJob, String adoptionApplicantReason, String adoptionApplicantNoAnimal, String adoptionApplicantHouseType, String adoptionApplicantHouseSize, String adoptionApplicantNoPeople, String adoptionApplicantCatPlace, String adoptionApplicantFamPermission, String adoptionApplicantMove, String adoptionApplicantMarriage, String adoptionApplicantKids, String adoptionApplicantFinancial, String adoptionApplicationStatus, String adoptionCatName, String adoptionCatPhoto, String adoptionApplicantName, String adoptionApplicantPhoto, String adoptionOwnerIdApponStatus, String adoptionCatIdApponStatus, String adoptionDeleteStatus, String adoptionOwnerDeleteStatus) {
        String appl_id = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(appl_id);
        Adoption adoptionAccept = new Adoption(adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionApplicantPhoto, adoptionOwnerIdApponStatus, adoptionCatIdApponStatus, adoptionDeleteStatus, adoptionOwnerDeleteStatus);
        databaseAdoptions.setValue(adoptionAccept);
        return true;
    }

    private void backToMainMenu() {
        String catId = getIntent().getStringExtra("cat_id");
        String ownerId = getIntent().getStringExtra("owner_id");
        String pActivity = getIntent().getStringExtra("previousActivity");
        if (pActivity.equals("findcat")) {
            Intent intent = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
            intent.putExtra("previousActivity", pActivity);
            intent.putExtra("cat_id", catId);
            intent.putExtra("owner_id", ownerId);
            Toast.makeText(getApplicationContext(), "Cat Adoption Status Successfully Edited", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        } else if (pActivity.equals("adoptionlist")) {
            Intent intent = new Intent(getApplicationContext(), UserAdoptionListActivity.class);
            intent.putExtra("previousActivity", pActivity);
            intent.putExtra("cat_id", catId);
            intent.putExtra("owner_id", ownerId);
            Toast.makeText(getApplicationContext(), "Cat Adoption Status Successfully Edited", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }
    }

    private boolean updateCatData(String catId, String catOwnerId, String catProfilePhoto, String catName, String catDob, String catGender, String catDescription, String catMedNote, String catVaccStat, String catSpayNeuterStat, String catReason, String catProvince, String catCity, String catAdoptedStatus, String adoptionDeleteStatus, String adoptionOwnerDeleteStatus, String catCityDeleteStatus) {
        String cId = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);

        Cat cat = new Cat(catId, catOwnerId, catProfilePhoto, catName, catDob, catGender, catDescription, catMedNote, catVaccStat, catSpayNeuterStat, catReason, catProvince, catCity, catAdoptedStatus, adoptionDeleteStatus, adoptionOwnerDeleteStatus, catCityDeleteStatus);

        databaseCats.setValue(cat);

        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCatDataAdoptedActivity.this);
        builder.setMessage("Are You sure want to quit editing?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pActivity = getIntent().getStringExtra("previousActivity");
                        String catId = getIntent().getStringExtra("cat_id");
                        String ownerId = getIntent().getStringExtra("owner_id");
                        Intent intent = new Intent(getApplicationContext(), CatProfileOwnerAdoptedActivity.class);
                        if (pActivity.equals("findcat")) {
                            intent.putExtra("previousActivity", pActivity);
                            intent.putExtra("cat_id", catId);
                            intent.putExtra("owner_id", ownerId);
                            startActivity(intent);
                            finish();
                        } else if (pActivity.equals("adoptionlist")) {
                            intent.putExtra("previousActivity", pActivity);
                            intent.putExtra("cat_id", catId);
                            intent.putExtra("owner_id", ownerId);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton("No", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
