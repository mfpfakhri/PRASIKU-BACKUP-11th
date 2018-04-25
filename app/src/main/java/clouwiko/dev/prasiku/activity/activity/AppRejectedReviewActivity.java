package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;

public class AppRejectedReviewActivity extends AppCompatActivity {
    private String TAG = "AppRejectedReviewActivity";
    private ImageView imCatPhoto;
    private TextView tvAppName, tvCatName, tvPhone, tvAddress, tvJob, tvReason, tvNoA, tvHouseType, tvHouseSize, tvNoP, tvCatPlace, tvHouseMember, tvMovingPlan, tvMarriagePlan, tvKids, tvFinancial, tvAppStatus;
    private Button btnWa, btnMessage, btnSaveNum;
    private FirebaseAuth auth;
    private DatabaseReference databaseAdoptions, databaseCats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_rejected_review);


        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Get Current User ID
        final String appId = getIntent().getStringExtra("application_id");

        imCatPhoto = findViewById(R.id.apprejectedreview_cat_photo);
        tvAppName = findViewById(R.id.apprejectedreview_applicant_name);
        tvCatName = findViewById(R.id.apprejectedreview_cat_name);
        tvPhone = findViewById(R.id.apprejectedreview_applicant_phone);
        tvAddress = findViewById(R.id.apprejectedreview_applicant_address);
        tvJob = findViewById(R.id.apprejectedreview_applicant_job);
        tvReason = findViewById(R.id.apprejectedreview_applicant_reason);
        tvNoA = findViewById(R.id.apprejectedreview_applicant_noa);
        tvHouseType = findViewById(R.id.apprejectedreview_applicant_house_type);
        tvHouseSize = findViewById(R.id.apprejectedreview_applicant_house_size);
        tvNoP = findViewById(R.id.apprejectedreview_applicant_nop);
        tvCatPlace = findViewById(R.id.apprejectedreview_applicant_catplace);
        tvHouseMember = findViewById(R.id.apprejectedreview_applicant_housemember);
        tvMovingPlan = findViewById(R.id.apprejectedreview_applicant_movingplan);
        tvMarriagePlan = findViewById(R.id.apprejectedreview_applicant_marriageplan);
        tvKids = findViewById(R.id.apprejectedreview_applicant_kids);
        tvFinancial = findViewById(R.id.apprejectedreview_applicant_financial);
        tvAppStatus = findViewById(R.id.apprejectedreview_applicant_status);
        btnSaveNum = findViewById(R.id.apprejectedreview_savenumber_button);
        btnWa = findViewById(R.id.apprejectedreview_whatsapp_button);
        btnMessage = findViewById(R.id.apprejectedreview_message_button);

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
        btnSaveNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(appId);
                databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Adoption adoption = dataSnapshot.getValue(Adoption.class);
                        String appname = adoption.getAdoptionApplicantName().toString().trim();
                        String appphone = adoption.getAdoptionApplicantPhone().toString().trim();

                        Intent intent = new Intent(Intent.ACTION_INSERT);
                        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                        intent.putExtra(ContactsContract.Intents.Insert.NAME, "SIKU " + appname);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, appphone);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btnWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String cname = getIntent().getStringExtra("cat_name");
                    String wamessage = "Hi, I saw " + cname + " on the SIKU application, does " + cname + " still available to adopt? I am interested for adopting " + cname + ". Thank You";
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, wamessage);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                }
            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(appId);
                databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Adoption adoption = dataSnapshot.getValue(Adoption.class);
                        String appphone = adoption.getAdoptionApplicantPhone().toString().trim();
                        String cname = getIntent().getStringExtra("cat_name");
                        String message = "Hi, I saw " + cname + " on the SIKU application, does " + cname + " still available to adopt? I am interested for adopting " + cname + ". Thank You";

                        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getApplicationContext());
                        Uri uri = Uri.parse("tel:" + appphone);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                        sendIntent.putExtra("address", appphone);
                        sendIntent.putExtra("sms_body", message);
                        sendIntent.setPackage(defaultSmsPackageName);
                        sendIntent.setType("vnd.android-dir/mms-sms");
                        startActivity(sendIntent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AppRejectedActivity.class);
        startActivity(intent);
    }
}
