package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import clouwiko.dev.prasiku.R;

public class CatProfileApplicantAvailableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_profile_available);

        String pActivity = getIntent().getStringExtra("previousActivity");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        String ownerId = getIntent().getStringExtra("owner_id");
        String catId = getIntent().getStringExtra("cat_id");

        TextView oId = findViewById(R.id.catprofile_ownervalue);
        TextView cId = findViewById(R.id.catprofile_cityvalue);

        oId.setText(ownerId);
        cId.setText(catId);
    }

    @Override
    public void onBackPressed() {
        String pActivity = getIntent().getStringExtra("previousActivity");
        Intent intentFindCat = new Intent(getApplicationContext(), FindCatForAdoptActivity.class);
        Intent intentMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);

        if (pActivity.equals("findcat")){
            startActivity(intentFindCat);
            finish();
        } else {
            startActivity(intentMainMenu);
            finish();
        }
    }
}
