package clouwiko.dev.prasiku.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;
import clouwiko.dev.prasiku.activity.model.Report;
import clouwiko.dev.prasiku.activity.model.User;

public class ReportFormActivity extends AppCompatActivity {
    private EditText etOwner, etAdopter, etMessage;
    private Button btnReport;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReports, databaseAdoptions, databaseCats, databaseUsers, databaseOwner, databaseAdopter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pengaduan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etOwner = findViewById(R.id.owner_name);
        etOwner.setEnabled(false);
        etAdopter = findViewById(R.id.adopter_name);
        etAdopter.setEnabled(false);
        etMessage = findViewById(R.id.report_message);
        btnReport = findViewById(R.id.action_send_report);

        String ownerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String adopterid = getIntent().getStringExtra("userId");

        databaseOwner = FirebaseDatabase.getInstance().getReference().child("users").child(ownerId);
        databaseOwner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User ownerData = dataSnapshot.getValue(User.class);
                String ownername = ownerData.getUserFname();

                etOwner.setText(ownername);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseAdopter = FirebaseDatabase.getInstance().getReference().child("users").child(adopterid);
        databaseAdopter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User adopterData = dataSnapshot.getValue(User.class);
                String adoptername = adopterData.getUserFname();

                etAdopter.setText(adoptername);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valMessage = etMessage.getText().toString();
                if (valMessage.isEmpty()){
                    etMessage.setError("Isi pesan laporan pengaduan Anda");
                    return;
                } else {

                }
                if (valMessage.length()<100){
                    Toast.makeText(getApplicationContext(), "Jelaskan pesan pengaduan dengan lebih rinci", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
                reportAdopter();
//                databaseReports = FirebaseDatabase.getInstance().getReference().child("reports");
//                String applicationid = getIntent().getStringExtra("application_id");
//                String userId = getIntent().getStringExtra("userId");
//                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
//                databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Adoption adoptionData = dataSnapshot.getValue(Adoption.class);
//                        final String reportedname = adoptionData.getAdoptionApplicantName();
//                        final String reportcat = adoptionData.getAdoptionCatId();
//                        final String reportadoptions = adoptionData.getAdoptionId();
//                        databaseOwner.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                User userData = dataSnapshot.getValue(User.class);
//                                String reporterid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                String reportedid = getIntent().getStringExtra("userId");
//                                String reportername = userData.getUserFname();
//                                String reportmessage = etMessage.getText().toString();
//                                String reportstatus = "Received";
//                                String reportkey = databaseReports.push().getKey();
//                                Date date = Calendar.getInstance().getTime();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//                                String reportdate = dateFormat.format(date);
//                                Report report = new Report(reportkey, reportdate, reporterid, reportername, reportedid, reportedname, reportmessage, reportstatus, reportcat, reportadoptions);
//                                databaseReports.child(reportkey).setValue(report);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                String userId = getIntent().getStringExtra("userId");
                String applicationid = getIntent().getStringExtra("application_id");
                Intent intent = new Intent(getApplicationContext(), UserHomeAcceptedActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("application_id", applicationid);
                startActivity(intent);
            }
        });
    }

    private void reportAdopter(){
        databaseReports = FirebaseDatabase.getInstance().getReference().child("reports");
        String applicationid = getIntent().getStringExtra("application_id");
        databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child(applicationid);
        databaseAdoptions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Adoption adoptionData = dataSnapshot.getValue(Adoption.class);
                final String reportedname = adoptionData.getAdoptionApplicantName();
                final String reportcat = adoptionData.getAdoptionCatId();
                final String reportadoptions = adoptionData.getAdoptionId();
                databaseOwner.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User userData = dataSnapshot.getValue(User.class);
                        String reporterid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String reportedid = getIntent().getStringExtra("userId");
                        String reportername = userData.getUserFname();
                        String reportmessage = etMessage.getText().toString();
                        String reportstatus = "Received";
                        String reportkey = databaseReports.push().getKey();
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        String reportdate = dateFormat.format(date);
                        Report report = new Report(reportkey, reportdate, reporterid, reportername, reportedid, reportedname, reportmessage, reportstatus, reportcat, reportadoptions);
                        databaseReports.child(reportkey).setValue(report);
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
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReportFormActivity.this);
        builder.setMessage("Are You sure want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String applicationid = getIntent().getStringExtra("application_id");
                        String userId = getIntent().getStringExtra("userId");
                        Intent intent = new Intent(getApplicationContext(), UserHomeAcceptedActivity.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("application_id", applicationid);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
