package com.example.furniture.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.ShippingAddress;

import java.util.ArrayList;

public class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.ViewHD> {

    Context context;

    ArrayList<ShippingAddress> arrayList;


    OnCheck onCheck;

    public void setOnCheck(OnCheck onCheck) {
        this.onCheck = onCheck;
    }

    //initilize selectCheck
    private ArrayList<Integer> selectCheck = new ArrayList<>();


    public ShippingAddressAdapter(Context context, ArrayList<ShippingAddress> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).isStatus() == true) {
                selectCheck.add(1);
            } else {
                selectCheck.add(0);
            }

        }

    }


    @NonNull
    @Override
    public ViewHD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_address, parent, false);
        ViewHD viewHD = new ViewHD(view);
        return viewHD;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHD holder, @SuppressLint("RecyclerView") int position) {
        ShippingAddress shippingAddress = arrayList.get(position);

        if (selectCheck.get(position) == 1) {
//            holder.btnRemove.setVisibility(View.GONE);
            holder.checkBox.setChecked(true);
        } else {
//            holder.btnRemove.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int k = 0; k < selectCheck.size(); k++) {
                    if (k == position) {
                        selectCheck.set(k, 1);
                    } else {
                        selectCheck.set(k, 0);
                    }
                }
                notifyDataSetChanged();

            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    //Do whatever you want to do with selected value
                    onCheck.setOnCheck(arrayList.get(holder.getAdapterPosition()));
                }else {
                    onCheck.setOnUnCheck(arrayList.get(holder.getAdapterPosition()));
                }
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                onCheck.modifyShipping(shippingAddress,pos);
            }
        });

//        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                onCheck.onRemoveItem(shippingAddress,holder.getAbsoluteAdapterPosition());
//            }
//        });


        String fullAddress =
                shippingAddress.getAddress() + ", " +
                        shippingAddress.getWard()+ ", " +
                        shippingAddress.getDistrict()+ ", " +
                        shippingAddress.getProvince();

        holder.textAddress.setText(fullAddress);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHD extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        ImageView btnEdit,btnRemove;

        TextView textAddress;

        public ViewHD(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxState);
            btnEdit = itemView.findViewById(R.id.ic_edit_address);
            textAddress = itemView.findViewById(R.id.tvAddress);
//            btnRemove=itemView.findViewById(R.id.ic_remove_address);
        }
    }

    public interface OnCheck {
        void setOnCheck(ShippingAddress shippingAddress);
        void setOnUnCheck(ShippingAddress shippingAddress);
        void modifyShipping(ShippingAddress shippingAddress,int pos);
        void onRemoveItem(ShippingAddress shippingAddress, int pos);
    }
}
