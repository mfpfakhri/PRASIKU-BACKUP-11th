package clouwiko.dev.prasiku.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Adoption;
import clouwiko.dev.prasiku.activity.model.Cat;

public class CatProfileOwnerAvailableActivity extends AppCompatActivity {

    private String TAG = "CatProfileOwnerAdopted";
    private ImageView imCatPhoto;
    private TextView tvCatName, tvOwner, tvCity, tvGender, tvDesc, tvDob, tvMed, tvVacc, tvSpNeu, tvReason, tvAdoptStatus;
    private Button btnAdopted;
    private FloatingActionMenu fam;
    private FloatingActionButton fabEdit, fabDelete;
    private FirebaseAuth auth;
    private DatabaseReference databaseCats, databaseUsers, databaseDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_profile_owner_available);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        imCatPhoto = findViewById(R.id.cpo_available_photovalue);
        tvCatName = findViewById(R.id.cpo_available_catnamevalue);
        tvOwner = findViewById(R.id.cpo_available_ownervalue);
        tvCity = findViewById(R.id.cpo_available_cityvalue);
        tvGender = findViewById(R.id.cpo_available_gendervalue);
        tvDesc = findViewById(R.id.cpo_available_descvalue);
        tvDob = findViewById(R.id.cpo_available_dobvalue);
        tvMed = findViewById(R.id.cpo_available_medvalue);
        tvVacc = findViewById(R.id.cpo_available_vaccinevalue);
        tvSpNeu = findViewById(R.id.cpo_available_spayneutervalue);
        tvReason = findViewById(R.id.cpo_available_reasonvalue);
        tvAdoptStatus = findViewById(R.id.cpo_available_adoptstatusvalue);
        btnAdopted = findViewById(R.id.cpo_available_button);
        fam = findViewById(R.id.cpo_fam);
        fabEdit = findViewById(R.id.cpo_available_fab_edit);
        fabDelete = findViewById(R.id.cpo_available_fab_delete);

        btnAdopted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This is your own cat", Toast.LENGTH_SHORT).show();
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String catId = getIntent().getStringExtra("cat_id");
                String ownerId = getIntent().getStringExtra("owner_id");
                String pActivity = getIntent().getStringExtra("previousActivity");
                Intent intent = new Intent(getApplicationContext(), EditCatDataAvailableActivity.class);
                intent.putExtra("cat_id", catId);
                intent.putExtra("owner_id", ownerId);
                intent.putExtra("previousActivity", pActivity);
                startActivity(intent);
                finish();
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CatProfileOwnerAvailableActivity.this);
                builder.setMessage("Are You sure want to delete this cat?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String catId = getIntent().getStringExtra("cat_id");
//                                String catapponreceived = catId + "_Received";
//                                String catapponaccepted = catId + "_Accepted";
//                                String catapponrejected = catId + "_Rejected";
                                databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catId);
                                databaseCats.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Cat cat = dataSnapshot.getValue(Cat.class);
                                        cat.setCatAdoptedStatus("Not Available");
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
//                                            updAdoption.setAdoptionCatIdApponStatus(updAdoption.getAdoptionCatId() + "_Not Available");
                                            updAdoption.setAdoptionOwnerIdApponStatus(updAdoption.getAdoptionOwnerId()+"_Not Available");
                                            updAdoption.setAdoptionDeleteStatus("1");
//                                            updAdoption.setAdoptionOwnerDeleteStatus(updAdoption.getAdoptionOwnerId()+"_1");
                                            databaseDelete.child(updAdoption.getAdoptionId()).setValue(updAdoption);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
