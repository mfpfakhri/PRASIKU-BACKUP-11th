package clouwiko.dev.prasiku.activity.activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
                Long appphone = dataSnapshot.child("adoptionApplicantPhone").getValue(Long.class);
                String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
                String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
                String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
                Long appnoa = dataSnapshot.child("adoptionApplicantNoAnimal").getValue(Long.class);
                String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
                Long apphousesize = dataSnapshot.child("adoptionApplicantHouseSize").getValue(Long.class);
                Long appnop = dataSnapshot.child("adoptionApplicantNoPeople").getValue(Long.class);
                String catplace = dataSnapshot.child("adoptionApplicantCatPlace").getValue(String.class);
                String housememberpermission = dataSnapshot.child("adoptionApplicantFamPermission").getValue(String.class);
                String sethousememberpermission = null;
                switch (housememberpermission){
                    case "Yes":
                        sethousememberpermission = "Sudah";
                        break;
                    case "No":
                        sethousememberpermission = "Belum";
                        break;
                }
                String movingplan = dataSnapshot.child("adoptionApplicantMove").getValue(String.class);
                String setmovingplan= null;
                switch (movingplan){
                    case "Yes":
                        setmovingplan = "Sudah";
                        break;
                    case "No":
                        setmovingplan = "Belum";
                        break;
                }
                String marriageplan = dataSnapshot.child("adoptionApplicantMarriage").getValue(String.class);
                String setmarriageplan = null;
                switch (marriageplan){
                    case "Yes":
                        setmarriageplan = "Sudah";
                        break;
                    case "No":
                        setmarriageplan = "Belum";
                        break;
                }
                String kids = dataSnapshot.child("adoptionApplicantKids").getValue(String.class);
                String setkids = null;
                switch (kids){
                    case "Yes":
                        setkids= "Sudah";
                        break;
                    case "No":
                        setkids = "Belum";
                        break;
                }
                String financial = dataSnapshot.child("adoptionApplicantFinancial").getValue(String.class);
                String setfinancial = null;
                switch (financial){
                    case "Yes":
                        setfinancial= "Sudah";
                        break;
                    case "No":
                        setfinancial= "Belum";
                        break;
                }
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
                tvPhone.setText(String.valueOf("0"+appphone));
                tvAddress.setText(appaddress);
                tvJob.setText(appjob);
                tvReason.setText(appreason);
                tvNoA.setText(String.valueOf(appnoa));
                tvHouseType.setText(apphousetype);
                tvHouseSize.setText(String.valueOf(apphousesize));
                tvNoP.setText(String.valueOf(appnop));
                tvCatPlace.setText(catplace);
                tvHouseMember.setText(sethousememberpermission);
                tvMovingPlan.setText(setmovingplan);
                tvMarriagePlan.setText(setmarriageplan);
                tvKids.setText(setkids);
                tvFinancial.setText(setfinancial);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tvAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(appId);
                databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Adoption adoption = dataSnapshot.getValue(Adoption.class);
                        String applicantid = adoption.getAdoptionApplicantId();
                        Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                        intent.putExtra("userId", applicantid);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

                        intent.putExtra(ContactsContract.Intents.Insert.NAME, "SIKUCING " + appname);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, ("0"+appphone));
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
                builder.setMessage("Apakah Anda Yakin Ingin Menetapkan Pengguna ini Sebagai Pengadopsi?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                acceptApplication();
//                                String applicationid = getIntent().getStringExtra("application_id");
//                                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
//                                databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        String adoptionid = dataSnapshot.child("adoptionId").getValue(String.class);
//                                        String catid = dataSnapshot.child("adoptionCatId").getValue(String.class);
//                                        String ownerid = dataSnapshot.child("adoptionOwnerId").getValue(String.class);
//                                        String appid = dataSnapshot.child("adoptionApplicantId").getValue(String.class);
//                                        Long appphone = Long.valueOf(dataSnapshot.child("adoptionApplicantPhone").getValue(Long.class));
//                                        String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
//                                        String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
//                                        String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
//                                        Long appnoanimal = Long.valueOf(dataSnapshot.child("adoptionApplicantNoAnimal").getValue(Long.class));
//                                        String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
//                                        Long apphousesize = Long.valueOf(dataSnapshot.child("adoptionApplicantHouseSize").getValue(Long.class));
//                                        Long appnopeople = Long.valueOf(dataSnapshot.child("adoptionApplicantNoPeople").getValue(Long.class));
//                                        String appcatplace = dataSnapshot.child("adoptionApplicantCatPlace").getValue(String.class);
//                                        String appfampermission = dataSnapshot.child("adoptionApplicantFamPermission").getValue(String.class);
//                                        String appmove = dataSnapshot.child("adoptionApplicantMove").getValue(String.class);
//                                        String appmarriage = dataSnapshot.child("adoptionApplicantMarriage").getValue(String.class);
//                                        String appkids = dataSnapshot.child("adoptionApplicantKids").getValue(String.class);
//                                        String appfinancial = dataSnapshot.child("adoptionApplicantFinancial").getValue(String.class);
//                                        String apponstatus = "Accepted";
//                                        String catname = dataSnapshot.child("adoptionCatName").getValue(String.class);
//                                        String catphoto = dataSnapshot.child("adoptionCatPhoto").getValue(String.class);
//                                        String appname = dataSnapshot.child("adoptionApplicantName").getValue(String.class);
//                                        String owneridapponstatus = ownerid + "_" + apponstatus;
//                                        String deletestatus = dataSnapshot.child("adoptionDeleteStatus").getValue(String.class);
//                                        String appliddelete = appid + "_0";
////                                        String ownerdeletestatus = dataSnapshot.child("adoptionOwnerDeleteStatus").getValue(String.class);
//                                        String catidapponstatus = catid + "_" + apponstatus;
//
//                                        updateAcceptedAdoptionData(deletestatus, adoptionid, catid, ownerid, appid, appliddelete, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, owneridapponstatus, catidapponstatus);
//                                        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catid);
//                                        databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                String catidupdate = dataSnapshot.child("catId").getValue(String.class);
//                                                String ownerupdate = dataSnapshot.child("catOwnerId").getValue(String.class);
//                                                String photoupdate = dataSnapshot.child("catProfilePhoto").getValue(String.class);
//                                                String nameupdate = dataSnapshot.child("catName").getValue(String.class);
//                                                String dobupdate = dataSnapshot.child("catDob").getValue(String.class);
//                                                String genderupdate = dataSnapshot.child("catGender").getValue(String.class);
//                                                String descriptionupdate = dataSnapshot.child("catDescription").getValue(String.class);
//                                                String mednoteupdate = dataSnapshot.child("catMedNote").getValue(String.class);
//                                                String vaccstatupdate = dataSnapshot.child("catVaccStat").getValue(String.class);
//                                                String spayneuterstatupdate = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
//                                                String reasonupdate = dataSnapshot.child("catReason").getValue(String.class);
//                                                String catprovinceupdate = dataSnapshot.child("catProvince").getValue(String.class);
//                                                String catcityupdate = dataSnapshot.child("catCity").getValue(String.class);
//                                                String catdelete = dataSnapshot.child("catDeleteStatus").getValue(String.class);
//                                                String ownercatdelete = dataSnapshot.child("catOwnerDeleteStatus").getValue(String.class);
//                                                String adoptedstatusupdate = "Adopted";
//                                                String citydeletestatus = dataSnapshot.child("catCityDeleteStatus").getValue(String.class);
//
//                                                updateAcceptedCatData(catidupdate, ownerupdate, photoupdate, nameupdate, dobupdate, genderupdate, descriptionupdate, mednoteupdate, vaccstatupdate, spayneuterstatupdate, reasonupdate, catprovinceupdate, catcityupdate, adoptedstatusupdate, catdelete, ownercatdelete, citydeletestatus);
//                                                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
//                                                Toast.makeText(getApplicationContext(), "Periksa opsi Pengajuan Diterima untuk mengunduh surat perjanjian adopsi", Toast.LENGTH_SHORT).show();
//                                                startActivity(intent);
//                                                finish();
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//                                String cat_extra = getIntent().getStringExtra("cat_id");
//                                String catapponstatus = cat_extra+"_Received";
//                                databaseAdoptionsReject = FirebaseDatabase.getInstance().getReference().child("adoptions");
//                                databaseAdoptionsReject.orderByChild("adoptionCatIdApponStatus").equalTo(catapponstatus).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot updRejectAppSnapshot : dataSnapshot.getChildren()){
//                                            Adoption updRejectAppAdoption = updRejectAppSnapshot.getValue(Adoption.class);
//                                            updRejectAppAdoption.setAdoptionApplicationStatus("Rejected");
//                                            updRejectAppAdoption.setAdoptionCatIdApponStatus(updRejectAppAdoption.getAdoptionCatId()+"_Rejected");
//                                            updRejectAppAdoption.setAdoptionOwnerIdApponStatus(updRejectAppAdoption.getAdoptionOwnerId()+"_Rejected");
//                                            databaseAdoptionsReject.child(updRejectAppAdoption.getAdoptionId()).setValue(updRejectAppAdoption);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
                            }
                        })
                        .setNegativeButton("Tidak", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppReceivedReviewActivity.this);
                builder.setMessage("Apakah Anda Yakin Ingin Menolak Pengguna ini Sebagai Pengadopsi?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                rejectApplication();
//                                String applicationid = getIntent().getStringExtra("application_id");
//                                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
//                                databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        String adoptionid = dataSnapshot.child("adoptionId").getValue(String.class);
//                                        String catid = dataSnapshot.child("adoptionCatId").getValue(String.class);
//                                        String ownerid = dataSnapshot.child("adoptionOwnerId").getValue(String.class);
//                                        String appid = dataSnapshot.child("adoptionApplicantId").getValue(String.class);
//                                        Long appphone = Long.valueOf(dataSnapshot.child("adoptionApplicantPhone").getValue(Long.class));
//                                        String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
//                                        String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
//                                        String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
//                                        Long appnoanimal = Long.valueOf(dataSnapshot.child("adoptionApplicantNoAnimal").getValue(Long.class));
//                                        String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
//                                        Long apphousesize = Long.valueOf(dataSnapshot.child("adoptionApplicantHouseSize").getValue(Long.class));
//                                        Long appnopeople = Long.valueOf(dataSnapshot.child("adoptionApplicantNoPeople").getValue(Long.class));
//                                        String appcatplace = dataSnapshot.child("adoptionApplicantCatPlace").getValue(String.class);
//                                        String appfampermission = dataSnapshot.child("adoptionApplicantFamPermission").getValue(String.class);
//                                        String appmove = dataSnapshot.child("adoptionApplicantMove").getValue(String.class);
//                                        String appmarriage = dataSnapshot.child("adoptionApplicantMarriage").getValue(String.class);
//                                        String appkids = dataSnapshot.child("adoptionApplicantKids").getValue(String.class);
//                                        String appfinancial = dataSnapshot.child("adoptionApplicantFinancial").getValue(String.class);
//                                        String apponstatus = "Rejected";
//                                        String catname = dataSnapshot.child("adoptionCatName").getValue(String.class);
//                                        String catphoto = dataSnapshot.child("adoptionCatPhoto").getValue(String.class);
//                                        String appname = dataSnapshot.child("adoptionApplicantName").getValue(String.class);
//                                        String owneridapponstatus = ownerid + "_" + apponstatus;
//                                        String deletestatus = dataSnapshot.child("adoptionDeleteStatus").getValue(String.class);
//                                        String applicantiddelete = appid + "_" + deletestatus;
//                                        String catidapponstatus = catid + "_" + apponstatus;
//
//                                        updateRejectedAdoptionData(deletestatus, adoptionid, catid, ownerid, appid, applicantiddelete, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, owneridapponstatus, catidapponstatus);
//                                        Intent intent = new Intent(getApplicationContext(), AppReceivedActivity.class);
//                                        startActivity(intent);
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
                            }
                        })
                        .setNegativeButton("Tidak", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String cname = getIntent().getStringExtra("cat_name");
                    String wamessage = "Hi, Saya melihat " + cname + " melalui aplikasi SIKUCING, apakah " + cname + " masih bisa untuk diadopsi? Saya tertarik untuk mengadopsi " + cname + ". Terimakasih";
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, wamessage);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getApplicationContext(), "Whatsapp Belum Dipasang Pada Perangkat Ini.", Toast.LENGTH_SHORT).show();
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
                        String message = "Hi, Saya melihat " + cname + " melalui aplikasi SIKUCING, apakah " + cname + " masih bisa untuk diadopsi? Saya tertarik untuk mengadopsi " + cname + ". Terimakasih";
                        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getApplicationContext());
                        Uri uri = Uri.parse("tel:" + appphone);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                        sendIntent.putExtra("address",("0"+appphone));
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

    private void acceptApplication(){
        String applicationid = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
        databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String adoptionid = dataSnapshot.child("adoptionId").getValue(String.class);
                String catid = dataSnapshot.child("adoptionCatId").getValue(String.class);
                String ownerid = dataSnapshot.child("adoptionOwnerId").getValue(String.class);
                String appid = dataSnapshot.child("adoptionApplicantId").getValue(String.class);
                Long appphone = Long.valueOf(dataSnapshot.child("adoptionApplicantPhone").getValue(Long.class));
                String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
                String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
                String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
                Long appnoanimal = Long.valueOf(dataSnapshot.child("adoptionApplicantNoAnimal").getValue(Long.class));
                String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
                Long apphousesize = Long.valueOf(dataSnapshot.child("adoptionApplicantHouseSize").getValue(Long.class));
                Long appnopeople = Long.valueOf(dataSnapshot.child("adoptionApplicantNoPeople").getValue(Long.class));
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
                String catidapponstatus = catid + "_" + apponstatus;

                updateAcceptedAdoptionData(deletestatus, adoptionid, catid, ownerid, appid, appliddelete, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, owneridapponstatus, catidapponstatus);
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
                        Toast.makeText(getApplicationContext(), "Periksa opsi Pengajuan Diterima untuk mengunduh surat perjanjian adopsi", Toast.LENGTH_SHORT).show();
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

    private void rejectApplication(){
        String applicationid = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
        databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String adoptionid = dataSnapshot.child("adoptionId").getValue(String.class);
                String catid = dataSnapshot.child("adoptionCatId").getValue(String.class);
                String ownerid = dataSnapshot.child("adoptionOwnerId").getValue(String.class);
                String appid = dataSnapshot.child("adoptionApplicantId").getValue(String.class);
                Long appphone = Long.valueOf(dataSnapshot.child("adoptionApplicantPhone").getValue(Long.class));
                String appaddress = dataSnapshot.child("adoptionApplicantAddress").getValue(String.class);
                String appjob = dataSnapshot.child("adoptionApplicantJob").getValue(String.class);
                String appreason = dataSnapshot.child("adoptionApplicantReason").getValue(String.class);
                Long appnoanimal = Long.valueOf(dataSnapshot.child("adoptionApplicantNoAnimal").getValue(Long.class));
                String apphousetype = dataSnapshot.child("adoptionApplicantHouseType").getValue(String.class);
                Long apphousesize = Long.valueOf(dataSnapshot.child("adoptionApplicantHouseSize").getValue(Long.class));
                Long appnopeople = Long.valueOf(dataSnapshot.child("adoptionApplicantNoPeople").getValue(Long.class));
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

                updateRejectedAdoptionData(deletestatus, adoptionid, catid, ownerid, appid, applicantiddelete, appphone, appaddress, appjob, appreason, appnoanimal, apphousetype, apphousesize, appnopeople, appcatplace, appfampermission, appmove, appmarriage, appkids, appfinancial, apponstatus, catname, catphoto, appname, owneridapponstatus, catidapponstatus);
                Intent intent = new Intent(getApplicationContext(), AppReceivedActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean updateAcceptedAdoptionData(String adoptionDeleteStatus, String adoptionId, String adoptionCatId, String adoptionOwnerId, String adoptionApplicantId, String adoptionApplicantIdDeleteStatus, Long adoptionApplicantPhone, String adoptionApplicantAddress, String adoptionApplicantJob, String adoptionApplicantReason, Long adoptionApplicantNoAnimal, String adoptionApplicantHouseType, Long adoptionApplicantHouseSize, Long adoptionApplicantNoPeople, String adoptionApplicantCatPlace, String adoptionApplicantFamPermission, String adoptionApplicantMove, String adoptionApplicantMarriage, String adoptionApplicantKids, String adoptionApplicantFinancial, String adoptionApplicationStatus, String adoptionCatName, String adoptionCatPhoto, String adoptionApplicantName, String adoptionOwnerIdApponStatus, String adoptionCatIdApponStatus) {
        String applicationid = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
        Adoption adoptionAccept = new Adoption(adoptionDeleteStatus, adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantIdDeleteStatus, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionOwnerIdApponStatus, adoptionCatIdApponStatus);
        databaseAdoptions.setValue(adoptionAccept);
        return true;
    }

    private boolean updateRejectedAdoptionData(String adoptionDeleteStatus, String adoptionId, String adoptionCatId, String adoptionOwnerId, String adoptionApplicantId, String adoptionApplicantIdDeleteStatus, Long adoptionApplicantPhone, String adoptionApplicantAddress, String adoptionApplicantJob, String adoptionApplicantReason, Long adoptionApplicantNoAnimal, String adoptionApplicantHouseType, Long adoptionApplicantHouseSize, Long adoptionApplicantNoPeople, String adoptionApplicantCatPlace, String adoptionApplicantFamPermission, String adoptionApplicantMove, String adoptionApplicantMarriage, String adoptionApplicantKids, String adoptionApplicantFinancial, String adoptionApplicationStatus, String adoptionCatName, String adoptionCatPhoto, String adoptionApplicantName, String adoptionOwnerIdApponStatus, String adoptionCatIdApponStatus) {
        String applicationid = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
        Adoption adoptionReject = new Adoption(adoptionDeleteStatus, adoptionId, adoptionCatId, adoptionOwnerId, adoptionApplicantId, adoptionApplicantIdDeleteStatus, adoptionApplicantPhone, adoptionApplicantAddress, adoptionApplicantJob, adoptionApplicantReason, adoptionApplicantNoAnimal, adoptionApplicantHouseType, adoptionApplicantHouseSize, adoptionApplicantNoPeople, adoptionApplicantCatPlace, adoptionApplicantFamPermission, adoptionApplicantMove, adoptionApplicantMarriage, adoptionApplicantKids, adoptionApplicantFinancial, adoptionApplicationStatus, adoptionCatName, adoptionCatPhoto, adoptionApplicantName, adoptionOwnerIdApponStatus, adoptionCatIdApponStatus);
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
