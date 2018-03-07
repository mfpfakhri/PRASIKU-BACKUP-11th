package clouwiko.dev.prasiku.activity.activity;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clouwiko.dev.prasiku.R;
import fr.ganfra.materialspinner.MaterialSpinner;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputFullName, inputDob, inputPhone, inputAddress;
    private DatePickerDialog.OnDateSetListener dobSetListener;
    private MaterialSpinner spinnerGender;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseCities, databaseUsers;
    private AutoCompleteTextView autoCompleteTextViewCity;
    private ImageView userPhotoIv;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // Action on Click
        btnSignUp = (Button) findViewById(R.id.action_sign_up_button);
        btnSignIn = (Button) findViewById(R.id.intent_sign_in_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputFullName = (EditText) findViewById(R.id.full_name);
        inputDob = (EditText) findViewById(R.id.dobDatepicker);
        inputPhone = (EditText) findViewById(R.id.phoneNumber);
        inputAddress = (EditText) findViewById(R.id.address);
        spinnerGender = (MaterialSpinner) findViewById(R.id.gender);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        autoCompleteTextViewCity = (AutoCompleteTextView) findViewById(R.id.cityAutoCompleteTextView);
        userPhotoIv = (ImageView) findViewById(R.id.userPhotos);

        databaseCities = FirebaseDatabase.getInstance().getReference().child("cities");
        final ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        databaseCities.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                autoCompleteTextViewCity.setThreshold(1);
                for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
                    String cityName = citySnapshot.child("cityName").getValue(String.class);
                    citiesAdapter.add(cityName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        autoCompleteTextViewCity.setAdapter(citiesAdapter);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        inputDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUpActivity.this,
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
                String fname = inputFullName.getText().toString().trim();
                if (TextUtils.isEmpty(fname)) {
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
                int spinnerPosition = spinnerGender.getSelectedItemPosition();
                if (spinnerPosition != 0) {

                } else {
                    Toast.makeText(SignUpActivity.this, "Pick Your Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                //City Validation
                String city = autoCompleteTextViewCity.getText().toString().trim();
                if (TextUtils.isEmpty(city)) {
                    Toast.makeText(getApplicationContext(), "Enter Your City", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Enter Valid Phone", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Address Validation
                String address = inputAddress.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //Create User
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "Your Account has been Created", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {
                                    userProfile();
                                    startActivity(new Intent(SignUpActivity.this, LandingActivity.class));
                                    finish();
                                }
                            }
                        });
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
            if (data != null){
                uri = data.getData();
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

    private void cropUserImage() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri,"image/*");

            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("outputX", 180);
            cropIntent.putExtra("outputY", 180);
            cropIntent.putExtra("aspectX", 3);
            cropIntent.putExtra("aspectY", 4);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            cropIntent.putExtra("return-data", true);

            startActivityForResult(cropIntent, 1);
        } catch (ActivityNotFoundException ex){

        }
    }

    private void userProfile() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, LandingActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
