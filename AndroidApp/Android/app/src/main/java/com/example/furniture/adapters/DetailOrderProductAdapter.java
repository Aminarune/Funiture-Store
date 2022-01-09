package com.example.furniture.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.models.Order;
import com.example.furniture.models.OrderDetail;
import com.example.furniture.models.Product;

import java.util.ArrayList;

public class DetailOrderProductAdapter extends RecyclerView.Adapter<DetailOrderProductAdapter.ViewHD> {

    private Context context;
    private ArrayList<Product> productArrayList;
    private ArrayList<OrderDetail> orderDetails;


    public DetailOrderProductAdapter(Context context, ArrayList<Product> productArrayList, ArrayList<OrderDetail> orderDetails) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public ViewHD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_order_product, parent, false);
        ViewHD viewHD = new ViewHD(view);
        return viewHD;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHD holder, @SuppressLint("RecyclerView") int position) {
        Product product = productArrayList.get(position);
        holder.imageView.setImageBitmap(product.getPicture());
        holder.textName.setText(product.getName());
        holder.textPrice.setText(product.getPrice());
        OrderDetail orderDetail=orderDetails.get(position);
        holder.tvQuantity.setText(""+orderDetail.getQuantity());
        holder.tvTotal.setText(orderDetail.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    class ViewHD extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textName, textPrice,tvQuantity,tvTotal;

        public ViewHD(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivProductOrderDetail);
            textName = itemView.findViewById(R.id.tvProductOrderDetail);
            textPrice = itemView.findViewById(R.id.tvPriceProductOrderDetail);
            tvQuantity=itemView.findViewById(R.id.tvQuantityOrderDetail);
            tvTotal=itemView.findViewById(R.id.tvTotalPriceOrderDetail);
        }
    }

}
