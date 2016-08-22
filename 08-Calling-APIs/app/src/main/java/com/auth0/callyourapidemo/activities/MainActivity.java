package com.auth0.callyourapidemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.callyourapidemo.R;
import com.auth0.callyourapidemo.application.App;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button mAuthenticatedRequestButton;
    private Button mNonAuthenticatedRequestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthenticatedRequestButton = (Button) findViewById(R.id.authenticatedButton);
        mNonAuthenticatedRequestButton = (Button) findViewById(R.id.nonAuthenticatedButton);


        mAuthenticatedRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateWithIdToken(App.getInstance().getUserCredentials().getIdToken());
            }
        });

        mNonAuthenticatedRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateWithIdToken("");
            }
        });


    }

    /**
     * This method request should work fine, if your server configuration is ok
     * and if you send the proper idToken
     */

    private void authenticateWithIdToken(String idToken) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "your api url"; // TODO Replace this

        AuthorizationRequestObject authorizationRequest = new AuthorizationRequestObject
                (Request.Method.GET, url, idToken,
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Log.i("API CALL SUCCESSFUL", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        Log.i("API CALL FAILED", error.toString());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(authorizationRequest);

    }

    public class AuthorizationRequestObject extends JsonObjectRequest {
        private String mHeaderTokenID = null;

        public AuthorizationRequestObject(int method, String url, String tokenID, JSONObject jsonRequest, Response.Listener listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
            mHeaderTokenID = tokenID;
        }

        @Override
        public Map getHeaders() throws AuthFailureError {
            Map headers = new HashMap();

            if (mHeaderTokenID != null)
                headers.put("Bearer " + mHeaderTokenID, "Authorization");

            return headers;
        }

    }

}
