package ro.basilescu.bogdan.templateapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ro.basilescu.bogdan.templateapplication.adapters.TabAdapter;
import ro.basilescu.bogdan.templateapplication.fragments.FirebaseFragment;
import ro.basilescu.bogdan.templateapplication.fragments.RetrofitFragment;
import ro.basilescu.bogdan.templateapplication.fragments.SQLiteFragment;

public class SwipeMainActivity extends AppCompatActivity implements FirebaseFragment.OnFragmentFirebaseListener,
        RetrofitFragment.OnFragmentRetrofitListener, SQLiteFragment.OnFragmentSQLiteListener {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(SwipeMainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        setContentView(R.layout.activity_swipe_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get access to the custom title view
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Set up adapters
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mTabAdapter = new TabAdapter(getSupportFragmentManager(), this, mTitle);
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOffscreenPageLimit(TabAdapter.Tab.values().length - 1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
            mTabAdapter.setTabLayout(tabLayout);
            mViewPager.addOnPageChangeListener(mTabAdapter);

            // Set up all tab headers
            for (int i = 0; i < TabAdapter.Tab.values().length; i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    View customView = mTabAdapter.getCustomView(i);
                    tab.setCustomView(customView);
                    // Highlight the first tab header
                    if (i == 0) {
                        customView.setSelected(true);
                        mTabAdapter.onPageSelected(0);
                    }
                }
            }
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mTabAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_swipe_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(SwipeMainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Use for activity-fragment interaction
     * Methods from fragment callback interfaces
     */
    @Override
    public void onFragmentFirebaseInteraction(String firebaseEvent) {

    }

    @Override
    public void onFragmentRetrofitInteraction(String retrofitEvent) {

    }

    @Override
    public void onFragmentSQLiteInteraction(String sqliteEvent) {

    }
}
