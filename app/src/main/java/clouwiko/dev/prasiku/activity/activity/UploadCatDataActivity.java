package clouwiko.dev.prasiku.activity.activity;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Cat;
import fr.ganfra.materialspinner.MaterialSpinner;

public class UploadCatDataActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;
    private DatabaseReference databaseCats, databaseUsers;
    private StorageReference storageCats;
    private static final String STORAGE_PATH = "catPhoto/";

    private DatePickerDialog.OnDateSetListener catDobSetListener;

    private EditText inputCatName, inputCatDob, inputCatDesc, inputCatMedNote;
    private MaterialSpinner spinnerCatGender, spinnerCatReasonOpenAdoption;
    private RadioGroup radioGroupVaccine, radioGroupSpayNeuter;
    private RadioButton radioButtonVacc, radioButtonSpayNeuter;
    private ImageView catPhotoIv;
    private Button btnUploadCatData;
    Uri uriCatPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_cat_data);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        catPhotoIv = (ImageView) findViewById(R.id.userPhotos);
        inputCatName = (EditText) findViewById(R.id.cat_name);
        inputCatDob = (EditText) findViewById(R.id.catDobDatepicker);
        inputCatDesc = (EditText) findViewById(R.id.catDesc);
        inputCatMedNote = (EditText) findViewById(R.id.catMedicalNote);
        spinnerCatGender = (MaterialSpinner) findViewById(R.id.catGender);
        spinnerCatReasonOpenAdoption = (MaterialSpinner) findViewById(R.id.catReasonOpenAdoption);
        radioGroupVaccine = (RadioGroup) findViewById(R.id.radioGroupVaccine);
        radioGroupSpayNeuter = (RadioGroup) findViewById(R.id.radioGroupSpayNeuter);
        btnUploadCatData = (Button) findViewById(R.id.action_upload_cat_data);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null) {
                    //User Auth State is Changed - User is Null
                    //Launch Login Activity
                    startActivity(new Intent(UploadCatDataActivity.this, LandingActivity.class));
                    finish();
                }
            }
        };

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        catPhotoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catPhotosMediaOpen();
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
                        UploadCatDataActivity.this,
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

                String date = dayOfMonth + "/" + month + "/" + year;
                inputCatDob.setText(date);
            }
        };

        btnUploadCatData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCatDataValidation();
                addCatData();
                Toast.makeText(UploadCatDataActivity.this, "Successfully Added Cats Data for Adopt", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadCatDataActivity.this, LandingActivity.class));
            }
        });
    }

    private void addCatData() {
        //TODO: Initial cities adapter
        final List<String> provincesNameArray;
        final List<String> citiesNameArray;
        //--

        //Get Current User UID
        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //TODO: Array List for userProvince and userCity
        provincesNameArray = new ArrayList<>();
        citiesNameArray = new ArrayList<>();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.orderByKey().equalTo(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    final String userProvinceValue = userSnapshot.child("userProvince").getValue(String.class);
                    final String userCityValue = userSnapshot.child("userCity").getValue(String.class);
                    provincesNameArray.add(userProvinceValue);
                    citiesNameArray.add(userCityValue);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //TODO:MFP
        databaseCats = FirebaseDatabase.getInstance().getReference("cats");
        storageCats = FirebaseStorage.getInstance().getReference();
        StorageReference reference = storageCats.child(STORAGE_PATH + System.currentTimeMillis() + "." + getActualImage(uriCatPhoto));
        reference.putFile(uriCatPhoto)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        int selectedVacc = radioGroupVaccine.getCheckedRadioButtonId();
                        radioButtonVacc = (RadioButton) findViewById(selectedVacc);
                        int selectedSpayNeuter = radioGroupSpayNeuter.getCheckedRadioButtonId();
                        radioButtonSpayNeuter = (RadioButton) findViewById(selectedSpayNeuter);

                        String id = databaseCats.push().getKey();
                        String ownerId = userUID;
                        String cPhotoUrl = taskSnapshot.getDownloadUrl().toString();
                        String name = inputCatName.getText().toString().trim();
                        String dob = inputCatDob.getText().toString().trim();
                        String spinnerGender = spinnerCatGender.getSelectedItem().toString().trim();
                        String desc = inputCatDesc.getText().toString().trim();
                        String medNote = inputCatMedNote.getText().toString().trim();
                        String vacc = radioButtonVacc.getText().toString().trim();
                        String spayNeuter = radioButtonSpayNeuter.getText().toString().trim();
                        String spinnerReason = spinnerCatReasonOpenAdoption.getSelectedItem().toString().trim();
                        String province = provincesNameArray.get(0).toString().trim();
                        String city = citiesNameArray.get(0).toString().trim();

                        Cat cat = new Cat(id, ownerId, cPhotoUrl, name, dob, spinnerGender, desc, medNote, vacc, spayNeuter, spinnerReason, province, city);

                        databaseCats.child(userUID).child(id).setValue(cat);
                    }
                });
    }

    private void catPhotosMediaOpen() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image from Gallery"), 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            cropUserImage();
        } else if (requestCode == 2) {
            if (data != null) {
                uriCatPhoto = data.getData();
                cropUserImage();
            }
        } else if (requestCode == 1) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                catPhotoIv.setImageBitmap(bitmap);
            }
        }
    }

    private String getActualImage(Uri uriUserPhoto) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uriUserPhoto));
    }

    private void cropUserImage() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uriCatPhoto, "image/*");

            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("outputX", 180);
            cropIntent.putExtra("outputY", 180);
            cropIntent.putExtra("aspectX", 4);
            cropIntent.putExtra("aspectY", 4);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            cropIntent.putExtra("return-data", true);

            startActivityForResult(cropIntent, 1);
        } catch (ActivityNotFoundException ex) {

        }
    }

    private void uploadCatDataValidation() {
        //Cat Photos Validation
        if (catPhotoIv.getDrawable() != null) {

        } else {
            Toast.makeText(getApplicationContext(), "Choose Your Cat Photo", Toast.LENGTH_SHORT).show();
            return;
        }

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
            Toast.makeText(UploadCatDataActivity.this, "Choose Your Cat's Gender", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(UploadCatDataActivity.this, "Choose The Reason for Open Adopt", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
