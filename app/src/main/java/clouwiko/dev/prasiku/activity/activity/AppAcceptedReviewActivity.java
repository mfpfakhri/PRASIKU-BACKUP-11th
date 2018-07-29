package clouwiko.dev.prasiku.activity.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;
import clouwiko.dev.prasiku.activity.other.AgreementNotificationGenerator;

public class AppAcceptedReviewActivity extends AppCompatActivity {
    private String TAG = "AppAcceptedReviewActivity";
    private ImageView imCatPhoto;
    private TextView tvAppName, tvCatName, tvPhone, tvAddress, tvJob, tvReason, tvNoA, tvHouseType, tvHouseSize, tvNoP, tvCatPlace, tvHouseMember, tvMovingPlan, tvMarriagePlan, tvKids, tvFinancial, tvAppStatus;
    private Button btnSaveNum, btnWa, btnMessage, btnAgreement;
    private FirebaseAuth auth;
    private DatabaseReference databaseAdoptions, databaseCats;
    private static final int PERMISSION_REQUEST_STORAGE = 1;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_accepted_review);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Get Current User ID
        final String appId = getIntent().getStringExtra("application_id");

        imCatPhoto = findViewById(R.id.appacceptedreview_cat_photo);
        tvAppName = findViewById(R.id.appacceptedreview_applicant_name);
        tvCatName = findViewById(R.id.appacceptedreview_cat_name);
        tvPhone = findViewById(R.id.appacceptedreview_applicant_phone);
        tvAddress = findViewById(R.id.appacceptedreview_applicant_address);
        tvJob = findViewById(R.id.appacceptedreview_applicant_job);
        tvReason = findViewById(R.id.appacceptedreview_applicant_reason);
        tvNoA = findViewById(R.id.appacceptedreview_applicant_noa);
        tvHouseType = findViewById(R.id.appacceptedreview_applicant_house_type);
        tvHouseSize = findViewById(R.id.appacceptedreview_applicant_house_size);
        tvNoP = findViewById(R.id.appacceptedreview_applicant_nop);
        tvCatPlace = findViewById(R.id.appacceptedreview_applicant_catplace);
        tvHouseMember = findViewById(R.id.appacceptedreview_applicant_housemember);
        tvMovingPlan = findViewById(R.id.appacceptedreview_applicant_movingplan);
        tvMarriagePlan = findViewById(R.id.appacceptedreview_applicant_marriageplan);
        tvKids = findViewById(R.id.appacceptedreview_applicant_kids);
        tvFinancial = findViewById(R.id.appacceptedreview_applicant_financial);
        tvAppStatus = findViewById(R.id.appacceptedreview_applicant_status);
        btnSaveNum = findViewById(R.id.appacceptedreview_savenumber_button);
        btnAgreement = findViewById(R.id.appacceptedreview_agreement_button);
        btnWa = findViewById(R.id.appacceptedreview_whatsapp_button);
        btnMessage = findViewById(R.id.appacceptedreview_message_button);
        btnAgreement = findViewById(R.id.appacceptedreview_agreement_button);

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
                        String applicationid = getIntent().getStringExtra("application_id");
                        Intent intent = new Intent(getApplicationContext(), UserHomeAcceptedActivity.class);
                        intent.putExtra("application_id", applicationid);
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

                        intent.putExtra(ContactsContract.Intents.Insert.NAME, "SIKU " + appname);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, ("0"+appphone));
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
                    String wamessage = "Hi, Saya melihat " + cname + " melalui aplikasi SIKUCING, apakah " + cname + " masih bisa untuk diadopsi? Saya tertarik untuk mengadopsi " + cname + ". Terimakasih";
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, wamessage);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whatsapp belum dipasang pada perangkat.", Toast.LENGTH_SHORT).show();
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
                        sendIntent.putExtra("address", ("0"+appphone));
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

        //Give the app permission to access storage
        if (ContextCompat.checkSelfPermission(AppAcceptedReviewActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AppAcceptedReviewActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(AppAcceptedReviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
            } else {
                ActivityCompat.requestPermissions(AppAcceptedReviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
            }
        } else {

        }

        btnAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAgreement();
//                downloadAsset("Surat Perjanjian Adopsi SIKUCING.pdf");
//                AgreementNotificationGenerator.openPdfNotification(getApplicationContext());
//                AlertDialog.Builder builder = new AlertDialog.Builder(AppAcceptedReviewActivity.this);
//                builder.setMessage("Do You want to open adoption agreement?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+File.separator+"Surat Perjanjian Adopsi SIKUCING.pdf");
//                                Uri path = Uri.fromFile(pdfFile);
//                                Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
//                                pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                pdfOpenIntent.setDataAndType(path, "application/pdf");
//                                try{
//                                    AppAcceptedReviewActivity.this.startActivity(pdfOpenIntent);
//                                } catch (ActivityNotFoundException e) {
//                                    Toast.makeText(getApplicationContext(), "No apps can perform this action, open the directory where adoption agreement manually", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        })
//                        .setNegativeButton("No", null);
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();

                //Another Option : open downloaded file directory
//                AlertDialog.Builder builder = new AlertDialog.Builder(AppAcceptedReviewActivity.this);
//                builder.setMessage("Do You want to open agreement directory?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+File.separator);
//                                Uri uri = Uri.fromFile(path);
//                                PackageManager packageManager = getPackageManager();
//                                boolean myfiles_installed;
//                                try {
//                                    packageManager.getPackageInfo("com.sec.android.app.myfiles", PackageManager.GET_ACTIVITIES);
//                                    myfiles_installed = true;
//                                } catch (PackageManager.NameNotFoundException e) {
//                                    myfiles_installed = false;
//                                }
//                                Intent intent = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.myfiles");
//                                if (myfiles_installed == true) {
//                                    intent.setAction("samsung.myfiles.intent.action.LAUNCH_MY_FILES");
//                                    intent.putExtra("samsung.myfiles.intent.extra.START_PATH", path.getAbsolutePath());
//                                    intent.setDataAndType(uri, "application/pdf");
//                                    startActivity(intent);
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "No apps can perform this action, open the directory where adoption agreement manually", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        })
//                        .setNegativeButton("No", null);
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
            }
        });
    }

    private void downloadAgreement(){
        downloadAsset("Surat Perjanjian Adopsi SIKUCING.pdf");
        AgreementNotificationGenerator.openPdfNotification(getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(AppAcceptedReviewActivity.this);
        builder.setMessage("Apakah Anda ingin membuka surat perjanjian adopsi?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+File.separator+"Surat Perjanjian Adopsi SIKUCING.pdf");
                        Uri path = Uri.fromFile(pdfFile);
                        Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
                        pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pdfOpenIntent.setDataAndType(path, "application/pdf");
                        try{
                            AppAcceptedReviewActivity.this.startActivity(pdfOpenIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "Tidak ada aplikasi yang dapat membuka surat perjanjian adopsi", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Tidak", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(AppAcceptedReviewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Tidak ada ijin yang diperbolehkan", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void downloadAsset(String filename) {
        //Another Option : Put Adoption Agreement on SIKUCING directory
//        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SIKUCING";

        //Put Adoption Agreement on System Default Downloads directory
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+File.separator;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            File outfile = new File(dirPath, filename);
            out = new FileOutputStream(outfile);
            downloadFile(in, out);
            Toast.makeText(this, "Surat perjanjian telah diunduh, periksa memori penyimpanan Anda", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal mengunduh surat perjanjian", Toast.LENGTH_SHORT).show();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void downloadFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AppAcceptedActivity.class);
        startActivity(intent);
    }
}
