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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Cat;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditCatDataAdoptedActivity extends AppCompatActivity {

    private MaterialSpinner spinnerAdoptionStatus;
    private Button btnDoneEditing;
    private DatabaseReference databaseCats;

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

        String pActivity = getIntent().getStringExtra("previousActivity");
        String catId = getIntent().getStringExtra("cat_id");
        String ownerId = getIntent().getStringExtra("owner_id");

        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catId);
        databaseCats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String catid = dataSnapshot.child("catId").getValue(String.class);
//                String ownerid = dataSnapshot.child("catOwnerId").getValue(String.class);
//                String photo = dataSnapshot.child("catProfilePhoto").getValue(String.class);
//                String name = dataSnapshot.child("catName").getValue(String.class);
//                String dob = dataSnapshot.child("catDob").getValue(String.class);
//                String gender = dataSnapshot.child("catGender").getValue(String.class);
//                String description = dataSnapshot.child("catDescription").getValue(String.class);
//                String mednote = dataSnapshot.child("catMedNote").getValue(String.class);
//                String vaccstat = dataSnapshot.child("catVaccStat").getValue(String.class);
//                String spayneuterstat = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
//                String reason = dataSnapshot.child("catReason").getValue(String.class);
//                String catprovince = dataSnapshot.child("catProvince").getValue(String.class);
//                String catcity = dataSnapshot.child("catCity").getValue(String.class);
                String adoptedstatus = dataSnapshot.child("catAdoptedStatus").getValue(String.class);

                switch (adoptedstatus) {
                    case "Available":
                        spinnerAdoptionStatus.setSelection(2);
                        break;
                    case "Adopted":
                        spinnerAdoptionStatus.setSelection(1);
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
                } else {

                }
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
                        String adoptedstatusupdate = spinnerAdoptionStatus.getSelectedItem().toString().trim();

                        updateCatData(catidupdate, ownerupdate, photoupdate, nameupdate, dobupdate, genderupdate, descriptionupdate, mednoteupdate, vaccstatupdate, spayneuterstatupdate, reasonupdate, catprovinceupdate, catcityupdate, adoptedstatusupdate);
//                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                backToMainMenu();
//                finish();
            }
        });
    }

    private void backToMainMenu() {
//        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
//        Toast toast = Toast.makeText(getApplicationContext(), "Cat Adoption Status Successfully Edited", Toast.LENGTH_SHORT);
//        toast.show();
//        startActivity(intent);
//        toast.cancel();
//        finish();
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

    private boolean updateCatData(String catId, String catOwnerId, String catProfilePhoto, String catName, String catDob, String catGender, String catDescription, String catMedNote, String catVaccStat, String catSpayNeuterStat, String catReason, String catProvince, String catCity, String catAdoptedStatus) {
        String cId = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);

        Cat cat = new Cat(catId, catOwnerId, catProfilePhoto, catName, catDob, catGender, catDescription, catMedNote, catVaccStat, catSpayNeuterStat, catReason, catProvince, catCity, catAdoptedStatus);

        databaseCats.setValue(cat);

        return true;
    }

//    private void adoptionStatusValidation() {
//        //Cat's Gender Validation
//        int adoptionStatusSpinner = spinnerAdoptionStatus.getSelectedItemPosition();
//        if (adoptionStatusSpinner == 0) {
//            Toast.makeText(getApplicationContext(), "Choose Adoption Status", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//
//        }
//    }

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
