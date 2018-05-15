package clouwiko.dev.prasiku.activity.activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
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
import clouwiko.dev.prasiku.activity.model.Cat;

public class AppReceivedReviewActivity extends AppCompatActivity {
    private String TAG = "AppReceivedReviewActivity";
    private ImageView imCatPhoto;
    private TextView tvAppName, tvCatName, tvPhone, tvAddress, tvJob, tvReason, tvNoA, tvHouseType, tvHouseSize, tvNoP, tvCatPlace, tvHouseMember, tvMovingPlan, tvMarriagePlan, tvKids, tvFinancial, tvAppStatus;
    private Button btnSaveNum, btnAccept, btnReject, btnWa, btnMessage;
    private FirebaseAuth auth;
    private DatabaseReference databaseAdoptions, databaseCats, databaseAdoptionsReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_received_review);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Get Current User ID
        final String appId = getIntent().getStringExtra("application_id");

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
        btnSaveNum = findViewById(R.id.appreceivedreview_savenumber_button);
        btnAccept = findViewById(R.id.appreceivedreview_accept_button);
        btnReject = findViewById(R.id.appreceivedreview_reject_button);
        btnWa = findViewById(R.id.appreceivedreview_whatsapp_button);
        btnMessage = findViewById(R.id.appreceivedreview_message_button);

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

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppReceivedReviewActivity.this);
                builder.setMessage("Are You sure want to set this person as adopter?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                        String owneridapponstatus = ownerid + "_" + apponstatus;
                                        String deletestatus = dataSnapshot.child("adoptionDeleteStatus").getValue(String.class);
                                        String appliddelete = appid + "_0";
//                                        String ownerdeletestatus = dataSnapshot.child("adoptionOwnerDeleteStatus").getValue(String.class);
                                        String catidapponstatus = catid + "_" + apponstatus;

                                        updateAcceptedAdoptionData(adoptionid, catid, ownerid, appid, appliddelete, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, owneridapponstatus, deletestatus, catidapponstatus);
                                        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catid);
                                        databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String catidupdate = dataSnapshot.child("catId").getValue(String.class);
                                                String ownerupdate = dataSnapshot.child("catOwnerId").getValue(String.class);
                                                String photoupdate = dataSnapshot.child("catProfilePhoto").getValue(String.class);
                                                String nameupdate = dataSnapshot.child("catName").getValue(String.class);
                                                String dobupdate = dataSnapshot.child("catDob").getValue(String.class);
                                                String genderupdate = dataSnapshot.child("catGender").getValue(String.class);
                                                String descriptionupdate = dataSnapshot.child("catDescription").getValue(String.class);
                                                String mednoteupdate = dataSnapshot.child("catMedNote").getValue(String.class);
                                                String vaccstatupdate = dataSnapshot.child("catVaccStat").getValue(String.class);
                                                String spayneuterstatupdate = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
                                                String reasonupdate = dataSnapshot.child("catReason").getValue(String.class);
                                                String catprovinceupdate = dataSnapshot.child("catProvince").getValue(String.class);
                                                String catcityupdate = dataSnapshot.child("catCity").getValue(String.class);
                                                String catdelete = dataSnapshot.child("catDeleteStatus").getValue(String.class);
                                                String ownercatdelete = dataSnapshot.child("catOwnerDeleteStatus").getValue(String.class);
                                                String adoptedstatusupdate = "Adopted";
                                                String citydeletestatus = dataSnapshot.child("catCityDeleteStatus").getValue(String.class);

                                                updateAcceptedCatData(catidupdate, ownerupdate, photoupdate, nameupdate, dobupdate, genderupdate, descriptionupdate, mednoteupdate, vaccstatupdate, spayneuterstatupdate, reasonupdate, catprovinceupdate, catcityupdate, adoptedstatusupdate, catdelete, ownercatdelete, citydeletestatus);
                                                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                                                Toast.makeText(getApplicationContext(), "Check your accepted Application to Download the terms", Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                String cat_extra = getIntent().getStringExtra("cat_id");
                                String catapponstatus = cat_extra+"_Received";
                                databaseAdoptionsReject = FirebaseDatabase.getInstance().getReference().child("adoptions");
                                databaseAdoptionsReject.orderByChild("adoptionCatIdApponStatus").equalTo(catapponstatus).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot updRejectAppSnapshot : dataSnapshot.getChildren()){
                                            Adoption updRejectAppAdoption = updRejectAppSnapshot.getValue(Adoption.class);
                                            updRejectAppAdoption.setAdoptionApplicationStatus("Rejected");
                                            updRejectAppAdoption.setAdoptionCatIdApponStatus(updRejectAppAdoption.getAdoptionCatId()+"_Rejected");
                                            updRejectAppAdoption.setAdoptionOwnerIdApponStatus(updRejectAppAdoption.getAdoptionOwnerId()+"_Rejected");
                                            databaseAdoptionsReject.child(updRejectAppAdoption.getAdoptionId()).setValue(updRejectAppAdoption);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppReceivedReviewActivity.this);
                builder.setMessage("Are You sure want to reject this person as adoption application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                        String owneridapponstatus = ownerid + "_" + apponstatus;
                                        String deletestatus = dataSnapshot.child("adoptionDeleteStatus").getValue(String.class);
                                        String applicantiddelete = appid + "_" + deletestatus;
                                        String catidapponstatus = catid + "_" + apponstatus;
//                                        String ownerdeletestatus = dataSnapshot.child("adoptionOwnerDeleteStatus").getValue(String.class);

                                        updateRejectedAdoptionData(adoptionid, catid, ownerid, appid, applicantiddelete, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, owneridapponstatus, deletestatus, catidapponstatus);
                                        Intent intent = new Intent(getApplicationContext(), AppReceivedActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
                } catch (android.content.ActivityNotFoundException ex){
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
                        sendIntent.putExtra("address",appphone);
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

    private boolean updateAcceptedAdoptionData(String adoptionId, String adoptionCatId, String adoptionOwnerId, String adoptionApplicantId, String adoptionApplicantIdDeleteStatus, String adoptionApplicantPhone, String adoptionApplicantAddress, String adoptionApplicantJob, String adoptionApplicantReason, String adoptionApplicantNoAnimal, String adoptionApplicantHouseType, String adoptionApplicantHouseSize, String adoptionApplicantNoPeople, String adoptionApplicantCatPlace, String adoptionApplicantFamPermission, String adoptionApplicantMove, String adoptionApplicantMarriage, String adoptionApplicantKids, String adoptionApplicantFinancial, String adoptionApplicationStatus, String adoptionCatName, String adoptionCatPhoto, String adoptionApplicantName, String adoptionOwnerIdApponStatus, String adoptionDeleteStatus, String adoptionCatIdApponStatus) {
        String applicationid = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
//        Adoption adoptionAccept = new Adoption(adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantIdDeleteStatus, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionOwnerIdApponStatus, adoptionCatIdApponStatus, adoptionDeleteStatus, adoptionOwnerDeleteStatus);
        Adoption adoptionAccept = new Adoption(adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantIdDeleteStatus, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionOwnerIdApponStatus, adoptionDeleteStatus, adoptionCatIdApponStatus);
        databaseAdoptions.setValue(adoptionAccept);
        return true;
    }

    private boolean updateRejectedAdoptionData(String adoptionId, String adoptionCatId, String adoptionOwnerId, String adoptionApplicantId, String adoptionApplicantIdDeleteStatus, String adoptionApplicantPhone, String adoptionApplicantAddress, String adoptionApplicantJob, String adoptionApplicantReason, String adoptionApplicantNoAnimal, String adoptionApplicantHouseType, String adoptionApplicantHouseSize, String adoptionApplicantNoPeople, String adoptionApplicantCatPlace, String adoptionApplicantFamPermission, String adoptionApplicantMove, String adoptionApplicantMarriage, String adoptionApplicantKids, String adoptionApplicantFinancial, String adoptionApplicationStatus, String adoptionCatName, String adoptionCatPhoto, String adoptionApplicantName, String adoptionOwnerIdApponStatus, String adoptionDeleteStatus, String adoptionCatIdApponStatus) {
        String applicationid = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
//        Adoption adoptionReject = new Adoption(adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantIdDeleteStatus, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionOwnerIdApponStatus, adoptionCatIdApponStatus, adoptionDeleteStatus, adoptionOwnerDeleteStatus);
        Adoption adoptionReject = new Adoption(adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantIdDeleteStatus, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionOwnerIdApponStatus, adoptionDeleteStatus, adoptionCatIdApponStatus);
        databaseAdoptions.setValue(adoptionReject);
        return true;
    }

    private boolean updateAcceptedCatData(String catId, String catOwnerId, String catProfilePhoto, String catName, String catDob, String catGender, String catDescription, String catMedNote, String catVaccStat, String catSpayNeuterStat, String catReason, String catProvince, String catCity, String catAdoptedStatus, String catDeleteStatus, String catOwnerDeleteStatus, String catCityDeleteStatus) {
        String catid = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catid);

        Cat cat = new Cat(catId, catOwnerId, catProfilePhoto, catName, catDob, catGender, catDescription, catMedNote, catVaccStat, catSpayNeuterStat, catReason, catProvince, catCity, catAdoptedStatus, catDeleteStatus, catOwnerDeleteStatus, catCityDeleteStatus);
        databaseCats.setValue(cat);

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AppReceivedActivity.class);
        startActivity(intent);
    }
}
