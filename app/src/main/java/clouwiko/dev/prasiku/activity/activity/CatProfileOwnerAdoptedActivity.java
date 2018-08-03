package clouwiko.dev.prasiku.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;
import clouwiko.dev.prasiku.activity.model.Cat;

public class CatProfileOwnerAdoptedActivity extends AppCompatActivity {

    private String TAG = "CatProfileOwnerAdopted";
    private ImageView imCatPhoto;
    private TextView tvCatName, tvOwner, tvCity, tvGender, tvDesc, tvDob, tvMed, tvVacc, tvSpNeu, tvReason, tvAdoptStatus;
    private Button btnAdopted;
    private FloatingActionMenu fam;
    private FloatingActionButton fabEdit, fabDelete;
    private FirebaseAuth auth;
    private DatabaseReference databaseCats, databaseUsers, databaseAdoptions, databaseDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_profile_owner_adopted);

        imCatPhoto = findViewById(R.id.cpo_adopted_photovalue);
        tvCatName = findViewById(R.id.cpo_adopted_catnamevalue);
        tvOwner = findViewById(R.id.cpo_adopted_ownervalue);
        tvCity = findViewById(R.id.cpo_adopted_cityvalue);
        tvGender = findViewById(R.id.cpo_adopted_gendervalue);
        tvDesc = findViewById(R.id.cpo_adopted_descvalue);
        tvDob = findViewById(R.id.cpo_adopted_dobvalue);
        tvMed = findViewById(R.id.cpo_adopted_medvalue);
        tvVacc = findViewById(R.id.cpo_adopted_vaccinevalue);
        tvSpNeu = findViewById(R.id.cpo_adopted_spayneutervalue);
        tvReason = findViewById(R.id.cpo_adopted_reasonvalue);
        tvAdoptStatus = findViewById(R.id.cpo_adopted_adoptstatusvalue);
        btnAdopted = findViewById(R.id.cpo_adopted_button);
        fam = findViewById(R.id.cpo_fam);
        fabEdit = findViewById(R.id.cpo_adopted_fab_edit);
        fabDelete = findViewById(R.id.cpo_adopted_fab_delete);

        btnAdopted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Kucing Ini Telah Diadopsi", Toast.LENGTH_SHORT).show();
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pActivity = getIntent().getStringExtra("previousActivity");
                final String catId = getIntent().getStringExtra("cat_id");
                final String ownerId = getIntent().getStringExtra("owner_id");
                final String apponstatus = catId + "_Accepted";
                databaseAdoptions = FirebaseDatabase.getInstance().getReference().child("adoptions").child("adoptionCatIdApponStatus");
                databaseAdoptions.equalTo(apponstatus).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot adoptionsnapshot : dataSnapshot.getChildren()){
                            if (dataSnapshot.exists()){
                                Adoption adoption = dataSnapshot.getValue(Adoption.class);
                                String appid = adoption.getAdoptionId();
                                Intent intentadopter = new Intent(getApplicationContext(), EditCatDataAdoptedActivity.class);
                                intentadopter.putExtra("previousActivity", pActivity);
                                intentadopter.putExtra("cat_id", catId);
                                intentadopter.putExtra("owner_id", ownerId);
                                intentadopter.putExtra("application_id", appid);
                                startActivity(intentadopter);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), EditCatDataAdoptedActivity.class);
                                intent.putExtra("previousActivity", pActivity);
                                intent.putExtra("cat_id", catId);
                                intent.putExtra("owner_id", ownerId);
                                startActivity(intent);
                                finish();
                            }
//                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CatProfileOwnerAdoptedActivity.this);
                builder.setMessage("Apakah Anda Yakin Ingin Menghapus Kucing Ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String catId = getIntent().getStringExtra("cat_id");
                                databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catId);
                                databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Cat cat = dataSnapshot.getValue(Cat.class);
                                        cat.setCatAdoptedStatus("Not Available");
                                        cat.setCatCityDeleteStatus(cat.getCatCity()+"_1");
                                        cat.setCatDeleteStatus("1");
                                        cat.setCatOwnerDeleteStatus(cat.getCatOwnerId()+"_1");
                                        databaseCats.setValue(cat);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseDelete = FirebaseDatabase.getInstance().getReference().child("adoptions");
                                databaseDelete.orderByChild("adoptionCatId").equalTo(catId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot updCatAdoptSnapshot : dataSnapshot.getChildren()){
                                            Adoption updAdoption = updCatAdoptSnapshot.getValue(Adoption.class);
                                            updAdoption.setAdoptionApplicationStatus("Not Available");
                                            updAdoption.setAdoptionCatIdApponStatus(updAdoption.getAdoptionCatId() + "_Not Available");
                                            updAdoption.setAdoptionOwnerIdApponStatus(updAdoption.getAdoptionOwnerId()+"_Not Available");
                                            updAdoption.setAdoptionDeleteStatus("1");
                                            updAdoption.setAdoptionApplicantIdDeleteStatus(updAdoption.getAdoptionApplicantId()+"_1");
                                            databaseDelete.child(updAdoption.getAdoptionId()).setValue(updAdoption);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                String pActivity = getIntent().getStringExtra("previousActivity");
                                Intent intentAdoptionList = new Intent(getApplicationContext(), UserCatListActivity.class);
                                Intent intentFindCat = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
                                Intent intentMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);

                                if (pActivity.equals("adoptionlist")) {
                                    startActivity(intentAdoptionList);
                                    finish();
                                } else if (pActivity.equals("findcat")) {
                                    startActivity(intentFindCat);
                                    finish();
                                } else {
                                    startActivity(intentMainMenu);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("Tidak", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

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
                tvAdoptStatus.setText(setadoptionstatus);
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
        if (fam.isOpened()){
            fam.close(true);
        } else {
            String pActivity = getIntent().getStringExtra("previousActivity");
            Intent intentAdoptionList = new Intent(getApplicationContext(), UserCatListActivity.class);
            Intent intentFindCat = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
            Intent intentMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);

            if (pActivity.equals("adoptionlist")) {
                startActivity(intentAdoptionList);
                finish();
            } else if (pActivity.equals("findcat")) {
                startActivity(intentFindCat);
                finish();
            } else {
                startActivity(intentMainMenu);
                finish();
            }
        }
    }
}
