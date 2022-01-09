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
import com.example.furniture.models.User;

public class ViewPagerAdapter extends FragmentStateAdapter {


    User user;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity,User user) {
        super(fragmentActivity);
        this.user=user;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new PendingFragment(user);
            case 1:
                return new DeliveringFragment(user);
            case 2:
                return new CompletedFragment(user);
            default:
                return new PendingFragment(user);
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }

}
