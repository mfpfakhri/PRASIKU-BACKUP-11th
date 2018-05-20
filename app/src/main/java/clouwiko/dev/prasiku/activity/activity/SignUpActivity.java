package clouwiko.dev.prasiku.activity.activity;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Province;
import clouwiko.dev.prasiku.activity.model.User;
import fr.ganfra.materialspinner.MaterialSpinner;

public class SignUpActivity extends AppCompatActivity {

    //TODO: Object Declaration
    private String TAG = "SignUpActivity";
    private TextView title;
    private EditText inputEmail, inputPassword, inputFullName, inputDob, inputPhone, inputAddress;
    private DatePickerDialog.OnDateSetListener dobSetListener;
    private MaterialSpinner spinnerGender;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseProvinces, databaseCities, databaseUsers;
    private StorageReference storageUsers;
    private static final String STORAGE_PATH = "userProfilePhoto/";
    private MaterialSpinner spinnerProvinces, spinnerCities;
    private ImageView userPhotoIv;
    Uri uriUserPhoto;

    //TODO: Initial cities Adapter
    ArrayAdapter<String> citiesAdapter;
    List<String> cities;
    List<String> provincesKey;
    //--

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //TODO: Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //TODO: Initialize Object
        title = findViewById(R.id.signup_title);
        Typeface facetitles = Typeface.createFromAsset(getAssets(), "fonts/segoeuisl.ttf");
        title.setTypeface(facetitles);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputFullName = findViewById(R.id.full_name);
        inputDob = findViewById(R.id.dobDatepicker);
        inputPhone = findViewById(R.id.phoneNumber);
        inputAddress = findViewById(R.id.address);
        spinnerGender = findViewById(R.id.genderSpinner_signup);
        spinnerProvinces = findViewById(R.id.provinceSpinner_signup);
        spinnerCities = findViewById(R.id.citySpinner_signup);
        userPhotoIv = findViewById(R.id.userPhotos);
        btnSignUp = findViewById(R.id.action_sign_up_button);
        Typeface facesignup = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");
        btnSignUp.setTypeface(facesignup);
        btnSignIn = findViewById(R.id.intent_sign_in_button);
        Typeface facesignupin = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");
        btnSignIn.setTypeface(facesignupin);
        progressBar = findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        //TODO: Sign In Button OnClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });

