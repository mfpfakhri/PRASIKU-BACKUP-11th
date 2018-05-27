package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import clouwiko.dev.prasiku.R;

public class UserBannedActivity extends AppCompatActivity {
    TextView instruction;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button btnLanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_banned);

        instruction = findViewById(R.id.banned_information);
        Typeface instructionstyle = Typeface.createFromAsset(getAssets(), "fonts/segoeuisl.ttf");
        instruction.setTypeface(instructionstyle);
        btnLanding = findViewById(R.id.banned_landing_button);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        final FirebaseUser user = auth.getCurrentUser();

        btnLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LandingActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
