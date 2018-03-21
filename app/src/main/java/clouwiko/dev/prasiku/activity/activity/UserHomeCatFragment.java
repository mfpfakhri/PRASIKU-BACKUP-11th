package clouwiko.dev.prasiku.activity.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import clouwiko.dev.prasiku.R;

public class UserHomeCatFragment extends Fragment {
    public UserHomeCatFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home_cat, container, false);
        return view;
    }
}