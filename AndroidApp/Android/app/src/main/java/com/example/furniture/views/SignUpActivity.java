package com.example.furniture.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.example.furniture.services.Api;
import com.example.furniture.utilities.DialogUtil;
import com.example.furniture.utilities.NetworkUtil;
import com.example.furniture.utilities.Validate;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SignUpActivity extends AppCompatActivity {


    private Button btnNextAccount;

    private TextInputLayout editUser, editEmail, editPass, editConfirm;

    private TextView tvSignIn;

    private static final String url = Api.url+"user";

    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);




        queue = Volley.newRequestQueue(this);

        editUser = findViewById(R.id.editUserAccount);
        editEmail = findViewById(R.id.editEmailAccount);
        editPass = findViewById(R.id.editPassAccount);
        editConfirm = findViewById(R.id.editConfirmAccount);


        getDataFromEnterPhone();


        tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnNextAccount = findViewById(R.id.btnNextAccount);
        btnNextAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = editUser.getEditText().getText().toString();
                String email = editEmail.getEditText().getText().toString();
                String pass = editPass.getEditText().getText().toString();
                String confirm = editConfirm.getEditText().getText().toString();

                if (checkConnection(view.getContext())) {
                    validate(view, user, email, pass, confirm);
                } else {
                    String str2 = "Please check your connection and try again.";
                    Dialog dialog = DialogUtil.showDialog(view.getContext(), R.raw.disconnected, str2);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }
        });

    }

    private void getDataFromEnterPhone() {

        String user = getIntent().getStringExtra("userName");
        String email = getIntent().getStringExtra("email");
        String pass = getIntent().getStringExtra("pass");

        editUser.getEditText().setText(user);
        editEmail.getEditText().setText(email);
        editPass.getEditText().setText(pass);
        editConfirm.getEditText().setText(pass);

    }

    private boolean checkConnection(Context context) {
        int status = NetworkUtil.getConnectivityStatus(SignUpActivity.this);
        if (status == NetworkUtil.TYPE_WIFI || status == NetworkUtil.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    private void validate(View view, String user, String email, String pass, String confirm) {


        boolean userCheck = Validate.isValidUsername(user);

        boolean emailCheck = Validate.emailValidator(email);

        boolean passwordCheck = Validate.isValidPassword(pass);

        String message = "";

        if (user.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            message += "Please input form.";
        } else if (!userCheck) {
            message += "The username must start:\n" +
                    " - The lowercase or uppercase.\n" +
                    " - Contain 4-20 characters.";
        } else if (!emailCheck) {
            message += "The given email is invalid.";
        } else if (!passwordCheck) {
            message += " The password must have:\n" +
                    " - At least one number.\n" +
                    " - At least one uppercase and one lowercase character.\n" +
                    " - At least one special symbol (@#$%^&+=%).\n" +
                    " - White spaces don’t allowed.\n"+
                    " - Between 5 to 8 characters long.";
        } else if (!pass.equals(confirm)) {
            message += "The password not match.";
        } else {
            CheckMail checkMail=new CheckMail(view, user, email, pass);
            checkMail.execute();
        }

        if (!message.isEmpty()) {
            Dialog dialog = DialogUtil.showDialog(view.getContext(), R.raw.wrong, message);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }

    }



    public class CheckMail extends AsyncTask<Void,Void,Void> {

        View view;
        String user_name;
        String email;
        String password;

        public CheckMail(View view, String user_name, String email, String password) {
            this.view = view;
            this.user_name = user_name;
            this.email = email;
            this.password = password;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    boolean check = false;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) response.get(i);

                            String em = jsonObject.getString("Email");

                            if (email.equals(em)) {
                                check = true;
                                break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (check) {
                        String message = "This email address is registered with another account";
                        Dialog dialog = DialogUtil.showDialog(view.getContext(), R.raw.wrong, message);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    } else {
                        moveToEnterPhone(view, user_name, email, password);
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


    private void moveToEnterPhone(View view, String user_name, String email, String pass) {
        Intent intent = new Intent(SignUpActivity.this, EnterPhoneActivity.class);
        intent.putExtra("userName", user_name);
        intent.putExtra("email", email);
        intent.putExtra("pass", pass);
        intent.putExtra("from","signup");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        String from = getIntent().getStringExtra("from");
        if(from.equals("signin")){
            Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }
        else if(from.equals("enter_phone")){
            Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }
    }
}