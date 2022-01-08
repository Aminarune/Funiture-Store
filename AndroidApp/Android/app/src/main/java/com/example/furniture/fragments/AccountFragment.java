package com.example.furniture.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.adapters.AvatarAdapter;
import com.example.furniture.models.Avatar;
import com.example.furniture.models.Product;
import com.example.furniture.models.User;
import com.example.furniture.services.Api;
import com.example.furniture.utilities.OnDataPassProduct;
import com.example.furniture.utilities.OnDataPassUser;
import com.example.furniture.views.ShippingActivity;
import com.example.furniture.views.SignInActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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


    public AccountFragment(User user) {
        this.user = user;
    }

    private TextView tvName, tvEmail;

    private ImageView ivUser, ivPhotoCamera,btnLogout;

    private ImageButton  ivEditOrder,ivEditShipping, ivEditSetting;

    private RequestQueue queue;

    private static final String url = Api.urlLocal +"user";

    private OnDataPassUser onDataPassUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initView(view);

        queue = Volley.newRequestQueue(view.getContext());

        setData(view);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToLogin();
            }
        });

        ivPhotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvatarDialog(view, user);
            }
        });


        return view;
    }


    private void moveToLogin() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.putExtra("from","account_logout");
        startActivity(intent);
        getActivity().finish();
    }

    private void initView(View view) {
        tvName = view.findViewById(R.id.tvNameAccountHome);
        tvEmail = view.findViewById(R.id.tvEmailAccountHome);
        ivUser = view.findViewById(R.id.ivAcvatarAccountHome);

        btnLogout = view.findViewById(R.id.btnLogout);

        ivPhotoCamera = view.findViewById(R.id.ivPhotoCamera);

        ivEditOrder = view.findViewById(R.id.ivEditOrder);

        ivEditSetting = view.findViewById(R.id.ivEditSetting);

        ivEditShipping=view.findViewById(R.id.ivEditShipping);
        ivEditShipping.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivEditShipping:
                sendDataToActivity(user,"account");
                break;
        }
    }


    private void setData(View view) {
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        ivUser.setImageResource(getPictureFromNumber());

    }


    private int getPictureFromNumber() {

        int pic = user.getPicture();

        switch (pic) {
            case 0:
                return R.drawable.ic_avatar1;
            case 1:
                return R.drawable.ic_avatar2;
            case 2:
                return R.drawable.ic_avatar3;
            case 3:
                return R.drawable.ic_avatar4;
            case 4:
                return R.drawable.ic_avatar5;
            case 5:
                return R.drawable.ic_avatar6;
            case 6:
                return R.drawable.ic_avatar7;
            case 7:
                return R.drawable.ic_avatar8;
            default:
                return R.drawable.ic_avatar1;
        }

    }

    private int getPictureFromSource(int id) {

        switch (id) {
            case R.drawable.ic_avatar1:
                return 0;
            case R.drawable.ic_avatar2:
                return 1;
            case R.drawable.ic_avatar3:
                return 2;
            case R.drawable.ic_avatar4:
                return 3;
            case R.drawable.ic_avatar5:
                return 4;
            case R.drawable.ic_avatar6:
                return 5;
            case R.drawable.ic_avatar7:
                return 6;
            case R.drawable.ic_avatar8:
                return 7;
            default:
                return 0;
        }

    }

    private void showAvatarDialog(View view, User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View row = getLayoutInflater().inflate(R.layout.layout_item_avatar, null);
        GridView gridView = row.findViewById(R.id.gridViewAvatar);
        ArrayList<Avatar> arrayList = new ArrayList<>();
        arrayList.add(new Avatar(R.drawable.ic_avatar1));
        arrayList.add(new Avatar(R.drawable.ic_avatar2));
        arrayList.add(new Avatar(R.drawable.ic_avatar3));
        arrayList.add(new Avatar(R.drawable.ic_avatar4));
        arrayList.add(new Avatar(R.drawable.ic_avatar5));
        arrayList.add(new Avatar(R.drawable.ic_avatar6));
        arrayList.add(new Avatar(R.drawable.ic_avatar7));
        arrayList.add(new Avatar(R.drawable.ic_avatar8));
        AvatarAdapter avatarAdapter = new AvatarAdapter(view.getContext(), arrayList);
        gridView.setAdapter(avatarAdapter);
        avatarAdapter.notifyDataSetChanged();
        builder.setView(row);
        AlertDialog dialog = builder.create();
        dialog.show();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ivUser.setImageResource(arrayList.get(i).getImg());

                UpdateAvatar updateAvatar = new UpdateAvatar(getActivity(), user, arrayList.get(i).getImg());
                updateAvatar.execute();

                dialog.dismiss();
            }
        });
    }



    public class UpdateAvatar extends AsyncTask<Void, Void, Void> {

        Activity activity;
        User u;
        int source;

        int image;

        public UpdateAvatar(Activity activity, User u, int source) {
            this.activity = activity;
            this.u = u;
            this.source = source;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            image = getPictureFromSource(source);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String link = url + "/" + u.getId();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, link, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new GsonBuilder().create();
                    User u = gson.fromJson(response, User.class);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("Id", u.getId());
                    params.put("User_Name", u.getName());
                    params.put("Email", u.getEmail());
                    params.put("Password", u.getPass());
                    params.put("Picture", String.valueOf(image));
                    params.put("Status", String.valueOf(u.isStatus()));
                    params.put("Phonenumber", u.getPhone());

                    return params;

                }
            };

            queue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            user.setPicture(image);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onDataPassUser = (OnDataPassUser) context;
    }

    public void sendDataToActivity(User user,String tag) {
        onDataPassUser.onDataPassUser(user,tag);
    }


}