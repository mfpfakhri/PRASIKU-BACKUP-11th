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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;
import clouwiko.dev.prasiku.activity.model.Cat;
import clouwiko.dev.prasiku.activity.other.RoundedCornersTransform;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditCatDataAvailableActivity extends AppCompatActivity {

    private EditText inputCatDob, inputCatDesc, inputCatMedNote;
    private MaterialSpinner spinnerCatGender, spinnerCatReasonOpenAdoption, spinnerAdoptionStatus;
    private RadioGroup radioGroupVaccine, radioGroupSpayNeuter;
    private RadioButton radioButtonVacc, radioButtonSpayNeuter;
    private DatePickerDialog.OnDateSetListener catDobSetListener;

    private Button btnDoneEditing;

    private DatabaseReference databaseCats, databaseAdoptions, databaseAdoptionsReject;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cat_data_available);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editcatavailable_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Cat Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputCatDob = findViewById(R.id.editcatavailable_dob);
        inputCatDesc = findViewById(R.id.editcatavailable_desc);
        inputCatMedNote = findViewById(R.id.editcatavailable_mednote);
        spinnerCatGender = findViewById(R.id.editcatavailable_gender);
        spinnerCatReasonOpenAdoption = findViewById(R.id.editcatavailable_reason);
        spinnerAdoptionStatus = findViewById(R.id.editcatavailable_adoptionstatus);
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
                switch (gender) {
                    case "Male":
                        spinnerCatGender.setSelection(1);
                        break;
                    case "Female":
                        spinnerCatGender.setSelection(2);
                        break;
                    case "Unknown":
                        spinnerCatGender.setSelection(3);
                        break;
                    default:

                }
                String vaccine = dataSnapshot.child("catVaccStat").getValue(String.class);
                switch (vaccine) {
                    case "Yes, Already Vaccinated":
                        radioGroupVaccine.check(R.id.editcatavailable_yes_vaccine);
                        break;
                    case "Not Yet":
                        radioGroupVaccine.check(R.id.editcatavailable_no_vaccine);
                        break;
                    default:

                }
                String spayneuter = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
                switch (spayneuter) {
                    case "Yes, Already Spayed/ Neutered":
                        radioGroupSpayNeuter.check(R.id.editcatavailable_yes_spayneuter);
                        break;
                    case "Not Yet":
                        radioGroupSpayNeuter.check(R.id.editcatavailable_no_spayneuter);
                        break;
                    default:

                }
                String reason = dataSnapshot.child("catReason").getValue(String.class);
                switch (reason) {
                    case "Stray":
                        spinnerCatReasonOpenAdoption.setSelection(1);
                        break;
                    case "Abandoned":
                        spinnerCatReasonOpenAdoption.setSelection(2);
                        break;
                    case "Abused":
                        spinnerCatReasonOpenAdoption.setSelection(3);
                        break;
                    case "Owner Dead":
                        spinnerCatReasonOpenAdoption.setSelection(4);
                        break;
                    case "Owner Give Up":
                        spinnerCatReasonOpenAdoption.setSelection(5);
                        break;
                    case "House Moving":
                        spinnerCatReasonOpenAdoption.setSelection(6);
                        break;
                    case "Financial":
                        spinnerCatReasonOpenAdoption.setSelection(7);
                        break;
                    case "Medical Problem":
                        spinnerCatReasonOpenAdoption.setSelection(8);
                        break;
                    case "Others":
                        spinnerCatReasonOpenAdoption.setSelection(9);
                        break;
                    default:

                }
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
                inputCatDob.setText(dob);
                inputCatDesc.setText(desc);
                inputCatMedNote.setText(mednote);
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
                int adoptionReasonSpinner = spinnerCatReasonOpenAdoption.getSelectedItemPosition();
                if (adoptionReasonSpinner == 0) {
                    Toast.makeText(getApplicationContext(), "Choose Reason for Open Adopt", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

                //Cat's Gender Validation
                int adoptionStatusSpinner = spinnerAdoptionStatus.getSelectedItemPosition();
                switch (adoptionStatusSpinner) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Choose Adoption Status", Toast.LENGTH_SHORT).show();
                        return;
                    case 1:
                        final String cat_extra = getIntent().getStringExtra("cat_id");
                        String catapponstatus = cat_extra + "_Received";
                        databaseAdoptionsReject = FirebaseDatabase.getInstance().getReference().child("adoptions");
                        databaseAdoptionsReject.orderByChild("adoptionCatIdApponStatus").equalTo(catapponstatus).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot updRejectAppSnapshot : dataSnapshot.getChildren()) {
                                    Adoption updRejectAppAdoption = updRejectAppSnapshot.getValue(Adoption.class);
                                    updRejectAppAdoption.setAdoptionApplicationStatus("Rejected");
                                    updRejectAppAdoption.setAdoptionCatIdApponStatus(updRejectAppAdoption.getAdoptionCatId() + "_Rejected");
                                    updRejectAppAdoption.setAdoptionOwnerIdApponStatus(updRejectAppAdoption.getAdoptionOwnerId() + "_Rejected");
                                    databaseAdoptionsReject.child(updRejectAppAdoption.getAdoptionId()).setValue(updRejectAppAdoption);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cat_extra);
                        databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Cat cat = dataSnapshot.getValue(Cat.class);
                                cat.setCatAdoptedStatus("Adopted");
                                databaseCats.child(cat_extra).setValue(cat);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        backToMainMenu();
                        finish();
                    case 2:
                        String cId = getIntent().getStringExtra("cat_id");
                        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
                        databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int selectedVacc = radioGroupVaccine.getCheckedRadioButtonId();
                                radioButtonVacc = findViewById(selectedVacc);
                                int selectedSpayNeuter = radioGroupSpayNeuter.getCheckedRadioButtonId();
                                radioButtonSpayNeuter = findViewById(selectedSpayNeuter);

                                String dobupdate = inputCatDob.getText().toString().trim();
                                String descriptionupdate = inputCatDesc.getText().toString().trim();
                                String mednoteupdate = inputCatMedNote.getText().toString().trim();
                                String genderupdate = spinnerCatGender.getSelectedItem().toString().trim();
                                String vaccstatupdate = radioButtonVacc.getText().toString().trim();
                                String spayneuterstatupdate = radioButtonSpayNeuter.getText().toString().trim();
                                String reasonupdate = spinnerCatReasonOpenAdoption.getSelectedItem().toString().trim();
                                String adoptedstatusupdate = spinnerAdoptionStatus.getSelectedItem().toString().trim();

                                String nameupdate = dataSnapshot.child("catName").getValue(String.class);
                                String catidupdate = dataSnapshot.child("catId").getValue(String.class);
                                String ownerupdate = dataSnapshot.child("catOwnerId").getValue(String.class);
                                String photoupdate = dataSnapshot.child("catProfilePhoto").getValue(String.class);
                                String catprovinceupdate = dataSnapshot.child("catProvince").getValue(String.class);
                                String catcityupdate = dataSnapshot.child("catCity").getValue(String.class);
                                String catdelete = dataSnapshot.child("catDeleteStatus").getValue(String.class);
                                String ownercatdelete = dataSnapshot.child("catOwnerDeleteStatus").getValue(String.class);
                                String catcitydeletestatus = dataSnapshot.child("catCityDeleteStatus").getValue(String.class);

                                updateCatData(catidupdate, ownerupdate, photoupdate, nameupdate, dobupdate, genderupdate, descriptionupdate, mednoteupdate, vaccstatupdate, spayneuterstatupdate, reasonupdate, catprovinceupdate, catcityupdate, adoptedstatusupdate, catdelete, ownercatdelete, catcitydeletestatus);
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

    private boolean updateCatData(String catId, String catOwnerId, String catProfilePhoto, String catName, String catDob, String catGender, String catDescription, String catMedNote, String catVaccStat, String catSpayNeuterStat, String catReason, String catProvince, String catCity, String catAdoptedStatus, String catDeleteStatus, String catOwnerDeleteStatus, String catCityDeleteStatus) {
        String cId = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);

        Cat cat = new Cat(catId, catOwnerId, catProfilePhoto, catName, catDob, catGender, catDescription, catMedNote, catVaccStat, catSpayNeuterStat, catReason, catProvince, catCity, catAdoptedStatus, catDeleteStatus, catOwnerDeleteStatus, catCityDeleteStatus);

        databaseCats.setValue(cat);

        return true;
    }

    private void backToMainMenu() {
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
        //Cat's Gender Validation
        int adoptionStatusSpinner = spinnerAdoptionStatus.getSelectedItemPosition();
        if (adoptionStatusSpinner == 0) {
            Toast.makeText(getApplicationContext(), "Choose Adoption Status", Toast.LENGTH_SHORT).show();
            return;
        } else {

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
