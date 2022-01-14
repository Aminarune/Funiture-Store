package com.example.furniture.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class SignInActivity extends AppCompatActivity {


    private TextView tvSignUp;

    private Button btnLogin;

    private TextInputLayout editMailLogin, editPassLogin;

    private TextView tvForgotPass;

    private RequestQueue queue;

    private static final String url = Api.urlLocal + "user";

    private AppCompatCheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        queue = Volley.newRequestQueue(this);

        editMailLogin = findViewById(R.id.editMailLogin);
        editPassLogin = findViewById(R.id.editPassLogin);

        checkBox = findViewById(R.id.chkRememberMe);

        String from = getIntent().getStringExtra("from");

        if (from != null && from.equals("account_logout")) {
            loadPreferences(editMailLogin,editPassLogin,checkBox);
        }
        else {
            loadPreferences(editMailLogin,editPassLogin,checkBox);
        }

        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSignUpScreen();
            }
        });


        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editMailLogin.getEditText().getText().toString();
                String pass = editPassLogin.getEditText().getText().toString();

                checkConnection(view.getContext(), email, pass);
            }
        });


        tvForgotPass = findViewById(R.id.tvForgotPass);
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToEnterNumberScreen();
            }
        });


    }

    private void savePreferences(String email, String pass, boolean check) {
        SharedPreferences sharedPreferences = getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("pass", pass);
        editor.putBoolean("check", check);
        editor.commit();
    }

    private void removePreferences(String email, String pass, boolean chk) {
        SharedPreferences preferences = getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("pass");
        editor.remove("check");
        editor.commit();
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

    private void loadPreferences(TextInputLayout editMailLogin, TextInputLayout editPassLogin, AppCompatCheckBox checkBox) {
        SharedPreferences sharedPreferences = getSharedPreferences("caches", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String pass = sharedPreferences.getString("pass", "");
        Boolean check = sharedPreferences.getBoolean("check", false);

        if (check == false) {
            editMailLogin.getEditText().setText("");
            editPassLogin.getEditText().setText("");
            checkBox.setChecked(check);
        } else {
            editMailLogin.getEditText().setText(email);
            editPassLogin.getEditText().setText(pass);
            checkBox.setChecked(check);
        }

    }

    private void moveToSignUpScreen() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        intent.putExtra("from", "signin");
        startActivity(intent);
        finish();
    }

    private void moveToEnterNumberScreen() {
        Intent intent = new Intent(SignInActivity.this, EnterPhoneActivity.class);
        intent.putExtra("from", "forgot");
        startActivity(intent);
        finish();
    }


    private void checkConnection(Context context, String editEmail, String editPass) {

        int status = NetworkUtil.getConnectivityStatus(SignInActivity.this);
        if (status == NetworkUtil.TYPE_WIFI || status == NetworkUtil.TYPE_MOBILE) {

            if (editEmail.trim().length() == 0 || editPass.trim().length() == 0) {
                String str = "Your email or password must not empty.";
                Dialog dialog = DialogUtil.showDialog(context, R.raw.wrong, str);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            } else {
                LoginUserDatabase loginUserDatabase = new LoginUserDatabase(context, editEmail, editPass);
                loginUserDatabase.execute();
            }
        } else {
            String str = "Please check your connection and try again.";
            Dialog dialog = DialogUtil.showDialog(context, R.raw.disconnected, str);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }


    public class LoginUserDatabase extends AsyncTask<Void, Void, Void> {

        Context context;
        String editEmail;
        String editPass;

        Dialog dialog;

        Intent intent;

        public LoginUserDatabase(Context context, String editEmail, String editPass) {
            this.context = context;
            this.editEmail = editEmail;
            this.editPass = editPass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intent = new Intent(SignInActivity.this, MainActivity.class);
            String str = "Please wait a moment.";
            dialog = DialogUtil.showDialog(context, R.raw.waiting, str);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    int temp = 0;//s=0 success
                    if (response.length() == 0) {
                        dialog.dismiss();
                        Dialog a = DialogUtil.showDialog(SignInActivity.this, R.raw.wrong,
                                "Account is not exist.\nTry to register a new account.");
                        a.setCanceledOnTouchOutside(true);
                        a.show();
                    } else {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = (JSONObject) response.get(i);

                                String id = jsonObject.getString("Id");
                                String user_name = jsonObject.getString("User_Name");
                                String email = jsonObject.getString("Email");
                                String pass = jsonObject.getString("Password");
                                int picture = jsonObject.getInt("Picture");
                                boolean status = jsonObject.getBoolean("Status");
                                String phone = jsonObject.getString("Phonenumber");

                                if (email.equals(editEmail) && pass.equals(editPass)) {

                                    temp = 0;

                                    User user = new User(id, user_name, email, pass, picture, status, phone);

                                    boolean chk = checkBox.isChecked();
                                    intent.putExtra("user", user);

                                    intent.putExtra("checkBox", chk);

                                    if (chk) {
                                        savePreferences(email, pass, chk);
                                    } else {
                                        removePreferences(email, pass, chk);
                                    }

                                    getToken(user);

                                    break;
                                } else {
                                    temp = 1;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                String str2 = "Some thing wrong please check your information and try again.";
                                Dialog dialog = DialogUtil.showDialog(context, R.raw.wrong, str2);
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.show();
                            }

                        }
                    }
                    if (temp == 1) {

                        dialog.dismiss();
                        String str = "Email or password is incorrect!";
                        Dialog b = DialogUtil.showDialog(context, R.raw.wrong, str);
                        b.setCanceledOnTouchOutside(true);
                        b.show();

                    } else {
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                timer.cancel();
                                dialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        }, 1500);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    String str = "Some thing wrong please try again.";
                    Dialog c = DialogUtil.showDialog(context, R.raw.disconnected, str);
                    c.setCanceledOnTouchOutside(true);
                    c.show();
                }
            });

            queue.add(jsonArrayRequest);
            return null;
        }

    }


    private void getToken(User user) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        UpdateToken updateToken = new UpdateToken(token,user);
                        updateToken.execute();
                    }
                });
    }

    class UpdateToken extends AsyncTask<Void, Void, Void> {

        String token;

        User user;

        public UpdateToken(String token, User user) {
            this.token = token;
            this.user = user;
        }

        private String link = Api.urlLocal + "user";

        @Override
        protected Void doInBackground(Void... voids) {

            String url = link + "/" + user.getId();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson=new GsonBuilder().create();
                    User user=gson.fromJson(response,User.class);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params=new HashMap<>();
                    params.put("Id",user.getId());
                    params.put("User_Name",user.getName());
                    params.put("Email",user.getEmail());
                    params.put("Password",user.getPass());
                    params.put("Picture", String.valueOf(user.getPicture()));
                    params.put("Status", String.valueOf(user.isStatus()));
                    params.put("Phonenumber",user.getPhone());
                    params.put("Token",token);
                    return params;
                }
            };

            queue.add(stringRequest);


            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}