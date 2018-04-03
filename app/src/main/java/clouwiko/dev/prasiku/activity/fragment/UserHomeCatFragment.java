package clouwiko.dev.prasiku.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.adapter.UserHomeCatListAdapter;
import clouwiko.dev.prasiku.activity.model.Cat;

public class UserHomeCatFragment extends Fragment {
    public UserHomeCatFragment() {
    }

    private FirebaseAuth auth;
    private String TAG = "UserHomeCatFragment";
    private DatabaseReference databaseCats;
    private List<Cat> cats = new ArrayList<>();
    private UserHomeCatListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home_cat, container, false);

        //Init View
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.catRecyclerView);

        recyclerView.setLayoutManager((new LinearLayoutManager(getActivity())));
        adapter = new UserHomeCatListAdapter(recyclerView, getActivity(), cats);
        recyclerView.setAdapter(adapter);

        //Cat List
        catList();

        return view;
    }

    private void catList() {
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //Get Current User ID
        final String userUID = auth.getCurrentUser().getUid();

        //Database Reference
        databaseCats = FirebaseDatabase.getInstance().getReference("cats");

        //Get Current User Value from Firebase Database
        databaseCats.orderByKey().equalTo(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long num = dataSnapshot.getChildrenCount();
                for (DataSnapshot catSnapshot : dataSnapshot.getChildren()){
                    String cat
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}