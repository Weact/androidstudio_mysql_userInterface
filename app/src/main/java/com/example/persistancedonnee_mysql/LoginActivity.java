package com.example.persistancedonnee_mysql;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    View mainView = null;

    TextView user_username_login_label = null;
    TextView user_password_login_label = null;

    EditText user_username_login_input = null;
    EditText user_password_login_input = null;

    Button loginUser = null;

    //v1 non opti : ligne 121 pareil
    RequestQueue requestQueue = null;

    //v2 opti http://developer.android.com/training/volley/requestqueue.html
    RequestHandler requestHandler = null;

    //SHAREDPREFERENCES
    SharedPreferenceManager mySharedPreferencesManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainView = (View)findViewById(R.id.mainView);
        user_username_login_label = (TextView)findViewById(R.id.user_username_login_laebel);
        user_password_login_label = (TextView)findViewById(R.id.user_password_login_label);
        user_username_login_input = (EditText) findViewById(R.id.user_username_login);
        user_password_login_input = (EditText)findViewById(R.id.user_password_login);
        loginUser = (Button)findViewById(R.id.loginscreen_loginbutton);

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mySharedPreferencesManager = SharedPreferenceManager.getInstance(this);
    }

    private void login(){
        String user_username_login = user_username_login_input.getText().toString().trim();
        String user_password_login = user_password_login_input.getText().toString().trim();

        if(!user_username_login.equals("") && !user_password_login.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.i("APP DEBUG MSG", response);
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        String username = jsonObject.getString("username");
                        String email = jsonObject.getString("email");
                        String birthdate = jsonObject.getString("birthdate");
                        String locality = jsonObject.getString("localite");

                        switchActivityAndSubmitIntent(username, email, birthdate, locality);
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //return super.getParams();
                    Map<String, String> params = new HashMap<>();
                    params.put("user_username_login", user_username_login);
                    params.put("user_password_login", user_password_login);
                    Log.d("DEBUG MESSAGE", user_username_login);
                    Log.d("DEBUG MESSAGE", user_password_login);
                    return params;
                }
            };

            //v1 non opti
            //requestQueue = Volley.newRequestQueue(getApplicationContext());
            //requestQueue.add(stringRequest);

            //v2 http://developer.android.com/training/volley/requestqueue.html
            requestHandler = RequestHandler.getInstance(this);
            requestHandler.addToRequestQueue(stringRequest);
        }else {
            Toast.makeText(LoginActivity.this, "Some fields are empty, please fill them and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchActivityAndSubmitIntent(String username, String email, String birthdate, String locality){
        Intent submitLoginAndSwitchActivityProfile = new Intent(LoginActivity.this, ProfileActivity.class);
        submitLoginAndSwitchActivityProfile.putExtra("EXTRA_USERNAME", username);
        submitLoginAndSwitchActivityProfile.putExtra("EXTRA_EMAIL", email);
        submitLoginAndSwitchActivityProfile.putExtra("EXTRA_BIRTHDATE", birthdate);
        submitLoginAndSwitchActivityProfile.putExtra("EXTRA_LOCALITY", locality);


        //Write shared preferences
        mySharedPreferencesManager.userLogin(username, email, birthdate, locality);

        startActivity(submitLoginAndSwitchActivityProfile);
        finish();
    }

}