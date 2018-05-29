package clouwiko.dev.prasiku.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import clouwiko.dev.prasiku.R;
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

        etOwner = findViewById(R.id.owner_name);
        etOwner.setEnabled(false);
        etAdopter = findViewById(R.id.adopter_name);
        etAdopter.setEnabled(false);
        etMessage = findViewById(R.id.report_message);
        btnReport = findViewById(R.id.action_send_report);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String ownerId = user.getUid();
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
    }
}
