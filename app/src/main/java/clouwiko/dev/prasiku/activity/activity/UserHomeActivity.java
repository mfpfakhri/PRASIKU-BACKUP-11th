package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.other.PicassoTransform;

public class UserHomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private UserHomePagerAdapter userHomePagerAdapter;
    private ViewPager userHomeViewPager;
    private ImageView userPhotoHome;
    private TextView userNameHome, userCityHome;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        tabLayout = (TabLayout)findViewById(R.id.userhome_tablayout);
        appBarLayout = (AppBarLayout)findViewById(R.id.userhome_appbar);
        userHomeViewPager = (ViewPager)findViewById(R.id.userhome_viewpager);

        userHomePagerAdapter = new UserHomePagerAdapter(getSupportFragmentManager());
        userHomePagerAdapter.addFragment(new UserHomeCatFragment(), "Cats");
        userHomePagerAdapter.addFragment(new UserHomeProfileFragment(), "Profile");

        userHomeViewPager.setAdapter(userHomePagerAdapter);
        tabLayout.setupWithViewPager(userHomeViewPager);

        userPhotoHome = (ImageView)findViewById(R.id.userPhoto_home);
        userNameHome = (TextView)findViewById(R.id.userName_home);
        userCityHome = (TextView)findViewById(R.id.userCity_home);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        final FirebaseUser user = auth.getCurrentUser();
        final String userUID = user.getUid();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null) {
                    //User Auth State is Changed - User is Null
                    //Launch Login Activity
                    startActivity(new Intent(UserHomeActivity.this, LandingActivity.class));
                    finish();
                }
            }
        };

        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userNameStr = dataSnapshot.child("userFname").getValue(String.class);
                String userCityStr = dataSnapshot.child("userCity").getValue(String.class);
                String userPhotoUri = dataSnapshot.child("userProfilePhoto").getValue(String.class);
                userNameHome.setText(userNameStr);
                userCityHome.setText(userCityStr);
                Picasso.get().load(userPhotoUri).transform(new PicassoTransform()).resize(192,192).into(userPhotoHome);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
