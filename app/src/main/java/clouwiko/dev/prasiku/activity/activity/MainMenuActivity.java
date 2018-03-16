package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import clouwiko.dev.prasiku.R;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;
    private CardView uploadCard, findCatCard, breedsStandardsCard;
    private long backPressedTime;
    private Toast backToast;
    private TextView userIdTv;

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

        //Defining CardView
        uploadCard = (CardView) findViewById(R.id.upload_cat_data);
        findCatCard = (CardView) findViewById(R.id.find_cat_for_adopt);
        breedsStandardsCard = (CardView) findViewById(R.id.cat_breeds_standards);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        final FirebaseUser user = auth.getCurrentUser();
//        final String userUID = user.getUid();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null) {
                    //User Auth State is Changed - User is Null
                    //Launch Login Activity
                    startActivity(new Intent(MainMenuActivity.this, LandingActivity.class));
                    finish();
                } else if (user != null) {
                    String userId = user.getUid();
                }
            }
        };

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
                Intent intent = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
                startActivity(intent);
            }
        });
        breedsStandardsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CatBreedsStandardsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        progressBar.setVisibility(View.VISIBLE);
        switch (id) {
            case R.id.action_edit_password:
                startActivity(new Intent(MainMenuActivity.this, ChangePasswordActivity.class));
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.action_sign_out:
                auth.signOut();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(MainMenuActivity.this, LandingActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            auth.getCurrentUser();
            startActivity(new Intent(getApplicationContext(), UserHomeActivity.class));
        } else if (id == R.id.nav_menu) {
            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        } else if (id == R.id.nav_app_received) {
            startActivity(new Intent(getApplicationContext(), AppReceivedActivity.class));
        } else if (id == R.id.nav_app_submitted) {
            startActivity(new Intent(getApplicationContext(), AppSubmittedActivity.class));
        } else if (id == R.id.nav_tutorial) {
            startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
        } else if (id == R.id.nav_about_us) {
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
