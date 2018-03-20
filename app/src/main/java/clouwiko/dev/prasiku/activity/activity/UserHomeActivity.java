package clouwiko.dev.prasiku.activity.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import clouwiko.dev.prasiku.R;

public class UserHomeActivity extends AppCompatActivity {

    private UserHomePagerAdapter userHomePagerAdapter;
    private ViewPager userHomeViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        userHomePagerAdapter = new UserHomePagerAdapter(getSupportFragmentManager());
        userHomeViewPager = (ViewPager)findViewById(R.id.userhome_container);
        setupUserHomeViewPager(userHomeViewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(userHomeViewPager);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupUserHomeViewPager(ViewPager viewPager) {
        UserHomePagerAdapter userHomeAdapter = new UserHomePagerAdapter(getSupportFragmentManager());
        userHomeAdapter.addFragment(new UserHomeCatFragment());
        userHomeAdapter.addFragment(new UserHomeProfileFragment());
        viewPager.setAdapter(userHomeAdapter);
    }
}
