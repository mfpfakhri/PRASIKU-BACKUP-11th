package clouwiko.dev.prasiku.activity.activity;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.CatList;

public class AdoptionListActivity extends AppCompatActivity {
    private String TAG = "AdoptionListActivity";
    private RecyclerView adoptionRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<CatList> catLists;
    private AdoptCatAdapter adoptionAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseCat, databaseCurrentUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_adoption);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Adoption List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adoptionRecyclerView = (RecyclerView) findViewById(R.id.main_adoptionlist);
        adoptionRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adoptionRecyclerView.setLayoutManager(linearLayoutManager);
        adoptionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adoptionRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        catLists = new ArrayList<>();

        adoptionAdapter = new AdoptCatAdapter(catLists);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();

        getCatData();
    }

    void getCatData() {
        //Firebase Current User UID
        String userUID = auth.getUid();

        //Database Reference
        databaseCat = firebaseDatabase.getReference().child("cats");

        //Current User Database
        databaseCurrentUser = databaseCat.child(userUID);

        databaseCurrentUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CatList userCatData = dataSnapshot.getValue(CatList.class);
                catLists.add(userCatData);
                adoptionRecyclerView.setAdapter(adoptionAdapter);
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

    public class AdoptCatAdapter extends RecyclerView.Adapter<AdoptCatAdapter.AdoptCatViewHolder> {
        List<CatList> catListArray;

        public AdoptCatAdapter(List<CatList> List) {
            this.catListArray = List;
        }

        @Override
        public AdoptCatAdapter.AdoptCatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adoption_list_layout, parent, false);

            return new AdoptCatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AdoptCatAdapter.AdoptCatViewHolder holder, int position) {
            CatList catListData = catListArray.get(position);

            holder.name.setText(catListData.getCatName());
            holder.reason.setText(catListData.getCatReason());
            Picasso.get().load(catListData.getCatProfilePhoto()).resize(128, 128).into(holder.photo);
        }

        @Override
        public int getItemCount() {
            return catListArray.size();
        }

        public class AdoptCatViewHolder extends RecyclerView.ViewHolder {
            TextView name, reason;
            ImageView photo;


            public AdoptCatViewHolder(View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.adoptionlist_catname);
                reason = (TextView) itemView.findViewById(R.id.adoptionlist_catreason);
                photo = (ImageView) itemView.findViewById(R.id.adoptionlist_catphotos);
            }
        }
    }
}
