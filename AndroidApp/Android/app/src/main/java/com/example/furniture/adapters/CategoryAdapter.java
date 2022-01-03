package com.example.furniture.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.models.Category;



import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHD> {

    private Context context;

    private ArrayList<Category> arrayList;

    private SetOnClickItemCategory setOnClickItem;

    public void setSetOnClickItem(SetOnClickItemCategory setOnClickItem) {
        this.setOnClickItem = setOnClickItem;
    }

    public CategoryAdapter(Context context, ArrayList<Category> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_category, parent, false);
        ViewHD viewHD = new ViewHD(view);
        return viewHD;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHD holder, @SuppressLint("RecyclerView") int position) {
        Category category = arrayList.get(position);

        holder.ivCategory.setImageBitmap(category.getPicture());
        holder.tvCategory.setText(category.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickItem.setOnClickItemCategory(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class ViewHD extends RecyclerView.ViewHolder {

        ImageView ivCategory;
        TextView tvCategory;

        public ViewHD(@NonNull View itemView) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.ivCategory);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }

    public interface SetOnClickItemCategory {
        void setOnClickItemCategory(View view, int pos);
    }


}
