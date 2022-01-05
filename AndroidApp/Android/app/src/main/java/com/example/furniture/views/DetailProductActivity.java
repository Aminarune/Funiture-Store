package com.example.furniture.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.models.Cart;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.Product;
import com.example.furniture.models.User;
import com.example.furniture.services.CheckCart;
import com.example.furniture.services.DownloadProductById;
import com.example.furniture.services.OnDataGetCart;
import com.example.furniture.services.OnDataProductByID;
import com.example.furniture.services.CheckFavorite;
import com.example.furniture.services.OnDataSaveCart;
import com.example.furniture.services.OnDataSaveListener;
import com.example.furniture.services.OnFavDataListener;
import com.example.furniture.services.RemoveFavorite;
import com.example.furniture.services.SaveToCart;
import com.example.furniture.services.SaveToFavorite;
import com.example.furniture.services.UpdateToCartDetailProduct;
import com.example.furniture.utilities.AlertDialogUtil;
import com.example.furniture.utilities.NetworkChangeReceiver;
import com.example.furniture.utilities.NumberUtilities;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Timer;
import java.util.TimerTask;

public class DetailProductActivity extends AppCompatActivity implements OnDataProductByID, View.OnClickListener, OnFavDataListener, OnDataSaveListener {

    private ImageView ivItemProductDetail, ivBackDetail;

    private TextView tvItemProductDetail, tvPriceProductDetail, tvDescProductDetail,
            tvInscrease, tvDescrease, tvQuantityDetail;

    private RequestQueue queue;

    private int quantity = 1; // max 10

    private RelativeLayout layoutProductDetail;

    private ShimmerFrameLayout shimmerFrameLayout;

    private ImageView ivFavourite;

    private Button btnAddToCart;

    private String idProduct;

    private User user;

    //check or not
    private boolean state;

    private String idFav;

    private String from;

    private Product product;

    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        queue = Volley.newRequestQueue(this);

        user = (User) getIntent().getSerializableExtra("user");

        idProduct = getIntent().getStringExtra("productID");

        from = getIntent().getStringExtra("from");

        initView();

        networkChangeReceiver = new NetworkChangeReceiver(
                AlertDialogUtil.showAlertDialog(DetailProductActivity.this,
                        R.raw.disconnected, "Please check your connection and try again"));

        //check first
        CheckFavorite checkFavorite = new CheckFavorite(user.getId(), idProduct, queue, this);
        checkFavorite.execute();

