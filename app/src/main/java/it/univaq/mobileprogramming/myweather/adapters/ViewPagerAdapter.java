package it.univaq.mobileprogramming.myweather.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<String> listFragmentTitles = new ArrayList<>();
        private final List<Fragment> listFragment = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

        @Override
        public Fragment getItem(int position) {
        return listFragment.get(position);
    }

        @Override
        public int getCount() {
        return listFragment.size();
    }


        public void addFragment(String FragmentTitles, Fragment fragment) {
        this.listFragmentTitles.add(FragmentTitles);
        this.listFragment.add(fragment);
    }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

        return listFragmentTitles.get(position);

    }
}

