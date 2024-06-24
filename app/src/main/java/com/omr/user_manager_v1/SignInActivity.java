package com.omr.user_manager_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.omr.user_manager_v1.helpers.StringHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    Button sign_in_btnjava;
    EditText et_email, et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Hook Edit Text Fields
        et_email = findViewById(R.id.emailSignIn);
        et_password = findViewById(R.id.passwordSignIn);

        // Hook Button Field:
        sign_in_btnjava = findViewById(R.id.sign_in_btn);

        // Set Sign İn Button OnClick Listener
        sign_in_btnjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

    }// End of OnClick Method

    // Start of authenticateUser Method:
    public void authenticateUser(){
        if (!validateEmail() || !validatePassword()) {

            Toast.makeText(SignInActivity.this, "E-mail or Password not Valid", Toast.LENGTH_LONG).show();
            return;
        }

        // Instantinate the Reqest Queue
        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        // The Url Posting to:
        String url = "http:192.168.1.143:9080/api/v1/user/login";

        //Set Parameters:
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", et_email.getText().toString());
        params.put("password", et_password.getText().toString());

        // Set Request Object:
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //get Values From Response Object:
                    String first_name = (String) response.get("first_name");
                    String last_name = (String) response.get("last_name");
                    String email = (String) response.get("email");

                    // Set Intent actions:
                    Intent goToProfile = new Intent(SignInActivity.this, ProfileActivity.class);
                    //Pass Values To Profile Activity:
                    goToProfile.putExtra("first_name", first_name);
                    goToProfile.putExtra("last_name", last_name);
                    goToProfile.putExtra("email", email);
                    // Start Activity:
                    startActivity(goToProfile);
                    finish();


                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("VolleyError_in_SignIn", "Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("VolleyError_in_sign","error1" + error.toString());
                Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });// End of Set Request Object:

        queue.add(jsonObjectRequest);


        Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
    }
    // End of authenticateUser Method.






    // Start GoToSignUpAct method:
    public void GoToSignUpAct(View view ) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    // End of GoToSignUpAct method
    public void GoToHome(View view ) {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    // end of GoToHome method


    // Start Validation filds:
    public boolean validateEmail(){
        String em = et_email.getText().toString();

        if (em.isEmpty()) {
            et_email.setError("Email can't be empty");
            return false;
        }else if(!StringHelper.regexEmailValidationPattern(em)){
            et_email.setError("Please enter Valid email");
            return false;
        }
        else{
            et_email.setError(null);
            return true;
        }
    }
    // End of validate Email

    public boolean validatePassword(){
        String psw = et_password.getText().toString();

        if (psw.isEmpty()) {
            et_password.setError("Password can't be empty");
            return false;
        }
        else{
            et_password.setError(null);
            return true;
        }


    }
    // End of validate Password
}