package clouwiko.dev.prasiku.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;
import fr.ganfra.materialspinner.MaterialSpinner;

public class ApplicantAdoptionFormActivity extends AppCompatActivity {

    private TextView title;
    private EditText etPhone, etAddress, etJob, etReason, etAnimalNumber, etHouseSize, etPeopleNumber, etAnimalLive;
    private MaterialSpinner msHouseType;
    private RadioGroup rgFamilyPermission, rgMovingPlan, rgMarriagePlan, rgKids, rgFinancial;
    private RadioButton rbFamilyPermission, rbMovingPlan, rbMarriagePlan, rbKids, rbFinancial;
    private Button btnApply;
    private DatabaseReference databaseAdoption;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_adoption_form);

        title = findViewById(R.id.adoptionform_title);
        Typeface faceslogan = Typeface.createFromAsset(getAssets(), "fonts/segoeuisl.ttf");
        title.setTypeface(faceslogan);
        etPhone = findViewById(R.id.adoptionform_phone);
        etAddress = findViewById(R.id.adoptionform_address);
        etJob = findViewById(R.id.adoptionform_job);
        etReason = findViewById(R.id.adoptionform_reasonwhy);
        etAnimalNumber = findViewById(R.id.adoptionform_animalnumber);
        etHouseSize = findViewById(R.id.adoptionform_housesize);
        etPeopleNumber = findViewById(R.id.adoptionform_peoplenumber);
        etAnimalLive = findViewById(R.id.adoptionform_animallive);
        msHouseType = findViewById(R.id.adoptionform_housetype);
        rgFamilyPermission = findViewById(R.id.adoptionform_housememberpermission);
        rgMovingPlan = findViewById(R.id.adoptionform_movingplan);
        rgMarriagePlan = findViewById(R.id.adoptionform_aftermarriage);
        rgKids = findViewById(R.id.adoptionform_kidsinhouse);
        rgFinancial = findViewById(R.id.adoptionform_financialplan);
        btnApply = findViewById(R.id.adoptionform_submit_button);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        user = auth.getCurrentUser();

        //Get Current User Key
        final String applicantID = user.getUid().toString().trim();

        //Database Reference Path
        databaseAdoption = FirebaseDatabase.getInstance().getReference().child("adoptions");

        String catId = getIntent().getStringExtra("cat_id");
        String ownerId = getIntent().getStringExtra("owner_id");
        String cName = getIntent().getStringExtra("cat_name");
        String cPhoto = getIntent().getStringExtra("cat_photo");

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Applicant Phone Validation
                String phoneValidation = etPhone.getText().toString();
                if (TextUtils.isEmpty(phoneValidation)) {
                    Toast.makeText(getApplicationContext(), "Enter Your phone number", Toast.LENGTH_SHORT).show();
                }
                final String validPhone = "^[+]?[0-9]{10,13}$";
                Matcher matcherPhone = Pattern.compile(validPhone).matcher(phoneValidation);
                if (matcherPhone.matches()) {

                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Applicant Address Validation
                String addressValidation = etAddress.getText().toString();
                if (TextUtils.isEmpty(addressValidation)) {
                    Toast.makeText(getApplicationContext(), "Enter Your address", Toast.LENGTH_SHORT).show();
                }
                //Applicant Reason Validation
                String reasonValidation = etReason.getText().toString();
                if (TextUtils.isEmpty(reasonValidation)) {
                    Toast.makeText(getApplicationContext(), "Enter Your reason to adopt", Toast.LENGTH_SHORT).show();
                }
                //Applicant Number of Animal Validation
                String noAnimalValidation = etAnimalNumber.getText().toString();
                if (TextUtils.isEmpty(noAnimalValidation)) {
                    Toast.makeText(getApplicationContext(), "Enter the Number of animal You have", Toast.LENGTH_SHORT).show();
                }
                String validNoa = "^\\d{1,3}$";
                Matcher matcherNoa= Pattern.compile(validNoa).matcher(noAnimalValidation);
                if (matcherNoa.matches()) {

                } else {
//                    Toast.makeText(getApplicationContext(), "Enter Number Only", Toast.LENGTH_SHORT).show();
                    etAnimalNumber.setError("Enter Number Only");
                    return;
                }
                //House Type Validation
                int houseTypePosition = msHouseType.getSelectedItemPosition();
                if (houseTypePosition != 0) {

                } else {
                    Toast.makeText(getApplicationContext(), "Choose Your house type", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Applicant House Size Validation
                String houseSizeValidation = etHouseSize.getText().toString();
                if (TextUtils.isEmpty(houseSizeValidation)) {
                    Toast.makeText(getApplicationContext(), "Enter the size of House Where You lived", Toast.LENGTH_SHORT).show();
                }
                String validHouseSize = "^\\d{1,5}$";
                Matcher matcherHouseSize = Pattern.compile(validHouseSize).matcher(houseSizeValidation);
                if (matcherHouseSize.matches()) {

                } else {
//                    Toast.makeText(getApplicationContext(), "Enter Number Only", Toast.LENGTH_SHORT).show();
                    etHouseSize.setError("Enter Number Only");
                    return;
                }
                //Number of People Validation
                String peopleNumberValidation = etPeopleNumber.getText().toString();
                if (TextUtils.isEmpty(peopleNumberValidation)) {
                    Toast.makeText(getApplicationContext(), "Enter Number of People who lived in the House", Toast.LENGTH_SHORT).show();
                }
                String validNop = "^\\d{1,3}$";
                Matcher matcherNop = Pattern.compile(validNop).matcher(peopleNumberValidation);
                if (matcherNop.matches()) {

                } else {
                    etPeopleNumber.setError("Enter Number Only");
                    return;
                }
                //Applicant Animal will Live Validation
                String animalPlaceValidation = etAnimalLive.getText().toString();
                if (TextUtils.isEmpty(animalPlaceValidation)) {
                    Toast.makeText(getApplicationContext(), "Enter the Place Where the Cats Will Stay", Toast.LENGTH_SHORT).show();
                }
                String validCatPlace = "(?<=\\s|^)[a-zA-Z]*(?=[.,;:]?\\s|$)";
                Matcher matcherCatPlace = Pattern.compile(validCatPlace).matcher(animalPlaceValidation);
                if (matcherCatPlace.matches()) {

                } else {
                    etAnimalLive.setError("Enter Alphabet Only");
                    return;
                }
                //Family Permission Validation
                if (rgFamilyPermission.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Pick Family Permission option", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
                //Moving Plan Validation
                if (rgMovingPlan.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Pick Your moving plan option", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
                //Marriage Plan Validation
                if (rgMarriagePlan.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Pick Your marriage plan option", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
                //Kids in The House Validation
                if (rgKids.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Pick kids in Your house condition option", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
                //Financial Plan Validation
                if (rgFinancial.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Pick Your financial plan option", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
                applyAdoption();
                backToMainMenu();
            }
        });
    }

    private void backToMainMenu() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        Toast.makeText(getApplicationContext(), "Your Adoption Application has been Sent", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    private void applyAdoption() {
        //Database Reference Path
        databaseAdoption = FirebaseDatabase.getInstance().getReference().child("adoptions");
        int selectedFamilyPermission = rgFamilyPermission.getCheckedRadioButtonId();
        rbFamilyPermission = findViewById(selectedFamilyPermission);
        int selectedMovingPlan = rgMovingPlan.getCheckedRadioButtonId();
        rbMovingPlan = findViewById(selectedMovingPlan);
        int selectedMarriagePlan = rgMarriagePlan.getCheckedRadioButtonId();
        rbMarriagePlan = findViewById(selectedMarriagePlan);
        int selectedKidsHouse = rgKids.getCheckedRadioButtonId();
        rbKids = findViewById(selectedKidsHouse);
        int selectedFinancial = rgFinancial.getCheckedRadioButtonId();
        rbFinancial = findViewById(selectedFinancial);

        String adoptionId = databaseAdoption.push().getKey();
        String catId = getIntent().getStringExtra("cat_id");
        String ownerId = getIntent().getStringExtra("owner_id");
        String catname = getIntent().getStringExtra("cat_name");
        String catphoto = getIntent().getStringExtra("cat_photo");
        String applicantname = getIntent().getStringExtra("applicant_name");
        String applicantId = auth.getCurrentUser().getUid().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String job = etJob.getText().toString().trim();
        String reasonwhy = etReason.getText().toString().trim();
        String numberofanimal = etAnimalNumber.getText().toString().trim();
        String houseize = etHouseSize.getText().toString().trim();
        String familynumber = etPeopleNumber.getText().toString().trim();
        String animallive = etAnimalLive.getText().toString().trim();
        String housetype = msHouseType.getSelectedItem().toString().trim();
        String familypermission = rbFamilyPermission.getText().toString().trim();
        String movingplan = rbMovingPlan.getText().toString().trim();
        String marriageplan = rbMarriagePlan.getText().toString().trim();
        String kidsinhouse = rbKids.getText().toString().trim();
        String financial = rbFinancial.getText().toString().trim();
        String status = "Received";
        String owner_status = ownerId + "_" + status;
        String delete_status = "0";
        String applicantIdDelete = applicantId + "_" + delete_status;
//        String owner_delete_status = ownerId + delete_status;
        String cat_status = catId + "_" + status;
        Adoption adoption = new Adoption(adoptionId, catId, ownerId, applicantId, applicantIdDelete, phone, address, job, reasonwhy, numberofanimal, housetype, houseize, familynumber, animallive, familypermission, movingplan, marriageplan, kidsinhouse, financial, status, catname, catphoto, applicantname, owner_status, delete_status, cat_status);

        databaseAdoption.child(adoptionId).setValue(adoption);
    }

    @Override
    public void onBackPressed() {
        final String catId = getIntent().getStringExtra("cat_id");
        final String ownerId = getIntent().getStringExtra("owner_id");
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantAdoptionFormActivity.this);
        builder.setMessage("Are You sure want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), CatProfileApplicantAvailableActivity.class);
                        intent.putExtra("previousActivity", "findcat");
                        intent.putExtra("owner_id", ownerId);
                        intent.putExtra("cat_id", catId);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
