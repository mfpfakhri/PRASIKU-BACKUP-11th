package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.City;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AddCityActivity extends AppCompatActivity {

    private EditText inputCity;
    private Button btnAddCity, btnClearText;
    private MaterialSpinner spinnerProvinces;

    private DatabaseReference databaseCities, databaseProvinces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        inputCity = (EditText) findViewById(R.id.add_city_edit_text);
        btnAddCity = (Button) findViewById(R.id.action_add_city_button);
        btnClearText = (Button) findViewById(R.id.action_clear_text_button);
        spinnerProvinces = (MaterialSpinner) findViewById(R.id.provinceSpinner);

        databaseProvinces = FirebaseDatabase.getInstance().getReference().child("provinces");
        databaseProvinces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> provinces = new ArrayList<String>();

                for (DataSnapshot provinceSnapshot : dataSnapshot.getChildren()) {
                    String provinceName = provinceSnapshot.child("provinceName").getValue(String.class);
                    provinces.add(provinceName);
                }

                MaterialSpinner provincesSpinner = (MaterialSpinner) findViewById(R.id.provinceSpinner);
                ArrayAdapter<String> provincesAdapter = new ArrayAdapter<String>(AddCityActivity.this, android.R.layout.simple_spinner_item, provinces);
                provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                provincesSpinner.setAdapter(provincesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputCity.setText("");
            }
        });

        btnAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCity();
            }
        });
    }

    private void addCity() {
        String name = inputCity.getText().toString().trim();
        String province = spinnerProvinces.getSelectedItem().toString().trim();
        databaseCities = FirebaseDatabase.getInstance().getReference("cities");
        if (!TextUtils.isEmpty(name)) {
            String id = databaseCities.push().getKey();

            City city = new City(id, name, province);

            databaseCities.child(id).setValue(city);

            Toast.makeText(this, "City Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Enter City Name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddCityActivity.this, LandingActivity.class));
        finish();
    }
}
