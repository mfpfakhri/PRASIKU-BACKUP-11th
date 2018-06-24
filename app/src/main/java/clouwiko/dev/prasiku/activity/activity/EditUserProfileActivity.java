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
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;
import clouwiko.dev.prasiku.activity.model.Cat;
import clouwiko.dev.prasiku.activity.model.User;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditUserProfileActivity extends AppCompatActivity {
    private ImageView ivPhoto, btnUpdatePhoto, btnDeletePhoto;
    private EditText etName, etDob, etPhone, etAddress;
    private MaterialSpinner msGender, msProvince, msCity;
    private Button btnUpdate;
    private DatabaseReference databaseUsers, databaseCats, databaseAdoptions, databaseProvinces, databaseCities;
    private DatePickerDialog.OnDateSetListener dobSetListener;
    private StorageReference storageUsers;
    private static final String STORAGE_PATH = "userProfilePhoto/";
    Uri uriUserPhoto;

    ArrayAdapter<String> citiesAdapter;
    List<String> cities;
    List<String> provincesKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edituserprofile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sunting Profil");

        ivPhoto = findViewById(R.id.edituserprofile_photo_imageview);
        btnUpdatePhoto = findViewById(R.id.edituserprofile_photo_edit_button);
        btnDeletePhoto = findViewById(R.id.edituserprofile_photo_delete_button);
        etName = findViewById(R.id.edituserprofile_full_name);
        etDob = findViewById(R.id.edituserprofile_dob);
        etPhone = findViewById(R.id.edituserprofile_phonenumber);
        etAddress = findViewById(R.id.edituserprofile_address);
        msGender = findViewById(R.id.edituserprofile_genderspinner);
        msProvince = findViewById(R.id.edituserprofile_provincespinner);
        msCity = findViewById(R.id.edituserprofile_cityspinner);
        btnUpdate = findViewById(R.id.edituserprofile_update_button);

        btnUpdatePhoto.setVisibility(View.GONE);
        btnDeletePhoto.setVisibility(View.GONE);

        final String uId = getIntent().getStringExtra("userId");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(uId);
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userData = dataSnapshot.getValue(User.class);
                String name = userData.getUserFname();
                String dob = userData.getUserDob();
                String phone = userData.getUserPhone();
                String address = userData.getUserAddress();
                String gender = userData.getUserGender();
                String photo = userData.getUserProfilePhoto();
                if (userData.getUserProfilePhoto().equals("")) {
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
                    default:

                }
                etName.setText(name);
                etDob.setText(dob);
                etPhone.setText(phone);
                etAddress.setText(address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserProfileActivity.this);
                builder.setMessage("Apakah Anda yakin ingin menghapus foto profil?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(uId);
                                databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User userData = dataSnapshot.getValue(User.class);
                                        String photo = userData.getUserProfilePhoto();
                                        try {
                                            if (photo.equals("")) {
                                                Toast.makeText(getApplicationContext(), "Tidak ada foto dipasang", Toast.LENGTH_SHORT).show();
                                            } else {
                                                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(photo);
                                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "Anda tidak memiliki foto profil saat ini", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                userData.setUserProfilePhoto("");
                                                databaseUsers.setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        startActivity(getIntent());
                                                    }
                                                });
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

        //Spinner Cities
        cities = new ArrayList<String>();
        citiesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, cities);
