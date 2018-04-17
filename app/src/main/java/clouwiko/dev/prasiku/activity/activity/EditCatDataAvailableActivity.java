package clouwiko.dev.prasiku.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.other.RoundedCornersTransform;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditCatDataAvailableActivity extends AppCompatActivity {

    private EditText inputCatName, inputCatDob, inputCatDesc, inputCatMedNote;
    private MaterialSpinner spinnerCatGender, spinnerCatReasonOpenAdoption;
    private RadioGroup radioGroupVaccine, radioGroupSpayNeuter;
    private RadioButton radioButtonVacc, radioButtonSpayNeuter;
    private ImageView catPhotoIv;

    private DatabaseReference databaseCats;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cat_data_available);

        Toolbar toolbar = (Toolbar)findViewById(R.id.editcatavailable_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Cat Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputCatName = findViewById(R.id.editcatavailable_name);
        inputCatDob = findViewById(R.id.editcatavailable_dob);
        inputCatDesc = findViewById(R.id.editcatavailable_desc);
        inputCatMedNote = findViewById(R.id.editcatavailable_mednote);
        catPhotoIv = findViewById(R.id.editcatavailable_photo);

        String catId = getIntent().getStringExtra("cat_id");
        databaseCats = FirebaseDatabase.getInstance().getReference().child("cats").child(catId);
        databaseCats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("catName").getValue(String.class);
                String dob = dataSnapshot.child("catDob").getValue(String.class);
                String desc = dataSnapshot.child("catDescription").getValue(String.class);
                String mednote = dataSnapshot.child("catMedNote").getValue(String.class);
                String photo = dataSnapshot.child("catProfilePhoto").getValue(String.class);

                inputCatName.setText(name);
                inputCatDob.setText(dob);
                inputCatDesc.setText(desc);
                inputCatMedNote.setText(mednote);
                Picasso.get().load(photo).transform(new RoundedCornersTransform()).centerCrop().resize(192, 192).into(catPhotoIv);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
