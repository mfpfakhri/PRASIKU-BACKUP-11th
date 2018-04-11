package clouwiko.dev.prasiku.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import clouwiko.dev.prasiku.R;

public class FindCatForAdoptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_cat_for_adopt);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_findcat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Find Cat to Adopt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
