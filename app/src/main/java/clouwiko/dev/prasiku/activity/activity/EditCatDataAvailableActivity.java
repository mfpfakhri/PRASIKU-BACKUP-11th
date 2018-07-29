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
    private ImageView ivPhoto, btnUpdatePhoto, btnDeletePhoto;
    private EditText etName, etDob, etDesc, etMedNote;
    private MaterialSpinner msGender, msReason, msAdoptionStatus;
    private RadioGroup rgVaccine, rgSpayNeuter;
    private RadioButton rbVaccine, rbSpayNeuter;
    private Button btnUpdate;
    private DatabaseReference databaseCats, databaseAdoptions;
    private DatePickerDialog.OnDateSetListener catDobSetListener;
    private StorageReference storageCats;
    private static final String STORAGE_PATH = "catPhoto/";
    Uri uriCatPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cat_data_available);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editcatavailable_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sunting Profil Kucing");

        ivPhoto = findViewById(R.id.editcatavailable_photo_imageview);
        btnUpdatePhoto = findViewById(R.id.editcatavailable_photo_edit_button);
        btnDeletePhoto = findViewById(R.id.editcatavailable_photo_delete_button);
        etName = findViewById(R.id.editcatavailable_name);
        etDob = findViewById(R.id.editcatavailable_dob);
        etDesc = findViewById(R.id.editcatavailable_desc);
        etMedNote = findViewById(R.id.editcatavailable_mednote);
        msGender = findViewById(R.id.editcatavailable_gender);
        msReason = findViewById(R.id.editcatavailable_reason);
        msAdoptionStatus = findViewById(R.id.editcatavailable_adoptionstatus);
        rgVaccine = findViewById(R.id.editcatavailable_vacc);
        rgSpayNeuter = findViewById(R.id.editcatavailable_spayneuter);
        btnUpdate = findViewById(R.id.editcatavailable_done_button);

        btnUpdatePhoto.setVisibility(View.GONE);
        btnDeletePhoto.setVisibility(View.GONE);

        final String cId = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
        databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cat catData = dataSnapshot.getValue(Cat.class);
                String name = catData.getCatName();
                String dob = catData.getCatDob();
                String description = catData.getCatDescription();
                String medicalnote = catData.getCatMedNote();
                String gender = catData.getCatGender();
                String photo = catData.getCatProfilePhoto();
                if (catData.getCatProfilePhoto().equals("")) {
                    btnUpdatePhoto.setVisibility(View.VISIBLE);
                } else {
                    btnDeletePhoto.setVisibility(View.VISIBLE);
                    Picasso.get().load(photo).resize(256, 256).centerInside().into(ivPhoto);
                }
                switch (gender) {
                    case "Male":
                        msGender.setSelection(1);
                        break;
                    case "Female":
                        msGender.setSelection(2);
                        break;
                    case "Unknown":
                        msGender.setSelection(3);
                        break;
                    default:

                }
                String reason = catData.getCatReason();
                switch (reason) {
                    case "Stray":
                        msReason.setSelection(1);
                        break;
                    case "Abandoned":
                        msReason.setSelection(2);
                        break;
                    case "Abused":
                        msReason.setSelection(3);
                        break;
                    case "Owner Dead":
                        msReason.setSelection(4);
                        break;
                    case "Owner Give Up":
                        msReason.setSelection(5);
                        break;
                    case "House Moving":
                        msReason.setSelection(6);
                        break;
                    case "Financial":
                        msReason.setSelection(7);
                        break;
                    case "Medical Problem":
                        msReason.setSelection(8);
                        break;
                    case "Others":
                        msReason.setSelection(9);
                        break;
                    default:

                }
                String adoptionstatus = catData.getCatAdoptedStatus();
                switch (adoptionstatus) {
                    case "Adopted":
                        msAdoptionStatus.setSelection(1);
                        break;
                    case "Available":
                        msAdoptionStatus.setSelection(2);
                        break;
                    default:

                }
                String vaccine = catData.getCatVaccStat();
                switch (vaccine) {
                    case "Yes, Already Vaccinated":
                        rgVaccine.check(R.id.editcatavailable_yes_vaccine);
                        break;
                    case "Not Yet":
                        rgVaccine.check(R.id.editcatavailable_no_vaccine);
                        break;
                    default:

                }
                String spayneuter = catData.getCatSpayNeuterStat();
                switch (spayneuter) {
                    case "Yes, Already Spayed/ Neutered":
                        rgSpayNeuter.check(R.id.editcatavailable_yes_spayneuter);
                        break;
                    case "Not Yet":
                        rgSpayNeuter.check(R.id.editcatavailable_no_spayneuter);
                        break;
                    default:

                }
                etName.setText(name);
                etDob.setText(dob);
                etDesc.setText(description);
                etMedNote.setText(medicalnote);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditCatDataAvailableActivity.this);
                builder.setMessage("Apakah Kamu yakin Ingin Menghapus Foto Kucing Kamu?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
                                databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Cat catData = dataSnapshot.getValue(Cat.class);
                                        String photo = catData.getCatProfilePhoto();
                                        try {
                                            if (photo.equals("")) {
                                                Toast.makeText(getApplicationContext(), "Tidak ada Foto yang Terpasang", Toast.LENGTH_SHORT).show();
                                            } else {
                                                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(photo);
                                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "Kucing Kamu Tidak Memiliki Foto Sekarang", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                catData.setCatProfilePhoto("");
                                                databaseCats.setValue(catData);
                                                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions");
                                                Query adoptionDeletePhoto = databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId);
                                                adoptionDeletePhoto.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot adoptionDeletePhotoSnapshot : dataSnapshot.getChildren()) {
                                                            Adoption adoptionData = adoptionDeletePhotoSnapshot.getValue(Adoption.class);
                                                            adoptionData.setAdoptionCatPhoto("");
                                                            databaseAdoptions.child(adoptionData.getAdoptionId()).setValue(adoptionData);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                startActivity(getIntent());
                                            }
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Tidak", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnUpdatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaOpen();
            }
        });

        etDob.setOnClickListener(new View.OnClickListener() {
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

                String date = dayOfMonth + "/" + month + "/" + year;
                etDob.setText(date);
            }
        };

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cId = getIntent().getStringExtra("cat_id");
                databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions");

                String valName = etName.getText().toString().trim();
                if (valName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ketikkan Nama Kucing", Toast.LENGTH_SHORT).show();
                    return;
                }

                String valDob = etDob.getText().toString().trim();
                if (valDob.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Masukkan Tanggal Lahir Kucing", Toast.LENGTH_SHORT).show();
                    return;
                }

                int valGender = msGender.getSelectedItemPosition();
                if (valGender != 0) {

                } else {
                    Toast.makeText(getApplicationContext(), "Pilih Jenis Kelamin Kucing", Toast.LENGTH_SHORT).show();
                    return;
                }

