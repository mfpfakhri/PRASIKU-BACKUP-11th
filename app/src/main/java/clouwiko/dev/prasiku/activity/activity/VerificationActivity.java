package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import clouwiko.dev.prasiku.R;

public class VerificationActivity extends AppCompatActivity {
    TextView instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        instruction = findViewById(R.id.verify_order);
        Typeface instructionstyle = Typeface.createFromAsset(getAssets(), "fonts/segoeuisl.ttf");
        instruction.setTypeface(instructionstyle);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VerificationActivity.this, LandingActivity.class));
    }
}
