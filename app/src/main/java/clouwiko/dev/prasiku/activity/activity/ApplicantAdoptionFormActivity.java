package clouwiko.dev.prasiku.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import clouwiko.dev.prasiku.R;
import fr.ganfra.materialspinner.MaterialSpinner;

public class ApplicantAdoptionFormActivity extends AppCompatActivity {

    private EditText etPhone, etAddress, etJob, etReason, etAnimalNumber, etHouseSize, etPeopleNumber, etAnimalLive;
    private MaterialSpinner msHouseType;
    private Switch swFamilyPermission, swMovingPlan, swMarriagePlan, swKids, swFinancial;
    private Button btnApply;
    private DatabaseReference databaseAdoption;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_adoption_form);

        etPhone = findViewById(R.id.adoptionform_phone);
        etAddress = findViewById(R.id.adoptionform_address);
        etJob = findViewById(R.id.adoptionform_job);
        etReason = findViewById(R.id.adoptionform_reasonwhy);
        etAnimalNumber = findViewById(R.id.adoptionform_animalnumber);
        etHouseSize = findViewById(R.id.adoptionform_housesize);
        etPeopleNumber = findViewById(R.id.adoptionform_peoplenumber);
        etAnimalLive = findViewById(R.id.adoptionform_animallive);
        msHouseType = findViewById(R.id.adoptionform_housetype);
        swFamilyPermission = findViewById(R.id.adoptionform_familypermission);
        swMovingPlan = findViewById(R.id.adoptionform_movingplan);
        swMarriagePlan = findViewById(R.id.adoptionform_aftermarriage);
        swKids = findViewById(R.id.adoptionform_kidsinhouse);
        swFinancial = findViewById(R.id.adoptionform_financialplan);
        btnApply = findViewById(R.id.adoptionform_submit_button);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        user = auth.getCurrentUser();

        //Get Current User Key
        String applicantID = user.getUid().toString().trim();

        //Database Reference Path
        databaseAdoption = FirebaseDatabase.getInstance().getReference().child("adoption");

        String catId = getIntent().getStringExtra("cat_id");
        String ownerId = getIntent().getStringExtra("owner_id");

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyAdoption();
            }
        });
        final String[] financial = new String[]{"No", "Yes"};
        swFinancial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    financial[0] = "Yes";
                } else {
                    financial[0] = "No";
                }
            }
        });
    }

    private void applyAdoption(){
        //Database Reference Path
        databaseAdoption = FirebaseDatabase.getInstance().getReference().child("adoption");

        String adoptionId = databaseAdoption.push().getKey();
        String catId = getIntent().getStringExtra("cat_id");
        String ownerId = getIntent().getStringExtra("owner_id");
        String applicantId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String job = etJob.getText().toString().trim();
        String reasonwhy = etReason.getText().toString().trim();
        String numberofanimal = etAnimalNumber.getText().toString().trim();
        String houseize = etHouseSize.getText().toString().trim();
        String familynumber = etPeopleNumber.getText().toString().trim();
        String animallive = etAnimalLive.getText().toString().trim();
        String housetype = msHouseType.getSelectedItem().toString().trim();
        final String[] familypermission = new String[]{"No", "Yes"};
        swFamilyPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    familypermission[0] = "Yes";
                } else {
                    familypermission[0] = "No";
                }
            }
        });
        final String[] movingplan = new String[]{"No", "Yes"};
        swMovingPlan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    movingplan[0] = "Yes";
                } else {
                    movingplan[0] = "No";
                }
            }
        });
        final String[] marriageplan = new String[]{"No", "Yes"};
        swMarriagePlan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    marriageplan[0] = "Yes";
                } else {
                    marriageplan[0] = "No";
                }
            }
        });
        final String[] kidsinhouse = new String[]{"No", "Yes"};
        swKids.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    kidsinhouse[0] = "Yes";
                } else {
                    kidsinhouse[0] = "No";
                }
            }
        });
        final String[] financial = new String[]{"No", "Yes"};
        swFinancial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    financial[0] = "Yes";
                } else {
                    financial[0] = "No";
                }
            }
        });
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