//                String valDescription = etDesc.getText().toString().trim();
//                if (valDescription.isEmpty()){
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Enter Cat Description", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                String valMedicalNote = etMedNote.getText().toString().trim();
                if (valMedicalNote.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Berikan Catatan Medis Kucing", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

                if (rgVaccine.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Pilih Status Vaksin", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

                if (rgSpayNeuter.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Pilih Status Kastrasi Kucing", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

                int valReason = msReason.getSelectedItemPosition();
                if (valReason != 0) {

                } else {
                    Toast.makeText(getApplicationContext(), "Pilih Alasan untuk Lepas Adopsi", Toast.LENGTH_SHORT).show();
                    return;
                }

                int valAdoptionStatus = msAdoptionStatus.getSelectedItemPosition();
                if (valAdoptionStatus != 0) {

                } else {
                    Toast.makeText(getApplicationContext(), "Pilih Status Adopsi", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateCat();

//                if (ivPhoto.getDrawable() != null) {
//                    databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Cat catCheck = dataSnapshot.getValue(Cat.class);
//                            String catPhotoCheck = catCheck.getCatProfilePhoto();
//                            if (catPhotoCheck.equals("")) {
//                                storageCats = FirebaseStorage.getInstance().getReference();
//                                StorageReference reference = storageCats.child(STORAGE_PATH + System.currentTimeMillis() + "." + getActualImage(uriCatPhoto));
//                                reference.putFile(uriCatPhoto)
//                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                            @Override
//                                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
//                                                int adoptionStatusSpinner = msAdoptionStatus.getSelectedItemPosition();
//                                                String cId = getIntent().getStringExtra("cat_id");
//                                                switch (adoptionStatusSpinner) {
//                                                    case 0:
//                                                        Toast.makeText(getApplicationContext(), "Pilih Status Adopsi", Toast.LENGTH_SHORT).show();
//                                                        return;
//                                                    case 1:
//                                                        databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                                Cat catUpdate = dataSnapshot.getValue(Cat.class);
//                                                                String updPhotoChange = taskSnapshot.getDownloadUrl().toString();
//                                                                String updNameChange = etName.getText().toString().trim();
//                                                                String updDobChange = etDob.getText().toString().trim();
//                                                                String updDescriptionChange = etDesc.getText().toString().trim();
//                                                                String updMedicalNoteChange = etMedNote.getText().toString().trim();
//                                                                String updGenderChange = msGender.getSelectedItem().toString().trim();
//                                                                String setUpdGender = null;
//                                                                switch (updGenderChange) {
//                                                                    case "Jantan":
//                                                                        setUpdGender = "Male";
//                                                                        break;
//                                                                    case "Betina":
//                                                                        setUpdGender = "Female";
//                                                                        break;
//                                                                    case "Tidak Diketahui":
//                                                                        setUpdGender = "Unknown";
//                                                                        break;
//                                                                }
//                                                                String updAdoptionStatusChange = "Adopted";
//                                                                int updVaccineId = rgVaccine.getCheckedRadioButtonId();
//                                                                rbVaccine = findViewById(updVaccineId);
//                                                                String updVaccineChange = rbVaccine.getText().toString().trim();
//                                                                String setUpdVacc = null;
//                                                                switch (updVaccineChange) {
//                                                                    case "Ya, Sudah Divaksin":
//                                                                        setUpdVacc = "Yes, Already Vaccinated";
//                                                                        break;
//                                                                    case "Belum Divaksin":
//                                                                        setUpdVacc = "Not Yet";
//                                                                        break;
//                                                                }
//                                                                int updSpayNeuterId = rgSpayNeuter.getCheckedRadioButtonId();
//                                                                rbSpayNeuter = findViewById(updSpayNeuterId);
//                                                                String updSpayNeuterChange = rbSpayNeuter.getText().toString().trim();
//                                                                String setUpdSpayNeuter = null;
//                                                                switch (updSpayNeuterChange) {
//                                                                    case "Ya, Sudah Dikastrasi":
//                                                                        setUpdSpayNeuter = "Yes, Already Spayed/ Neutered";
//                                                                        break;
//                                                                    case "Belum Dikastrasi":
//                                                                        setUpdSpayNeuter = "Not Yet";
//                                                                        break;
//                                                                }
//                                                                String updReasonChange = msReason.getSelectedItem().toString().trim();
//                                                                String setUpdReason = null;
//                                                                switch (updReasonChange) {
//                                                                    case "Liar":
//                                                                        setUpdReason = "Stray";
//                                                                        break;
//                                                                    case "Terlantar":
//                                                                        setUpdReason = "Abandoned";
//                                                                        break;
//                                                                    case "Disiksa":
//                                                                        setUpdReason = "Abused";
//                                                                        break;
//                                                                    case "Pemilik Meninggal":
//                                                                        setUpdReason = "Owner Dead";
//                                                                        break;
//                                                                    case "Pemilik Menyerah":
//                                                                        setUpdReason = "Owner Give Up";
//                                                                        break;
//                                                                    case "Pindah Rumah":
//                                                                        setUpdReason = "House Moving";
//                                                                        break;
//                                                                    case "Keuangan":
//                                                                        setUpdReason = "Financial";
//                                                                        break;
//                                                                    case "Masalah Kesehatan":
//                                                                        setUpdReason = "Medical Problem";
//                                                                        break;
//                                                                    case "Lainnya":
//                                                                        setUpdReason = "Others";
//                                                                        break;
//                                                                }
//
//                                                                catUpdate.setCatProfilePhoto(updPhotoChange);
//                                                                catUpdate.setCatName(updNameChange);
//                                                                catUpdate.setCatDob(updDobChange);
//                                                                catUpdate.setCatDescription(updDescriptionChange);
//                                                                catUpdate.setCatMedNote(updMedicalNoteChange);
//                                                                catUpdate.setCatGender(setUpdGender);
//                                                                catUpdate.setCatAdoptedStatus(updAdoptionStatusChange);
//                                                                catUpdate.setCatVaccStat(setUpdVacc);
//                                                                catUpdate.setCatSpayNeuterStat(setUpdSpayNeuter);
//                                                                catUpdate.setCatReason(setUpdReason);
//                                                                databaseCats.setValue(catUpdate);
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(DatabaseError databaseError) {
//
//                                                            }
//                                                        });
//                                                        databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                                for (DataSnapshot updateSnapshot : dataSnapshot.getChildren()) {
//                                                                    Adoption adoptionUpdate = updateSnapshot.getValue(Adoption.class);
//                                                                    String updPhotoChange = taskSnapshot.getDownloadUrl().toString();
//                                                                    String updNameChange = etName.getText().toString().trim();
//
//                                                                    adoptionUpdate.setAdoptionCatPhoto(updPhotoChange);
//                                                                    adoptionUpdate.setAdoptionCatName(updNameChange);
//                                                                    adoptionUpdate.setAdoptionApplicationStatus("Rejected");
//                                                                    adoptionUpdate.setAdoptionCatIdApponStatus(adoptionUpdate.getAdoptionCatId() + "_Rejected");
//                                                                    adoptionUpdate.setAdoptionOwnerIdApponStatus(adoptionUpdate.getAdoptionOwnerId() + "_Rejected");
//                                                                    databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(DatabaseError databaseError) {
//
//                                                            }
//                                                        });
//                                                        backToMainMenu();
//                                                        finish();
//                                                        break;
//                                                    case 2:
//                                                        databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                                Cat catUpdate = dataSnapshot.getValue(Cat.class);
//                                                                String updPhotoIdle = taskSnapshot.getDownloadUrl().toString();
//                                                                String updNameIdle = etName.getText().toString().trim();
//                                                                String updDob = etDob.getText().toString().trim();
//                                                                String updDescription = etDesc.getText().toString().trim();
//                                                                String updMedicalNote = etMedNote.getText().toString().trim();
//                                                                String updGender = msGender.getSelectedItem().toString().trim();
//                                                                String setGender = null;
//                                                                switch (updGender) {
//                                                                    case "Jantan":
//                                                                        setGender = "Male";
//                                                                        break;
//                                                                    case "Betina":
//                                                                        setGender = "Female";
//                                                                        break;
//                                                                    case "Tidak Diketahui":
//                                                                        setGender = "Unknown";
//                                                                        break;
//                                                                }
//                                                                String updAdoptionStatus = "Available";
//                                                                int updVaccineId = rgVaccine.getCheckedRadioButtonId();
//                                                                rbVaccine = findViewById(updVaccineId);
//                                                                String updVaccine = rbVaccine.getText().toString().trim();
//                                                                String setVacc = null;
//                                                                switch (updVaccine) {
//                                                                    case "Ya, Sudah Divaksin":
//                                                                        setVacc = "Yes, Already Vaccinated";
//                                                                        break;
//                                                                    case "Belum Divaksin":
//                                                                        setVacc = "Not Yet";
//                                                                        break;
//                                                                }
//                                                                int updSpayNeuterId = rgSpayNeuter.getCheckedRadioButtonId();
//                                                                rbSpayNeuter = findViewById(updSpayNeuterId);
//                                                                String updSpayNeuter = rbSpayNeuter.getText().toString().trim();
//                                                                String setSpayNeuter = null;
//                                                                switch (updSpayNeuter) {
//                                                                    case "Ya, Sudah Dikastrasi":
//                                                                        setSpayNeuter = "Yes, Already Spayed/ Neutered";
//                                                                        break;
//                                                                    case "Belum Dikastrasi":
//                                                                        setSpayNeuter = "Not Yet";
//                                                                        break;
//                                                                }
//                                                                String updReason = msReason.getSelectedItem().toString().trim();
//                                                                String setReason = null;
//                                                                switch (updReason) {
//                                                                    case "Liar":
//                                                                        setReason = "Stray";
//                                                                        break;
//                                                                    case "Terlantar":
//                                                                        setReason = "Abandoned";
//                                                                        break;
//                                                                    case "Disiksa":
//                                                                        setReason = "Abused";
//                                                                        break;
//                                                                    case "Pemilik Meninggal":
//                                                                        setReason = "Owner Dead";
//                                                                        break;
//                                                                    case "Pemilik Menyerah":
//                                                                        setReason = "Owner Give Up";
//                                                                        break;
//                                                                    case "Pindah Rumah":
//                                                                        setReason = "House Moving";
//                                                                        break;
//                                                                    case "Keuangan":
//                                                                        setReason = "Financial";
//                                                                        break;
//                                                                    case "Masalah Kesehatan":
//                                                                        setReason = "Medical Problem";
//                                                                        break;
//                                                                    case "Lainnya":
//                                                                        setReason = "Others";
//                                                                        break;
//                                                                }
//
//                                                                catUpdate.setCatProfilePhoto(updPhotoIdle);
//                                                                catUpdate.setCatName(updNameIdle);
//                                                                catUpdate.setCatDob(updDob);
//                                                                catUpdate.setCatDescription(updDescription);
//                                                                catUpdate.setCatMedNote(updMedicalNote);
//                                                                catUpdate.setCatGender(setGender);
//                                                                catUpdate.setCatReason(setReason);
//                                                                catUpdate.setCatAdoptedStatus(updAdoptionStatus);
//                                                                catUpdate.setCatVaccStat(setVacc);
//                                                                catUpdate.setCatSpayNeuterStat(setSpayNeuter);
//                                                                databaseCats.setValue(catUpdate);
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(DatabaseError databaseError) {
//
//                                                            }
//                                                        });
//                                                        databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                                for (DataSnapshot updateSnapshot : dataSnapshot.getChildren()) {
//                                                                    Adoption adoptionUpdate = updateSnapshot.getValue(Adoption.class);
//                                                                    String updPhotoIdle = taskSnapshot.getDownloadUrl().toString();
//                                                                    String updNameIdle = etName.getText().toString().trim();
//
//                                                                    adoptionUpdate.setAdoptionCatPhoto(updPhotoIdle);
//                                                                    adoptionUpdate.setAdoptionCatName(updNameIdle);
//                                                                    databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(DatabaseError databaseError) {
//
//                                                            }
//                                                        });
//                                                        backToMainMenu();
//                                                        finish();
//                                                        break;
//                                                }
//                                            }
//                                        });
//                            } else {
//                                updateCheckSpinner();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                } else {
//                    updateCheckSpinner();
//                }
            }
        });
    }

    private void updateCat(){
        if (ivPhoto.getDrawable() != null) {
            databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Cat catCheck = dataSnapshot.getValue(Cat.class);
                    String catPhotoCheck = catCheck.getCatProfilePhoto();
                    if (catPhotoCheck.equals("")) {
                        dbPhotoEmpty();
//                        storageCats = FirebaseStorage.getInstance().getReference();
//                        StorageReference reference = storageCats.child(STORAGE_PATH + System.currentTimeMillis() + "." + getActualImage(uriCatPhoto));
//                        reference.putFile(uriCatPhoto)
//                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
//                                        int adoptionStatusSpinner = msAdoptionStatus.getSelectedItemPosition();
//                                        String cId = getIntent().getStringExtra("cat_id");
//                                        switch (adoptionStatusSpinner) {
//                                            case 0:
//                                                Toast.makeText(getApplicationContext(), "Pilih Status Adopsi", Toast.LENGTH_SHORT).show();
//                                                return;
//                                            case 1:
//                                                databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        Cat catUpdate = dataSnapshot.getValue(Cat.class);
//                                                        String updPhotoChange = taskSnapshot.getDownloadUrl().toString();
//                                                        String updNameChange = etName.getText().toString().trim();
//                                                        String updDobChange = etDob.getText().toString().trim();
//                                                        String updDescriptionChange = etDesc.getText().toString().trim();
//                                                        String updMedicalNoteChange = etMedNote.getText().toString().trim();
//                                                        String updGenderChange = msGender.getSelectedItem().toString().trim();
//                                                        String setUpdGender = null;
//                                                        switch (updGenderChange) {
//                                                            case "Jantan":
//                                                                setUpdGender = "Male";
//                                                                break;
//                                                            case "Betina":
//                                                                setUpdGender = "Female";
//                                                                break;
//                                                            case "Tidak Diketahui":
//                                                                setUpdGender = "Unknown";
//                                                                break;
//                                                        }
//                                                        String updAdoptionStatusChange = "Adopted";
//                                                        int updVaccineId = rgVaccine.getCheckedRadioButtonId();
//                                                        rbVaccine = findViewById(updVaccineId);
//                                                        String updVaccineChange = rbVaccine.getText().toString().trim();
//                                                        String setUpdVacc = null;
//                                                        switch (updVaccineChange) {
//                                                            case "Ya, Sudah Divaksin":
//                                                                setUpdVacc = "Yes, Already Vaccinated";
//                                                                break;
//                                                            case "Belum Divaksin":
//                                                                setUpdVacc = "Not Yet";
//                                                                break;
//                                                        }
//                                                        int updSpayNeuterId = rgSpayNeuter.getCheckedRadioButtonId();
//                                                        rbSpayNeuter = findViewById(updSpayNeuterId);
//                                                        String updSpayNeuterChange = rbSpayNeuter.getText().toString().trim();
//                                                        String setUpdSpayNeuter = null;
//                                                        switch (updSpayNeuterChange) {
//                                                            case "Ya, Sudah Dikastrasi":
//                                                                setUpdSpayNeuter = "Yes, Already Spayed/ Neutered";
//                                                                break;
//                                                            case "Belum Dikastrasi":
//                                                                setUpdSpayNeuter = "Not Yet";
//                                                                break;
//                                                        }
//                                                        String updReasonChange = msReason.getSelectedItem().toString().trim();
//                                                        String setUpdReason = null;
//                                                        switch (updReasonChange) {
//                                                            case "Liar":
//                                                                setUpdReason = "Stray";
//                                                                break;
//                                                            case "Terlantar":
//                                                                setUpdReason = "Abandoned";
//                                                                break;
//                                                            case "Disiksa":
//                                                                setUpdReason = "Abused";
//                                                                break;
//                                                            case "Pemilik Meninggal":
//                                                                setUpdReason = "Owner Dead";
//                                                                break;
//                                                            case "Pemilik Menyerah":
//                                                                setUpdReason = "Owner Give Up";
//                                                                break;
//                                                            case "Pindah Rumah":
//                                                                setUpdReason = "House Moving";
//                                                                break;
//                                                            case "Keuangan":
//                                                                setUpdReason = "Financial";
//                                                                break;
//                                                            case "Masalah Kesehatan":
//                                                                setUpdReason = "Medical Problem";
//                                                                break;
//                                                            case "Lainnya":
//                                                                setUpdReason = "Others";
//                                                                break;
//                                                        }
//
//                                                        catUpdate.setCatProfilePhoto(updPhotoChange);
//                                                        catUpdate.setCatName(updNameChange);
//                                                        catUpdate.setCatDob(updDobChange);
//                                                        catUpdate.setCatDescription(updDescriptionChange);
//                                                        catUpdate.setCatMedNote(updMedicalNoteChange);
//                                                        catUpdate.setCatGender(setUpdGender);
//                                                        catUpdate.setCatAdoptedStatus(updAdoptionStatusChange);
//                                                        catUpdate.setCatVaccStat(setUpdVacc);
//                                                        catUpdate.setCatSpayNeuterStat(setUpdSpayNeuter);
//                                                        catUpdate.setCatReason(setUpdReason);
//                                                        databaseCats.setValue(catUpdate);
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });
//                                                databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        for (DataSnapshot updateSnapshot : dataSnapshot.getChildren()) {
//                                                            Adoption adoptionUpdate = updateSnapshot.getValue(Adoption.class);
//                                                            String updPhotoChange = taskSnapshot.getDownloadUrl().toString();
//                                                            String updNameChange = etName.getText().toString().trim();
//
//                                                            adoptionUpdate.setAdoptionCatPhoto(updPhotoChange);
//                                                            adoptionUpdate.setAdoptionCatName(updNameChange);
//                                                            adoptionUpdate.setAdoptionApplicationStatus("Rejected");
//                                                            adoptionUpdate.setAdoptionCatIdApponStatus(adoptionUpdate.getAdoptionCatId() + "_Rejected");
//                                                            adoptionUpdate.setAdoptionOwnerIdApponStatus(adoptionUpdate.getAdoptionOwnerId() + "_Rejected");
//                                                            databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });
//                                                backToMainMenu();
//                                                finish();
//                                                break;
//                                            case 2:
//                                                databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        Cat catUpdate = dataSnapshot.getValue(Cat.class);
//                                                        String updPhotoIdle = taskSnapshot.getDownloadUrl().toString();
//                                                        String updNameIdle = etName.getText().toString().trim();
//                                                        String updDob = etDob.getText().toString().trim();
//                                                        String updDescription = etDesc.getText().toString().trim();
//                                                        String updMedicalNote = etMedNote.getText().toString().trim();
//                                                        String updGender = msGender.getSelectedItem().toString().trim();
//                                                        String setGender = null;
//                                                        switch (updGender) {
//                                                            case "Jantan":
//                                                                setGender = "Male";
//                                                                break;
//                                                            case "Betina":
//                                                                setGender = "Female";
//                                                                break;
//                                                            case "Tidak Diketahui":
//                                                                setGender = "Unknown";
//                                                                break;
//                                                        }
//                                                        String updAdoptionStatus = "Available";
//                                                        int updVaccineId = rgVaccine.getCheckedRadioButtonId();
//                                                        rbVaccine = findViewById(updVaccineId);
//                                                        String updVaccine = rbVaccine.getText().toString().trim();
//                                                        String setVacc = null;
//                                                        switch (updVaccine) {
//                                                            case "Ya, Sudah Divaksin":
//                                                                setVacc = "Yes, Already Vaccinated";
//                                                                break;
//                                                            case "Belum Divaksin":
//                                                                setVacc = "Not Yet";
//                                                                break;
//                                                        }
//                                                        int updSpayNeuterId = rgSpayNeuter.getCheckedRadioButtonId();
//                                                        rbSpayNeuter = findViewById(updSpayNeuterId);
//                                                        String updSpayNeuter = rbSpayNeuter.getText().toString().trim();
//                                                        String setSpayNeuter = null;
//                                                        switch (updSpayNeuter) {
//                                                            case "Ya, Sudah Dikastrasi":
//                                                                setSpayNeuter = "Yes, Already Spayed/ Neutered";
//                                                                break;
//                                                            case "Belum Dikastrasi":
//                                                                setSpayNeuter = "Not Yet";
//                                                                break;
//                                                        }
//                                                        String updReason = msReason.getSelectedItem().toString().trim();
//                                                        String setReason = null;
//                                                        switch (updReason) {
//                                                            case "Liar":
//                                                                setReason = "Stray";
//                                                                break;
//                                                            case "Terlantar":
//                                                                setReason = "Abandoned";
//                                                                break;
//                                                            case "Disiksa":
//                                                                setReason = "Abused";
//                                                                break;
//                                                            case "Pemilik Meninggal":
//                                                                setReason = "Owner Dead";
//                                                                break;
//                                                            case "Pemilik Menyerah":
//                                                                setReason = "Owner Give Up";
//                                                                break;
//                                                            case "Pindah Rumah":
//                                                                setReason = "House Moving";
//                                                                break;
//                                                            case "Keuangan":
//                                                                setReason = "Financial";
//                                                                break;
//                                                            case "Masalah Kesehatan":
//                                                                setReason = "Medical Problem";
//                                                                break;
//                                                            case "Lainnya":
//                                                                setReason = "Others";
//                                                                break;
//                                                        }
//
//                                                        catUpdate.setCatProfilePhoto(updPhotoIdle);
//                                                        catUpdate.setCatName(updNameIdle);
//                                                        catUpdate.setCatDob(updDob);
//                                                        catUpdate.setCatDescription(updDescription);
//                                                        catUpdate.setCatMedNote(updMedicalNote);
//                                                        catUpdate.setCatGender(setGender);
//                                                        catUpdate.setCatReason(setReason);
//                                                        catUpdate.setCatAdoptedStatus(updAdoptionStatus);
//                                                        catUpdate.setCatVaccStat(setVacc);
//                                                        catUpdate.setCatSpayNeuterStat(setSpayNeuter);
//                                                        databaseCats.setValue(catUpdate);
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });
//                                                databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        for (DataSnapshot updateSnapshot : dataSnapshot.getChildren()) {
//                                                            Adoption adoptionUpdate = updateSnapshot.getValue(Adoption.class);
//                                                            String updPhotoIdle = taskSnapshot.getDownloadUrl().toString();
//                                                            String updNameIdle = etName.getText().toString().trim();
//
//                                                            adoptionUpdate.setAdoptionCatPhoto(updPhotoIdle);
//                                                            adoptionUpdate.setAdoptionCatName(updNameIdle);
//                                                            databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });
//                                                backToMainMenu();
//                                                finish();
//                                                break;
//                                        }
//                                    }
//                                });
                    } else {
                        updateData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            updateData();
        }
    }

    private void updateData() {
        int adoptionStatusSpinner = msAdoptionStatus.getSelectedItemPosition();
        String cId = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(cId);
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions");
        switch (adoptionStatusSpinner) {
            case 0:
                Toast.makeText(getApplicationContext(), "Pilih Status Adopsi", Toast.LENGTH_SHORT).show();
                return;
            case 1:
                databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Cat catUpdate = dataSnapshot.getValue(Cat.class);
                        String updNameChange = etName.getText().toString().trim();
                        String updDobChange = etDob.getText().toString().trim();
                        String updDescriptionChange = etDesc.getText().toString().trim();
                        String updMedicalNoteChange = etMedNote.getText().toString().trim();
                        String updGenderChange = msGender.getSelectedItem().toString().trim();
                        String setUpdGender = null;
                        switch (updGenderChange) {
                            case "Jantan":
                                setUpdGender = "Male";
                                break;
                            case "Betina":
                                setUpdGender = "Female";
                                break;
                            case "Tidak Diketahui":
                                setUpdGender = "Unknown";
                                break;
                        }
                        String updAdoptionStatusChange = "Adopted";
                        int updVaccineId = rgVaccine.getCheckedRadioButtonId();
                        rbVaccine = findViewById(updVaccineId);
                        String updVaccineChange = rbVaccine.getText().toString().trim();
                        String setUpdVacc = null;
                        switch (updVaccineChange) {
                            case "Ya, Sudah Divaksin":
                                setUpdVacc = "Yes, Already Vaccinated";
                                break;
                            case "Belum Divaksin":
                                setUpdVacc = "Not Yet";
                                break;
                        }
                        int updSpayNeuterId = rgSpayNeuter.getCheckedRadioButtonId();
                        rbSpayNeuter = findViewById(updSpayNeuterId);
                        String updSpayNeuterChange = rbSpayNeuter.getText().toString().trim();
                        String setUpdSpayNeuter = null;
                        switch (updSpayNeuterChange) {
                            case "Ya, Sudah Dikastrasi":
                                setUpdSpayNeuter = "Yes, Already Spayed/ Neutered";
                                break;
                            case "Belum Dikastrasi":
                                setUpdSpayNeuter = "Not Yet";
                                break;
                        }
                        String updReasonChange = msReason.getSelectedItem().toString().trim();
                        String setUpdReason = null;
                        switch (updReasonChange) {
                            case "Liar":
                                setUpdReason = "Stray";
                                break;
                            case "Terlantar":
                                setUpdReason = "Abandoned";
                                break;
                            case "Disiksa":
                                setUpdReason = "Abused";
                                break;
                            case "Pemilik Meninggal":
                                setUpdReason = "Owner Dead";
                                break;
                            case "Pemilik Menyerah":
                                setUpdReason = "Owner Give Up";
                                break;
                            case "Pindah Rumah":
                                setUpdReason = "House Moving";
                                break;
                            case "Keuangan":
                                setUpdReason = "Financial";
                                break;
                            case "Masalah Kesehatan":
                                setUpdReason = "Medical Problem";
                                break;
                            case "Lainnya":
                                setUpdReason = "Others";
                                break;
                        }

                        catUpdate.setCatName(updNameChange);
                        catUpdate.setCatDob(updDobChange);
                        catUpdate.setCatDescription(updDescriptionChange);
                        catUpdate.setCatMedNote(updMedicalNoteChange);
                        catUpdate.setCatGender(setUpdGender);
                        catUpdate.setCatReason(setUpdReason);
                        catUpdate.setCatAdoptedStatus(updAdoptionStatusChange);
                        catUpdate.setCatVaccStat(setUpdVacc);
                        catUpdate.setCatSpayNeuterStat(setUpdSpayNeuter);
                        databaseCats.setValue(catUpdate);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot updateSnapshot : dataSnapshot.getChildren()) {
                            Adoption adoptionUpdate = updateSnapshot.getValue(Adoption.class);
                            String updNameChange = etName.getText().toString().trim();
                            adoptionUpdate.setAdoptionCatName(updNameChange);
                            adoptionUpdate.setAdoptionApplicationStatus("Rejected");
                            adoptionUpdate.setAdoptionCatIdApponStatus(adoptionUpdate.getAdoptionCatId() + "_Rejected");
                            adoptionUpdate.setAdoptionOwnerIdApponStatus(adoptionUpdate.getAdoptionOwnerId() + "_Rejected");
                            databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                backToMainMenu();
                finish();
                break;
            case 2:
                databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Cat catUpdate = dataSnapshot.getValue(Cat.class);
                        String updNameIdle = etName.getText().toString().trim();
                        String updDobIdle = etDob.getText().toString().trim();
                        String updDescriptionIdle = etDesc.getText().toString().trim();
                        String updMedicalNoteIdle = etMedNote.getText().toString().trim();
                        String updGenderIdle = msGender.getSelectedItem().toString().trim();
                        String setGender = null;
                        switch (updGenderIdle) {
                            case "Jantan":
                                setGender = "Male";
                                break;
                            case "Betina":
                                setGender = "Female";
                                break;
                            case "Tidak Diketahui":
                                setGender = "Unknown";
                                break;
                        }
                        String updAdoptionStatusIdle = "Available";
                        int updVaccineId = rgVaccine.getCheckedRadioButtonId();
                        rbVaccine = findViewById(updVaccineId);
                        String updVaccineIdle = rbVaccine.getText().toString().trim();
                        String setVacc = null;
                        switch (updVaccineIdle) {
                            case "Ya, Sudah Divaksin":
                                setVacc = "Yes, Already Vaccinated";
                                break;
                            case "Belum Divaksin":
                                setVacc = "Not Yet";
                                break;
                        }
                        int updSpayNeuterId = rgSpayNeuter.getCheckedRadioButtonId();
                        rbSpayNeuter = findViewById(updSpayNeuterId);
                        String updSpayNeuterIdle = rbSpayNeuter.getText().toString().trim();
                        String setSpayNeuter = null;
                        switch (updSpayNeuterIdle) {
                            case "Ya, Sudah Dikastrasi":
                                setSpayNeuter = "Yes, Already Spayed/ Neutered";
                                break;
                            case "Belum Dikastrasi":
                                setSpayNeuter = "Not Yet";
                                break;
                        }
                        String updReasonIdle = msReason.getSelectedItem().toString().trim();
                        String setReason = null;
                        switch (updReasonIdle) {
                            case "Liar":
                                setReason = "Stray";
                                break;
                            case "Terlantar":
                                setReason = "Abandoned";
                                break;
                            case "Disiksa":
                                setReason = "Abused";
                                break;
                            case "Pemilik Meninggal":
                                setReason = "Owner Dead";
                                break;
                            case "Pemilik Menyerah":
                                setReason = "Owner Give Up";
                                break;
                            case "Pindah Rumah":
                                setReason = "House Moving";
                                break;
                            case "Keuangan":
                                setReason = "Financial";
                                break;
                            case "Masalah Kesehatan":
                                setReason = "Medical Problem";
                                break;
                            case "Lainnya":
                                setReason = "Others";
                                break;
                        }

                        catUpdate.setCatName(updNameIdle);
                        catUpdate.setCatDob(updDobIdle);
                        catUpdate.setCatDescription(updDescriptionIdle);
                        catUpdate.setCatMedNote(updMedicalNoteIdle);
                        catUpdate.setCatGender(setGender);
                        catUpdate.setCatReason(setReason);
                        catUpdate.setCatAdoptedStatus(updAdoptionStatusIdle);
                        catUpdate.setCatVaccStat(setVacc);
                        catUpdate.setCatSpayNeuterStat(setSpayNeuter);
                        databaseCats.setValue(catUpdate);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot updateSnapshot : dataSnapshot.getChildren()) {
                            Adoption adoptionUpdate = updateSnapshot.getValue(Adoption.class);
                            String updNameIdle = etName.getText().toString().trim();
                            adoptionUpdate.setAdoptionCatName(updNameIdle);
                            databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                backToMainMenu();
                finish();
                break;
        }
    }

    private void dbPhotoEmpty(){
        storageCats = FirebaseStorage.getInstance().getReference();
        StorageReference reference = storageCats.child(STORAGE_PATH + System.currentTimeMillis() + "." + getActualImage(uriCatPhoto));
        reference.putFile(uriCatPhoto)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        int adoptionStatusSpinner = msAdoptionStatus.getSelectedItemPosition();
                        String cId = getIntent().getStringExtra("cat_id");
                        switch (adoptionStatusSpinner) {
                            case 0:
                                Toast.makeText(getApplicationContext(), "Pilih Status Adopsi", Toast.LENGTH_SHORT).show();
                                return;
                            case 1:
                                databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Cat catUpdate = dataSnapshot.getValue(Cat.class);
                                        String updPhotoChange = taskSnapshot.getDownloadUrl().toString();
                                        String updNameChange = etName.getText().toString().trim();
                                        String updDobChange = etDob.getText().toString().trim();
                                        String updDescriptionChange = etDesc.getText().toString().trim();
                                        String updMedicalNoteChange = etMedNote.getText().toString().trim();
                                        String updGenderChange = msGender.getSelectedItem().toString().trim();
                                        String setUpdGender = null;
                                        switch (updGenderChange) {
                                            case "Jantan":
                                                setUpdGender = "Male";
                                                break;
                                            case "Betina":
                                                setUpdGender = "Female";
                                                break;
                                            case "Tidak Diketahui":
                                                setUpdGender = "Unknown";
                                                break;
                                        }
                                        String updAdoptionStatusChange = "Adopted";
                                        int updVaccineId = rgVaccine.getCheckedRadioButtonId();
                                        rbVaccine = findViewById(updVaccineId);
                                        String updVaccineChange = rbVaccine.getText().toString().trim();
                                        String setUpdVacc = null;
                                        switch (updVaccineChange) {
                                            case "Ya, Sudah Divaksin":
                                                setUpdVacc = "Yes, Already Vaccinated";
                                                break;
                                            case "Belum Divaksin":
                                                setUpdVacc = "Not Yet";
                                                break;
                                        }
                                        int updSpayNeuterId = rgSpayNeuter.getCheckedRadioButtonId();
                                        rbSpayNeuter = findViewById(updSpayNeuterId);
                                        String updSpayNeuterChange = rbSpayNeuter.getText().toString().trim();
                                        String setUpdSpayNeuter = null;
                                        switch (updSpayNeuterChange) {
                                            case "Ya, Sudah Dikastrasi":
                                                setUpdSpayNeuter = "Yes, Already Spayed/ Neutered";
                                                break;
                                            case "Belum Dikastrasi":
                                                setUpdSpayNeuter = "Not Yet";
                                                break;
                                        }
                                        String updReasonChange = msReason.getSelectedItem().toString().trim();
                                        String setUpdReason = null;
                                        switch (updReasonChange) {
                                            case "Liar":
                                                setUpdReason = "Stray";
                                                break;
                                            case "Terlantar":
                                                setUpdReason = "Abandoned";
                                                break;
                                            case "Disiksa":
                                                setUpdReason = "Abused";
                                                break;
                                            case "Pemilik Meninggal":
                                                setUpdReason = "Owner Dead";
                                                break;
                                            case "Pemilik Menyerah":
                                                setUpdReason = "Owner Give Up";
                                                break;
                                            case "Pindah Rumah":
                                                setUpdReason = "House Moving";
                                                break;
                                            case "Keuangan":
                                                setUpdReason = "Financial";
                                                break;
                                            case "Masalah Kesehatan":
                                                setUpdReason = "Medical Problem";
                                                break;
                                            case "Lainnya":
                                                setUpdReason = "Others";
                                                break;
                                        }

                                        catUpdate.setCatProfilePhoto(updPhotoChange);
                                        catUpdate.setCatName(updNameChange);
                                        catUpdate.setCatDob(updDobChange);
                                        catUpdate.setCatDescription(updDescriptionChange);
                                        catUpdate.setCatMedNote(updMedicalNoteChange);
                                        catUpdate.setCatGender(setUpdGender);
                                        catUpdate.setCatAdoptedStatus(updAdoptionStatusChange);
                                        catUpdate.setCatVaccStat(setUpdVacc);
                                        catUpdate.setCatSpayNeuterStat(setUpdSpayNeuter);
                                        catUpdate.setCatReason(setUpdReason);
                                        databaseCats.setValue(catUpdate);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot updateSnapshot : dataSnapshot.getChildren()) {
                                            Adoption adoptionUpdate = updateSnapshot.getValue(Adoption.class);
                                            String updPhotoChange = taskSnapshot.getDownloadUrl().toString();
                                            String updNameChange = etName.getText().toString().trim();

                                            adoptionUpdate.setAdoptionCatPhoto(updPhotoChange);
                                            adoptionUpdate.setAdoptionCatName(updNameChange);
                                            adoptionUpdate.setAdoptionApplicationStatus("Rejected");
                                            adoptionUpdate.setAdoptionCatIdApponStatus(adoptionUpdate.getAdoptionCatId() + "_Rejected");
                                            adoptionUpdate.setAdoptionOwnerIdApponStatus(adoptionUpdate.getAdoptionOwnerId() + "_Rejected");
                                            databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                backToMainMenu();
                                finish();
                                break;
                            case 2:
                                databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Cat catUpdate = dataSnapshot.getValue(Cat.class);
                                        String updPhotoIdle = taskSnapshot.getDownloadUrl().toString();
                                        String updNameIdle = etName.getText().toString().trim();
                                        String updDob = etDob.getText().toString().trim();
                                        String updDescription = etDesc.getText().toString().trim();
                                        String updMedicalNote = etMedNote.getText().toString().trim();
                                        String updGender = msGender.getSelectedItem().toString().trim();
                                        String setGender = null;
                                        switch (updGender) {
                                            case "Jantan":
                                                setGender = "Male";
                                                break;
                                            case "Betina":
                                                setGender = "Female";
                                                break;
                                            case "Tidak Diketahui":
                                                setGender = "Unknown";
                                                break;
                                        }
                                        String updAdoptionStatus = "Available";
                                        int updVaccineId = rgVaccine.getCheckedRadioButtonId();
                                        rbVaccine = findViewById(updVaccineId);
                                        String updVaccine = rbVaccine.getText().toString().trim();
                                        String setVacc = null;
                                        switch (updVaccine) {
                                            case "Ya, Sudah Divaksin":
                                                setVacc = "Yes, Already Vaccinated";
                                                break;
                                            case "Belum Divaksin":
                                                setVacc = "Not Yet";
                                                break;
                                        }
                                        int updSpayNeuterId = rgSpayNeuter.getCheckedRadioButtonId();
                                        rbSpayNeuter = findViewById(updSpayNeuterId);
                                        String updSpayNeuter = rbSpayNeuter.getText().toString().trim();
                                        String setSpayNeuter = null;
                                        switch (updSpayNeuter) {
                                            case "Ya, Sudah Dikastrasi":
                                                setSpayNeuter = "Yes, Already Spayed/ Neutered";
                                                break;
                                            case "Belum Dikastrasi":
                                                setSpayNeuter = "Not Yet";
                                                break;
                                        }
                                        String updReason = msReason.getSelectedItem().toString().trim();
                                        String setReason = null;
                                        switch (updReason) {
                                            case "Liar":
                                                setReason = "Stray";
                                                break;
                                            case "Terlantar":
                                                setReason = "Abandoned";
                                                break;
                                            case "Disiksa":
                                                setReason = "Abused";
                                                break;
                                            case "Pemilik Meninggal":
                                                setReason = "Owner Dead";
                                                break;
                                            case "Pemilik Menyerah":
                                                setReason = "Owner Give Up";
                                                break;
                                            case "Pindah Rumah":
                                                setReason = "House Moving";
                                                break;
                                            case "Keuangan":
                                                setReason = "Financial";
                                                break;
                                            case "Masalah Kesehatan":
                                                setReason = "Medical Problem";
                                                break;
                                            case "Lainnya":
                                                setReason = "Others";
                                                break;
                                        }

                                        catUpdate.setCatProfilePhoto(updPhotoIdle);
                                        catUpdate.setCatName(updNameIdle);
                                        catUpdate.setCatDob(updDob);
                                        catUpdate.setCatDescription(updDescription);
                                        catUpdate.setCatMedNote(updMedicalNote);
                                        catUpdate.setCatGender(setGender);
                                        catUpdate.setCatReason(setReason);
                                        catUpdate.setCatAdoptedStatus(updAdoptionStatus);
                                        catUpdate.setCatVaccStat(setVacc);
                                        catUpdate.setCatSpayNeuterStat(setSpayNeuter);
                                        databaseCats.setValue(catUpdate);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseAdoptions.orderByChild("adoptionCatId").equalTo(cId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot updateSnapshot : dataSnapshot.getChildren()) {
                                            Adoption adoptionUpdate = updateSnapshot.getValue(Adoption.class);
                                            String updPhotoIdle = taskSnapshot.getDownloadUrl().toString();
                                            String updNameIdle = etName.getText().toString().trim();

                                            adoptionUpdate.setAdoptionCatPhoto(updPhotoIdle);
                                            adoptionUpdate.setAdoptionCatName(updNameIdle);
                                            databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                backToMainMenu();
                                finish();
                                break;
                        }
                    }
                });
    }

    private void mediaOpen() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galleryIntent, "Pilih Gambar dari Galeri"), 2);
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
                ivPhoto.setImageBitmap(bitmap);
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

