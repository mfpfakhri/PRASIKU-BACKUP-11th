package clouwiko.dev.prasiku.activity.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import clouwiko.dev.prasiku.R;

public class UserHomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private UserHomePagerAdapter userHomePagerAdapter;
    private ViewPager userHomeViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        tabLayout = (TabLayout)findViewById(R.id.userhome_tablayout);
        appBarLayout = (AppBarLayout)findViewById(R.id.userhome_appbar);
        userHomeViewPager = (ViewPager)findViewById(R.id.userhome_viewpager);

        userHomePagerAdapter = new UserHomePagerAdapter(getSupportFragmentManager());
        userHomePagerAdapter.addFragment(new UserHomeCatFragment(), "Cats");
        userHomePagerAdapter.addFragment(new UserHomeProfileFragment(), "Profile");

        userHomeViewPager.setAdapter(userHomePagerAdapter);
        tabLayout.setupWithViewPager(userHomeViewPager);
    }
}
