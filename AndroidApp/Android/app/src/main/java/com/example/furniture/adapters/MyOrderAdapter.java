package com.example.furniture.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.models.Order;
import com.example.furniture.models.User;

import java.util.ArrayList;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHD>{

    Context context;

    ArrayList<Order> arrayList;

    User user;

    SetOnClickOrderItem setOnClickOrderItem;

    public MyOrderAdapter(Context context, ArrayList<Order> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void setSetOnClickOrderItem(SetOnClickOrderItem setOnClickOrderItem) {
        this.setOnClickOrderItem = setOnClickOrderItem;
    }

    @NonNull
    @Override
    public ViewHD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.item_order,parent,false);
        ViewHD viewHD=new ViewHD(view);
        return viewHD;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHD holder, int position) {
        Order order=arrayList.get(position);
        holder.tvOrderNo.setText("OrderNo: "+order.getId());
        holder.tvDate.setText(order.getDate());
        holder.tvPriceOrderTotal.setText(order.getPrice());
        holder.tvStatus.setText(order.getState());
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickOrderItem.SetOnClickOrderItem(order,user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHD extends RecyclerView.ViewHolder{

        TextView tvOrderNo,tvDate,tvPriceOrderTotal,tvStatus;
        Button btnDetail;

        public ViewHD(@NonNull View itemView) {
            super(itemView);
            tvOrderNo=itemView.findViewById(R.id.tvOrderNo);
            tvDate=itemView.findViewById(R.id.tvDateOrder);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvPriceOrderTotal=itemView.findViewById(R.id.tvPriceOrderTotal);
            btnDetail=itemView.findViewById(R.id.btnDetailOrder);
        }
    }

    public interface SetOnClickOrderItem{
        void SetOnClickOrderItem(Order order, User user);
    }
}
