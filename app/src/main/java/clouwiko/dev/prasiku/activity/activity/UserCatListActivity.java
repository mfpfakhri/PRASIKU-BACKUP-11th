package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

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
import clouwiko.dev.prasiku.activity.model.Cat;

public class UserCatListActivity extends AppCompatActivity {
    private String TAG = "UserAdoptionList";
    private RecyclerView adoptionRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Cat> catLists;
    private AdoptCatAdapter adoptionAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseCats;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_adoption_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        String userId = auth.getCurrentUser().getUid();
        String ownerdeletestat = userId + "_0";

        //Database Reference
        databaseCats = firebaseDatabase.getReference().child("cats");
        databaseCats.orderByChild("catOwnerDeleteStatus").equalTo(ownerdeletestat).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Cat userCatData = dataSnapshot.getValue(Cat.class);
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
        List<Cat> catListArray;

        public AdoptCatAdapter(List<Cat> List) {
            this.catListArray = List;
        }

        @Override
        public AdoptCatAdapter.AdoptCatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_adoption_list_layout, parent, false);

            return new AdoptCatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final AdoptCatAdapter.AdoptCatViewHolder holder, int position) {
            Cat catData = catListArray.get(position);
            final String oId = catData.getCatOwnerId().toString().trim();
            final String cId = catData.getCatId().toString().trim();
            final String cStat = catData.getCatAdoptedStatus().toString().trim();
            String cPhoto = catData.getCatProfilePhoto().toString().trim();

            holder.name.setText(catData.getCatName());
            holder.reason.setText(catData.getCatReason());
            holder.gender.setText(catData.getCatGender());
            if (catData.getCatProfilePhoto().equals("")){
                String noPhoto = "@drawable/no_image";
                int imageResource = getResources().getIdentifier(noPhoto, null, getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                holder.photo.setImageDrawable(res);
            } else {
                Picasso.get().load(cPhoto).resize(64, 64).into(holder.photo);
            }

            holder.layoutroot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cStat.equals("Available")) {
                        Intent intent = new Intent(getApplicationContext(), CatProfileOwnerAvailableActivity.class);
                        intent.putExtra("previousActivity", "adoptionlist");
                        intent.putExtra("owner_id", oId);
                        intent.putExtra("cat_id", cId);
                        startActivity(intent);
                        finish();
                    } else if (cStat.equals("Adopted")) {
                        Intent intent = new Intent(getApplicationContext(), CatProfileOwnerAdoptedActivity.class);
                        intent.putExtra("previousActivity", "adoptionlist");
                        intent.putExtra("owner_id", oId);
                        intent.putExtra("cat_id", cId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "You have no right to choose this option", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return catListArray.size();
        }

        public class AdoptCatViewHolder extends RecyclerView.ViewHolder {
            TextView name, reason, gender;
            ImageView photo;
            LinearLayout layoutroot;

            public AdoptCatViewHolder(View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.adoptionlist_catname);
                reason = (TextView) itemView.findViewById(R.id.adoptionlist_catreason);
                gender = (TextView) itemView.findViewById(R.id.userhome_catgender);
                photo = (ImageView) itemView.findViewById(R.id.adoptionlist_catphotos);
                layoutroot = (LinearLayout) itemView.findViewById(R.id.adoptionlist_root);
            }
        }
    }
}
