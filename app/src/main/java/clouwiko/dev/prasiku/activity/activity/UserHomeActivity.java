package clouwiko.dev.prasiku.activity.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.User;

public class UserHomeActivity extends AppCompatActivity {
    private ImageView ivPhoto;
    private TextView tvName, tvDob, tvGender, tvProvince, tvCity, tvAddress, tvPhone, tvEmail;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        ivPhoto = findViewById(R.id.userprofile_photo);
        tvName = findViewById(R.id.userprofile_name_value);
        tvDob = findViewById(R.id.userprofile_dob_value);
        tvGender = findViewById(R.id.userprofile_gender_value);
        tvProvince = findViewById(R.id.userprofile_province_value);
        tvCity = findViewById(R.id.userprofile_city_value);
        tvAddress = findViewById(R.id.userprofile_address_value);
        tvPhone = findViewById(R.id.userprofile_phone_value);
        tvEmail = findViewById(R.id.userprofile_email_value);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        final String uId = getIntent().getStringExtra("userId");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(uId);
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userData = dataSnapshot.getValue(User.class);
                String name = userData.getUserFname();
                String dob = userData.getUserDob();
                String gender = userData.getUserGender();
                String setgender = null;
                switch (gender){
                    case "Male":
                        setgender = "Pria";
                        break;
                    case "Female":
                        setgender = "Wanita";
                        break;
                }
                String province = userData.getUserProvince();
                String city = userData.getUserCity();
                String address = userData.getUserAddress();
                Long phone = userData.getUserPhone();
                String email = userData.getUserEmail();
                String photo = userData.getUserProfilePhoto();

                tvName.setText(name);
                tvDob.setText(dob);
                tvGender.setText(setgender);
                tvProvince.setText(province);
                tvCity.setText(city);
                tvAddress.setText(address);
                tvPhone.setText(String.valueOf("0"+phone));
                tvEmail.setText(email);
                if (userData.getUserProfilePhoto().equals("")) {
                    String noPhoto = "@drawable/no_image";
                    int imageResource = getResources().getIdentifier(noPhoto, null, getPackageName());
                    Drawable res = getResources().getDrawable(imageResource);
                    ivPhoto.setImageDrawable(res);
                    ivPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } else {
                    Picasso.get().load(photo).resize(256, 256).into(ivPhoto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
