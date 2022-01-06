package com.example.furniture.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.furniture.models.City;
import com.example.furniture.models.District;

import java.util.ArrayList;

public class SpinnerDistrictAdapter extends ArrayAdapter<District> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<District> districts;

    public SpinnerDistrictAdapter(Context context, int textViewResourceId,
                                  ArrayList<District> districts) {
        super(context, textViewResourceId, districts);
        this.context = context;
        this.districts = districts;
    }

    @Override
    public int getCount(){
        return districts.size();
    }

    @Override
    public District getItem(int position){
        return districts.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(districts.get(position).getTitle());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(districts.get(position).getTitle());

        return label;
    }
}
