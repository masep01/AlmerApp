package com.example.apptr.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.apptr.R;
import com.example.apptr.itinerari_cientific;
import com.example.apptr.itinerari_humanistic;
import com.example.apptr.itinerari_social;
import com.example.apptr.itinerari_tecno;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new itinerari_tecno();
                break;
            case 1:
                fragment = new itinerari_cientific();
                break;
            case 2:
                fragment = new itinerari_social();
                break;
            case 3:
                fragment = new itinerari_humanistic();
                break;
        }
        return fragment;
        }


    @Override
    public int getCount() {
        // Mostrar 4 pàgines en total.
        return 4;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        //El nom de cada pàgina
        switch (position) {
            case 0:
                return "Tecnològ.";
            case 1:
                return "Científic";
            case 2:
                return "Social";
            case 3:
                return "Human.";
        }
        return null;
    }
}
