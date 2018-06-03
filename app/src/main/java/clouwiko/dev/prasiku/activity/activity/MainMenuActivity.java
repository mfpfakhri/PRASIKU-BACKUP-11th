package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.other.CircleTransform;
import clouwiko.dev.prasiku.activity.other.RoundedCornersTransform;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;
    private CardView uploadCard, findCatCard;
    private long backPressedTime;
    private Toast backToast;
    private ImageView userPhoto;
    private TextView userName, userEmail;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        //Defining Object
        uploadCard = (CardView) findViewById(R.id.upload_cat_data);
        findCatCard = (CardView) findViewById(R.id.find_cat_for_adopt);
        userPhoto = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.userProfilePhotoND);
        userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userFNameND);
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmailND);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        final FirebaseUser user = auth.getCurrentUser();
        final String userUID = user.getUid();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    if (user.isEmailVerified()) {

                    } else {
                        startActivity(new Intent(MainMenuActivity.this, VerificationActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(MainMenuActivity.this, LandingActivity.class));
                    finish();
                }
            }
        };

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getCurrentUser();
                String userId = auth.getCurrentUser().getUid();
                Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getCurrentUser();
                String userId = auth.getCurrentUser().getUid();
                Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        userEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getCurrentUser();
                String userId = auth.getCurrentUser().getUid();
                Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userNameStr = dataSnapshot.child("userFname").getValue(String.class);
                String userEmailStr = dataSnapshot.child("userEmail").getValue(String.class);
                String userPhotoUri = dataSnapshot.child("userProfilePhoto").getValue(String.class);
                userName.setText(userNameStr);
                userEmail.setText(userEmailStr);
                if (userPhotoUri.equals("")) {
//                    String noPhoto = "@drawable/no_image";
//                    int imageResource = getResources().getIdentifier(noPhoto, null, getPackageName());
                    Drawable nophoto = getResources().getDrawable(R.drawable.no_image);
                    Bitmap imageResource = ((BitmapDrawable) nophoto).getBitmap();
                    Drawable res = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(imageResource, 140, 140, true));
                    userPhoto.setImageDrawable(res);
                } else {
                    Picasso.get().load(userPhotoUri).transform(new CircleTransform()).centerInside().resize(256, 256).into(userPhoto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //CardView Click Listener
        uploadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadCatDataActivity.class);
                startActivity(intent);
            }
        });
        findCatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
                databaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String applicantname = dataSnapshot.child("userFname").getValue(String.class);
                        String applicantphoto = dataSnapshot.child("userProfilePhoto").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
                        intent.putExtra("applicant_name", applicantname);
                        intent.putExtra("applicant_photo", applicantphoto);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        String userId = auth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        switch (id) {
            case R.id.action_edit_profile:
                Intent intentEditUser = new Intent(getApplicationContext(), EditUserProfileActivity.class);
                intentEditUser.putExtra("userId", userId);
                startActivity(intentEditUser);
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.action_edit_password:
                Intent intentChangePassword = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                intentChangePassword.putExtra("userId", userId);
                startActivity(intentChangePassword);
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.action_sign_out:
                auth.signOut();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(MainMenuActivity.this, LandingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        } else if (id == R.id.nav_catlist) {
            startActivity(new Intent(getApplicationContext(), UserCatListActivity.class));
        } else if (id == R.id.nav_my_app_list) {
            startActivity(new Intent(getApplicationContext(), UserApplicationListActivity.class));
        } else if (id == R.id.nav_app_received) {
            startActivity(new Intent(getApplicationContext(), AppReceivedActivity.class));
        } else if (id == R.id.nav_app_submitted) {
            startActivity(new Intent(getApplicationContext(), AppAcceptedActivity.class));
        } else if (id == R.id.nav_app_rejected) {
            startActivity(new Intent(getApplicationContext(), AppRejectedActivity.class));
        } else if (id == R.id.nav_tutorial) {
            startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
        } else if (id == R.id.nav_about_us) {
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
//            backToast.cancel();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press Back Again to Exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
