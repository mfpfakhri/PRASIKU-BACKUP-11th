package clouwiko.dev.prasiku.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import clouwiko.dev.prasiku.R;

public class ApplicantAdoptionFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_adoption_form);

        String catId = getIntent().getStringExtra("cat_id");
        String ownerId = getIntent().getStringExtra("owner_id");
    }

    @Override
    public void onBackPressed() {
        final String catId = getIntent().getStringExtra("cat_id");
        final String ownerId = getIntent().getStringExtra("owner_id");
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantAdoptionFormActivity.this);
        builder.setMessage("Are You sure want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), CatProfileApplicantAvailableActivity.class);
                        intent.putExtra("previousActivity", "findcat");
                        intent.putExtra("owner_id", ownerId);
                        intent.putExtra("cat_id", catId);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
