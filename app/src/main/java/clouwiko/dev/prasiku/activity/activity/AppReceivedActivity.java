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

public class AppReceivedActivity extends AppCompatActivity {
    private String TAG = "AppReceivedList";
    private RecyclerView appReceivedRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Adoption> appReceivedLists;
    private AppReceivedAdapter appReceivedAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseAdoptions;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_received);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pengajuan Diterima");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appReceivedRecyclerView = (RecyclerView) findViewById(R.id.main_appreceivedlist);
        appReceivedRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        appReceivedRecyclerView.setLayoutManager(linearLayoutManager);
        appReceivedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        appReceivedRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        appReceivedLists = new ArrayList<>();

        appReceivedAdapter = new AppReceivedAdapter(appReceivedLists);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();

        getReceivedApplicationList();
    }

    void getReceivedApplicationList() {
        //Firebase Current User UID
        String userId = auth.getCurrentUser().getUid();
        String useridapponstatus = userId+"_Received";

        //Database Reference
        databaseAdoptions = firebaseDatabase.getReference().child("adoptions");
        databaseAdoptions.orderByChild("adoptionOwnerIdApponStatus").equalTo(useridapponstatus).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Adoption userAppReceivedData = dataSnapshot.getValue(Adoption.class);
                appReceivedLists.add(userAppReceivedData);
                appReceivedRecyclerView.setAdapter(appReceivedAdapter);
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

    public class AppReceivedAdapter extends RecyclerView.Adapter<AppReceivedAdapter.AppReceivedViewHolder>{
        List<Adoption> adoptionList;

        public AppReceivedAdapter(List<Adoption> List){
            this.adoptionList = List;
        }

        @Override
        public AppReceivedAdapter.AppReceivedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_owner_appon_list_layout, parent, false);

            return new AppReceivedViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AppReceivedAdapter.AppReceivedViewHolder holder, int position) {
            Adoption receivedData = adoptionList.get(position);
            String applicantname = receivedData.getAdoptionApplicantName();
            final String catname = receivedData.getAdoptionCatName();
            final String applicationid = receivedData.getAdoptionId();
            final String catid = receivedData.getAdoptionCatId();
            String apponstatus = receivedData.getAdoptionApplicationStatus();
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

//            SpannableStringBuilder receivedBuilder = new SpannableStringBuilder();
//            SpannableString receivedSpannable = new SpannableString(apponstatus);
//            receivedSpannable.setSpan(new ForegroundColorSpan(Color.BLUE),0, receivedSpannable.length(), 0);
//            receivedBuilder.append(receivedSpannable);
//            holder.apponstatus.setText(receivedBuilder, TextView.BufferType.SPANNABLE);

//            Picasso.get().load(receivedData.getAdoptionCatPhoto()).centerCrop().resize(128,128).into(holder.photo);
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
                    Intent intent = new Intent(getApplicationContext(), AppReceivedReviewActivity.class);
                    intent.putExtra("application_id", applicationid);
                    intent.putExtra("cat_id", catid);
                    intent.putExtra("cat_name", catname);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {

            return adoptionList.size();
        }

        public class AppReceivedViewHolder extends RecyclerView.ViewHolder {
            TextView appname, catname, apponstatus;
            ImageView photo;
            LinearLayout layoutroot;

            public AppReceivedViewHolder (View itemView){
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
