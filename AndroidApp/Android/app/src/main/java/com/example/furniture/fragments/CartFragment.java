package com.example.furniture.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.adapters.CartAdapter;
import com.example.furniture.models.Cart;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.Product;
import com.example.furniture.models.User;
import com.example.furniture.services.Api;
import com.example.furniture.services.CheckCart;
import com.example.furniture.services.DownloadDataProduct;
import com.example.furniture.services.GetCart;
import com.example.furniture.services.OnDataCartList;
import com.example.furniture.services.OnDataGetCart;
import com.example.furniture.services.OnDataProductListener;
import com.example.furniture.services.OnDataSaveCart;
import com.example.furniture.services.RemoveCart;
import com.example.furniture.services.UpdateToCart;
import com.example.furniture.utilities.NumberUtilities;
import com.example.furniture.utilities.OnDataPassProduct;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements OnDataCartList {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ShimmerFrameLayout shimmerCart;

    private RecyclerView recycleViewCart;

    private RequestQueue queue;

    private String link = Api.urlLocal + "cart";

    private User user;

    private OnDataPassProduct onDataPassProduct;

    private CartAdapter cartAdapter;

    private TextView tvTotalPriceAllCart;

    private Button btnCheckOut;

    private LinearLayout layoutCheckOut;


    public CartFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        shimmerCart = view.findViewById(R.id.shimmerCart);

        recycleViewCart = view.findViewById(R.id.recycleViewCart);

        tvTotalPriceAllCart=view.findViewById(R.id.tvTotalPriceAllCart);

        btnCheckOut=view.findViewById(R.id.btnCheckOut);

        layoutCheckOut=view.findViewById(R.id.layoutCheckOut);

        queue = Volley.newRequestQueue(view.getContext());

        shimmerCart.startShimmer();
        shimmerCart.setVisibility(View.VISIBLE);
        recycleViewCart.setVisibility(View.GONE);

        //getListFav
        GetCart getCart = new GetCart(view.getContext(), user.getId(), queue, this);
        getCart.execute();

        return view;
    }

    @Override
    public void onDataCartFound(Context view, ArrayList<Cart> cartArrayList) {
        DownloadDataProduct downloadDataProduct = new DownloadDataProduct(view, cartArrayList, new OnDataProductListener() {
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
                ArrayList<Product> arrayListProduct = new ArrayList<>();

                for (Cart cart : carts) {
                    for (Product product : products) {
                        if (cart.getIdProduct().equals(product.getId())) {
                            arrayListProduct.add(product);
                        }
                    }
                }

                createUiProduct(view, arrayListProduct, carts);
            }
        });
        downloadDataProduct.execute();

    }

    @Override
    public void onDataCartNotFound(Context view, String error) {
        shimmerCart.stopShimmer();
        shimmerCart.setVisibility(View.GONE);
        recycleViewCart.setVisibility(View.VISIBLE);
    }

    private void updateTotalPrice(ArrayList<Product> products,ArrayList<Cart> carts){
        float total=0;
        for(int i=0;i<carts.size();i++){
            total+=carts.get(i).getTotalPrice();
        }
        tvTotalPriceAllCart.setText(NumberUtilities.getFloatDecimal("###.##").format(total));
        if(carts.size()>0){
            layoutCheckOut.setVisibility(View.VISIBLE);
        }
        else {
            layoutCheckOut.setVisibility(View.GONE);
        }
    }

    private void createUiProduct(Context context, ArrayList<Product> products, ArrayList<Cart> carts) {
        cartAdapter = new CartAdapter(context, products, carts);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        recycleViewCart.setLayoutManager(layoutManager);
        recycleViewCart.setAdapter(cartAdapter);

        updateTotalPrice(products,carts);



        shimmerCart.stopShimmer();
        shimmerCart.setVisibility(View.GONE);
        recycleViewCart.setVisibility(View.VISIBLE);

        cartAdapter.setSetOnClickCart(new CartAdapter.SetOnClickCart() {
            @Override
            public void onRemoveItem(View view, Cart cart, int pos) {
                carts.remove(pos);
                cartAdapter.notifyItemRemoved(pos);
                RemoveCart removeCart = new RemoveCart(cart.getId(), queue);
                removeCart.execute();
                updateTotalPrice(products,carts);
            }

            @Override
            public void onIncreaseItem(View view, Cart cart, int quantity) {
                getCart(cart, quantity);
                updateTotalPrice(products,carts);
            }

            @Override
            public void onDecreaseItem(View view, Cart cart, int quantity) {
                getCart(cart, quantity);
                updateTotalPrice(products,carts);
            }

            @Override
            public void onClickItemCart(View view, Product product) {
                sendDataToActivity(product);
            }
        });


    }

    private void getCart(Cart cartA, int quantity) {

        CheckCart checkCart = new CheckCart(user.getId(), cartA.getIdProduct(), queue, new OnDataGetCart() {
            @Override
            public void onFoundCartItem(Cart cart) {
                //found - update
                updateCart(cart, quantity);
            }

            @Override
            public void onNotFoundItem(String error) {

            }
        });
        checkCart.execute();
    }

    private void updateCart(Cart cart, int quantity) {

        UpdateToCart updateToCart = new UpdateToCart(quantity, queue, cart, new OnDataSaveCart() {
            @Override
            public void onSuccess(boolean result) {

            }

            @Override
            public void onFailure(String result) {

            }
        });
        updateToCart.execute();

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onDataPassProduct = (OnDataPassProduct) context;
    }

    public void sendDataToActivity(Product product) {
        onDataPassProduct.onDataPassProduct(product, "Cart");
    }


    @Override
    public void onStart() {
        super.onStart();
        //getListFav
        shimmerCart.startShimmer();
        shimmerCart.setVisibility(View.VISIBLE);
        recycleViewCart.setVisibility(View.GONE);
        GetCart getCart = new GetCart(getActivity(), user.getId(), queue, this);
        getCart.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}