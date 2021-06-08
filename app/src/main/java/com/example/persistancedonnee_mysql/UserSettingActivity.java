package com.example.persistancedonnee_mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class UserSettingActivity extends AppCompatActivity implements View.OnClickListener{

    //VIEWS
    View mainView = null;

    //TEXTVIEWS
    TextView newUsernameInput = null;
    TextView newPasswordInput = null;
    TextView newEmailInput = null;
    TextView newBirthdateInput = null;
    TextView newLocalityInput = null;

    //BUTTONS
    Button updateButton = null;
    Button deleteUserButton = null;

    //REQUESTS
    RequestQueue requestQueue = null;
    RequestHandler requestHandler = null;

    //VARIABLES
    String baseUsername = "";

    String newUsername = "";
    String newPassword = "";
    String newEmail = "";
    String newBirthdate = "";
    String newLocality = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        //VIEWS SET
        mainView = (View)findViewById(R.id.mainView);

        //TEXTVIEWS SET
        newUsernameInput = (TextView)findViewById(R.id.newUsername_input);
        newPasswordInput = (TextView)findViewById(R.id.newPassword_input);
        newEmailInput = (TextView)findViewById(R.id.newEmail_input);
        newBirthdateInput = (TextView)findViewById(R.id.newBirthdate_input);
        newLocalityInput = (TextView)findViewById(R.id.newLocality_input);

        //BUTTONS SET
        updateButton = (Button)findViewById(R.id.updateButton);
        deleteUserButton = (Button)findViewById(R.id.deleteUserButton);

        //BUTTONS CLICK LISTENER
        updateButton.setOnClickListener((View.OnClickListener) this);
        deleteUserButton.setOnClickListener((View.OnClickListener) this);

        //SHARED PREFERENCES
        baseUsername = SharedPreferenceManager.getInstance(this).getUsername();
    }

    @Override
    public void onClick(View v) {
        if(v == updateButton){
            if(!baseUsername.equals("")){
                newUsername = newUsernameInput.getText().toString().trim();
                newPassword = newPasswordInput.getText().toString().trim();
                newEmail = newEmailInput.getText().toString().trim();
                newBirthdate = newBirthdateInput.getText().toString().trim();
                newLocality = newLocalityInput.getText().toString().trim();

                updateUser(); // UPDATE USER AND BACK TO PROFILE PAGE
            }
        }
        if(v == deleteUserButton){
            if(!baseUsername.equals("")){
                deleteUser(); //DELETE USER AND BACK TO MAIN ACTIVITY
            }
        }
    }

    private void updateUser(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_USERUPDATE,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("APP DEBUG MESSAGE", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("RESPONSE DEBUG MESSAGE : ", response);
                    Toast.makeText(UserSettingActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    SharedPreferenceManager.getInstance(UserSettingActivity.this).logout(UserSettingActivity.this, "USER INFORMATION HAVE BEEN CHANGED, PLEASE LOGIN AGAIN");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("APP DEBUG MESSAGE", "ERROR RESPONSE");
                        Toast.makeText(UserSettingActivity.this, "ERROR RESPONSE", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //return super.getParams();
                Map<String, String> params = new HashMap<>();

                if(!newUsername.equals("")){params.put("user_newUsername", newUsername);}
                if(!newPassword.equals("")){params.put("user_newPassword", newPassword);}
                if(!newEmail.equals("")){params.put("user_newEmail", newEmail);}
                if(!newBirthdate.equals("")){params.put("user_newBirthdate", newBirthdate);}
                if(!newLocality.equals("")){params.put("user_newLocality", newLocality);}
                params.put("user_baseUsername", baseUsername);

                return params;
            }
        };
        requestHandler = RequestHandler.getInstance(this);
        requestHandler.addToRequestQueue(stringRequest);
    }

    private void deleteUser(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_USERDELETE,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("APP DEBUG MESSAGE", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("RESPONSE DEBUG MESSAGE : ", response);
                    Toast.makeText(UserSettingActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    SharedPreferenceManager.getInstance(UserSettingActivity.this).logout(UserSettingActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("APP DEBUG MESSAGE", "ERROR RESPONSE");
                        Toast.makeText(UserSettingActivity.this, "ERROR RESPONSE", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //return super.getParams();
                Map<String, String> params = new HashMap<>();

                params.put("user_username", baseUsername);

                return params;
            }
        };
        requestHandler = RequestHandler.getInstance(this);
        requestHandler.addToRequestQueue(stringRequest);
    }
}