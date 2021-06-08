package com.example.persistancedonnee_mysql;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    View mainView = null;

    TextView createUser_title = null;

    TextView createUser_username_label = null;
    TextView createUser_password_label = null;
    TextView createUser_email_label = null;
    TextView createUser_locality_label = null;
    TextView logoutMessage_label = null;

    EditText createUser_username_input = null;
    EditText createUser_password_input = null;
    EditText createUser_email_input = null;
    EditText createUser_birthdate_input = null;
    EditText createUser_locality_input = null;

    Button createUser_btnsubmit = null;

    RequestQueue requestQueue = null;

    RequestHandler requestHandler = null;

    // LOGIN BUTTON
    Button switchToLoginActivity = null;

    //INTENT FOR LOGOUT
    Intent logoutUpdateIntent = null;
    private final String EXTRA_LOGOUTMESSAGE = "EXTRA_LOGOUTMESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPreferenceManager.getInstance(this).isLoggedIn()){
            finish();
            Intent switchToProfileActivity = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(switchToProfileActivity);
        }

        logoutMessage_label = (TextView)findViewById(R.id.logoutMessage_label); //LOGOUT LABEL
        updateLogOutMessage();

        mainView = (View)findViewById(R.id.mainView); //MAIN VIEW
        createUser_title = (TextView)findViewById(R.id.createUser_title); //SCREEN TITLE

        createUser_username_label = (TextView)findViewById(R.id.createUser_username); //USERNAME LABEL
        createUser_password_label = (TextView)findViewById(R.id.createUser_password); //PASSWORD LABEL
        createUser_email_label = (TextView)findViewById(R.id.createUser_email); //EMAIL LABEL
        createUser_locality_label = (TextView)findViewById(R.id.createUser_locality); //LOCALITY LABEL

        createUser_username_input = (EditText) findViewById(R.id.createUser_username_input); //USERNAME INPUT
        createUser_password_input = (EditText)findViewById(R.id.createUser_password_input); //PASSWORD INPUT
        createUser_email_input = (EditText)findViewById(R.id.createUser_email_input); //EMAIL INPUT
        createUser_birthdate_input = (EditText)findViewById(R.id.createUser_birthdate_input); //BIRTHDATE INPUT
        createUser_locality_input = (EditText)findViewById(R.id.createUser_localite_input); //LOCALITY INPUT

        createUser_btnsubmit = (Button)findViewById(R.id.createUser_btnsubmit); //BTN REGISTER

        switchToLoginActivity = (Button)findViewById(R.id.loginActivitySwitch); //BUTTON TO SWITCH TO USER LOGIN ACTIVITY

        createUser_btnsubmit.setOnClickListener((View.OnClickListener) this); //BTN REGISTER ON CLICK LISTENER

        switchToLoginActivity.setOnClickListener((View.OnClickListener) this);

        requestQueue = Volley.newRequestQueue(this);

    }

    StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_REGISTER,new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {
            Log.d("APP DEBUG MESSAGE", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                //Log.d("APPMSG",response);

                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("APP DEBUG MESSAGE", "ERROR RESPONSE" + error.getMessage());
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            //return super.getParams();
            Map<String, String> params = new HashMap<>();
            params.put("user_username", createUser_username_input.getText().toString().trim());
            params.put("user_password", createUser_password_input.getText().toString().trim());
            params.put("user_email", createUser_email_input.getText().toString().trim());
            params.put("user_birthdate", createUser_birthdate_input.getText().toString().trim());
            params.put("user_localite", createUser_locality_input.getText().toString().trim());
            return params;
        }
    };

    private void registerUser(){
        Toast.makeText(MainActivity.this, "User is being created...", Toast.LENGTH_LONG).show();

        requestHandler = RequestHandler.getInstance(this);
        requestHandler.addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == createUser_btnsubmit){
            registerUser();
        }
        if(v == switchToLoginActivity){
            intentSubmit(); //SWITCH ACTIVITY
        }
    }


    private void intentSubmit(){
        Intent submitIntentAndSwitchActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(submitIntentAndSwitchActivity);
        finish();
    }

    private void updateLogOutMessage(){
        logoutUpdateIntent = getIntent();
        if(logoutUpdateIntent != null){
            if(logoutUpdateIntent.hasExtra(EXTRA_LOGOUTMESSAGE)){
                if(!logoutUpdateIntent.getStringExtra(EXTRA_LOGOUTMESSAGE).equals("")){
                    logoutMessage_label.setVisibility(View.VISIBLE);
                    logoutMessage_label.setText(logoutUpdateIntent.getStringExtra(EXTRA_LOGOUTMESSAGE));
                }
            }
        }
    }
}