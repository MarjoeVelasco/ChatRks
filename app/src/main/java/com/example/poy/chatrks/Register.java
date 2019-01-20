package com.example.poy.chatrks;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;



public class Register extends AppCompatActivity {



    Button register,confirm,back;
    Spinner q1,q2;
    EditText fullname,username,password,c_password,a1,a2;
    String url="http://"+MainActivity.network+"/CHAT_RKS/android_register.php";
    String fullname2,username2,password2,c_password2;
    String qq1,qq2,aa1,aa2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = (EditText)findViewById(R.id.fullname);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        c_password=(EditText)findViewById(R.id.c_password);
        register = (Button)findViewById(R.id.register);
        confirm=(Button)findViewById(R.id.confirm);
        back=(Button)findViewById(R.id.back);




        q1=(Spinner) findViewById(R.id.q1);
        q2=(Spinner)findViewById(R.id.q2);

        a1=(EditText)findViewById(R.id.a1);
        a2=(EditText)findViewById(R.id.a2);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.array_name4, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        q1.setAdapter(adapter);

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.array_name3, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        q2.setAdapter(adapter2);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname.setVisibility(View.VISIBLE);
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                c_password.setVisibility(View.VISIBLE);

                register.setVisibility(View.VISIBLE);

                q1.setVisibility(View.GONE);
                a1.setVisibility(View.GONE);
                q2.setVisibility(View.GONE);
                a2.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullname2 = fullname.getText().toString();
                username2 = username.getText().toString();
                password2 = password.getText().toString();
                c_password2 = c_password.getText().toString();


                if (fullname2.isEmpty()||username2.isEmpty()||password2.isEmpty()||c_password2.isEmpty()) {
                    Toast.makeText(Register.this, "Please complete form",Toast.LENGTH_SHORT).show();
                }




                else {

                    if(!password2.equals(c_password2))
                    {
                        Toast.makeText(Register.this, "Password does not match",Toast.LENGTH_SHORT).show();

                    }
                    else {

                        fullname.setVisibility(View.GONE);
                        username.setVisibility(View.GONE);
                        password.setVisibility(View.GONE);
                        c_password.setVisibility(View.GONE);
                        register.setVisibility(View.GONE);

                        q1.setVisibility(View.VISIBLE);
                        a1.setVisibility(View.VISIBLE);
                        q2.setVisibility(View.VISIBLE);
                        a2.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.VISIBLE);
                        back.setVisibility(View.VISIBLE);

                    }
                }

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               qq1 = q1.getSelectedItem().toString();
               qq2 = q2.getSelectedItem().toString();
               aa1 = a1.getText().toString();
               aa2 = a2.getText().toString();

                if(aa1.equals("")||aa2.equals(""))
                {
                    Message.message(getApplicationContext(),"Please complete form");
                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equalsIgnoreCase("Username Already Exists!"))
                            {
                                Toast.makeText(Register.this, "" + response, Toast.LENGTH_SHORT).show();
                                Log.i("My success", "" + response);
                            }
                            else if(response.equalsIgnoreCase("Successfully Registered\nPlease wait for confirmation\nfrom the System Administrator"))
                            {
                                Toast.makeText(Register.this, "" + response, Toast.LENGTH_SHORT).show();
                                Log.i("My success", "" + response);

                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);
                                Register.this.finish();
                            }





                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(Register.this, "Failed to connect to Network", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> map = new HashMap<String, String>();
                            map.put("fullname", fullname2);
                            map.put("username", username2);
                            map.put("password", password2);
                            map.put("q1", qq1);
                            map.put("q2", qq2);
                            map.put("a1", aa1);
                            map.put("a2", aa2);
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
        Intent intent = new Intent(Register.this,MainActivity.class);
        startActivity(intent);
        Register.this.finish();



    }

}