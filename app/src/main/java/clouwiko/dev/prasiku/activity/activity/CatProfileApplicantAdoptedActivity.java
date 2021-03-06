package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
    private TextView tvCatName, tvOwner, tvCity, tvGender, tvDesc, tvDob, tvMed, tvVacc, tvSpNeu, tvReason, tvAdoptionStatus;
    private Button btnAdopted;
    private FirebaseAuth auth;
    private DatabaseReference databaseCats, databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_profile_applicant_adopted);

        imCatPhoto = findViewById(R.id.cpa_adopted_photovalue);
        tvCatName = findViewById(R.id.cpa_adopted_catnamevalue);
        tvOwner = findViewById(R.id.cpa_adopted_ownervalue);
        tvCity = findViewById(R.id.cpa_adopted_cityvalue);
        tvGender = findViewById(R.id.cpa_adopted_gendervalue);
        tvDesc = findViewById(R.id.cpa_adopted_descvalue);
        tvDob = findViewById(R.id.cpa_adopted_dobvalue);
        tvMed = findViewById(R.id.cpa_adopted_medvalue);
        tvVacc = findViewById(R.id.cpa_adopted_vaccinevalue);
        tvSpNeu = findViewById(R.id.cpa_adopted_spayneutervalue);
        tvReason = findViewById(R.id.cpa_adopted_reasonvalue);
        tvAdoptionStatus = findViewById(R.id.cpa_adopted_adoptstatusvalue);
        btnAdopted = findViewById(R.id.cpa_adopted_button);

        btnAdopted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Kucing Ini Telah Diadopsi", Toast.LENGTH_SHORT).show();
            }
        });

        String pActivity = getIntent().getStringExtra("previousActivity");

        getCatProfile();
    }

    private void getCatProfile() {
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
                String setgender = null;
                switch (gender) {
                    case "Male":
                        setgender = "Jantan";
                        break;
                    case "Female":
                        setgender = "Betina";
                        break;
                    case "Unknown":
                        setgender = "Tidak Diketahui";
                        break;
                }
                String desc = dataSnapshot.child("catDescription").getValue(String.class);
                String dob = dataSnapshot.child("catDob").getValue(String.class);
                String mednote = dataSnapshot.child("catMedNote").getValue(String.class);
                String vaccine = dataSnapshot.child("catVaccStat").getValue(String.class);
                String setvaccine = null;
                switch (vaccine){
                    case "Yes, Already Vaccinated":
                        setvaccine = "Ya, Sudah Divaksin";
                        break;
                    case "Not Yet":
                        setvaccine = "Belum Divaksin";
                        break;
                }
                String spayneuter = dataSnapshot.child("catSpayNeuterStat").getValue(String.class);
                String setspayneuter = null;
                switch (spayneuter){
                    case "Yes, Already Spayed/ Neutered":
                        setspayneuter = "Ya, Sudah Dikastrasi";
                        break;
                    case "Not Yet":
                        setspayneuter = "Belum Dikastrasi";
                        break;
                }
                String reason = dataSnapshot.child("catReason").getValue(String.class);
                String setreason = null;
                switch (reason) {
                    case "Stray":
                        setreason = "Liar";
                        break;
                    case "Abandoned":
                        setreason = "Terlantar";
                        break;
                    case "Abused":
                        setreason = "Disiksa";
                        break;
                    case "Owner Dead":
                        setreason = "Pemilik Meninggal";
                        break;
                    case "Owner Give Up":
                        setreason = "Pemilik Menyerah";
                        break;
                    case "House Moving":
                        setreason = "Pindah Rumah";
                        break;
                    case "Financial":
                        setreason = "Keuangan";
                        break;
                    case "Medical Problem":
                        setreason = "Masalah Kesehatan";
                        break;
                    case "Others":
                        setreason = "Lainnya";
                        break;
                }
                String adoptionstatus = dataSnapshot.child("catAdoptedStatus").getValue(String.class);
                String setadoptionstatus = null;
                switch (adoptionstatus){
                    case "Available":
                        setadoptionstatus = "Belum Diadopsi";
                        break;
                    case "Adopted":
                        setadoptionstatus = "Sudah Diadopsi";
                        break;
                }
                String catphotouri = dataSnapshot.child("catProfilePhoto").getValue(String.class);

                tvCatName.setText(catname);
                tvCity.setText(city);
                tvGender.setText(setgender);
                tvDesc.setText(desc);
                tvDob.setText(dob);
                tvMed.setText(mednote);
                tvVacc.setText(setvaccine);
                tvSpNeu.setText(setspayneuter);
                tvReason.setText(setreason);
                tvAdoptionStatus.setText(setadoptionstatus);
                if (catphotouri.equals("")) {
                    String noPhoto = "@drawable/no_image";
                    int imageResource = getResources().getIdentifier(noPhoto, null, getPackageName());
                    Drawable res = getResources().getDrawable(imageResource);
                    imCatPhoto.setImageDrawable(res);
                    imCatPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } else {
                    Picasso.get().load(catphotouri).centerCrop().resize(256, 256).into(imCatPhoto);
                }

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

    @Override
    public void onBackPressed() {
        String pActivity = getIntent().getStringExtra("previousActivity");
        String locHistory = getIntent().getStringExtra("locHistory");
        Intent intentFindCat = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
        Intent intentMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);

        if (pActivity.equals("findcat")) {
            intentFindCat.putExtra("locHistory", locHistory);
            startActivity(intentFindCat);
            finish();
        } else {
            startActivity(intentMainMenu);
            finish();
        }
    }
}
