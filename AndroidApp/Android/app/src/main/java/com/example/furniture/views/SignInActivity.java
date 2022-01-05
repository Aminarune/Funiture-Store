package com.example.furniture.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.models.User;

import com.example.furniture.services.Api;
import com.example.furniture.utilities.DialogUtil;
import com.example.furniture.utilities.NetworkUtil;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class SignInActivity extends AppCompatActivity {


    private TextView tvSignUp;

    private Button btnLogin;

    private TextInputLayout editMailLogin, editPassLogin;

    private TextView tvForgotPass;

    private RequestQueue queue;

    private static final String url = Api.url + "user";

    private AppCompatCheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        queue = Volley.newRequestQueue(this);

        editMailLogin = findViewById(R.id.editMailLogin);
        editPassLogin = findViewById(R.id.editPassLogin);

        checkBox = findViewById(R.id.chkRememberMe);

        loadPreferences(editMailLogin, editPassLogin, checkBox);

        String from = getIntent().getStringExtra("from");

        if (from != null && from.equals("account_logout")) {
            boolean checkRemember = getIntent().getBooleanExtra("checkBoxLogout", false);
            if (checkRemember) {
                checkBox.setChecked(checkRemember);
            } else {
                checkBox.setChecked(checkRemember);
                editMailLogin.getEditText().setText("");
                editPassLogin.getEditText().setText("");
            }
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
        intent.putExtra("from","signin");
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
            dialog = DialogUtil.showDialog(context, R.raw.loading, str);
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
                        Dialog a = DialogUtil.showDialog(SignInActivity.this, R.raw.disconnected,
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
                                    intent.putExtra("userObject", user);

                                    intent.putExtra("checkBox", chk);

                                    if (chk) {
                                        savePreferences(email, pass, chk);
                                    }

                                    break;
                                } else {
                                    temp = 1;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                String str2 = "Some thing wrong please check your information and try again.";
                                Dialog dialog = DialogUtil.showDialog(context, R.raw.disconnected, str2);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}