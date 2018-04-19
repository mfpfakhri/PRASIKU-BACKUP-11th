package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Cat;
import fr.ganfra.materialspinner.MaterialSpinner;

public class FindCatForAdoptActivity extends AppCompatActivity {
    private String TAG = "FindCat";
    private MaterialSpinner spinnerProvinces, spinnerCities;
    private Button btnSearchCat;
    private FirebaseAuth auth;
    private DatabaseReference databaseProvinces, databaseCities, databaseCats;
    private RecyclerView catResultsRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Cat> catLists;
    private FindCatAdapter findCatAdapter;

    //TODO: Initial cities Adapter
    ArrayAdapter<String> citiesAdapter;
    List<String> cities;
    List<String> provincesKey;
    //--

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_cat_for_adopt);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_findcat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Find Cat to Adopt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize RecyclerView
        catResultsRecyclerView = findViewById(R.id.main_findcat_result);
        catResultsRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        catResultsRecyclerView.setLayoutManager(linearLayoutManager);
        catResultsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        catResultsRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        //Create Object
        spinnerProvinces = findViewById(R.id.findcatspinner_province);
        spinnerCities = findViewById(R.id.findcatspinner_city);
        btnSearchCat = findViewById(R.id.findcat_button);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Firebase Current User Logged In ID
        String userID = auth.getCurrentUser().getUid();

        //Initialize Spinner for Cities
        //TODO:RWP Initial Spinner for cities
        cities = new ArrayList<String>();
//        final MaterialSpinner citiesSpinner = (MaterialSpinner) findViewById(R.id.findcatspinner_city);
        citiesAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, cities);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(citiesAdapter);
        provincesKey = new ArrayList<>();
        //

        databaseProvinces = FirebaseDatabase.getInstance().getReference().child("provinces");
        //TODO:RWP change addEventListener to addListenerForSingleValueEvent
        databaseProvinces.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> provinces = new ArrayList<String>();
                for (DataSnapshot provinceSnapshot : dataSnapshot.getChildren()) {
                    final String provinceName = provinceSnapshot.child("provinceName").getValue(String.class);
                    provinces.add(provinceName);
                    //TODO:RWP add key to array
                    String key = provinceSnapshot.getKey();
                    provincesKey.add(key);
                    //--
                    spinnerProvinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int spinnerProvincesPosition = spinnerProvinces.getSelectedItemPosition();
                            //TODO:RWP remove all cities and clear selection city
                            cities.clear();
                            spinnerCities.setSelection(0);
                            //--
                            if (spinnerProvincesPosition != 0) {
                                //TODO:RWP get key by its position
                                String provinceKey = provincesKey.get(position);
                                //
                                databaseCities = FirebaseDatabase.getInstance().getReference().child("cities");
                                //TODO:RWP change orderBy and equalTo with child and change addEventListener to addListenerForSingleValueEvent
                                databaseCities.child(provinceKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot cityPSnapshot : dataSnapshot.getChildren()) {
                                            //TODO:RWP delete multiple loop for cities
                                            String cityName = cityPSnapshot.child("cityName").getValue(String.class);
                                            cities.add(cityName);
                                        }
                                        //TODO:RWP notify all new cities
                                        citiesAdapter.notifyDataSetChanged();
                                        //--
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
//                MaterialSpinner provincesSpinner = (MaterialSpinner) findViewById(R.id.findcatspinner_province);
                ArrayAdapter<String> provincesAdapter = new ArrayAdapter<String>(FindCatForAdoptActivity.this, android.R.layout.simple_spinner_item, provinces);
                provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProvinces.setAdapter(provincesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        catLists = new ArrayList<>();

        findCatAdapter = new FindCatForAdoptActivity.FindCatAdapter(catLists);

        btnSearchCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAdoptList();
            }
        });
    }

    void getAdoptList() {
        //Firebase Current User UID
        String userUID = auth.getUid();

        String keyCityValue = spinnerCities.getSelectedItem().toString().trim();

        //Database Reference
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats");

        databaseCats.orderByChild("catCity").equalTo(keyCityValue).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Cat userCatData = dataSnapshot.getValue(Cat.class);
                catLists.add(userCatData);
                catResultsRecyclerView.setAdapter(findCatAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class FindCatAdapter extends RecyclerView.Adapter<FindCatForAdoptActivity.FindCatAdapter.AdoptCatViewHolder> {
        List<Cat> catListArray;

        public FindCatAdapter(List<Cat> List) {
            this.catListArray = List;
        }

        @Override
        public FindCatForAdoptActivity.FindCatAdapter.AdoptCatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adoption_list_layout, parent, false);

            return new FindCatForAdoptActivity.FindCatAdapter.AdoptCatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FindCatForAdoptActivity.FindCatAdapter.AdoptCatViewHolder holder, int position) {
            Cat catData = catListArray.get(position);
            final String oId = catData.getCatOwnerId().toString().trim();
            final String cId = catData.getCatId().toString().trim();
            final String cStat = catData.getCatAdoptedStatus().toString().trim();

            holder.name.setText(catData.getCatName());
            holder.reason.setText(catData.getCatReason());
            holder.gender.setText(catData.getCatGender());
            Picasso.get().load(catData.getCatProfilePhoto()).resize(128, 128).into(holder.photo);

            holder.layoutroot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Firebase Current User Logged In ID
                    String userID = auth.getCurrentUser().getUid().toString().trim();

                    if (userID.equals(oId)){
                        if (cStat.equals("Available")){
                            Intent intent = new Intent(getApplicationContext(), CatProfileOwnerAvailableActivity.class);
                            intent.putExtra("previousActivity", "findcat");
                            intent.putExtra("owner_id", oId);
                            intent.putExtra("cat_id", cId);
                            startActivity(intent);
                            finish();
                        } else if (cStat.equals("Adopted")){
                            Intent intent = new Intent(getApplicationContext(), CatProfileOwnerAdoptedActivity.class);
                            intent.putExtra("previousActivity", "findcat");
                            intent.putExtra("owner_id", oId);
                            intent.putExtra("cat_id", cId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "You have no right to choose this option", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (cStat.equals("Available")){
                            Intent intent = new Intent(getApplicationContext(), CatProfileApplicantAvailableActivity.class);
                            intent.putExtra("previousActivity", "findcat");
                            intent.putExtra("owner_id", oId);
                            intent.putExtra("cat_id", cId);
                            startActivity(intent);
                            finish();
                        } else if (cStat.equals("Adopted")){
                            Intent intent = new Intent(getApplicationContext(), CatProfileApplicantAdoptedActivity.class);
                            intent.putExtra("previousActivity", "findcat");
                            intent.putExtra("owner_id", oId);
                            intent.putExtra("cat_id", cId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "You have no right to choose this option", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return catListArray.size();
        }

        public class AdoptCatViewHolder extends RecyclerView.ViewHolder {
            TextView name, reason, gender, ownerid, catid;
            ImageView photo;
            LinearLayout layoutroot;

            public AdoptCatViewHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.adoptionlist_catname);
                reason = itemView.findViewById(R.id.adoptionlist_catreason);
                gender = itemView.findViewById(R.id.userhome_catgender);
                photo = itemView.findViewById(R.id.adoptionlist_catphotos);
                layoutroot = itemView.findViewById(R.id.adoptionlist_root);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }
}