        //TODO: Initial Spinner for cities
        cities = new ArrayList<String>();
        citiesAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, cities);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(citiesAdapter);
        provincesKey = new ArrayList<>();
        //
        //TODO: Set Database Reference
        databaseProvinces = FirebaseDatabase.getInstance().getReference().child("provinces");
        //TODO: Change addEventListener to addListenerForSingleValueEvent
        databaseProvinces.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                //TODO: Initialize List of provinces
                final List<String> provinces = new ArrayList<String>();
                for (DataSnapshot provinceSnapshot : dataSnapshot.getChildren()) {
                    //TODO: Get Value of provinceName
                    final String provinceName = provinceSnapshot.child("provinceName").getValue(String.class);
                    provinces.add(provinceName);
                    //TODO: Add key to array
                    String key = provinceSnapshot.getKey();
                    provincesKey.add(key);
                    //--
                    spinnerProvinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int spinnerProvincesPosition = spinnerProvinces.getSelectedItemPosition();
                            //TODO: Remove all cities and clear selection city
                            cities.clear();
                            spinnerCities.setSelection(0);
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
//                MaterialSpinner provincesSpinner = (MaterialSpinner) findViewById(R.id.provinceSpinner_signup);
                ArrayAdapter<String> provincesAdapter = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_item, provinces);
                provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProvinces.setAdapter(provincesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        inputDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUpActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dobSetListener,
                        day, month, year);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dobSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int dayOfMonth, int month, int year) {
                month = month + 1;
                Log.d("onDateSet: date: " + dayOfMonth + "-" + month + "-" + year, dobSetListener.toString());

                String date = dayOfMonth + "-" + month + "-" + year;
                inputDob.setText(date);
            }
        };

        userPhotoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhotosMediaOpen();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Photos Validation
                if (userPhotoIv.getDrawable() != null) {

                } else {
                    Toast.makeText(getApplicationContext(), "Choose Your Profile Photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Email Validation
                final String validEmail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+";
                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                Matcher matcherEmail = Pattern.compile(validEmail).matcher(email);
                if (matcherEmail.matches()) {

                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Password Validation
                final String validPassword = "^(?=\\S+$).{4,}$";
                String password = inputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too Short, Enter Minimum 6 Characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                Matcher matcherPassword = Pattern.compile(validPassword).matcher(password);
                if (matcherPassword.matches()) {

                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Full Name Validation
                String fName = inputFullName.getText().toString().trim();
                if (TextUtils.isEmpty(fName)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Full Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                //DOB Validation
                String dobDate = inputDob.getText().toString().trim();
                if (TextUtils.isEmpty(dobDate)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Birth Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Gender Validation
                int spinnerGenderPosition = spinnerGender.getSelectedItemPosition();
                if (spinnerGenderPosition != 0) {

                } else {
                    Toast.makeText(SignUpActivity.this, "Pick Your Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Province Validation
                int spinnerProvincePosition = spinnerProvinces.getSelectedItemPosition();
                if (spinnerProvincePosition != 0) {

                } else {
                    Toast.makeText(getApplicationContext(), "Select Your Province", Toast.LENGTH_SHORT).show();
                    return;
                }

                //City Validation
                int spinnerCityPosition = spinnerCities.getSelectedItemPosition();
                if (spinnerCityPosition != 0) {

                } else {
                    Toast.makeText(getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Phone Number Validation
                final String validPhone = "^[+]?[0-9]{10,13}$";
                String phone = inputPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                Matcher matcherPhone = Pattern.compile(validPhone).matcher(phone);
                if (matcherPhone.matches()) {

                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Address Validation
                String address = inputAddress.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                emailExistFirebase();

                //Create User
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                addUserData();
                                final FirebaseUser user = auth.getCurrentUser();
                                user.sendEmailVerification();
                                Toast.makeText(getApplicationContext(), "Your Account has been Created, Check Your Email to Verified", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(SignUpActivity.this, LandingActivity.class));
//                                finish();
                                if (task.isComplete()) {
                                    startActivity(new Intent(SignUpActivity.this, LandingActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Authentication Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void emailExistFirebase() {
        //Email Check on Firebase
        String email = inputEmail.getText().toString();
        auth.fetchProvidersForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        boolean checkEmail = !task.getResult().getProviders().isEmpty();
                        if (!checkEmail) {

                        } else {
                            Toast.makeText(getApplicationContext(), "Email Already Used", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void userPhotosMediaOpen() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image from Gallery"), 2);
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
                userPhotoIv.setImageBitmap(bitmap);
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

    private void addUserData() {
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        storageUsers = FirebaseStorage.getInstance().getReference();
        StorageReference reference = storageUsers.child(STORAGE_PATH + System.currentTimeMillis() + "." + getActualImage(uriUserPhoto));
        reference.putFile(uriUserPhoto)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String email = inputEmail.getText().toString().trim();
                        String userUid = auth.getCurrentUser().getUid();
                        String fName = inputFullName.getText().toString().trim();
                        String dobDate = inputDob.getText().toString().trim();
                        String spinnerValue = spinnerGender.getSelectedItem().toString().trim();
                        String pPhotoUrl = taskSnapshot.getDownloadUrl().toString();
                        String province = spinnerProvinces.getSelectedItem().toString().trim();
                        String city = spinnerCities.getSelectedItem().toString().trim();
                        String phone = inputPhone.getText().toString().trim();
                        String address = inputAddress.getText().toString().trim();
                        String status = "0";
                        String citystatus = city+"_0";

                        User user = new User(email, userUid, fName, dobDate, spinnerValue, pPhotoUrl, province, city, phone, address, status, citystatus);

                        databaseUsers.child(userUid).setValue(user);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, LandingActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}