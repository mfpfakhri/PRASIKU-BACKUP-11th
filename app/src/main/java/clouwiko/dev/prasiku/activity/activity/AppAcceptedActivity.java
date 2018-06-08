package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

public class AppAcceptedActivity extends AppCompatActivity {
    private String TAG = "AppAcceptedList";
    private RecyclerView appAcceptedRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Adoption> appAcceptedLists;
    private AppAcceptedAdapter appAcceptedAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseAdoptions;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_accepted);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pengajuan Disetujui");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appAcceptedRecyclerView = (RecyclerView) findViewById(R.id.main_appacceptedlist);
        appAcceptedRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        appAcceptedRecyclerView.setLayoutManager(linearLayoutManager);
        appAcceptedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        appAcceptedRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        appAcceptedLists = new ArrayList<>();

        appAcceptedAdapter = new AppAcceptedAdapter(appAcceptedLists);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();

        getAppAcceptedData();
    }

    void getAppAcceptedData() {
        //Firebase Current User UID
        String userId = auth.getCurrentUser().getUid();
        String useridapponstatus = userId + "_Accepted".toString().trim();

        //Database Reference
        databaseAdoptions = firebaseDatabase.getReference().child("adoptions");
        databaseAdoptions.orderByChild("adoptionOwnerIdApponStatus").equalTo(useridapponstatus).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Adoption userAppAcceptedData = dataSnapshot.getValue(Adoption.class);
                appAcceptedLists.add(userAppAcceptedData);
                appAcceptedRecyclerView.setAdapter(appAcceptedAdapter);
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

    public class AppAcceptedAdapter extends RecyclerView.Adapter<AppAcceptedAdapter.AppAcceptedViewHolder> {
        List<Adoption> adoptionList;

        public AppAcceptedAdapter(List<Adoption> List) {
            this.adoptionList = List;
        }

        @Override
        public AppAcceptedAdapter.AppAcceptedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_owner_appon_list_layout, parent, false);

            return new AppAcceptedViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AppAcceptedAdapter.AppAcceptedViewHolder holder, int position) {
            Adoption receivedData = adoptionList.get(position);
            String applicantname = receivedData.getAdoptionApplicantName().toString().trim();
            String catname = receivedData.getAdoptionCatName().toString().trim();
            String apponstatus = receivedData.getAdoptionApplicationStatus().toString().trim();
            final String applicationid = receivedData.getAdoptionId().toString().trim();
            final String catid = receivedData.getAdoptionCatId().toString().trim();
            String setstatus = null;
            switch (apponstatus){
                case "Received":
                    setstatus = "Diterima";
                    SpannableStringBuilder receivedBuilder = new SpannableStringBuilder();
                    SpannableString receivedSpannable = new SpannableString(setstatus);
                    receivedSpannable.setSpan(new ForegroundColorSpan(Color.BLUE),0, receivedSpannable.length(), 0);
                    receivedBuilder.append(receivedSpannable);
                    holder.apponstatus.setText(receivedBuilder, TextView.BufferType.SPANNABLE);
                    break;
                case "Accepted":
                    setstatus = "Disetujui";
                    SpannableStringBuilder acceptedBuilder = new SpannableStringBuilder();
                    SpannableString acceptedSpannable = new SpannableString(setstatus);
                    acceptedSpannable.setSpan(new ForegroundColorSpan(Color.GREEN),0, acceptedSpannable.length(), 0);
                    acceptedBuilder.append(acceptedSpannable);
                    holder.apponstatus.setText(acceptedBuilder, TextView.BufferType.SPANNABLE);
                    break;
                case "Rejected":
                    setstatus = "Ditolak";
                    SpannableStringBuilder rejectedBuilder = new SpannableStringBuilder();
                    SpannableString rejectedSpannable = new SpannableString(setstatus);
                    rejectedSpannable.setSpan(new ForegroundColorSpan(Color.RED),0, rejectedSpannable.length(), 0);
                    rejectedBuilder.append(rejectedSpannable);
                    holder.apponstatus.setText(rejectedBuilder, TextView.BufferType.SPANNABLE);
                    break;
            }

            holder.appname.setText(applicantname);
            holder.catname.setText(catname);

//            SpannableStringBuilder acceptedBuilder = new SpannableStringBuilder();
//            SpannableString acceptedSpannable = new SpannableString(apponstatus);
//            acceptedSpannable.setSpan(new ForegroundColorSpan(Color.GREEN),0, acceptedSpannable.length(), 0);
//            acceptedBuilder.append(acceptedSpannable);
//            holder.apponstatus.setText(acceptedBuilder, TextView.BufferType.SPANNABLE);

//            Picasso.get().load(receivedData.getAdoptionCatPhoto()).centerCrop().resize(128, 128).into(holder.photo);
            if (receivedData.getAdoptionCatPhoto().equals("")) {
                String noPhoto = "@drawable/no_image";
                int imageResource = getResources().getIdentifier(noPhoto, null, getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                holder.photo.setImageDrawable(res);
                holder.photo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                Picasso.get().load(receivedData.getAdoptionCatPhoto()).resize(256, 256).into(holder.photo);
            }

            holder.layoutroot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AppAcceptedReviewActivity.class);
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

        public class AppAcceptedViewHolder extends RecyclerView.ViewHolder {
            TextView appname, catname, apponstatus;
            ImageView photo;
            LinearLayout layoutroot;

            public AppAcceptedViewHolder(View itemView) {
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
