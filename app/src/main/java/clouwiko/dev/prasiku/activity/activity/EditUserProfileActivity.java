package clouwiko.dev.prasiku.activity.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.User;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditUserProfileActivity extends AppCompatActivity {
    private String TAG = "EditUserProfileActivity";
    private EditText txtFullName, txtDob, txtPhone, txtAddress;
    private DatePickerDialog.OnDateSetListener dobSetListener;
    private MaterialSpinner spinnerGender, spinnerProvinces, spinnerCities;
    private Button btnUpdate;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseProvinces, databaseCities, databaseUsers;

    ArrayAdapter<String> citiesAdapter;
    List<String> cities;
    List<String> provincesKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edituserprofile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();
        final String userID = auth.getCurrentUser().getUid();

        txtFullName = findViewById(R.id.edituserprofile_full_name);
        txtDob = findViewById(R.id.edituserprofile_dob);
        txtPhone = findViewById(R.id.edituserprofile_phonenumber);
        txtAddress = findViewById(R.id.edituserprofile_address);
        spinnerGender = findViewById(R.id.edituserprofile_genderspinner);
        spinnerProvinces = findViewById(R.id.edituserprofile_provincespinner);
        spinnerCities = findViewById(R.id.edituserprofile_cityspinner);
        btnUpdate = findViewById(R.id.edituserprofile_update_button);
        progressBar = findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

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
                ArrayAdapter<String> provincesAdapter = new ArrayAdapter<String>(EditUserProfileActivity.this, android.R.layout.simple_spinner_item, provinces);
                provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProvinces.setAdapter(provincesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditUserProfileActivity.this,
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
                txtDob.setText(date);
            }
        };

        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        databaseUsers.orderByKey().equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userdatasnapshot : dataSnapshot.getChildren()) {
                    User userdata = userdatasnapshot.getValue(User.class);
                    txtFullName.setText(userdata.getUserFname());
                    txtDob.setText(userdata.getUserDob());
                    txtPhone.setText(userdata.getUserPhone());
                    txtAddress.setText(userdata.getUserAddress());
                    String gender = userdata.getUserGender();
                    switch (gender) {
                        case "Male":
                            spinnerGender.setSelection(1);
                            break;
                        case "Female":
                            spinnerGender.setSelection(2);
                            break;
                        default:
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Full Name Validation
                String fName = txtFullName.getText().toString().trim();
                if (TextUtils.isEmpty(fName)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Full Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                //DOB Validation
                String dobDate = txtDob.getText().toString().trim();
                if (TextUtils.isEmpty(dobDate)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Birth Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Gender Validation
                int spinnerGenderPosition = spinnerGender.getSelectedItemPosition();
                if (spinnerGenderPosition != 0) {

                } else {
                    Toast.makeText(EditUserProfileActivity.this, "Pick Your Gender", Toast.LENGTH_SHORT).show();
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
                String phone = txtPhone.getText().toString().trim();
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
                String address = txtAddress.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
                databaseUsers.orderByKey().equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userdatasnapshot : dataSnapshot.getChildren()) {
                            User userdata = userdatasnapshot.getValue(User.class);
                            String updName = txtFullName.getText().toString().trim();
                            String updDob = txtDob.getText().toString().trim();
                            String updGender = spinnerGender.getSelectedItem().toString().trim();
                            String updProvince = spinnerProvinces.getSelectedItem().toString().trim();
                            String updCity = spinnerCities.getSelectedItem().toString().trim();
                            String updPhone = txtPhone.getText().toString().trim();
                            String updAddress = txtAddress.getText().toString().trim();
                            userdata.setUserFname(updName);
                            userdata.setUserDob(updDob);
                            userdata.setUserGender(updGender);
                            userdata.setUserProvince(updProvince);
                            userdata.setUserCity(updCity);
                            userdata.setUserPhone(updPhone);
                            userdata.setUserAddress(updAddress);
                            databaseUsers.child(userdata.getUserUid()).setValue(userdata);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                backToMainMenu();
                finish();
            }
        });
    }

    private void backToMainMenu() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        Toast.makeText(getApplicationContext(), "Your Information Successfully Updated", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
}