//        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msCity.setAdapter(citiesAdapter);
        provincesKey = new ArrayList<>();

        databaseProvinces = FirebaseDatabase.getInstance().getReference().child("provinces");
        //TODO:RWP change addEventListener to addListenerForSingleValueEvent
        databaseProvinces.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> provinces = new ArrayList<String>();
                for (DataSnapshot provinceSnapshot : dataSnapshot.getChildren()) {
                    final String provinceName = provinceSnapshot.child("provinceName").getValue(String.class);
                    provinces.add(provinceName);
                    //TODO:RWP add key to array
                    String key = provinceSnapshot.getKey();
                    provincesKey.add(key);
                    //--
                    msProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int spinnerProvincesPosition = msProvince.getSelectedItemPosition();
                            //TODO:RWP remove all cities and clear selection city
                            cities.clear();
                            msCity.setSelection(0);
                            //--
                            if (spinnerProvincesPosition != 0) {
                                //TODO:RWP get key by its position
                                String provinceKey = provincesKey.get(position);
                                //
                                databaseCities = FirebaseDatabase.getInstance().getReference().child("cities");
                                //TODO:RWP change orderBy and equalTo with child and change addEventListener to addListenerForSingleValueEvent
                                databaseCities.child(provinceKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot cityPSnapshot : dataSnapshot.getChildren()) {
                                            //TODO:RWP delete multiple loop for cities
                                            String cityName = cityPSnapshot.child("cityName").getValue(String.class);
                                            cities.add(cityName);
                                        }
                                        //TODO:RWP notify all new cities
                                        citiesAdapter.notifyDataSetChanged();
                                        //--
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                ArrayAdapter<String> provincesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, provinces);
//                provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                msProvince.setAdapter(provincesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                        EditUserProfileActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dobSetListener,
                        day, month, year);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dobSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int dayOfMonth, int month, int year) {
                month = month + 1;
                Log.d("onDateSet: date: " + dayOfMonth + "/" + month + "/" + year, dobSetListener.toString());

                String date = dayOfMonth + "/" + month + "/" + year;
                etDob.setText(date);
            }
        };

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valName = etName.getText().toString().trim();
                if (valName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ketik nama lengkap Anda", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

                String valDob = etDob.getText().toString().trim();
                if (valDob.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ketik tanggal lahir Anda", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

                int valGender = msGender.getSelectedItemPosition();
                if (valGender != 0) {

                } else {
                    Toast.makeText(getApplicationContext(), "Pilih jenis kelamin Anda", Toast.LENGTH_SHORT).show();
                    return;
                }

                int valProvince = msProvince.getSelectedItemPosition();
                if (valProvince != 0) {

                } else {
                    Toast.makeText(getApplicationContext(), "Pilih provinsi Anda", Toast.LENGTH_SHORT).show();
                    return;
                }

                int valCity = msCity.getSelectedItemPosition();
                if (valCity != 0) {

                } else {
                    Toast.makeText(getApplicationContext(), "Pilih kota/ kabupaten Anda", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String validPhone = "^[+]?[0-9]{10,13}$";
                String valPhone = etPhone.getText().toString().trim();
                if (valPhone.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ketik nomor telepon Anda", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
                Matcher matcherPhone = Pattern.compile(validPhone).matcher(valPhone);
                if (matcherPhone.matches()) {

                } else {
                    Toast.makeText(getApplicationContext(), "Ketik nomor telepon yang valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                String valAddress = etAddress.getText().toString().trim();
                if (valAddress.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ketik alamat Anda", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

                if (ivPhoto.getDrawable() != null) {
                    databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(uId);
                    databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User userCheck = dataSnapshot.getValue(User.class);
                            String photoCheck = userCheck.getUserProfilePhoto();
                            if (photoCheck.equals("")) {
                                storageUsers = FirebaseStorage.getInstance().getReference();
                                StorageReference reference = storageUsers.child(STORAGE_PATH + System.currentTimeMillis() + "." + getActualImage(uriUserPhoto));
                                reference.putFile(uriUserPhoto)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                                databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(uId);
                                                databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        User userUpdate = dataSnapshot.getValue(User.class);
                                                        String photo = taskSnapshot.getDownloadUrl().toString();
                                                        String name = etName.getText().toString().trim();
                                                        String dob = etDob.getText().toString().trim();
                                                        String phone = etPhone.getText().toString().trim();
                                                        String address = etAddress.getText().toString().trim();
                                                        String gender = msGender.getSelectedItem().toString().trim();
                                                        String setgender = null;
                                                        switch (gender){
                                                            case "Pria":
                                                                setgender = "Male";
                                                                break;
                                                            case "Wanita":
                                                                setgender = "Female";
                                                                break;
                                                        }
                                                        String province = msProvince.getSelectedItem().toString().trim();
                                                        String city = msCity.getSelectedItem().toString().trim();

                                                        userUpdate.setUserProfilePhoto(photo);
                                                        userUpdate.setUserFname(name);
                                                        userUpdate.setUserDob(dob);
                                                        userUpdate.setUserPhone(phone);
                                                        userUpdate.setUserAddress(address);
                                                        userUpdate.setUserGender(setgender);
                                                        userUpdate.setUserProvince(province);
                                                        userUpdate.setUserCity(city);
                                                        userUpdate.setUserCityStatus(city + "_" + userUpdate.getUserStatus());
                                                        databaseUsers.setValue(userUpdate);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                databaseCats = FirebaseDatabase.getInstance().getReference().child("cats");
                                                databaseCats.orderByChild("catOwnerId").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot catUpdateSnapshot : dataSnapshot.getChildren()) {
                                                            Cat catUpdate = catUpdateSnapshot.getValue(Cat.class);
                                                            String province = msProvince.getSelectedItem().toString().trim();
                                                            String city = msCity.getSelectedItem().toString().trim();

                                                            catUpdate.setCatProvince(province);
                                                            catUpdate.setCatCity(city);
                                                            catUpdate.setCatCityDeleteStatus(city + "_" + catUpdate.getCatDeleteStatus());
                                                            databaseCats.child(catUpdate.getCatId()).setValue(catUpdate);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions");
                                                databaseAdoptions.orderByChild("adoptionApplicantId").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot adoptionUpdateSnapshot : dataSnapshot.getChildren()) {
                                                            Adoption adoptionUpdate = adoptionUpdateSnapshot.getValue(Adoption.class);
                                                            String name = etName.getText().toString().trim();
                                                            adoptionUpdate.setAdoptionApplicantName(name);
                                                            databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                Toast.makeText(getApplicationContext(), "Profil Anda telah diperbarui", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(uId);
                                databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User userUpdate = dataSnapshot.getValue(User.class);
                                        String name = etName.getText().toString().trim();
                                        String dob = etDob.getText().toString().trim();
                                        String phone = etPhone.getText().toString().trim();
                                        String address = etAddress.getText().toString().trim();
                                        String gender = msGender.getSelectedItem().toString().trim();
                                        String setgender = null;
                                        switch (gender){
                                            case "Pria":
                                                setgender = "Male";
                                                break;
                                            case "Wanita":
                                                setgender = "Female";
                                                break;
                                        }
                                        String province = msProvince.getSelectedItem().toString().trim();
                                        String city = msCity.getSelectedItem().toString().trim();

                                        userUpdate.setUserFname(name);
                                        userUpdate.setUserDob(dob);
                                        userUpdate.setUserPhone(phone);
                                        userUpdate.setUserAddress(address);
                                        userUpdate.setUserGender(setgender);
                                        userUpdate.setUserProvince(province);
                                        userUpdate.setUserCity(city);
                                        userUpdate.setUserCityStatus(city + "_" + userUpdate.getUserStatus());
                                        databaseUsers.setValue(userUpdate);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseCats = FirebaseDatabase.getInstance().getReference().child("cats");
                                databaseCats.orderByChild("catOwnerId").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot catUpdateSnapshot : dataSnapshot.getChildren()) {
                                            Cat catUpdate = catUpdateSnapshot.getValue(Cat.class);
                                            String province = msProvince.getSelectedItem().toString().trim();
                                            String city = msCity.getSelectedItem().toString().trim();

                                            catUpdate.setCatProvince(province);
                                            catUpdate.setCatCity(city);
                                            catUpdate.setCatCityDeleteStatus(city + "_" + catUpdate.getCatDeleteStatus());
                                            databaseCats.child(catUpdate.getCatId()).setValue(catUpdate);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions");
                                databaseAdoptions.orderByChild("adoptionApplicantId").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot adoptionUpdateSnapshot : dataSnapshot.getChildren()) {
                                            Adoption adoptionUpdate = adoptionUpdateSnapshot.getValue(Adoption.class);
                                            String name = etName.getText().toString().trim();
                                            adoptionUpdate.setAdoptionApplicantName(name);
                                            databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Profil Anda telah diperbarui", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(uId);
                    databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User userUpdate = dataSnapshot.getValue(User.class);
                            String name = etName.getText().toString().trim();
                            String dob = etDob.getText().toString().trim();
                            String phone = etPhone.getText().toString().trim();
                            String address = etAddress.getText().toString().trim();
                            String gender = msGender.getSelectedItem().toString().trim();
                            String setgender = null;
                            switch (gender){
                                case "Pria":
                                    setgender = "Male";
                                    break;
                                case "Wanita":
                                    setgender = "Female";
                                    break;
                            }
                            String province = msProvince.getSelectedItem().toString().trim();
                            String city = msCity.getSelectedItem().toString().trim();

                            userUpdate.setUserFname(name);
                            userUpdate.setUserDob(dob);
                            userUpdate.setUserPhone(phone);
                            userUpdate.setUserAddress(address);
                            userUpdate.setUserGender(setgender);
                            userUpdate.setUserProvince(province);
                            userUpdate.setUserCity(city);
                            userUpdate.setUserCityStatus(city + "_" + userUpdate.getUserStatus());
                            databaseUsers.setValue(userUpdate);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    databaseCats = FirebaseDatabase.getInstance().getReference().child("cats");
                    databaseCats.orderByChild("catOwnerId").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot catUpdateSnapshot : dataSnapshot.getChildren()) {
                                Cat catUpdate = catUpdateSnapshot.getValue(Cat.class);
                                String province = msProvince.getSelectedItem().toString().trim();
                                String city = msCity.getSelectedItem().toString().trim();

                                catUpdate.setCatProvince(province);
                                catUpdate.setCatCity(city);
                                catUpdate.setCatCityDeleteStatus(city + "_" + catUpdate.getCatDeleteStatus());
                                databaseCats.child(catUpdate.getCatId()).setValue(catUpdate);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions");
                    databaseAdoptions.orderByChild("adoptionApplicantId").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot adoptionUpdateSnapshot : dataSnapshot.getChildren()) {
                                Adoption adoptionUpdate = adoptionUpdateSnapshot.getValue(Adoption.class);
                                String name = etName.getText().toString().trim();
                                adoptionUpdate.setAdoptionApplicantName(name);
                                databaseAdoptions.child(adoptionUpdate.getAdoptionId()).setValue(adoptionUpdate);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                backToMainMenu();
            }
        });
    }

    private void mediaOpen() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galleryIntent, "Pilih gambar dari galeri"), 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            cropUserImage();
        } else if (requestCode == 2) {
            if (data != null) {
                uriUserPhoto = data.getData();
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
            cropIntent.setDataAndType(uriUserPhoto, "image/*");

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

    private void backToMainMenu() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        Toast.makeText(getApplicationContext(), "Profil Anda telah diperbarui", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditUserProfileActivity.this);
        builder.setMessage("Apakah Anda yakin ingin membatalkan pengeditan?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uId = getIntent().getStringExtra("userId");
                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        intent.putExtra("userId", uId);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Tidak", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
