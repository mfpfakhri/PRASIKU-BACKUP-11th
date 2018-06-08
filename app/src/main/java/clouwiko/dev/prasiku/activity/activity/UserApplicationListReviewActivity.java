package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

public class UserApplicationListReviewActivity extends AppCompatActivity {
    private String TAG = "ApplicantAdoptionReview";
    private ImageView imCatPhoto;
    private TextView tvAppName, tvCatName, tvPhone, tvAddress, tvJob, tvReason, tvNoA, tvHouseType, tvHouseSize, tvNoP, tvCatPlace, tvHouseMember, tvMovingPlan, tvMarriagePlan, tvKids, tvFinancial, tvAppStatus;
    private FirebaseAuth auth;
    private DatabaseReference databaseAdoptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_application_list_review);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Get Current User ID
        String userId = auth.getCurrentUser().getUid().toString().trim();
        String appId = getIntent().getStringExtra("application_id");

        imCatPhoto = findViewById(R.id.adoptionreview_cat_photo);
        tvAppName = findViewById(R.id.adoptionreview_applicant_name);
        tvCatName = findViewById(R.id.adoptionreview_cat_name);
        tvPhone = findViewById(R.id.adoptionreview_applicant_phone);
        tvAddress = findViewById(R.id.adoptionreview_applicant_address);
        tvJob = findViewById(R.id.adoptionreview_applicant_job);
        tvReason = findViewById(R.id.adoptionreview_applicant_reason);
        tvNoA = findViewById(R.id.adoptionreview_applicant_noa);
        tvHouseType = findViewById(R.id.adoptionreview_applicant_house_type);
        tvHouseSize = findViewById(R.id.adoptionreview_applicant_house_size);
        tvNoP = findViewById(R.id.adoptionreview_applicant_nop);
        tvCatPlace = findViewById(R.id.adoptionreview_applicant_catplace);
        tvHouseMember = findViewById(R.id.adoptionreview_applicant_housemember);
        tvMovingPlan = findViewById(R.id.adoptionreview_applicant_movingplan);
        tvMarriagePlan = findViewById(R.id.adoptionreview_applicant_marriageplan);
        tvKids = findViewById(R.id.adoptionreview_applicant_kids);
        tvFinancial = findViewById(R.id.adoptionreview_applicant_financial);
        tvAppStatus = findViewById(R.id.adoptionreview_applicant_status);

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
                String setstatus = null;
                switch (appstatus) {
                    case "Received":
                        setstatus = "Diterima";
                        SpannableStringBuilder receivedBuilder = new SpannableStringBuilder();
                        SpannableString receivedSpannable = new SpannableString(setstatus);
                        receivedSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, receivedSpannable.length(), 0);
                        receivedBuilder.append(receivedSpannable);
                        tvAppStatus.setText(receivedBuilder, TextView.BufferType.SPANNABLE);
                        break;
                    case "Accepted":
                        setstatus = "Disetujui";
                        SpannableStringBuilder acceptedBuilder = new SpannableStringBuilder();
                        SpannableString acceptedSpannable = new SpannableString(setstatus);
                        acceptedSpannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, acceptedSpannable.length(), 0);
                        acceptedBuilder.append(acceptedSpannable);
                        tvAppStatus.setText(acceptedBuilder, TextView.BufferType.SPANNABLE);
                        break;
                    case "Rejected":
                        setstatus = "Ditolak";
                        SpannableStringBuilder rejectedBuilder = new SpannableStringBuilder();
                        SpannableString rejectedSpannable = new SpannableString(setstatus);
                        rejectedSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, rejectedSpannable.length(), 0);
                        rejectedBuilder.append(rejectedSpannable);
                        tvAppStatus.setText(rejectedBuilder, TextView.BufferType.SPANNABLE);
                        break;
                }
//                Picasso.get().load(catphoto).centerCrop().resize(128, 128).into(imCatPhoto);
                if (catphoto.equals("")) {
                    String noPhoto = "@drawable/no_image";
                    int imageResource = getResources().getIdentifier(noPhoto, null, getPackageName());
                    Drawable res = getResources().getDrawable(imageResource);
                    imCatPhoto.setImageDrawable(res);
                    imCatPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } else {
                    Picasso.get().load(catphoto).resize(256, 256).into(imCatPhoto);
                }
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), UserApplicationListActivity.class);
        startActivity(intent);
        finish();
    }
}
