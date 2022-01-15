package com.example.furniture.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.example.furniture.R;
import com.example.furniture.models.User;
import com.example.furniture.services.Api;
import com.example.furniture.utilities.DialogUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity {

    private static final String TAG = "OTP";
    private PinView pinView;
    private FirebaseAuth mAuth;

    private String codeBySystem = "";

    private Button btnVerify;

    private PhoneAuthProvider.ForceResendingToken mResendToken;


    private LinearLayout layoutResend;

    private TextView textViewCountTimer, tvResendCode, textView_enter_onepass_title, tvBackToEnter;



    private String phone;


    private static final String url = Api.urlLocal + "user";

    private CountDownTimer mCountDownTimer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            textViewCountTimer.setText(millisUntilFinished / 1000 + " s");
        }

        @Override
        public void onFinish() {
            textViewCountTimer.setVisibility(View.GONE);
            pinView.setText("");
            time = false;
            layoutResend.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);


        pinView = findViewById(R.id.pinView);

        mAuth = FirebaseAuth.getInstance();

        layoutResend = findViewById(R.id.layoutResend);
        layoutResend.setVisibility(View.GONE);

        textViewCountTimer = findViewById(R.id.textViewCountTimer);


        // The test phone number and code should be whitelisted in the console.
        String phoneNo = getIntent().getStringExtra("phoneNo");
        phone = getIntent().getStringExtra("phone");

        textView_enter_onepass_title = findViewById(R.id.textView_enter_onepass_title);
        textView_enter_onepass_title.setText("Enter onetime pass send to " + phoneNo);

        tvBackToEnter = findViewById(R.id.tvBackToEnter);
        tvBackToEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToEnterPhone();
            }
        });

        sendVerification(phoneNo,mCountDownTimer);



        btnVerify = findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = pinView.getText().toString();
                if (code != null) {
                    if (time) {
                        verifyCode(code);
                    } else {
                        Toast.makeText(VerifyOtpActivity.this, "OTP out of time", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        tvResendCode = findViewById(R.id.tvResendCode);
        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Sending OTP", Toast.LENGTH_SHORT).show();

                pinView.setText("");




                layoutResend.setVisibility(View.GONE);

                textViewCountTimer.setVisibility(View.VISIBLE);



                sendVerification(phoneNo,mCountDownTimer);



            }
        });
    }

    private boolean time = false;




    private void countTimer(CountDownTimer timerCount) {

        if( !time ){
            time = true;
            pinView.setText("");
            codeBySystem = "";
            timerCount.start();
            textViewCountTimer.setVisibility(View.VISIBLE);
            layoutResend.setVisibility(View.GONE);

        }
        else{
            timerCount.cancel(); // cancel
            pinView.setText("");
            codeBySystem = "";
            timerCount.start();// then restart
            textViewCountTimer.setVisibility(View.VISIBLE);
            layoutResend.setVisibility(View.GONE);

        }


    }

    private void moveToLogin(Dialog dialog) {

        Timer timer = new Timer();
        dialog.show();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                dialog.dismiss();
                Intent intent = new Intent(VerifyOtpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);

    }


    private void moveToEnterPhone() {

        String from = getIntent().getStringExtra("from");
        String country = getIntent().getStringExtra("phoneCountry");
        String user = getIntent().getStringExtra("userName");
        String email = getIntent().getStringExtra("email");
        String pass = getIntent().getStringExtra("pass");

        if (from != null) {
            Intent intent = new Intent(VerifyOtpActivity.this, EnterPhoneActivity.class);
            intent.putExtra("phone", phone);
            intent.putExtra("phoneCountry", country);
            intent.putExtra("fromS", "backToEnter");
            if (from.equals("signup")) {
                intent.putExtra("userName", user);
                intent.putExtra("email", email);
                intent.putExtra("pass", pass);
                intent.putExtra("from", "signup");
            } else if (from.equals("forgot")) {
                intent.putExtra("from", "forgot");
            }
            startActivity(intent);
        } else {
            Intent intent = new Intent(VerifyOtpActivity.this, SignInActivity.class);
            startActivity(intent);
        }

        finish();

    }

    private void moveToNewPassword(Dialog dialog) {
        Timer timer = new Timer();
        dialog.show();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                dialog.dismiss();
                Intent intent = new Intent(VerifyOtpActivity.this, NewPassWordActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    private class SaveToDataBase extends AsyncTask<Void, Void, Void> {

        Context context;

        public SaveToDataBase(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            String userName = getIntent().getStringExtra("userName");
            String email = getIntent().getStringExtra("email");
            String pass = getIntent().getStringExtra("pass");
            String phone = getIntent().getStringExtra("phone");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Id", id);
                    params.put("User_Name", userName);
                    params.put("Email", email);
                    params.put("Password", pass);
                    params.put("Picture", "0");
                    params.put("Status", String.valueOf(true));
                    params.put("Phonenumber", phone);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(VerifyOtpActivity.this);
            queue.add(stringRequest);
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        moveToEnterPhone();
    }


    //    /*for releasse*/
    private void sendVerification(String phoneNumber,CountDownTimer mCountDownTimer) {

        countTimer(mCountDownTimer);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.

            String code = credential.getSmsCode();
            if (code != null) {
                pinView.setText(code);
                verifyCode(code);
            }
            if (credential != null) {
                signInWithPhoneAuthCredential(credential);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            Toast.makeText(VerifyOtpActivity.this, "Something when wrong. Could not send OTP.", Toast.LENGTH_SHORT).show();




            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.


            // Save verification ID and resending token so we can use them later
            codeBySystem = verificationId;

            mResendToken = token;
        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            FirebaseUser user = task.getResult().getUser();
                            // Update UI

                            moveToNextScreen();




                        } else {
                            // Sign in failed, display a message and update the UI

                            Toast.makeText(VerifyOtpActivity.this, "Validation OTP fail.", Toast.LENGTH_SHORT).show();


                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                            }
                        }
                    }
                });
    }


    private void verifyCode(String code) {
        //code (user input)
        //codeBySystem (receive from firebase)
        if (code.isEmpty()) {
            Toast.makeText(VerifyOtpActivity.this, "OTP must have filled.", Toast.LENGTH_SHORT).show();
        } else {
            if (!codeBySystem.isEmpty()) {
                if(time==false){
                    Toast.makeText(VerifyOtpActivity.this, "Out of time", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
                    signInWithPhoneAuthCredential(credential);
                }
            } else {
                Toast.makeText(VerifyOtpActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void moveToNextScreen() {

        String from = getIntent().getStringExtra("from");
        Dialog dialog = DialogUtil.showDialog(VerifyOtpActivity.this,
                R.raw.success, "Success");
        if (from.equals("signup")) {
            textViewCountTimer.setVisibility(View.GONE);
            SaveToDataBase saveToDataBase = new SaveToDataBase(VerifyOtpActivity.this);
            saveToDataBase.execute();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            moveToLogin(dialog);
        } else {
            if (from.equals("forgot")) {
                moveToNewPassword(dialog);
            }
        }

    }


}