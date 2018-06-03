package clouwiko.dev.prasiku.activity.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import clouwiko.dev.prasiku.R;

public class NoResultFoundActivity extends AppCompatActivity {
    private ImageView ivNoResult;
    private TextView tvNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_result_found);

        ivNoResult = findViewById(R.id.noresult_image);
        tvNoResult = findViewById(R.id.noresult_text);
        Typeface noresult = Typeface.createFromAsset(getAssets(), "fonts/segoeuisl.ttf");
        tvNoResult.setTypeface(noresult);
    }
}
