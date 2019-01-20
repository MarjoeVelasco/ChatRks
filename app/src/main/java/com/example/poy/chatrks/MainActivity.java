package com.example.poy.chatrks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    myDbAdapter helper;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private EditText etPassword;
    EditText ip;
    TextView space;
    Button set,reset,login,forget,register;
    public static String network="";
    public static String username="";
    public static String res="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Reference to variables
        space = (TextView) findViewById(R.id.space);
        etEmail = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        ip = (EditText) findViewById(R.id.cur_net);
        set = (Button) findViewById(R.id.set);
        reset = (Button) findViewById(R.id.reset);
        login = (Button) findViewById(R.id.button);
        forget = (Button) findViewById(R.id.forget);
        register = (Button) findViewById(R.id.register);
        helper = new myDbAdapter(this);
        Cursor dbres = helper.getAllData();


        if(network!=""&&username!=""&&res.equalsIgnoreCase("true"))
        {
            Intent intent = new Intent(MainActivity.this,Menu.class);
            startActivity(intent);
            MainActivity.this.finish();
        }

        if(dbres.getCount() == 0)
        {
            //show message
            Message.message(getApplicationContext(),"No IP address found"+"\n"+"Please enter new one");
            ip.setEnabled(true);
            login.setVisibility(View.GONE);
            etEmail.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
            set.setVisibility(View.VISIBLE);
            ip.setVisibility(View.VISIBLE);
            reset.setVisibility(View.GONE);
            space.setVisibility(View.VISIBLE);
            forget.setVisibility(View.GONE);
            register.setVisibility(View.GONE);

        }
        else
        {
            while (dbres.moveToNext()){

                network = String.format(dbres.getString(1));
            }
            ip.setEnabled(false);
            login.setVisibility(View.VISIBLE);
            etEmail.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            set.setVisibility(View.GONE);
            ip.setVisibility(View.GONE);
            reset.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);
            forget.setVisibility(View.VISIBLE);
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Forget.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network=ip.getText().toString();
                String address=network;
                String aa="a";


                if(network.isEmpty())
                {
                    Message.message(getApplicationContext(), "IP address cannot be empty");
                }
                else
                {

                    long id = helper.insertData(address,aa);
                    if(id<=0)
                    {
                        Message.message(getApplicationContext(),"Unsuccessful");

                    } else
                    {
                        Message.message(getApplicationContext(),"IP address has been set");
                        ip.setEnabled(false);
                        login.setVisibility(View.VISIBLE);
                        etEmail.setVisibility(View.VISIBLE);
                        etPassword.setVisibility(View.VISIBLE);
                        set.setVisibility(View.GONE);
                        ip.setVisibility(View.GONE);
                        reset.setVisibility(View.VISIBLE);
                        register.setVisibility(View.VISIBLE);
                        forget.setVisibility(View.VISIBLE);

                    }



                }

            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aa="a";
                int a= helper.delete(aa);
                if(a<=0)
                {
                    Message.message(getApplicationContext(),"Unsuccessful");
                }
                else
                {
                    Message.message(getApplicationContext(), "IP address has been reset"+"\n"+" Please enter new one");
                    network=null;
                    ip.setEnabled(true);
                    login.setVisibility(View.GONE);
                    etEmail.setVisibility(View.GONE);
                    etPassword.setVisibility(View.GONE);
                    set.setVisibility(View.VISIBLE);
                    ip.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.GONE);
                    register.setVisibility(View.GONE);
                    forget.setVisibility(View.GONE);
                }



            }
        });



    }

    // Triggers when LOGIN Button clicked
    public void checkLogin(View arg0) {

        // Get text from email and passord field
        final String email = etEmail.getText().toString();
        username=email;
        final String password = etPassword.getText().toString();

        // Initialize  AsyncLogin() class with email and password
        new AsyncLogin().execute(email,password);

    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://"+network+"/CHAT_RKS/login_android.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            res=result;

            if(result.equalsIgnoreCase("true"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Message.message(getApplicationContext(), "Welcome "+username+"!");

                Intent intent = new Intent(MainActivity.this,Menu.class);
                startActivity(intent);
                MainActivity.this.finish();

            }
            else if (result.equalsIgnoreCase("disabled"))
            {

                // If username and password does not match display a error message
                Message.message(getApplicationContext(), "Account is disabled"+"\n"+"Please contact your administrator");

            }
            else if (result.equalsIgnoreCase("false"))
            {

                // If username and password does not match display a error message
                Message.message(getApplicationContext(), "Invalid username or password");

            }

            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Message.message(getApplicationContext(), "OOPs! Something went wrong. Connection Problem"+"\n"+"Please check your network");

            }
        }

    }
}
