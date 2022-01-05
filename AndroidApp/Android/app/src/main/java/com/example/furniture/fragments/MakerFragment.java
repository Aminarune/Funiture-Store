package com.example.furniture.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.adapters.FavoriteAdapter;
import com.example.furniture.models.Cart;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.Product;
import com.example.furniture.models.User;
import com.example.furniture.services.Api;
import com.example.furniture.services.DownloadDataProduct;
import com.example.furniture.services.GetFavorite;
import com.example.furniture.services.OnDataFavList;
import com.example.furniture.services.OnDataProductListener;
import com.example.furniture.services.RemoveFavorite;
import com.example.furniture.utilities.OnDataPassProduct;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakerFragment extends Fragment implements OnDataFavList, OnDataProductListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MakerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakerFragment newInstance(String param1, String param2) {
        MakerFragment fragment = new MakerFragment();
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

    private RecyclerView recycleViewFavorite;

    private RequestQueue queue;

    private String link = Api.url + "favorite";

    private User user;

    public MakerFragment(User user) {
        this.user = user;
    }

    private ShimmerFrameLayout shimmerFrameLayout;

    private OnDataPassProduct onDataPassProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maker, container, false);

        initView(view);

        queue = Volley.newRequestQueue(view.getContext());


        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recycleViewFavorite.setVisibility(View.GONE);

        //getListFav
        GetFavorite getFavorite = new GetFavorite(view.getContext(), user.getId(), queue, this);
        getFavorite.execute();


        return view;
    }


    private void initView(View view) {
        recycleViewFavorite = view.findViewById(R.id.recycleViewFavorite);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFav);
    }

    @Override
    public void onDataFavFound(Context context, ArrayList<Favourite> favourite) {
        DownloadDataProduct downloadDataProduct = new DownloadDataProduct(context, this, favourite);
        downloadDataProduct.execute();

    }

    @Override
    public void onDataFavNotFound(Context view, String error) {

    }


    @Override
    public void onCompleteDataProduct(Context view, ArrayList<Product> arrayList) {

    }

    @Override
    public void onErrorDataProduct(Context view, String error) {

    }

    @Override
    public void onCompleteDataFavProduct(Context context, ArrayList<Product> products, ArrayList<Favourite> favourites) {

        ArrayList<Product> arrayListProduct = new ArrayList<>();

        for (Favourite fav : favourites) {
            for (Product product : products) {
                if (fav.getIdProduct().equals(product.getId())) {
                    arrayListProduct.add(product);
                }
            }
        }

        createUiFav(context, arrayListProduct, favourites);
    }

    @Override
    public void onCompleteDataCartProduct(Context view, ArrayList<Product> products, ArrayList<Cart> carts) {

    }

    private void createUiFav(Context context, ArrayList<Product> products, ArrayList<Favourite> favourites) {
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(context, products, favourites);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        recycleViewFavorite.setLayoutManager(layoutManager);
        recycleViewFavorite.setAdapter(favoriteAdapter);


        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recycleViewFavorite.setVisibility(View.VISIBLE);

        favoriteAdapter.setOnClickFav(new FavoriteAdapter.SetOnClickFav() {
            @Override
            public void onRemoveItem(Favourite favourite, int pos) {
                favourites.remove(favourite);
                favoriteAdapter.notifyItemRemoved(pos);
                favoriteAdapter.notifyItemRangeChanged(pos, favourites.size());
                RemoveFavorite removeFavorite = new RemoveFavorite(favourite.getId(), queue);
                removeFavorite.execute();
            }

            @Override
            public void onClickItemFav(View view, Product product) {
                sendDataToActivity(product);
            }


        });

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onDataPassProduct = (OnDataPassProduct) context;
    }

    public void sendDataToActivity(Product product) {
        onDataPassProduct.onDataPassProduct(product,"Maker");
    }

    @Override
    public void onStart() {
        super.onStart();

        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recycleViewFavorite.setVisibility(View.GONE);
        //getListFav
        GetFavorite getFavorite = new GetFavorite(getActivity(), user.getId(), queue, this);
        getFavorite.execute();
    }
}