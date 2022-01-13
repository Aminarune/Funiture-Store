package com.example.furniture.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.models.User;
import com.example.furniture.services.Api;
import com.example.furniture.utilities.DialogUtil;
import com.example.furniture.utilities.NetworkUtil;
import com.example.furniture.utilities.Validate;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPassWordActivity extends AppCompatActivity {

    private String phone;

    private TextInputLayout editNewPass, editNewConfirm;

    private Button btnNewPassSave;

    private RequestQueue queue;

    private static final String url = Api.urlLocal +"user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass_word);

        phone = getIntent().getStringExtra("phone");

        editNewPass = findViewById(R.id.editNewPass);
        editNewConfirm = findViewById(R.id.editNewConfirm);
        btnNewPassSave = findViewById(R.id.btnNewPassSave);

        queue = Volley.newRequestQueue(this);


        btnNewPassSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection(view.getContext(), editNewPass, editNewConfirm);
            }
        });

    }

    private void checkConnection(Context context, TextInputLayout editTextPass, TextInputLayout editTextConfirm) {
        int status = NetworkUtil.getConnectivityStatus(NewPassWordActivity.this);
        if (status == NetworkUtil.TYPE_WIFI || status == NetworkUtil.TYPE_MOBILE) {
            validatePhone(context, editNewPass, editNewConfirm);
        } else {
            String str = "Please check your connection and try again";
            Dialog dialog = DialogUtil.showDialog(context, R.raw.disconnected, str);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void validatePhone(Context context, TextInputLayout editNewPass, TextInputLayout editNewConfirm) {
        String pass = editNewPass.getEditText().getText().toString();
        String confirm = editNewConfirm.getEditText().getText().toString();

        int temp = 0;
        String message = "";

        if (pass.isEmpty()) {
            message += "Please input password";
        } else {

            boolean validatePass = Validate.isValidPassword(pass);

            if (!validatePass) {
                message += " The password must have:\n" +
                        " - At least one number.\n" +
                        " - At least one uppercase and one lowercase character.\n" +
                        " - At least one special symbol (@#$%^&+=%).\n" +
                        " - White spaces don’t allowed.\n" +
                        " - Between 5 to 8 characters long.";
            } else {
                if (!pass.equals(confirm)) {
                    message += "The password not match.";
                } else {
                    temp = 1;
                }
            }
        }

        if (temp == 0) {
            Dialog dialog = DialogUtil.showDialog(context, R.raw.wrong, message);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        } else {
            FindPhoneMatch findPhoneMatch = new FindPhoneMatch(context, phone, pass);
            findPhoneMatch.execute();
        }
    }


    private class FindPhoneMatch extends AsyncTask<Void, Void, Void> {

        Context context;
        String phone;
        String pass;

        public FindPhoneMatch(Context context, String phone, String pass) {
            this.context = context;
            this.phone = phone;
            this.pass = pass;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    for (int i = 0; i < response.length(); i++) {
                        try {

                            JSONObject jsonObject = (JSONObject) response.get(i);

                            String id = jsonObject.getString("Id");
                            String user_name = jsonObject.getString("User_Name");
                            String email = jsonObject.getString("Email");
                            int picture = jsonObject.getInt("Picture");
                            boolean status = jsonObject.getBoolean("Status");
                            String phonenumber = jsonObject.getString("Phonenumber");

                            if (phone.equals(phonenumber)) {

                                User user = new User(id, user_name, email, pass, picture, status, phonenumber);

                                UpdateToDataBase updateToDataBase = new UpdateToDataBase(context, user);
                                updateToDataBase.execute();

                                break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


            queue.add(jsonArrayRequest);
            return null;
        }
    }


    private class UpdateToDataBase extends AsyncTask<Void, Void, Void> {
        String link;
        Context context;
        User u;

        public UpdateToDataBase(Context context, User u) {
            this.context = context;
            this.u = u;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            link = url + "/" + u.getId();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, link, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new GsonBuilder().create();
                    User user = gson.fromJson(response, User.class);
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
                    params.put("Picture", String.valueOf(u.getPicture()));
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
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            moveToLoginScreen();

        }
    }


    private void moveToLoginScreen() {
        Intent intent = new Intent(NewPassWordActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewPassWordActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}