        getData(this, idProduct);
    }


    private void initView() {
        ivItemProductDetail = findViewById(R.id.ivItemProductDetail);
        tvItemProductDetail = findViewById(R.id.tvItemProductDetail);
        tvPriceProductDetail = findViewById(R.id.tvPriceProductDetail);
        tvDescProductDetail = findViewById(R.id.tvDescProductDetail);
        ivBackDetail = findViewById(R.id.ivBackDetail);

        tvInscrease = findViewById(R.id.tvInscrease);
        tvDescrease = findViewById(R.id.tvDescrease);
        tvDescrease.setEnabled(false);
        tvQuantityDetail = findViewById(R.id.tvQuantityDetail);

        shimmerFrameLayout = findViewById(R.id.shimmerProductDetail);

        layoutProductDetail = findViewById(R.id.layoutProductDetail);

        ivBackDetail.setOnClickListener(this::onClick);

        tvInscrease.setOnClickListener(this::onClick);

        tvDescrease.setOnClickListener(this::onClick);

        ivFavourite = findViewById(R.id.ivFavourite);
        ivFavourite.setOnClickListener(this::onClick);

        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvInscrease:
                increaseNumber();
                break;

            case R.id.tvDescrease:
                decreaseNumber();
                break;

            case R.id.ivBackDetail:
                finish();
                break;

            case R.id.ivFavourite:
                //change when click
                checkFav();
                break;

            case R.id.btnAddToCart:
                getCart();
                break;
        }
    }

    private void getCart() {

        CheckCart checkCart = new CheckCart(user.getId(), idProduct, queue, new OnDataGetCart() {
            @Override
            public void onFoundCartItem(Cart cart) {
                //found - update
                if (cart.getQuantity() == 10) {
                    AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(DetailProductActivity.this,
                            R.raw.wrong, "You have reached the maximum purchase limit");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                } else {
                    int quantity = Integer.valueOf(tvQuantityDetail.getText().toString());
                    updateCart(cart, quantity);
                }
            }

            @Override
            public void onNotFoundItem(String error) {
                //notfound - save
                saveToCart();
            }
        });
        checkCart.execute();
    }


    private void saveToCart() {
        int quantity = Integer.valueOf(tvQuantityDetail.getText().toString());

        float price = Float.valueOf(tvPriceProductDetail.getText().toString());

        float total = price * quantity;

        SaveToCart saveToCart = new SaveToCart(user.getId(), idProduct, quantity, price, total, queue, new OnDataSaveCart() {
            @Override
            public void onSuccess(boolean result) {
                AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(DetailProductActivity.this,
                        R.raw.loading,
                        "Item successfully added to your cart");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }

            @Override
            public void onFailure(String result) {
                AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(DetailProductActivity.this,
                        R.raw.wrong,
                        "Item could not  added to your cart");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });
        tvQuantityDetail.setText(formatString(1));
        saveToCart.execute();
    }

    private void updateCart(Cart cart, int quantity) {

        int quan = cart.getQuantity() + quantity;
        if (quan > 10) {
            AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(DetailProductActivity.this,
                    R.raw.wrong,
                    "You have reach the maximum number product (10)");
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } else {
            UpdateToCartDetailProduct updateToCartDetailProduct = new UpdateToCartDetailProduct(quantity, queue, cart, new OnDataSaveCart() {
                @Override
                public void onSuccess(boolean result) {
                    AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(DetailProductActivity.this,
                            R.raw.loading,
                            "Item successfully added to your cart");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }

                @Override
                public void onFailure(String result) {

                }
            });
            tvQuantityDetail.setText(formatString(1));
            updateToCartDetailProduct.execute();
        }
    }


    private void getData(Context context, String idProduct) {

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        layoutProductDetail.setVisibility(View.GONE);

        DownloadProductById downloadProductById = new DownloadProductById(
                context, idProduct, queue, this);
        downloadProductById.execute();
    }

    private void createUIProduct(Product product) {
        ivItemProductDetail.setImageBitmap(product.getPicture());
        tvItemProductDetail.setText(product.getName());
        tvPriceProductDetail.setText(product.getPrice());
        tvDescProductDetail.setText(product.getDesc());
    }

    @Override
    public void onFound(Context view, Product product) {
        //loading first
        layoutProductDetail.setVisibility(View.VISIBLE);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        createUIProduct(product);
        this.product = product;
    }

    @Override
    public void onNotFound(Context view, String error) {

        AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(DetailProductActivity.this,
                R.raw.wrong, error);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                alertDialog.dismiss();
                finish();
            }
        }, 1500);
    }

    private void increaseNumber() {
        tvDescrease.setEnabled(true);
        quantity++;
        String numIns = formatString(quantity);
        tvQuantityDetail.setText(numIns);
        if (quantity == 10) {
            Toast.makeText(this, "You have reach the maximum number", Toast.LENGTH_SHORT).show();
            tvInscrease.setEnabled(false);
            tvDescrease.setEnabled(true);
        }
    }

    private void decreaseNumber() {
        tvInscrease.setEnabled(true);
        quantity--;
        String numDes = formatString(quantity);
        tvQuantityDetail.setText(numDes);
        if (quantity == 1) {
            tvDescrease.setEnabled(false);
        }
    }


    private String formatString(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        }
        return "0" + String.valueOf(number);
    }


    //change when click
    private void checkFav() {
        if (state == false) {
            //uncheck to check
            SaveToFavorite saveToFavorite = new SaveToFavorite(user.getId(), idProduct, queue, this);
            saveToFavorite.execute();
            ivFavourite.setImageResource(R.drawable.ic_marker_black_check);
            state = true;
        } else {
            //check to uncheck
            RemoveFavorite removeFavorite = new RemoveFavorite(idFav, queue);
            removeFavorite.execute();
            ivFavourite.setImageResource(R.drawable.ic_marker_black);
            state = false;
        }
    }


    //check exist
    @Override
    public void onFoundFav(boolean result, String id) {
        state = result;
        idFav = id;
        ivFavourite.setImageResource(R.drawable.ic_marker_black_check);
    }

    @Override
    public void onNotFoundFav(boolean result) {
        state = result;
        ivFavourite.setImageResource(R.drawable.ic_marker_black);
    }


    //save success
    @Override
    public void onSuccess(Favourite favourite) {
        idFav = favourite.getId();
    }

    @Override
    public void onFail(String error) {

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

    @Override
    public void onBackPressed() {

    }
}