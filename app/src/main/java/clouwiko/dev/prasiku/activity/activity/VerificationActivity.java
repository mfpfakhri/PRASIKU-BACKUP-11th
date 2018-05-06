package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import clouwiko.dev.prasiku.R;

public class VerificationActivity extends AppCompatActivity {
    TextView instruction;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button btnVerif, btnEmail, btnLanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        instruction = findViewById(R.id.verify_order);
        Typeface instructionstyle = Typeface.createFromAsset(getAssets(), "fonts/segoeuisl.ttf");
        instruction.setTypeface(instructionstyle);

        btnVerif = findViewById(R.id.verify_send_button);
        btnEmail = findViewById(R.id.verify_email_button);
        btnLanding = findViewById(R.id.verify_landing_button);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        final FirebaseUser user = auth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(VerificationActivity.this, MainMenuActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(VerificationActivity.this, VerificationActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(VerificationActivity.this, LandingActivity.class));
                    finish();
                }
            }
        };

        btnVerif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(VerificationActivity.this, "Verification email sent, check Your email to verify", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
            }
        });

        btnLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(VerificationActivity.this, LandingActivity.class));
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
