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

public class LandingActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btnSignIn, btnSignUp;
    private Button btnInputProvinces, btnInputCities;
    TextView textSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Check if Already Session
        if (auth.getCurrentUser() != null){
            startActivity(new Intent(LandingActivity.this, HomeActivity.class));
        }

        textSlogan = (TextView)findViewById(R.id.textSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        textSlogan.setTypeface(face);

        btnSignIn = (Button)findViewById(R.id.main_sign_in_button);
        btnSignUp = (Button)findViewById(R.id.main_sign_up_button);

        btnInputProvinces = (Button)findViewById(R.id.main_province_intent_button);
        btnInputCities = (Button)findViewById(R.id.main_city_intent_button);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, SignInActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, SignUpActivity.class));
                finish();
            }
        });

        btnInputProvinces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, AddProvinceActivity.class));
                finish();
            }
        });

        btnInputCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, AddCityActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
