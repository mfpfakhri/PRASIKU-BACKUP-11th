package clouwiko.dev.prasiku.activity.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
                String catId = dataSnapshot.child("catId").getValue(String.class);
                String catOwnerId = dataSnapshot.child("catOwnerId").getValue(String.class);
                String catProfilePhoto = dataSnapshot.child("catProfilePhoto").getValue(String.class);
                String catName = dataSnapshot.child("catName").getValue(String.class);
                String catDob = dataSnapshot.child("catDob").getValue(String.class);
                String catGender = dataSnapshot.child("catGender").getValue(String.class);
                String catDescription = dataSnapshot.child("catDescription").getValue(String.class);
                String catMedNote = dataSnapshot.child("catMedNote").getValue(String.class);
                String catVaccStat = dataSnapshot.child("catVaccStat").getValue(String.class);
                String catSpayNeuterStat = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
                String catReason = dataSnapshot.child("catReason").getValue(String.class);
                String catProvince = dataSnapshot.child("catProvince").getValue(String.class);
                String catCity = dataSnapshot.child("catCity").getValue(String.class);
                String catAdoptedStatus = dataSnapshot.child("catAdoptedStatus").getValue(String.class);

                if (catAdoptedStatus.equals("1")) {
                    spinnerAdoptionStatus.setSelection(1);
                } else if (catAdoptedStatus.equals("0")) {
                    spinnerAdoptionStatus.setSelection(2);
                } else {
                    Toast.makeText(getApplicationContext(), "Choose " + catName + " Adoption Status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnDoneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cId = getIntent().getStringExtra("cat_id");
                databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
                databaseCats.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String catId = dataSnapshot.child("catId").getValue(String.class);
                        String catOwnerId = dataSnapshot.child("catOwnerId").getValue(String.class);
                        String catProfilePhoto = dataSnapshot.child("catProfilePhoto").getValue(String.class);
                        String catName = dataSnapshot.child("catName").getValue(String.class);
                        String catDob = dataSnapshot.child("catDob").getValue(String.class);
                        String catGender = dataSnapshot.child("catGender").getValue(String.class);
                        String catDescription = dataSnapshot.child("catDescription").getValue(String.class);
                        String catMedNote = dataSnapshot.child("catMedNote").getValue(String.class);
                        String catVaccStat = dataSnapshot.child("catVaccStat").getValue(String.class);
                        String catSpayNeuterStat = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
                        String catReason = dataSnapshot.child("catReason").getValue(String.class);
                        String catProvince = dataSnapshot.child("catProvince").getValue(String.class);
                        String catCity = dataSnapshot.child("catCity").getValue(String.class);
                        String adoptedValue = spinnerAdoptionStatus.getSelectedItem().toString().trim();
                        String catAdoptedStatus = null;

                        if (adoptedValue.equals("Available")){
                            catAdoptedStatus = "0";
                        } else if (adoptedValue.equals("Adopted")){
                            catAdoptedStatus = "1";
                        }
                        
                        adoptionStatusValidation();
                        updateCatData(catId, catOwnerId, catProfilePhoto, catName, catDob, catGender, catDescription, catMedNote, catVaccStat, catSpayNeuterStat, catReason, catProvince, catCity, catAdoptedStatus);
                        backToPreviousActivity();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void backToPreviousActivity() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        Toast toast = Toast.makeText(getApplicationContext(), "Cat Adoption Status Successfully Edited", Toast.LENGTH_SHORT);
        toast.show();
        startActivity(intent);
        toast.cancel();
        finish();
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
//            Intent intent = new Intent(getApplicationContext(), AdoptionListActivity.class);
//            intent.putExtra("previousActivity", pActivity);
//            intent.putExtra("cat_id", catId);
//            intent.putExtra("owner_id", ownerId);
//            Toast.makeText(getApplicationContext(), "Cat Adoption Status Successfully Edited", Toast.LENGTH_SHORT).show();
//            startActivity(intent);
//            finish();
//        }
    }

    private boolean updateCatData(String catId, String catOwnerId, String catProfilePhoto, String catName, String catDob, String catGender, String catDescription, String catMedNote, String catVaccStat, String catSpayNeuterStat, String catReason, String catProvince, String catCity, String catAdoptedStatus) {
        String cId = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);

        Cat cat = new Cat(catId, catOwnerId, catProfilePhoto, catName, catDob, catGender, catDescription, catMedNote, catVaccStat, catSpayNeuterStat, catReason, catProvince, catCity, catAdoptedStatus);

        databaseCats.setValue(cat);

        return true;
    }

    private void adoptionStatusValidation() {
        //Cat's Gender Validation
        int adoptionStatusSpinner = spinnerAdoptionStatus.getSelectedItemPosition();
        if (adoptionStatusSpinner != 0) {

        } else {
            Toast.makeText(getApplicationContext(), "Choose Adoption Status", Toast.LENGTH_SHORT).show();
            return;
        }
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
