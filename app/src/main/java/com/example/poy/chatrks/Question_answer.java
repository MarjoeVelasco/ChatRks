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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Question_answer extends AppCompatActivity {

    myDbAdapter helper;
    TextView q1,q2;
    EditText a1,a2;
    Button confirm;

    TextView instruct,instruct2;
    EditText pass1,pass2;
    Button reset_pass;

    String u_a1="";
    String u_a2="";

    String url="http://"+MainActivity.network+"/CHAT_RKS/android_change_pass.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        instruct = (TextView) findViewById(R.id.instruct);
        q1 = (TextView)findViewById(R.id.q1);
        q2 = (TextView)findViewById(R.id.q2);
        a1 = (EditText)findViewById(R.id.a1);
        a2 = (EditText)findViewById(R.id.username);
        confirm = (Button)findViewById(R.id.btn);

        pass1= (EditText) findViewById(R.id.pass1);
        pass2=(EditText)findViewById(R.id.pass2);
        reset_pass=(Button)findViewById(R.id.reset_pass);

        instruct2=(TextView)findViewById(R.id.instruct2);

        q1.setText(Forget.qq1);
        q2.setText(Forget.qq2);

        u_a1=Forget.aa1;
        u_a2=Forget.aa2;



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input1,input2;
                input1=a1.getText().toString();
                input2=a2.getText().toString();

                if(input1.equalsIgnoreCase(u_a1)&&input2.equalsIgnoreCase(u_a2))
                {
                  Message.message(getApplicationContext(),"Success!\nYou can now change reset your password");
                  instruct.setVisibility(View.GONE);
                    q1.setVisibility(View.GONE);
                    q2.setVisibility(View.GONE);
                    a1.setVisibility(View.GONE);
                    a2.setVisibility(View.GONE);
                    confirm.setVisibility(View.GONE);

                    instruct2.setVisibility(View.VISIBLE);
                    pass1.setVisibility(View.VISIBLE);
                    pass2.setVisibility(View.VISIBLE);
                    reset_pass.setVisibility(View.VISIBLE);

                }
                else
                {
                    Message.message(getApplicationContext(),"Incorrect Answers!\nPlease try again");
                }

            }
        });

        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String pp1 = pass1.getText().toString();
                final String pp2 = pass2.getText().toString();
                final String u_user = Forget.user;


                if (pp1.equals("") || pp2.equals("")) {
                    Message.message(getApplicationContext(), "Please complete form");
                } else {
                    if (!pp1.equals(pp2)) {
                        Message.message(getApplicationContext(), "Passwords does not match");
                    } else if (pp1.equals(pp2)) {

                        RequestQueue queue = Volley.newRequestQueue(Question_answer.this);
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(Question_answer.this, "" + response, Toast.LENGTH_SHORT).show();
                                Log.i("My success", "" + response);

                                Intent intent = new Intent(Question_answer.this, MainActivity.class);
                                startActivity(intent);
                                Question_answer.this.finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                                Toast.makeText(Question_answer.this, "Failed to connect to Network", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> map = new HashMap<String, String>();
                                map.put("username", u_user);
                                map.put("password", pp1);


                                return map;
                            }
                        };
                        queue.add(request);

                    }

                }
            }
        });

    }
    @Override

    public void onBackPressed() {
        Intent intent = new Intent(Question_answer.this,MainActivity.class);
        startActivity(intent);
        Question_answer.this.finish();



    }
}
