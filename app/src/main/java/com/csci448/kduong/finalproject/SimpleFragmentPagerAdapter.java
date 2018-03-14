package com.csci448.kduong.finalproject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by darks on 3/14/2018.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new OptionFragment();
            case 2:
                return new ProfileFragment();
            case 3:
                return new SettingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return mContext.getString(R.string.home);
            case 1:
                return mContext.getString(R.string.search);
            case 2:
                return mContext.getString(R.string.profile);
            case 3:
                return mContext.getString(R.string.settings);
            default:
                return null;
        }
    }

}
