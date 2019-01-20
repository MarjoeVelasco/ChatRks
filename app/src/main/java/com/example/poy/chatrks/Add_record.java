package com.example.poy.chatrks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.Map;
public class Add_record extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    Button time,date,send,clear;
    EditText observer,t_ins1,t_ins2,t_out1,t_out2,h_ins,h_out;
    TextView get_time,get_date;
    Spinner crop;

    private ListView mainListView ;
    ArrayAdapter<String> spinnerArrayAdapter;

    String url="http://"+MainActivity.network+"/CHAT_RKS/android_add_environment.php";
    public static String time3,date3,observer3,greenhouse3,crop3,t_ins3,t_ins4,t_out3,t_out4,h_ins3,h_out3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        time=(Button) findViewById(R.id.btn_time);
        date= (Button)findViewById(R.id.btn_date);
        send= (Button)findViewById(R.id.send);
        clear= (Button)findViewById(R.id.clear);

        get_date = (TextView)findViewById(R.id.in_date);
        get_time = (TextView)findViewById(R.id.in_time);

        observer = (EditText)findViewById(R.id.observer);

        crop = (Spinner) findViewById(R.id.spinner);

        t_ins1 = (EditText)findViewById(R.id.t_ins1);
        t_ins2 = (EditText)findViewById(R.id.t_ins2);
        t_out1 = (EditText)findViewById(R.id.t_out1);
        t_out2 = (EditText)findViewById(R.id.t_out2);
        h_ins = (EditText)findViewById(R.id.h_ins);
        h_out = (EditText)findViewById(R.id.h_out);

        observer.setText(MainActivity.username);
        observer.setEnabled(false);

        get_time.setText(time3);
        get_date.setText(date3);

        t_ins1.setText(t_ins3);
        t_ins2.setText(t_ins4);
        t_out1.setText(t_out3);
        t_out2.setText(t_out4);
        h_ins.setText(h_ins3);
        h_out.setText(h_out3);



        final ArrayList<String> planetList = new ArrayList<String>();

       spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,planetList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        crop.setAdapter(spinnerArrayAdapter);

        String user=MainActivity.username;
        new AsyncLogin().execute(user);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"Date Picker");
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_date.setText(null);
                get_time.setText(null);

                crop.post(new Runnable() {
                    @Override
                    public void run() {
                        crop.setSelection(0);
                    }
                });

                t_ins1.setText(null);
                t_ins2.setText(null);

                t_out1.setText(null);
                t_out2.setText(null);

                h_ins.setText(null);
                h_out.setText(null);
                time3 = null;
                date3 = null;
                greenhouse3 = null;

                t_ins3 = null;
                t_ins4 = null;
                t_out3 = null;
                t_out4 = null;
                h_ins3 =null;
                h_out3 = null;
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String time = get_time.getText().toString();
                final String date = get_date.getText().toString();
                final String observer1 = observer.getText().toString();

                final String t_ins11 = t_ins1.getText().toString();
                final String t_ins22 = t_ins2.getText().toString();
                final String t_out11 = t_out1.getText().toString();
                final String t_out22 = t_out2.getText().toString();
                final String h_ins1 = h_ins.getText().toString();
                final String h_out1 = h_out.getText().toString();

                String temp =crop.getSelectedItem().toString();



                if (time.isEmpty()||temp.equals("Please Select Crop")||temp.equals("No crop data found")||date.isEmpty()||observer1.isEmpty()||t_ins11.isEmpty()||t_ins22.isEmpty()||t_out11.isEmpty()||t_out22.isEmpty()||h_ins1.isEmpty()||h_out1.isEmpty()) {
                    Toast.makeText(Add_record.this, "Please complete form",Toast.LENGTH_SHORT).show();

                }
                else {

                    String temp1=temp.replace(")","");
                    String temp2=temp1.replaceAll("Greenhouse_no","");
                    String temp3=temp2.replaceAll("-","");
                    String temp4=temp3.replace("(","");

                    String[] in=temp4.split(":");
                    final String crop2 = in[0];
                    final String greenhouse2 = in[1];

                    RequestQueue queue = Volley.newRequestQueue(Add_record.this);
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(Add_record.this, "" + response, Toast.LENGTH_SHORT).show();
                            Log.i("My success", "" + response);
                            time3 = null;
                            date3 = null;

                            t_ins3 = null;
                            t_ins4 = null;
                            t_out3 = null;
                            t_out4 = null;
                            h_ins3 =null;
                            h_out3 = null;


                            Intent intent = new Intent(Add_record.this, Menu.class);
                            startActivity(intent);
                            Add_record.this.finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(Add_record.this, "Failed to connect to Network", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> map = new HashMap<String, String>();
                            map.put("date", date);
                            map.put("time", time);
                            map.put("crop", crop2);
                            map.put("greenhouse", greenhouse2);
                            map.put("observer", observer1);


                            map.put("t_ins1", t_ins11);
                            map.put("t_ins2", t_ins22);

                            map.put("t_out1", t_out11);
                            map.put("t_out2", t_out22);

                            map.put("h_ins", h_ins1);
                            map.put("h_outs", h_out1);


                            return map;
                        }
                    };
                    queue.add(request);
                }

            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Add_record.this,Menu.class);
        startActivity(intent);
        Add_record.this.finish();
         time3 = get_time.getText().toString();
         date3 = get_date.getText().toString();

         t_ins3 = t_ins1.getText().toString();
         t_ins4 = t_ins2.getText().toString();
         t_out3 = t_out1.getText().toString();
         t_out4 = t_out2.getText().toString();
         h_ins3 = h_ins.getText().toString();
         h_out3 = h_out.getText().toString();


    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Add_record.this);
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
                url = new URL("http://"+MainActivity.network+"/CHAT_RKS/android_show_crop.php");

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
                spinnerArrayAdapter.add("No crop data found");
                Message.message(getApplicationContext(), "No crop data found\nPlease Contact your System administrator");


            }
            else
            {
                String temp0=temp.replace('"','=');
                String temp1 = temp0.replace("[","");
                String temp2=temp1.replace("]","");
                String temp3=temp2.replace("{","");


                String temp4=temp3.replace("}","");
                String temp5=temp4.replaceAll("=","");
                String temp6=temp5.replaceAll("greenhouse_no:","");
                String temp7=temp6.replaceAll("name:","");
                String temp8=temp7.replaceAll("start_activity:","");
                String temp9=temp8.replaceAll("last_activity:","");

                String[] out = temp9.split(",");

                spinnerArrayAdapter.add("Please Select Crop");
                int i=0;
                for(i=0;i<out.length;i++) {
                    spinnerArrayAdapter.add(out[i] + "(Greenhouse_no:" + out[i+1]+")"+ ":("+ out[i+2]+" - " + out[i+3]+")");
                    i++;
                    i++;
                    i++;
                }



                spinnerArrayAdapter.notifyDataSetChanged();


            }







        }

    }





}
