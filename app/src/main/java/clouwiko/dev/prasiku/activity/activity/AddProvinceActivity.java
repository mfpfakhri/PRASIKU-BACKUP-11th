package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.Province;

public class AddProvinceActivity extends AppCompatActivity {

    private EditText inputProvince;
    private Button btnAddProvince, btnClearText;

    DatabaseReference databaseProvinces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_province);

        databaseProvinces = FirebaseDatabase.getInstance().getReference("province");

        inputProvince = (EditText)findViewById(R.id.add_province_edit_text);
        btnAddProvince = (Button)findViewById(R.id.action_add_province_button);
        btnClearText = (Button)findViewById(R.id.action_clear_text_button);

        btnClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputProvince.setText("");
            }
        });

        btnAddProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProvince();
            }
        });
    }

    private void addProvince(){
        String name = inputProvince.getText().toString().trim();

        if (!TextUtils.isEmpty(name)){

            String id = databaseProvinces.push().getKey();

            Province province = new Province(id, name);

            databaseProvinces.child(id).setValue(province);

            Toast.makeText(this, "Province Added", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Enter Province Name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddProvinceActivity.this, LandingActivity.class));
        finish();
    }
}
