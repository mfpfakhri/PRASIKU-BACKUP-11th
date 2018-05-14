package clouwiko.dev.prasiku.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import clouwiko.dev.prasiku.R;

public class UserHomeProfileFragment extends Fragment {
    private String TAG = "UserHomeProfile";
    private TextView profileCity, profilePhone, profileEmail, profileAddress;
    private FirebaseAuth auth;
    private DatabaseReference databaseUsers;
    private ProgressBar progressBar;

    public UserHomeProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home_profile, container, false);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //Database Reference
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        //Object Declaration
        profileCity = (TextView) view.findViewById(R.id.userhomeprofile_cityvalue);
        profilePhone = (TextView) view.findViewById(R.id.userhomeprofile_phonevalue);
        profileEmail = (TextView) view.findViewById(R.id.userhomeprofile_emailvalue);
        profileAddress = (TextView)view.findViewById(R.id.userhomeprofile_addressvalue);

        //Get Current User UID
        final String userUID = auth.getCurrentUser().getUid();

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        //Get Current User Value from Firebase Database
        databaseUsers.orderByKey().equalTo(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    final String userCityValue = userSnapshot.child("userCity").getValue(String.class);
                    final String userPhoneValue = userSnapshot.child("userPhone").getValue(String.class);
                    final String userEmailValue = userSnapshot.child("userEmail").getValue(String.class);
                    final String userAddressValue = userSnapshot.child("userAddress").getValue(String.class);
                    profileCity.setText(userCityValue);
                    profilePhone.setText(userPhoneValue);
                    profileEmail.setText(userEmailValue);
                    profileAddress.setText(userAddressValue);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}