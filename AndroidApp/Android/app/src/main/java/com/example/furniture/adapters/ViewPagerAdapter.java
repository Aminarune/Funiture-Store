package com.example.furniture.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.furniture.fragments.CompletedFragment;
import com.example.furniture.fragments.DeliveringFragment;
import com.example.furniture.fragments.HomeFragment;
import com.example.furniture.fragments.PendingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new PendingFragment();
            case 1:
                return new DeliveringFragment();
            case 2:
                return new CompletedFragment();
            default:
                return new PendingFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }

}
