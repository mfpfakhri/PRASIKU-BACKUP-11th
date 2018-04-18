package clouwiko.dev.prasiku.activity.activity;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Cat;
import clouwiko.dev.prasiku.activity.other.RoundedCornersTransform;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditCatDataAvailableActivity extends AppCompatActivity {

    private EditText inputCatName, inputCatDob, inputCatDesc, inputCatMedNote;
    private MaterialSpinner spinnerCatGender, spinnerCatReasonOpenAdoption;
    private RadioGroup radioGroupVaccine, radioGroupSpayNeuter;
    private RadioButton radioButtonVacc, radioButtonSpayNeuter;
    private DatePickerDialog.OnDateSetListener catDobSetListener;

    private Button btnDoneEditing;

    private DatabaseReference databaseCats;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cat_data_available);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editcatavailable_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Cat Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputCatName = findViewById(R.id.editcatavailable_name);
        inputCatDob = findViewById(R.id.editcatavailable_dob);
        inputCatDesc = findViewById(R.id.editcatavailable_desc);
        inputCatMedNote = findViewById(R.id.editcatavailable_mednote);
        spinnerCatGender = findViewById(R.id.editcatavailable_gender);
        spinnerCatReasonOpenAdoption = findViewById(R.id.editcatavailable_reason);
        radioGroupVaccine = findViewById(R.id.editcatavailable_vacc);
        radioGroupSpayNeuter = findViewById(R.id.editcatavailable_spayneuter);
        btnDoneEditing = findViewById(R.id.editcatavailable_done_button);

        String catId = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catId);
        databaseCats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("catName").getValue(String.class);
                String dob = dataSnapshot.child("catDob").getValue(String.class);
                String desc = dataSnapshot.child("catDescription").getValue(String.class);
                String mednote = dataSnapshot.child("catMedNote").getValue(String.class);
                String gender = dataSnapshot.child("catGender").getValue(String.class);
                String vaccine = dataSnapshot.child("catVaccStat").getValue(String.class);
                String spayneuter = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
                String reason = dataSnapshot.child("catReason").getValue(String.class);

                inputCatName.setText(name);
                inputCatDob.setText(dob);
                inputCatDesc.setText(desc);
                inputCatMedNote.setText(mednote);
                if (gender.equals("Male")) {
                    spinnerCatGender.setSelection(1);
                } else if (gender.equals("Female")) {
                    spinnerCatGender.setSelection(2);
                } else if (gender.equals("Unknown")) {
                    spinnerCatGender.setSelection(3);
                } else {
                    Toast.makeText(getApplicationContext(), "Choose Your Cat Gender", Toast.LENGTH_SHORT).show();
                }
                if (vaccine.equals("Yes, Already Vaccinated")) {
                    radioGroupVaccine.check(R.id.editcatavailable_yes_vaccine);
                } else {
                    radioGroupVaccine.check(R.id.editcatavailable_no_vaccine);
                }
                if (spayneuter.equals("Yes, Already Spayed/ Neutered")) {
                    radioGroupSpayNeuter.check(R.id.editcatavailable_yes_spayneuter);
                } else {
                    radioGroupSpayNeuter.check(R.id.editcatavailable_no_spayneuter);
                }
                if (reason.equals("Stray")) {
                    spinnerCatReasonOpenAdoption.setSelection(1);
                } else if (reason.equals("Abandoned")) {
                    spinnerCatReasonOpenAdoption.setSelection(2);
                } else if (reason.equals("Abused")) {
                    spinnerCatReasonOpenAdoption.setSelection(3);
                } else if (reason.equals("Owner Dead")) {
                    spinnerCatReasonOpenAdoption.setSelection(4);
                } else if (reason.equals("Owner Give Up")) {
                    spinnerCatReasonOpenAdoption.setSelection(5);
                } else if (reason.equals("House Moving")) {
                    spinnerCatReasonOpenAdoption.setSelection(6);
                } else if (reason.equals("Financial")) {
                    spinnerCatReasonOpenAdoption.setSelection(7);
                } else if (reason.equals("Medical Problem")) {
                    spinnerCatReasonOpenAdoption.setSelection(8);
                } else if (reason.equals("Others")) {
                    spinnerCatReasonOpenAdoption.setSelection(9);
                } else {
                    Toast.makeText(getApplicationContext(), "Choose The Reason for Open Adopt", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        inputCatDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditCatDataAvailableActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        catDobSetListener,
                        day, month, year);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        catDobSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int dayOfMonth, int month, int year) {
                month = month + 1;
                Log.d("onDateSet: date: " + dayOfMonth + "/" + month + "/" + year, catDobSetListener.toString());

                String date = dayOfMonth + "-" + month + "-" + year;
                inputCatDob.setText(date);
            }
        };

        btnDoneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cId = getIntent().getStringExtra("cat_id");
                databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
                databaseCats.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int selectedVacc = radioGroupVaccine.getCheckedRadioButtonId();
                        radioButtonVacc = findViewById(selectedVacc);
                        int selectedSpayNeuter = radioGroupSpayNeuter.getCheckedRadioButtonId();
                        radioButtonSpayNeuter = findViewById(selectedSpayNeuter);

                        String catName = inputCatName.getText().toString().trim();
                        String catDob = inputCatDob.getText().toString().trim();
                        String catDescription = inputCatDesc.getText().toString().trim();
                        String catMedNote = inputCatMedNote.getText().toString().trim();
                        String catGender = spinnerCatGender.getSelectedItem().toString().trim();
                        String catVaccStat = radioButtonVacc.getText().toString().trim();
                        String catSpayNeuterStat = radioButtonSpayNeuter.getText().toString().trim();
                        String catReason = spinnerCatReasonOpenAdoption.getSelectedItem().toString().trim();

                        String catId = dataSnapshot.child("catId").getValue(String.class);
                        String catOwnerId = dataSnapshot.child("catOwnerId").getValue(String.class);
                        String catProfilePhoto = dataSnapshot.child("catProfilePhoto").getValue(String.class);
                        String catProvince = dataSnapshot.child("catProvince").getValue(String.class);
                        String catCity = dataSnapshot.child("catCity").getValue(String.class);
                        String catAdoptedStatus = dataSnapshot.child("catAdoptedStatus").getValue(String.class);

                        catDataValidation();
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

    private boolean updateCatData(String catId, String catOwnerId, String catProfilePhoto, String catName, String catDob, String catGender, String catDescription, String catMedNote, String catVaccStat, String catSpayNeuterStat, String catReason, String catProvince, String catCity, String catAdoptedStatus) {
        String cId = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);

        Cat cat = new Cat(catId, catOwnerId, catProfilePhoto, catName, catDob, catGender, catDescription, catMedNote, catVaccStat, catSpayNeuterStat, catReason, catProvince, catCity, catAdoptedStatus);

        databaseCats.setValue(cat);

        return true;
    }

    private void backToPreviousActivity() {
        String catId = getIntent().getStringExtra("cat_id");
        String ownerId = getIntent().getStringExtra("owner_id");
        String pActivity = getIntent().getStringExtra("previousActivity");
        Intent intent = new Intent(getApplicationContext(), CatProfileOwnerAvailableActivity.class);
        if (pActivity.equals("findcat")) {
            intent.putExtra("previousActivity", pActivity);
            intent.putExtra("cat_id", catId);
            intent.putExtra("owner_id", ownerId);
            Toast.makeText(getApplicationContext(), "Cat Data Successfully Edited", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        } else if (pActivity.equals("adoptionlist")) {
            intent.putExtra("previousActivity", pActivity);
            intent.putExtra("cat_id", catId);
            intent.putExtra("owner_id", ownerId);
            Toast.makeText(getApplicationContext(), "Cat Data Successfully Edited", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }
    }

    private void catDataValidation() {
        //Cat's Name Validation
        String cName = inputCatName.getText().toString();
        if (TextUtils.isEmpty(cName)) {
            Toast.makeText(getApplicationContext(), "Enter Your Cat Name", Toast.LENGTH_SHORT).show();
        }

        //Cat's DOB Validation
        String catDobDate = inputCatDob.getText().toString().trim();
        if (TextUtils.isEmpty(catDobDate)) {
            Toast.makeText(getApplicationContext(), "Enter Your Cat's Birth Date", Toast.LENGTH_SHORT).show();
            return;
        }

        //Cat's Gender Validation
        int catSpinnerPosition = spinnerCatGender.getSelectedItemPosition();
        if (catSpinnerPosition != 0) {

        } else {
            Toast.makeText(getApplicationContext(), "Choose Your Cat's Gender", Toast.LENGTH_SHORT).show();
            return;
        }

        //Cat's Description Validation
        String desc = inputCatDesc.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            Toast.makeText(getApplicationContext(), "Please Describe Your Cat", Toast.LENGTH_SHORT).show();
            return;
        }

        //Vaccine Status Validation
        if (radioGroupVaccine.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Choose Cat's Vaccine Status", Toast.LENGTH_SHORT).show();
            return;
        } else {

        }

        //Spay/ Neuter Validation
        if (radioGroupSpayNeuter.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Choose Cat's Spay/ Neuter Status", Toast.LENGTH_SHORT).show();
            return;
        } else {

        }

        //Open Adoption Reason
        int catReasonPosition = spinnerCatReasonOpenAdoption.getSelectedItemPosition();
        if (catReasonPosition != 0) {

        } else {
            Toast.makeText(getApplicationContext(), "Choose The Reason for Open Adopt", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCatDataAvailableActivity.this);
        builder.setMessage("Are You sure want to quit editing?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pActivity = getIntent().getStringExtra("previousActivity");
                        String catId = getIntent().getStringExtra("cat_id");
                        String ownerId = getIntent().getStringExtra("owner_id");
                        Intent intent = new Intent(getApplicationContext(), CatProfileOwnerAvailableActivity.class);
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
