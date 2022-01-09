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
import com.example.furniture.adapters.MyOrderAdapter;
import com.example.furniture.models.Order;
import com.example.furniture.models.User;
import com.example.furniture.services.GetOrder;
import com.example.furniture.utilities.SendDetailToActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedFragment extends Fragment implements GetOrder.OnDataGetOrder, MyOrderAdapter.SetOnClickOrderItem {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompletedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompletedFragment newInstance(String param1, String param2) {
        CompletedFragment fragment = new CompletedFragment();
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

    private User user;

    public CompletedFragment(User user) {
        this.user = user;
    }

    private RequestQueue queue;

    private RecyclerView recyclerView;

    private static final String state = "Completed";

    private SendDetailToActivity sendDetailToActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        queue = Volley.newRequestQueue(view.getContext());

        recyclerView = view.findViewById(R.id.recyclerViewCompleted);

        GetOrder getOrder = new GetOrder(view.getContext(), queue, state, user);
        getOrder.execute();

        getOrder.setOnDataGetOrder(this);

        return view;
    }

    @Override
    public void onDataGetOrder(Context context, ArrayList<Order> orders) {
        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(context, orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(myOrderAdapter);
        recyclerView.setLayoutManager(layoutManager);
        myOrderAdapter.setSetOnClickOrderItem(this);

    }

    @Override
    public void SetOnClickOrderItem(Order order, User user) {
        sendDetailToActivity.sendDetailToActivity(order,user);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sendDetailToActivity= (SendDetailToActivity) context;
    }

}