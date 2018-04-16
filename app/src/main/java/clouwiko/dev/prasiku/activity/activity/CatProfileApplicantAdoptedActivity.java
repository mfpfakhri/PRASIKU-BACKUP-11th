package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class CatProfileApplicantAdoptedActivity extends AppCompatActivity {

    private String TAG = "CatProfileApplicantAdopted";
    private ImageView imCatPhoto;
    private TextView tvCatName, tvOwner, tvCity, tvGender, tvDesc, tvDob, tvMed, tvVacc, tvSpNeu, tvReason;
    private Button btnAdopted;
    private FirebaseAuth auth;
    private DatabaseReference databaseCats, databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_profile_applicant_adopted);

        imCatPhoto = findViewById(R.id.cpa__adopted__photovalue);
        tvCatName = findViewById(R.id.cpa__adopted_catnamevalue);
        tvOwner = findViewById(R.id.cpa_adopted_ownervalue);
        tvCity = findViewById(R.id.cpa__adopted__cityvalue);
        tvGender = findViewById(R.id.cpa__adopted__gendervalue);
        tvDesc = findViewById(R.id.cpa__adopted__descvalue);
        tvDob = findViewById(R.id.cpa__adopted__dobvalue);
        tvMed = findViewById(R.id.cpa__adopted__medvalue);
        tvVacc = findViewById(R.id.cpa__adopted__vaccinevalue);
        tvSpNeu = findViewById(R.id.cpa__adopted__spayneutervalue);
        tvReason = findViewById(R.id.cpa__adopted__reasonvalue);
        btnAdopted = findViewById(R.id.cpa_adopted_button);

        btnAdopted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This cat has been adopted", Toast.LENGTH_SHORT).show();
            }
        });

        String pActivity = getIntent().getStringExtra("previousActivity");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getCatDataAppAdopt();
    }

    private void getCatDataAppAdopt() {
        String catId = getIntent().getStringExtra("cat_id");
        String ownerId = getIntent().getStringExtra("owner_id");

        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(ownerId);
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catId);
        databaseCats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String catname = dataSnapshot.child("catName").getValue(String.class);
                String city = dataSnapshot.child("catCity").getValue(String.class);
                String gender = dataSnapshot.child("catGender").getValue(String.class);
                String desc = dataSnapshot.child("catDescription").getValue(String.class);
                String dob = dataSnapshot.child("catDob").getValue(String.class);
                String mednote = dataSnapshot.child("catMedNote").getValue(String.class);
                String vaccine = dataSnapshot.child("catVaccStat").getValue(String.class);
                String spayneuter = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
                String reason = dataSnapshot.child("catReason").getValue(String.class);
                String catphotouri = dataSnapshot.child("catProfilePhoto").getValue(String.class);

                tvCatName.setText(catname);
                tvCity.setText(city);
                tvGender.setText(gender);
                tvDesc.setText(desc);
                tvDob.setText(dob);
                tvMed.setText(mednote);
                tvVacc.setText(vaccine);
                tvSpNeu.setText(spayneuter);
                tvReason.setText(reason);
                Picasso.get().load(catphotouri).centerCrop().resize(192, 192).into(imCatPhoto);

                databaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String owner = dataSnapshot.child("userFname").getValue(String.class);

                        tvOwner.setText(owner);
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

//    private void getOwnerDataAppAdopt() {
//        String ownerId = getIntent().getStringExtra("owner_id");
//
//        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(ownerId);
//        databaseUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String owner = dataSnapshot.child("userFName").getValue(String.class);
//
//                tvOwner.setText(owner);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        String pActivity = getIntent().getStringExtra("previousActivity");
        Intent intentFindCat = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
        Intent intentMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);

        if (pActivity.equals("findcat")) {
            startActivity(intentFindCat);
            finish();
        } else {
            startActivity(intentMainMenu);
            finish();
        }
    }
}
