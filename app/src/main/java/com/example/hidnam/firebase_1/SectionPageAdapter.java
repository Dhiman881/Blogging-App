package com.example.hidnam.firebase_1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by hidnam on 5/9/17.
 */

class SectionPageAdapter extends FragmentPagerAdapter {

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                    WallFragment w = new WallFragment();
                    return w;
            case 1 :
                    ChatFragment c = new ChatFragment();
                    return c;
            case 2 :
                    FollowFragment f= new FollowFragment();
                    return f;
            default:
                    return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position){

        switch (position){
            case 0 :

                return "Post Wall";
            case 1 :

                return "Chat";
            case 2 :

                return "People";
            default:
                return null;
        }


    }
}
