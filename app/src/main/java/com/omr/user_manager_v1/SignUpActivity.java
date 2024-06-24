package com.omr.user_manager_v1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.omr.user_manager_v1.helpers.StringHelper;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText first_name, last_name, password, confirm, email;
    Button sign_up_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Hook Edit Text Fields
        first_name = findViewById(R.id.first_nameSignUp);
        last_name = findViewById(R.id.last_nameSignUp);
        password = findViewById(R.id.passwordSignUp);
        confirm = findViewById(R.id.confirmSignUp);
        email = findViewById(R.id.emailSignUp);


        // Hook Sign Up Button
        sign_up_btn = findViewById(R.id.sign_up_btn);

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proccesFromField();
            }
        });

    }
    // End of onCreate Method

    public void GoToSignInAct(View view ) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void GoToHome(View view ) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void proccesFromField(){
        if (!validateFirstName() || !validateLastName() || !validateEmail() || !validatePasswordAndValidateConfirm()) {

         Toast.makeText(SignUpActivity.this,"Registration UnSuccessful", Toast.LENGTH_LONG).show();
         return;
        }
        // girilen kayıtların düzgünülüğünün kontroli BİTTİ

        // Instantinate the Reqest Queue
        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
        // The Url Posting to:
        String url = "http:192.168.1.143:9080/api/v1/user/register";

        // String request objekt:
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("success")){
                    first_name.setText(null);
                    last_name.setText(null);
                    email.setText(null);
                    password.setText(null);
                    confirm.setText(null);

                    Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                }
                // End Of Response if block


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error.getMessage());
                Log.d("VolleyError", "Error: " + error.toString());
                Toast.makeText(SignUpActivity.this, "Registration Unsuccessful", Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", first_name.getText().toString());
                params.put("last_name", last_name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());



                return params;
            }
        }; // End of String request objekct.

        // Zaman aşımı süresini ayarla
        int socketTimeout = 30000; // 30 saniye
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        queue.add(stringRequest);



    }
    // End of Procces control from fields

    // Validations
    public boolean validateFirstName(){
        String firstName = first_name.getText().toString();

        if (firstName.isEmpty()) {
            first_name.setError("First name can\'t be empty");
            return false;
        }else {
            first_name.setError(null);
            return true;
        }
    }
    // End of validate First Name

    public boolean validateLastName(){
        String lastName = last_name.getText().toString();

        if (lastName.isEmpty()) {
            last_name.setError("Last name can\'t be empty");
            return false;
        }else {
            last_name.setError(null);
            return true;
        }
    }
    // End of validate Last Name

    public boolean validateEmail(){
        String em = email.getText().toString();

        if (em.isEmpty()) {
            email.setError("Email can't be empty");
            return false;
        }else if(!StringHelper.regexEmailValidationPattern(em)){
            email.setError("Please enter Valid email");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }
    // End of validate Email

    public boolean validatePasswordAndValidateConfirm(){
        String psw = password.getText().toString();
        String conf = confirm.getText().toString();
        if (psw.isEmpty() || conf.isEmpty()) {
            password.setError("Password can't be empty");
            confirm.setError("First name can\'t be empty");
            return false;
        }
        else if (!psw.equals(conf)) {
            password.setError("Password do not match");
            return false;
        }

        else{
            password.setError(null);
            confirm.setError(null);
            return true;
        }


    }
    // End of validate Password// End of validate Confirm



}