//    private void backToPrevious() {
//        String catId = getIntent().getStringExtra("cat_id");
//        String ownerId = getIntent().getStringExtra("owner_id");
//        String pActivity = getIntent().getStringExtra("previousActivity");
//        Intent intent = new Intent(getApplicationContext(), CatProfileOwnerAvailableActivity.class);
//        if (pActivity.equals("findcat")) {
//            intent.putExtra("previousActivity", pActivity);
//            intent.putExtra("cat_id", catId);
//            intent.putExtra("owner_id", ownerId);
//            Toast.makeText(getApplicationContext(), "Cat Data Successfully Edited", Toast.LENGTH_SHORT).show();
//            startActivity(intent);
//            finish();
//        } else if (pActivity.equals("adoptionlist")) {
//            intent.putExtra("previousActivity", pActivity);
//            intent.putExtra("cat_id", catId);
//            intent.putExtra("owner_id", ownerId);
//            Toast.makeText(getApplicationContext(), "Cat Data Successfully Edited", Toast.LENGTH_SHORT).show();
//            startActivity(intent);
//            finish();
//        }
//    }

//    private void backToMainMenu() {
//        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
//        startActivity(intent);
//        finish();
//    }

    private void backToMainMenu() {
        Toast.makeText(getApplicationContext(), "Profil Kucing berhasil Diperbarui", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCatDataAvailableActivity.this);
        builder.setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
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
                .setNegativeButton("Tidak", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}