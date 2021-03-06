package it.univaq.mobileprogramming.myweather.activity;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.univaq.mobileprogramming.myweather.R;


public class SearchActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    public SearchActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        showFragment(new SlidingSearchFragment());
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }
}
