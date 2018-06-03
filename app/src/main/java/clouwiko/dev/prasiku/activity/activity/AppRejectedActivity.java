package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

public class AppRejectedActivity extends AppCompatActivity {
    private String TAG = "AppRejectedList";
    private RecyclerView appRejectedRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Adoption> appRejectedLists;
    private AppRejectedAdapter appRejectedAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseAdoptions;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_rejected);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Application Rejected");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appRejectedRecyclerView = (RecyclerView) findViewById(R.id.main_apprejectedlist);
        appRejectedRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        appRejectedRecyclerView.setLayoutManager(linearLayoutManager);
        appRejectedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        appRejectedRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        appRejectedLists = new ArrayList<>();

        appRejectedAdapter = new AppRejectedAdapter(appRejectedLists);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();

        getAppRejectedData();
    }

    void getAppRejectedData() {
        //Firebase Current User UID
        String userId = auth.getCurrentUser().getUid();
        String useridapponstatus = userId+"_Rejected";

        //Database Reference
        databaseAdoptions = firebaseDatabase.getReference().child("adoptions");
        databaseAdoptions.orderByChild("adoptionOwnerIdApponStatus").equalTo(useridapponstatus).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Adoption userAppReceivedData = dataSnapshot.getValue(Adoption.class);
                appRejectedLists.add(userAppReceivedData);
                appRejectedRecyclerView.setAdapter(appRejectedAdapter);
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

    public class AppRejectedAdapter extends RecyclerView.Adapter<AppRejectedAdapter.AppRejectedViewHolder>{
        List<Adoption> adoptionList;

        public AppRejectedAdapter(List<Adoption> List){
            this.adoptionList = List;
        }


        @Override
        public AppRejectedAdapter.AppRejectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_app_status_layout, parent, false);

            return new AppRejectedViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AppRejectedAdapter.AppRejectedViewHolder holder, int position) {
            Adoption receivedData = adoptionList.get(position);
            String applicantname = receivedData.getAdoptionApplicantName().toString().trim();
            String catname = receivedData.getAdoptionCatName().toString().trim();
            String apponstatus = receivedData.getAdoptionApplicationStatus().toString().trim();
            final String applicationid = receivedData.getAdoptionId().toString().trim();
            final String catid = receivedData.getAdoptionCatId().toString().trim();

            holder.appname.setText(applicantname);
            holder.catname.setText(catname);

            SpannableStringBuilder rejectedBuilder = new SpannableStringBuilder();
            SpannableString rejectedSpannable = new SpannableString(apponstatus);
            rejectedSpannable.setSpan(new ForegroundColorSpan(Color.RED),0, rejectedSpannable.length(), 0);
            rejectedBuilder.append(rejectedSpannable);
            holder.apponstatus.setText(rejectedBuilder, TextView.BufferType.SPANNABLE);

            Picasso.get().load(receivedData.getAdoptionCatPhoto()).centerCrop().resize(128,128).into(holder.photo);

            holder.layoutroot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AppRejectedReviewActivity.class);
                    intent.putExtra("application_id", applicationid);
                    intent.putExtra("cat_id", catid);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return adoptionList.size();
        }

        public class AppRejectedViewHolder extends RecyclerView.ViewHolder {
            TextView appname, catname, apponstatus;
            ImageView photo;
            LinearLayout layoutroot;

            public AppRejectedViewHolder (View itemView){
                super(itemView);

                appname = (TextView) itemView.findViewById(R.id.application_status_appname);
                catname = (TextView) itemView.findViewById(R.id.application_status_catname);
                apponstatus = (TextView) itemView.findViewById(R.id.application_status_stat);
                photo = (ImageView) itemView.findViewById(R.id.application_status_catphotos);
                layoutroot = (LinearLayout) itemView.findViewById(R.id.application_status_root);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }
}
