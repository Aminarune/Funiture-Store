package com.example.furniture.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.services.Api;
import com.example.furniture.utilities.AlertDialogUtil;
import com.example.furniture.utilities.NetworkUtil;
import com.example.furniture.utilities.Validate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EnterPhoneActivity extends AppCompatActivity {


    private CountryCodePicker countryCodePicker;
    private TextInputEditText editTextPhone;
    private Button btnSend, btnBackToRegister, btnBackToLogin;

    private static final String url = Api.url + "user";

    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);

        countryCodePicker = findViewById(R.id.countryCodePicker);

        editTextPhone = findViewById(R.id.editTextPhone);

        queue = Volley.newRequestQueue(this);

        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callVerifyOtp(view);
            }
        });

    }

    private void moveToLoginScreen() {
        Intent intent = new Intent(EnterPhoneActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToRegisterScreen() {

        String user = getIntent().getStringExtra("userName");
        String email = getIntent().getStringExtra("email");
        String pass = getIntent().getStringExtra("pass");

        Intent intent = new Intent(EnterPhoneActivity.this, SignUpActivity.class);
        intent.putExtra("userName", user);
        intent.putExtra("email", email);
        intent.putExtra("pass", pass);
        intent.putExtra("from", "enter_phone");
        startActivity(intent);
        finish();
    }

    private void callVerifyOtp(View view) {

        String phoneEnter = editTextPhone.getEditableText().toString();

        if (!checkConnection()) {
            String str = "Please check your connection and try again.";
            AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(view.getContext(),
                    R.raw.disconnected, str);
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } else {
            if (phoneEnter.isEmpty()) {
                String str = "Your phone enter phone number.";
                AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(EnterPhoneActivity.this, R.raw.wrong, str);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            } else {
                boolean validPhone = Validate.isValidPhone(phoneEnter);
                if (!validPhone) {
                    String str = "Your phone must have 10 digital number.";
                    AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(EnterPhoneActivity.this, R.raw.wrong, str);
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                } else {
                    String phoneNo = "+" + countryCodePicker.getSelectedCountryCode() + phoneEnter;

                    ValidatePhone validatePhone = new ValidatePhone(phoneEnter, phoneNo);
                    validatePhone.execute();
                }
            }

        }

    }

    private boolean checkConnection() {
        int status = NetworkUtil.getConnectivityStatus(EnterPhoneActivity.this);
        if (status == NetworkUtil.TYPE_WIFI || status == NetworkUtil.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    private class ValidatePhone extends AsyncTask<Void, Void, Void> {

        String editPhone;
        String phoneNo;

        public ValidatePhone(String editPhone, String phoneNo) {
            this.editPhone = editPhone;
            this.phoneNo = phoneNo;
        }

        @Override
        protected Void doInBackground(Void... voids) {


            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    String from = getIntent().getStringExtra("from");

                    int temp = 0;
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = (JSONObject) response.get(i);

                                String num = jsonObject.getString("Phonenumber");


                                if (from.equals("signup") && num.equals(editPhone)) {
                                    AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(EnterPhoneActivity.this,
                                            R.raw.wrong, "This phone number is already registered for another. Try forgot password!");
                                    alertDialog.setCanceledOnTouchOutside(true);
                                    alertDialog.show();
                                } else if (from.equals("signup") && !num.equals(editPhone)) {
                                    fromSignUpToEnterPhoneScreen(phoneNo, editPhone);
                                    break;
                                } else {
                                    fromForgotToEnterPhoneScreen(phoneNo, editPhone);
                                    break;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        //first account
                        if (from.equals("signup")) {
                            fromSignUpToEnterPhoneScreen(phoneNo, editPhone);
                        } else {
                            AlertDialog alertDialog = AlertDialogUtil.showAlertDialog(EnterPhoneActivity.this,
                                    R.raw.wrong, "AAA This phone number is not link with any account. Try register account!");
                            alertDialog.setCanceledOnTouchOutside(true);
                            alertDialog.show();
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

    private void fromSignUpToEnterPhoneScreen(String phoneNo, String phone) {


        String user = getIntent().getStringExtra("userName");
        String email = getIntent().getStringExtra("email");
        String pass = getIntent().getStringExtra("pass");

        Intent intent = new Intent(EnterPhoneActivity.this, VerifyOtpActivity.class);

        intent.putExtra("userName", user);
        intent.putExtra("email", email);
        intent.putExtra("pass", pass);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("phone", phone);
        intent.putExtra("from", "signup");
        startActivity(intent);

        finish();
    }

    private void fromForgotToEnterPhoneScreen(String phoneNo, String phone) {
        Intent intent = new Intent(EnterPhoneActivity.this, VerifyOtpActivity.class);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("phone", phone);
        intent.putExtra("from", "forgot");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        String from = getIntent().getStringExtra("from");
        if (from.equals("signup")) {
            moveToRegisterScreen();
        } else if (from.equals("forgot")) {
            moveToLoginScreen();
        }
    }
}