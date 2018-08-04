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
import com.google.firebase.database.Query;
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
        getSupportActionBar().setTitle("Ubah Status Adopsi");

        spinnerAdoptionStatus = findViewById(R.id.editcatadopted_adoptionstatus);
        btnDoneEditing = findViewById(R.id.editcatadopted_done_button);

        final String catId = getIntent().getStringExtra("cat_id");
        final String ownerId = getIntent().getStringExtra("owner_id");

        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catId);
        databaseCats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String adoptedstatus = dataSnapshot.child("catAdoptedStatus").getValue(String.class);

                switch (adoptedstatus) {
                    case "Adopted":
                        spinnerAdoptionStatus.setSelection(1);
                        break;
                    case "Available":
                        spinnerAdoptionStatus.setSelection(2);
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
                    Toast.makeText(getApplicationContext(), "Pilih Status Adopsi", Toast.LENGTH_SHORT).show();
                    return;
                } else if (adoptionStatusSpinner == 1) {
                    Toast.makeText(getApplicationContext(), "Status Kucing Ini Telah Diadopsi", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String cId = getIntent().getStringExtra("cat_id");
//                    final String appl_id = getIntent().getStringExtra("application_id");
                    databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
                    databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Cat cat = dataSnapshot.getValue(Cat.class);
                            cat.setCatAdoptedStatus("Available");

                            databaseCats.setValue(cat);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions");
                    final Query adoptionQuery = databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId);
                    adoptionQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Adoption adoptionAdoption = dataSnapshot.getValue(Adoption.class);
                            adoptionAdoption.setAdoptionApplicationStatus("Rejected");
                            adoptionAdoption.setAdoptionOwnerIdApponStatus(adoptionAdoption.getAdoptionOwnerId()+"_Rejected");
                            adoptionAdoption.setAdoptionApplicantIdDeleteStatus(adoptionAdoption.getAdoptionApplicantId()+adoptionAdoption.getAdoptionDeleteStatus());
                            adoptionAdoption.setAdoptionCatIdApponStatus(adoptionAdoption.getAdoptionCatId()+"_Rejected");

                            databaseAdoptions.child(adoptionAdoption.getAdoptionId()).setValue(adoptionAdoption);
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
                    //-----------------------------------------------------------------------------------//
//                    String cId = getIntent().getStringExtra("cat_id");
//                    databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
//                    databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            String catidupdate = dataSnapshot.child("catId").getValue(String.class);
//                            String ownerupdate = dataSnapshot.child("catOwnerId").getValue(String.class);
//                            String photoupdate = dataSnapshot.child("catProfilePhoto").getValue(String.class);
//                            String nameupdate = dataSnapshot.child("catName").getValue(String.class);
//                            String dobupdate = dataSnapshot.child("catDob").getValue(String.class);
//                            String genderupdate = dataSnapshot.child("catGender").getValue(String.class);
//                            String descriptionupdate = dataSnapshot.child("catDescription").getValue(String.class);
//                            String mednoteupdate = dataSnapshot.child("catMedNote").getValue(String.class);
//                            String vaccstatupdate = dataSnapshot.child("catVaccStat").getValue(String.class);
//                            String spayneuterstatupdate = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
//                            String reasonupdate = dataSnapshot.child("catReason").getValue(String.class);
//                            String catprovinceupdate = dataSnapshot.child("catProvince").getValue(String.class);
//                            String catcityupdate = dataSnapshot.child("catCity").getValue(String.class);
//                            String catdelete = dataSnapshot.child("catDeleteStatus").getValue(String.class);
//                            String ownercatdelete = dataSnapshot.child("catOwnerDeleteStatus").getValue(String.class);
//                            String catcitydeletestatus = dataSnapshot.child("catCityDeleteStatus").getValue(String.class);
//                            String adoptedstatusupdate = "Available";
//
//                            updateCatData(catidupdate, ownerupdate, photoupdate, nameupdate, dobupdate, genderupdate, descriptionupdate, mednoteupdate, vaccstatupdate, spayneuterstatupdate, reasonupdate, catprovinceupdate, catcityupdate, adoptedstatusupdate, catdelete, ownercatdelete, catcitydeletestatus);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
////                    String apponstatus = cId + "_Accepted";
//                    databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions");
////                    databaseAdoptions.orderByChild("adoptionCatIdApponStatus").equalTo(apponstatus).addChildEventListener(new ChildEventListener() {
//                    databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId).addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            String adoptionid = dataSnapshot.child("adoptionId").getValue(String.class);
//                            String catid = dataSnapshot.child("adoptionCatId").getValue(String.class);
//                            String owid = dataSnapshot.child("adoptionOwnerId").getValue(String.class);
//                            String appid = dataSnapshot.child("adoptionApplicantId").getValue(String.class);
//                            Long appphone = Long.valueOf(dataSnapshot.child("adoptionApplicantPhone").getValue(Long.class));
//                            String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
//                            String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
//                            String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
//                            Long appnoanimal = Long.valueOf(dataSnapshot.child("adoptionApplicantNoAnimal").getValue(Long.class));
//                            String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
//                            Long apphousesize = Long.valueOf(dataSnapshot.child("adoptionApplicantHouseSize").getValue(Long.class));
//                            Long appnopeople = Long.valueOf(dataSnapshot.child("adoptionApplicantNoPeople").getValue(Long.class));
//                            String appcatplace = dataSnapshot.child("adoptionApplicantCatPlace").getValue(String.class);
//                            String appfampermission = dataSnapshot.child("adoptionApplicantFamPermission").getValue(String.class);
//                            String appmove = dataSnapshot.child("adoptionApplicantMove").getValue(String.class);
//                            String appmarriage = dataSnapshot.child("adoptionApplicantMarriage").getValue(String.class);
//                            String appkids = dataSnapshot.child("adoptionApplicantKids").getValue(String.class);
//                            String appfinancial = dataSnapshot.child("adoptionApplicantFinancial").getValue(String.class);
//                            String apponstatus = "Rejected";
//                            String catname = dataSnapshot.child("adoptionCatName").getValue(String.class);
//                            String catphoto = dataSnapshot.child("adoptionCatPhoto").getValue(String.class);
//                            String appname = dataSnapshot.child("adoptionApplicantName").getValue(String.class);
//                            String owneridapponstatus = owid + "_" + apponstatus;
//                            String catdelete = dataSnapshot.child("adoptionDeleteStatus").getValue(String.class);
//                            String applicantiddelete = appid + "_" + catdelete;
////                            String ownercatdelete = dataSnapshot.child("adoptionOwnerDeleteStatus").getValue(String.class);
//                            String catidapponstatus = catid + "_" + apponstatus;
//
//                            updateAcceptedAdoptionData(catdelete, adoptionid, catid, ownerId, appid, applicantiddelete, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, owneridapponstatus, catidapponstatus);
////                            Toast.makeText(getApplicationContext(), "Profil Kucing Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
                    Toast.makeText(getApplicationContext(), "Profil Kucing Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

//    private boolean updateAcceptedAdoptionData(String adoptionDeleteStatus, String adoptionId, String adoptionCatId, String adoptionOwnerId, String adoptionApplicantId, String adoptionApplicantIdDeleteStatus, Long adoptionApplicantPhone, String adoptionApplicantAddress, String adoptionApplicantJob, String adoptionApplicantReason, Long adoptionApplicantNoAnimal, String adoptionApplicantHouseType, Long adoptionApplicantHouseSize, Long adoptionApplicantNoPeople, String adoptionApplicantCatPlace, String adoptionApplicantFamPermission, String adoptionApplicantMove, String adoptionApplicantMarriage, String adoptionApplicantKids, String adoptionApplicantFinancial, String adoptionApplicationStatus, String adoptionCatName, String adoptionCatPhoto, String adoptionApplicantName, String adoptionOwnerIdApponStatus, String adoptionCatIdApponStatus) {
//        String appl_id = getIntent().getStringExtra("application_id");
//        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(appl_id);
//        Adoption adoptionAccept = new Adoption(adoptionDeleteStatus, adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantIdDeleteStatus, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionOwnerIdApponStatus, adoptionCatIdApponStatus);
//        databaseAdoptions.setValue(adoptionAccept);
//        return true;
//    }

//    private void backToMainMenu() {
//        String catId = getIntent().getStringExtra("cat_id");
//        String ownerId = getIntent().getStringExtra("owner_id");
//        String pActivity = getIntent().getStringExtra("previousActivity");
//        if (pActivity.equals("findcat")) {
//            Intent intent = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
//            intent.putExtra("previousActivity", pActivity);
//            intent.putExtra("cat_id", catId);
//            intent.putExtra("owner_id", ownerId);
//            Toast.makeText(getApplicationContext(), "Cat Adoption Status Successfully Edited", Toast.LENGTH_SHORT).show();
//            startActivity(intent);
//            finish();
//        } else if (pActivity.equals("adoptionlist")) {
//            Intent intent = new Intent(getApplicationContext(), UserAdoptionListActivity.class);
//            intent.putExtra("previousActivity", pActivity);
//            intent.putExtra("cat_id", catId);
//            intent.putExtra("owner_id", ownerId);
//            Toast.makeText(getApplicationContext(), "Cat Adoption Status Successfully Edited", Toast.LENGTH_SHORT).show();
//            startActivity(intent);
//            finish();
//        }
//    }

//    private boolean updateCatData(String catId, String catOwnerId, String catProfilePhoto, String catName, String catDob, String catGender, String catDescription, String catMedNote, String catVaccStat, String catSpayNeuterStat, String catReason, String catProvince, String catCity, String catAdoptedStatus, String adoptionDeleteStatus, String adoptionOwnerDeleteStatus, String catCityDeleteStatus) {
//        String cId = getIntent().getStringExtra("cat_id");
//        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
//
//        Cat cat = new Cat(catId, catOwnerId, catProfilePhoto, catName, catDob, catGender, catDescription, catMedNote, catVaccStat, catSpayNeuterStat, catReason, catProvince, catCity, catAdoptedStatus, adoptionDeleteStatus, adoptionOwnerDeleteStatus, catCityDeleteStatus);
//
//        databaseCats.setValue(cat);
//
//        return true;
//    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCatDataAdoptedActivity.this);
        builder.setMessage("Apakah Anda Yakin Ingin Kembali?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pActivity = getIntent().getStringExtra("previousActivity");
                        String catId = getIntent().getStringExtra("cat_id");
                        String ownerId = getIntent().getStringExtra("owner_id");
                        Intent intent = new Intent(getApplicationContext(), CatProfileOwnerAdoptedActivity.class);
                        if (pActivity.equals("findcat")) {
                            String locHistory = getIntent().getStringExtra("locHistory");
                            intent.putExtra("previousActivity", pActivity);
                            intent.putExtra("cat_id", catId);
                            intent.putExtra("owner_id", ownerId);
                            intent.putExtra("locHistory", locHistory);
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
                .setNegativeButton("Tidak", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