//                                databaseDelete.orderByChild("adoptionCatIdApponStatus").equalTo(catapponreceived).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot updRejectAppSnapshot : dataSnapshot.getChildren()) {
//                                            Adoption adoptionreceived = updRejectAppSnapshot.getValue(Adoption.class);
//                                            adoptionreceived.setAdoptionApplicationStatus("Not Available");
//                                            adoptionreceived.setAdoptionCatIdApponStatus(adoptionreceived.getAdoptionCatId() + "_Not Available");
//                                            adoptionreceived.setAdoptionOwnerIdApponStatus(adoptionreceived.getAdoptionOwnerId()+"_Not Available");
//                                            adoptionreceived.setAdoptionDeleteStatus("1");
//                                            adoptionreceived.setAdoptionOwnerDeleteStatus(adoptionreceived.getAdoptionOwnerId()+"_1");
//                                            databaseDelete.child(adoptionreceived.getAdoptionId()).setValue(adoptionreceived);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//                                databaseDelete.orderByChild("adoptionCatIdApponStatus").equalTo(catapponaccepted).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot updRejectAppSnapshot : dataSnapshot.getChildren()) {
//                                            Adoption adoptionaccepted = updRejectAppSnapshot.getValue(Adoption.class);
//                                            adoptionaccepted.setAdoptionApplicationStatus("Not Available");
//                                            adoptionaccepted.setAdoptionCatIdApponStatus(adoptionaccepted.getAdoptionCatId() + "_Not Available");
//                                            adoptionaccepted.setAdoptionOwnerIdApponStatus(adoptionaccepted.getAdoptionOwnerId()+"_Not Available");
//                                            adoptionaccepted.setAdoptionDeleteStatus("1");
//                                            adoptionaccepted.setAdoptionOwnerDeleteStatus(adoptionaccepted.getAdoptionOwnerId()+"_1");
//                                            databaseDelete.child(adoptionaccepted.getAdoptionId()).setValue(adoptionaccepted);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//                                databaseDelete.orderByChild("adoptionCatIdApponStatus").equalTo(catapponrejected).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot updRejectAppSnapshot : dataSnapshot.getChildren()) {
//                                            Adoption adoptionrejected = updRejectAppSnapshot.getValue(Adoption.class);
//                                            adoptionrejected.setAdoptionApplicationStatus("Not Available");
//                                            adoptionrejected.setAdoptionCatIdApponStatus(adoptionrejected.getAdoptionCatId() + "_Not Available");
//                                            adoptionrejected.setAdoptionOwnerIdApponStatus(adoptionrejected.getAdoptionOwnerId()+"_Not Available");
//                                            adoptionrejected.setAdoptionDeleteStatus("1");
//                                            adoptionrejected.setAdoptionOwnerDeleteStatus(adoptionrejected.getAdoptionOwnerId()+"_1");
//                                            databaseDelete.child(adoptionrejected.getAdoptionId()).setValue(adoptionrejected);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
                                String pActivity = getIntent().getStringExtra("previousActivity");
                                Intent intentAdoptionList = new Intent(getApplicationContext(), UserAdoptionListActivity.class);
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
                        .setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        getCatDataOwnAvailable();
    }

    private void getCatDataOwnAvailable() {
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
                String adoptionstatus = dataSnapshot.child("catAdoptedStatus").getValue(String.class);

                tvCatName.setText(catname);
                tvCity.setText(city);
                tvGender.setText(gender);
                tvDesc.setText(desc);
                tvDob.setText(dob);
                tvMed.setText(mednote);
                tvVacc.setText(vaccine);
                tvSpNeu.setText(spayneuter);
                tvReason.setText(reason);
                tvAdoptStatus.setText(adoptionstatus);
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

    @Override
    public void onBackPressed() {
        String pActivity = getIntent().getStringExtra("previousActivity");
        Intent intentAdoptionList = new Intent(getApplicationContext(), UserAdoptionListActivity.class);
        Intent intentFindCat = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
        Intent intentMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);

        if (pActivity.equals("adoptionlist")){
            startActivity(intentAdoptionList);
            finish();
        } else if (pActivity.equals("findcat")){
            startActivity(intentFindCat);
            finish();
        } else {
            startActivity(intentMainMenu);
            finish();
        }
    }
}
