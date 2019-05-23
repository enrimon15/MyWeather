package it.univaq.mobileprogramming.myweather;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arlib.floatingsearchview.FloatingSearchView;


public class SearchActivity extends AppCompatActivity implements BaseExampleFragment.BaseExampleFragmentCallbacks  {

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

    @Override
    public void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
        //searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
