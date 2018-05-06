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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import clouwiko.dev.prasiku.R;

public class LandingActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button btnSignIn, btnSignUp;
//    private Button btnInputProvinces, btnInputCities;
    TextView textSlogan;
//    private long backPressedTime;
//    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(LandingActivity.this, MainMenuActivity.class));
                        finish();
                    } else {
                        auth.signOut();
                        startActivity(new Intent(LandingActivity.this, VerificationActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(LandingActivity.this, LandingActivity.class));
                    finish();
                }
            }
        };

        textSlogan = findViewById(R.id.textSlogan);
        Typeface faceslogan = Typeface.createFromAsset(getAssets(),"fonts/segoeuisl.ttf");
        textSlogan.setTypeface(faceslogan);

        btnSignIn = findViewById(R.id.main_sign_in_button);
        Typeface facesignin = Typeface.createFromAsset(getAssets(),"fonts/segoeuil.ttf");
        btnSignIn.setTypeface(faceslogan);

        btnSignUp = findViewById(R.id.main_sign_up_button);
        Typeface facesignup = Typeface.createFromAsset(getAssets(),"fonts/segoeuil.ttf");
        btnSignUp.setTypeface(faceslogan);

//        btnInputProvinces = (Button)findViewById(R.id.main_province_intent_button);
//        btnInputCities = (Button)findViewById(R.id.main_city_intent_button);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

//        btnInputProvinces.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LandingActivity.this, AddProvinceActivity.class));
//                finish();
//            }
//        });
//
//        btnInputCities.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LandingActivity.this, AddCityActivity.class));
//                finish();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        //Get Firebase auth instance
//        auth = FirebaseAuth.getInstance();
//
//        //Get Current User
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user == null) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
    }
}
