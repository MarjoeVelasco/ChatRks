package com.example.poy.chatrks;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Histo extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    private ListView mainListView ;
    private ListView mainListView2 ;
    private ArrayAdapter<String> listAdapter ;
    private ArrayAdapter<String> listAdapter2 ;
    Button btn1,btn2;
    TextView pane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histo);

        mainListView = (ListView) findViewById( R.id.mainListView );
        mainListView2 = (ListView) findViewById( R.id.mainListView2 );
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        pane=(TextView)findViewById(R.id.pane);


        final ArrayList<String> planetList = new ArrayList<String>();
        final ArrayList<String> planetList2 = new ArrayList<String>();


        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, planetList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };

        listAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, planetList2){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };


        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.

        String user=MainActivity.username;

        new AsyncLogin().execute(user);
        listAdapter.notifyDataSetChanged();
        mainListView.setAdapter( listAdapter );

        new AsyncLogin2().execute(user);
        listAdapter2.notifyDataSetChanged();
        mainListView2.setAdapter( listAdapter2 );



        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String val = planetList.get(position);

                String temp=val;
                String temp1=temp.replace(")","");
                String temp2=temp1.replaceAll("Date","");
                String temp3=temp2.replace("(","");

                String[] in=temp3.split(":");
                String val4=in[0];

                Intent intent = new Intent(Histo.this,View_edit1.class);
                intent.putExtra("id", val4);
                startActivity(intent);
                Histo.this.finish();
            }
        });

        mainListView2.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String val2 = planetList2.get(position);

                String temp=val2;
                String temp1=temp.replace(")","");
                String temp2=temp1.replaceAll("Date","");
                String temp3=temp2.replace("(","");

                String[] in=temp3.split(":");
                String val3=in[0];

                Intent intent = new Intent(Histo.this,View_edit2.class);
                intent.putExtra("id", val3);
                startActivity(intent);
                Histo.this.finish();

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pane.setText("GREENHOUSE ENVIRONMENT RECORDS");
                mainListView.setVisibility(View.VISIBLE);
                mainListView2.setVisibility(View.GONE);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pane.setText("HYDROPONIC SOLUTION RECORDS");
                mainListView.setVisibility(View.GONE);
                mainListView2.setVisibility(View.VISIBLE);
            }
        });


    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Histo.this);
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
                url = new URL("http://"+MainActivity.network+"/CHAT_RKS/android_show_greenhouse.php");

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
                        .appendQueryParameter("username", params[0]);
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
            String temp=result;
            if(result.equalsIgnoreCase("na"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
            }
            else {


                String temp0 = temp.replace('"', '=');
                String temp1 = temp0.replace("[", "");
                String temp2 = temp1.replace("]", "");
                String temp3 = temp2.replace("{", "");
                String temp4 = temp3.replace(":", "");
                String temp5 = temp4.replace("gip_id", "");
                String temp6 = temp5.replace("date_record", "");
                String temp7 = temp6.replace("}", "");
                String temp8 = temp7.replaceAll("=", "");


                String[] out = temp8.split(",");

                int i = 0;
                for (i = 0; i < out.length; i++) {
                    listAdapter.add(out[i] + "(Date:" + out[i + 1] + ")");
                    i++;
                }

            }






        }

    }

    private class AsyncLogin2 extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Histo.this);
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
                url = new URL("http://"+MainActivity.network+"/CHAT_RKS/android_show_hydroponic.php");

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
                        .appendQueryParameter("username", params[0]);
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
            String temp=result;
            if(result.equalsIgnoreCase("na"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
            }
            else {


                String temp0 = temp.replace('"', '=');
                String temp1 = temp0.replace("[", "");
                String temp2 = temp1.replace("]", "");
                String temp3 = temp2.replace("{", "");
                String temp4 = temp3.replace(":", "");
                String temp5 = temp4.replace("gip_id", "");
                String temp6 = temp5.replace("date_record", "");
                String temp7 = temp6.replace("}", "");
                String temp8 = temp7.replaceAll("=", "");


                String[] out = temp8.split(",");

                int i = 0;
                for (i = 0; i < out.length; i++) {
                    listAdapter2.add(out[i] + "(Date:" + out[i + 1] + ")");
                    i++;
                }

            }





        }

    }


}

