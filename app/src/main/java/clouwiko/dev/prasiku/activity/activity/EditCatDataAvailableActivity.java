package clouwiko.dev.prasiku.activity.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.other.RoundedCornersTransform;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditCatDataAvailableActivity extends AppCompatActivity {

    private EditText inputCatName, inputCatDob, inputCatDesc, inputCatMedNote;
    private MaterialSpinner spinnerCatGender, spinnerCatReasonOpenAdoption;
    private RadioGroup radioGroupVaccine, radioGroupSpayNeuter;
    private RadioButton radioButtonVacc, radioButtonSpayNeuter;
    private ImageView catPhotoIv;
    Uri uriCatPhoto;

    private Button btnDoneEditing;

    private DatabaseReference databaseCats;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cat_data_available);

        Toolbar toolbar = (Toolbar)findViewById(R.id.editcatavailable_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Cat Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputCatName = findViewById(R.id.editcatavailable_name);
        inputCatDob = findViewById(R.id.editcatavailable_dob);
        inputCatDesc = findViewById(R.id.editcatavailable_desc);
        inputCatMedNote = findViewById(R.id.editcatavailable_mednote);
        catPhotoIv = findViewById(R.id.editcatavailable_photo);
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
                String photo = dataSnapshot.child("catProfilePhoto").getValue(String.class);

                inputCatName.setText(name);
                inputCatDob.setText(dob);
                inputCatDesc.setText(desc);
                inputCatMedNote.setText(mednote);
                Picasso.get().load(photo).transform(new RoundedCornersTransform()).centerCrop().resize(192, 192).into(catPhotoIv);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        catPhotoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catPhotosMediaOpen();
            }
        });

        btnDoneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catDataValidation();
//                updateCatData();
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

    private void catDataValidation() {
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
}
