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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import clouwiko.dev.prasiku.activity.model.Adoption;

public class UserApplicationListActivity extends AppCompatActivity {
    private String TAG = "UserApplicationList";
    private RecyclerView applicationRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Adoption> applicationLists;
    private ApplicationListAdapter applicationListAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseCats, databaseAdoptions;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_application_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Application List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        applicationRecyclerView = (RecyclerView) findViewById(R.id.main_applicationlist);
        applicationRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        applicationRecyclerView.setLayoutManager(linearLayoutManager);
        applicationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        applicationRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        applicationLists = new ArrayList<>();

        applicationListAdapter = new UserApplicationListActivity.ApplicationListAdapter(applicationLists);

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
        databaseAdoptions = firebaseDatabase.getReference().child("adoptions");
        databaseAdoptions.orderByChild("adoptionApplicantId").equalTo(userUID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Adoption adoptionData = dataSnapshot.getValue(Adoption.class);
                applicationLists.add(adoptionData);
                applicationRecyclerView.setAdapter(applicationListAdapter);
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

    public class ApplicationListAdapter extends RecyclerView.Adapter<UserApplicationListActivity.ApplicationListAdapter.ApplicationListViewHolder> {
        List<Adoption> applicationListArray;

        public ApplicationListAdapter(List<Adoption> List) {
            this.applicationListArray = List;
        }

        @Override
        public UserApplicationListActivity.ApplicationListAdapter.ApplicationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_application_list_layout, parent, false);

            return new UserApplicationListActivity.ApplicationListAdapter.ApplicationListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final UserApplicationListActivity.ApplicationListAdapter.ApplicationListViewHolder holder, int position) {
            Adoption adoptionData = applicationListArray.get(position);
            final String oId = adoptionData.getAdoptionOwnerId().toString().trim();
            final String cId = adoptionData.getAdoptionCatId().toString().trim();
            final String aId = adoptionData.getAdoptionApplicantId().toString().trim();
            final String appId = adoptionData.getAdoptionId().toString().trim();

            holder.status.setText(adoptionData.getAdoptionApplicationStatus().toString().trim());
            holder.name.setText(adoptionData.getAdoptionCatName().toString().trim());
            Picasso.get().load(adoptionData.getAdoptionCatPhoto()).resize(128, 128).into(holder.photo);

            holder.layoutroot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserApplicationListReviewActivity.class);
                    intent.putExtra("application_id", appId);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return applicationListArray.size();
        }

        public class ApplicationListViewHolder extends RecyclerView.ViewHolder {
            TextView name, status;
            ImageView photo;
            LinearLayout layoutroot;

            public ApplicationListViewHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.application_catname);
                status = itemView.findViewById(R.id.application_statusvalue);
                photo = itemView.findViewById(R.id.application_catphotos);
                layoutroot = itemView.findViewById(R.id.application_root);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}