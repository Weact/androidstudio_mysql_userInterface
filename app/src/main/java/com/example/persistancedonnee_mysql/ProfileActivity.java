package com.example.persistancedonnee_mysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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

public class ProfileActivity extends AppCompatActivity {

    Intent profileIntent = null;
    private final String EXTRA_USERNAME = "EXTRA_USERNAME";
    private final String EXTRA_EMAIL = "EXTRA_EMAIL";
    private final String EXTRA_BIRTHDATE = "EXTRA_BIRTHDATE";
    private final String EXTRA_LOCALITY = "EXTRA_LOCALITY";

    //WebView webProfileViewer = null;

    Button searchbtn = null;
    EditText searchtext = null;

    TextView username_label = null;
    TextView email_label = null;
    TextView birthdate_label = null;
    TextView locality_label = null;

    TextView found_username_label = null;
    TextView found_email_label = null;
    TextView found_birthdate_label = null;
    TextView found_locality_label = null;

    RequestQueue requestQueue = null;

    RequestHandler requestHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileIntent = getIntent();

        //webProfileViewer = (WebView)findViewById(R.id.webprofile);

        searchbtn = (Button)findViewById(R.id.btnsearch);
        searchtext = (EditText)findViewById(R.id.searchtext);

        username_label = (TextView)findViewById(R.id.user_username);
        email_label = (TextView)findViewById(R.id.user_email);
        birthdate_label = (TextView)findViewById(R.id.user_birthdate);
        locality_label = (TextView)findViewById(R.id.user_locality);

        found_username_label = (TextView)findViewById(R.id.found_user_username);
        found_email_label = (TextView)findViewById(R.id.found_user_email);
        found_birthdate_label = (TextView)findViewById(R.id.found_user_birthdate);
        found_locality_label = (TextView)findViewById(R.id.found_user_locality);

        if(SharedPreferenceManager.getInstance(this).isLoggedIn()){
            setFields(SharedPreferenceManager.getInstance(this).getUsername(), SharedPreferenceManager.getInstance(this).getUserEmail(), SharedPreferenceManager.getInstance(this).getUserBirthdate(), SharedPreferenceManager.getInstance(this).getUserLocality());
        }else{
            setFields(profileIntent.getStringExtra(EXTRA_USERNAME), profileIntent.getStringExtra(EXTRA_EMAIL), profileIntent.getStringExtra(EXTRA_BIRTHDATE), profileIntent.getStringExtra(EXTRA_LOCALITY));
        }

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser();
            }
        });

        requestQueue = Volley.newRequestQueue(this);

        //setupWebViewer();
    }

    private void setFields(String username, String email, String birthdate, String locality){
        username_label.setText(username);
        email_label.setText(email);
        birthdate_label.setText(birthdate);
        locality_label.setText(locality);
    }

    private void searchUser(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_USERLIST,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("APP DEBUG MESSAGE", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("error").equals("true")){
                        setEmptyFoundFields();
                        Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }

                    found_username_label.setText("Found user's username : " + jsonObject.getString("username"));
                    found_email_label.setText("Found user's email : " + jsonObject.getString("email"));
                    found_birthdate_label.setText("Found user's birthdate : " + jsonObject.getString("birthdate"));
                    found_locality_label.setText("Found user's locality : " + jsonObject.getString("localite"));
                    Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("APP DEBUG MESSAGE", "ERROR RESPONSE");
                        setEmptyFoundFields();
                        Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //return super.getParams();
                Map<String, String> params = new HashMap<>();
                params.put("user_username", searchtext.getText().toString().trim());
                return params;
            }
        };

        requestHandler = RequestHandler.getInstance(this);
        requestHandler.addToRequestQueue(stringRequest);
    }

    private void setEmptyFoundFields(){
        found_username_label.setText("User Not Found, could not fetch username.");
        found_email_label.setText("User Not Found, could not fetch email.");
        found_birthdate_label.setText("User Not Found, could not fetch birthdate.");
        found_locality_label.setText("User Not Found, could not fetch locality.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.mymenu_item_logout:
                Log.d("APP DEBUG","CASE 0 OF onOptionsItemSelected, SHOULD SWITCH ACTIVITY AND GOTO MAIN ACTIVITY");
                finish();
                SharedPreferenceManager.getInstance(this).logout(ProfileActivity.this);
                break;
            case R.id.mymenu_item_settings:
                Log.d("APP DEBUG","CASE 1 OF onOptionsItemSelected, SHOULD OPEN SETTINGS PANNEL");
                Intent gotoSettingsActivity = new Intent(ProfileActivity.this, UserSettingActivity.class);
                startActivity(gotoSettingsActivity);
                break;
            default:
                Log.d("APP DEBUG","CASE DEFAULT OF onOptionsItemSelected");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //WEB

//    private void setupWebViewer(){
//        String loginurl = Constants.URL_LOGIN + "?user_username_login="+profileIntent.getStringExtra(EXTRA_USERNAME)+"&user_password_login="+profileIntent.getStringExtra(EXTRA_PASSWORD);
//        Log.d("APP DEBUG", loginurl);
//        webProfileViewer.loadUrl(loginurl);
//
//        WebSettings settings = webProfileViewer.getSettings();
//        settings.setBuiltInZoomControls(true);
//        settings.setJavaScriptEnabled(true);
//
//        deleteWebCache();
//        clearStorageCache(settings);
//
//    }

//    protected void clearStorageCache(WebSettings s){
//        s.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webProfileViewer.getSettings().setDomStorageEnabled(true);
//    }
//
//    protected void deleteWebCache(){
//        webProfileViewer.clearCache(true);
//    }

}