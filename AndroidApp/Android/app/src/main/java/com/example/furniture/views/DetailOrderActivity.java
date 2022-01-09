package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.adapters.DetailOrderProductAdapter;
import com.example.furniture.models.Cart;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.Order;
import com.example.furniture.models.OrderDetail;
import com.example.furniture.models.Product;
import com.example.furniture.models.ShippingAddress;
import com.example.furniture.models.User;
import com.example.furniture.services.DownloadDataProduct;
import com.example.furniture.services.DownloadProductById;
import com.example.furniture.services.GetOrderDetail;
import com.example.furniture.services.GetShippingAddressId;
import com.example.furniture.services.OnDataProductByID;
import com.example.furniture.services.OnDataProductListener;
import com.example.furniture.utilities.AlbertDialogUtil;
import com.example.furniture.utilities.NetworkChangeReceiver;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class DetailOrderActivity extends AppCompatActivity {

    User user;

    Order order;

    TextView tvIdOrderDetail, tvDateOrderDetail, tvNameOrderDetail, tvPhoneOrderDetail, tvAddressOrderDetail;

    RequestQueue queue;

    RecyclerView recyclerView;


    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;

    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        queue = Volley.newRequestQueue(this);

        AlertDialog alertDialog = AlbertDialogUtil.showAlertDialog(this);
        networkChangeReceiver = new NetworkChangeReceiver(alertDialog);

        shimmerFrameLayout=findViewById(R.id.shimmerDetailOrder);
        linearLayout=findViewById(R.id.layoutDetailOrder);


        tvIdOrderDetail = findViewById(R.id.tvIdOrderDetail);
        tvDateOrderDetail = findViewById(R.id.tvDateOrderDetail);
        tvNameOrderDetail = findViewById(R.id.tvNameOrderDetail);
        tvPhoneOrderDetail = findViewById(R.id.tvPhoneOrderDetail);
        tvAddressOrderDetail = findViewById(R.id.tvAddressOrderDetail);

        recyclerView = findViewById(R.id.recycleViewDetailOrder);


        order = (Order) getIntent().getSerializableExtra("idOder");
        user = (User) getIntent().getSerializableExtra("user");


        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

        tvIdOrderDetail.setText(order.getId());
        tvDateOrderDetail.setText(order.getDate());
        tvNameOrderDetail.setText(user.getName());
        tvPhoneOrderDetail.setText(user.getPhone());

        GetShippingAddressId getShippingAddressId = new GetShippingAddressId(user, order.getIdShipping(), queue);
        getShippingAddressId.execute();
        getShippingAddressId.setOnDataAddressObject(new GetShippingAddressId.OnDataAddressObject() {
            @Override
            public void onDataAddressObject(ShippingAddress shippingAddress) {

                String address = shippingAddress.getAddress();
                String ward = shippingAddress.getWard();
                String district = shippingAddress.getDistrict();
                String city = shippingAddress.getProvince();
                String fullAddress = address + ", " +
                        ward + ", " + district + ", " + city;

                tvAddressOrderDetail.setText(fullAddress);

                GetOrderDetail getOrderDetail = new GetOrderDetail(order.getId(), queue);
                getOrderDetail.execute();


                getOrderDetail.setGetOrderDetailInter(new GetOrderDetail.GetOrderDetailInter() {
                    @Override
                    public void getOrderDetail(ArrayList<OrderDetail> orderDetails) {


                        DownloadDataProduct downloadDataProduct = new DownloadDataProduct(DetailOrderActivity.this,
                                new OnDataProductListener() {
                                    @Override
                                    public void onCompleteDataProduct(Context view, ArrayList<Product> arrayList) {

                                    }

                                    @Override
                                    public void onErrorDataProduct(Context view, String error) {

                                    }

                                    @Override
                                    public void onCompleteDataFavProduct(Context view, ArrayList<Product> products, ArrayList<Favourite> favourites) {

                                    }

                                    @Override
                                    public void onCompleteDataCartProduct(Context view, ArrayList<Product> products, ArrayList<Cart> carts) {

                                    }

                                    @Override
                                    public void onCompleteDataOrderDetailProduct(Context view, ArrayList<Product> products, ArrayList<OrderDetail> orderDetails) {
                                        ArrayList<Product> arrayListProduct = new ArrayList<>();

                                        for (OrderDetail orderDetail : orderDetails) {
                                            for (Product product : products) {
                                                if (orderDetail.getIdProduct().equals(product.getId())) {
                                                    arrayListProduct.add(product);
                                                }
                                            }
                                        }

                                        createUiProduct(view, arrayListProduct, orderDetails);
                                    }
                                }, orderDetails, 0);
                        downloadDataProduct.execute();


                    }
                });
            }
        });

    }


    private void createUiProduct(Context context, ArrayList<Product> products, ArrayList<OrderDetail> orderDetail) {
        DetailOrderProductAdapter adapter = new DetailOrderProductAdapter(DetailOrderActivity.this,
                products, orderDetail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailOrderActivity.this,
                RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeReceiver);
    }

}