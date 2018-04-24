package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;

public class AppReceivedReviewActivity extends AppCompatActivity {
    private String TAG = "AppReceivedReviewActivity";
    private ImageView imCatPhoto;
    private TextView tvAppName, tvCatName, tvPhone, tvAddress, tvJob, tvReason, tvNoA, tvHouseType, tvHouseSize, tvNoP, tvCatPlace, tvHouseMember, tvMovingPlan, tvMarriagePlan, tvKids, tvFinancial, tvAppStatus;
    private Button btnAccept, btnReject;
    private FirebaseAuth auth;
    private DatabaseReference databaseAdoptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_received_review);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Get Current User ID
        String appId = getIntent().getStringExtra("application_id");

        imCatPhoto = findViewById(R.id.appreceivedreview_cat_photo);
        tvAppName = findViewById(R.id.appreceivedreview_applicant_name);
        tvCatName = findViewById(R.id.appreceivedreview_cat_name);
        tvPhone = findViewById(R.id.appreceivedreview_applicant_phone);
        tvAddress = findViewById(R.id.appreceivedreview_applicant_address);
        tvJob = findViewById(R.id.appreceivedreview_applicant_job);
        tvReason = findViewById(R.id.appreceivedreview_applicant_reason);
        tvNoA = findViewById(R.id.appreceivedreview_applicant_noa);
        tvHouseType = findViewById(R.id.appreceivedreview_applicant_house_type);
        tvHouseSize = findViewById(R.id.appreceivedreview_applicant_house_size);
        tvNoP = findViewById(R.id.appreceivedreview_applicant_nop);
        tvCatPlace = findViewById(R.id.appreceivedreview_applicant_catplace);
        tvHouseMember = findViewById(R.id.appreceivedreview_applicant_housemember);
        tvMovingPlan = findViewById(R.id.appreceivedreview_applicant_movingplan);
        tvMarriagePlan = findViewById(R.id.appreceivedreview_applicant_marriageplan);
        tvKids = findViewById(R.id.appreceivedreview_applicant_kids);
        tvFinancial = findViewById(R.id.appreceivedreview_applicant_financial);
        tvAppStatus = findViewById(R.id.appreceivedreview_applicant_status);
        btnAccept = findViewById(R.id.appreceivedreview_accept_button);
        btnReject = findViewById(R.id.appreceivedreview_reject_button);

        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(appId);
        databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String catphoto = dataSnapshot.child("adoptionCatPhoto").getValue(String.class);
                String appname = dataSnapshot.child("adoptionApplicantName").getValue(String.class);
                String catname = dataSnapshot.child("adoptionCatName").getValue(String.class);
                String appphone = dataSnapshot.child("adoptionApplicantPhone").getValue(String.class);
                String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
                String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
                String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
                String appnoa = dataSnapshot.child("adoptionApplicantNoAnimal").getValue(String.class);
                String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
                String apphousesize = dataSnapshot.child("adoptionApplicantHouseSize").getValue(String.class);
                String appnop = dataSnapshot.child("adoptionApplicantNoPeople").getValue(String.class);
                String catplace = dataSnapshot.child("adoptionApplicantCatPlace").getValue(String.class);
                String apphousemember = dataSnapshot.child("adoptionApplicantFamPermission").getValue(String.class);
                String appmovingplan = dataSnapshot.child("adoptionApplicantMove").getValue(String.class);
                String appmarriageplan = dataSnapshot.child("adoptionApplicantMarriage").getValue(String.class);
                String appkids = dataSnapshot.child("adoptionApplicantKids").getValue(String.class);
                String appfinancial = dataSnapshot.child("adoptionApplicantFinancial").getValue(String.class);
                String appstatus = dataSnapshot.child("adoptionApplicationStatus").getValue(String.class);

                Picasso.get().load(catphoto).centerCrop().resize(128, 128).into(imCatPhoto);
                tvAppName.setText(appname);
                tvCatName.setText(catname);
                tvPhone.setText(appphone);
                tvAddress.setText(appaddress);
                tvJob.setText(appjob);
                tvReason.setText(appreason);
                tvNoA.setText(appnoa);
                tvHouseType.setText(apphousetype);
                tvHouseSize.setText(apphousesize);
                tvNoP.setText(appnop);
                tvCatPlace.setText(catplace);
                tvHouseMember.setText(apphousemember);
                tvMovingPlan.setText(appmovingplan);
                tvMarriagePlan.setText(appmarriageplan);
                tvKids.setText(appkids);
                tvFinancial.setText(appfinancial);
                tvAppStatus.setText(appstatus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String applicationid = getIntent().getStringExtra("application_id");
                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
                databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String adoptionid = dataSnapshot.child("adoptionId").getValue(String.class);
                        String catid = dataSnapshot.child("adoptionCatId").getValue(String.class);
                        String ownerid = dataSnapshot.child("adoptionOwnerId").getValue(String.class);
                        String appid = dataSnapshot.child("adoptionApplicantId").getValue(String.class);
                        String appphone = dataSnapshot.child("adoptionApplicantPhone").getValue(String.class);
                        String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
                        String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
                        String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
                        String appnoanimal = dataSnapshot.child("adoptionApplicantNoAnimal").getValue(String.class);
                        String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
                        String apphousesize = dataSnapshot.child("adoptionApplicantHouseSize").getValue(String.class);
                        String appnopeople = dataSnapshot.child("adoptionApplicantNoPeople").getValue(String.class);
                        String appcatplace = dataSnapshot.child("adoptionApplicantCatPlace").getValue(String.class);
                        String appfampermission = dataSnapshot.child("adoptionApplicantFamPermission").getValue(String.class);
                        String appmove = dataSnapshot.child("adoptionApplicantMove").getValue(String.class);
                        String appmarriage = dataSnapshot.child("adoptionApplicantMarriage").getValue(String.class);
                        String appkids = dataSnapshot.child("adoptionApplicantKids").getValue(String.class);
                        String appfinancial = dataSnapshot.child("adoptionApplicantFinancial").getValue(String.class);
                        String apponstatus = "Accepted";
                        String catname = dataSnapshot.child("adoptionCatName").getValue(String.class);
                        String catphoto = dataSnapshot.child("adoptionCatPhoto").getValue(String.class);
                        String appname = dataSnapshot.child("adoptionApplicantName").getValue(String.class);
                        String appphoto = dataSnapshot.child("adoptionApplicantPhoto").getValue(String.class);
                        String owneridapponstatus = ownerid + "_" + apponstatus;

                        updateAcceptedAdoptionData(adoptionid, catid, ownerid, appid, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, appphoto, owneridapponstatus);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String applicationid = getIntent().getStringExtra("application_id");
                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
                databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String adoptionid = dataSnapshot.child("adoptionId").getValue(String.class);
                        String catid = dataSnapshot.child("adoptionCatId").getValue(String.class);
                        String ownerid = dataSnapshot.child("adoptionOwnerId").getValue(String.class);
                        String appid = dataSnapshot.child("adoptionApplicantId").getValue(String.class);
                        String appphone = dataSnapshot.child("adoptionApplicantPhone").getValue(String.class);
                        String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
                        String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
                        String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
                        String appnoanimal = dataSnapshot.child("adoptionApplicantNoAnimal").getValue(String.class);
                        String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
                        String apphousesize = dataSnapshot.child("adoptionApplicantHouseSize").getValue(String.class);
                        String appnopeople = dataSnapshot.child("adoptionApplicantNoPeople").getValue(String.class);
                        String appcatplace = dataSnapshot.child("adoptionApplicantCatPlace").getValue(String.class);
                        String appfampermission = dataSnapshot.child("adoptionApplicantFamPermission").getValue(String.class);
                        String appmove = dataSnapshot.child("adoptionApplicantMove").getValue(String.class);
                        String appmarriage = dataSnapshot.child("adoptionApplicantMarriage").getValue(String.class);
                        String appkids = dataSnapshot.child("adoptionApplicantKids").getValue(String.class);
                        String appfinancial = dataSnapshot.child("adoptionApplicantFinancial").getValue(String.class);
                        String apponstatus = "Rejected";
                        String catname = dataSnapshot.child("adoptionCatName").getValue(String.class);
                        String catphoto = dataSnapshot.child("adoptionCatPhoto").getValue(String.class);
                        String appname = dataSnapshot.child("adoptionApplicantName").getValue(String.class);
                        String appphoto = dataSnapshot.child("adoptionApplicantPhoto").getValue(String.class);
                        String owneridapponstatus = ownerid + "_" + apponstatus;

                        updateRejectedAdoptionData(adoptionid, catid, ownerid, appid, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, appphoto, owneridapponstatus);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private boolean updateAcceptedAdoptionData(String adoptionId, String adoptionCatId, String adoptionOwnerId, String adoptionApplicantId, String adoptionApplicantPhone, String adoptionApplicantAddress, String adoptionApplicantJob, String adoptionApplicantReason, String adoptionApplicantNoAnimal, String adoptionApplicantHouseType, String adoptionApplicantHouseSize, String adoptionApplicantNoPeople, String adoptionApplicantCatPlace, String adoptionApplicantFamPermission, String adoptionApplicantMove, String adoptionApplicantMarriage, String adoptionApplicantKids, String adoptionApplicantFinancial, String adoptionApplicationStatus, String adoptionCatName, String adoptionCatPhoto, String adoptionApplicantName, String adoptionApplicantPhoto, String adoptionOwnerIdApponStatus) {
        String applicationid = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
        Adoption adoptionAccept = new Adoption(adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionApplicantPhoto, adoptionOwnerIdApponStatus);
        databaseAdoptions.setValue(adoptionAccept);
        return true;
    }

    private boolean updateRejectedAdoptionData(String adoptionId, String adoptionCatId, String adoptionOwnerId, String adoptionApplicantId, String adoptionApplicantPhone, String adoptionApplicantAddress, String adoptionApplicantJob, String adoptionApplicantReason, String adoptionApplicantNoAnimal, String adoptionApplicantHouseType, String adoptionApplicantHouseSize, String adoptionApplicantNoPeople, String adoptionApplicantCatPlace, String adoptionApplicantFamPermission, String adoptionApplicantMove, String adoptionApplicantMarriage, String adoptionApplicantKids, String adoptionApplicantFinancial, String adoptionApplicationStatus, String adoptionCatName, String adoptionCatPhoto, String adoptionApplicantName, String adoptionApplicantPhoto, String adoptionOwnerIdApponStatus) {
        String applicationid = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
        Adoption adoptionReject = new Adoption(adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionApplicantPhoto, adoptionOwnerIdApponStatus);
        databaseAdoptions.setValue(adoptionReject);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AppReceivedActivity.class);
        startActivity(intent);
    }
}
