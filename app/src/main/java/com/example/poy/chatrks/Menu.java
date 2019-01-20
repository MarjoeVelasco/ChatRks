package com.example.poy.chatrks;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;


public class Menu extends AppCompatActivity {

    myDbAdapter helper;
    TextView ip,user;
    Button greenhouse,hydroponic,logout,exit,reset,history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ip = (TextView) findViewById(R.id.cur_net);
        user = (TextView) findViewById(R.id.user);

        greenhouse = (Button)findViewById(R.id.greenhouse);
        hydroponic = (Button)findViewById(R.id.hydroponic);
        reset = (Button)findViewById(R.id.reset);
        history = (Button)findViewById(R.id.history);

        logout = (Button)findViewById(R.id.logout);
        helper = new myDbAdapter(this);
        final Cursor dbres = helper.getAllData();

        ip.setText("Current Network: "+MainActivity.network);
        user.setText("Welcome "+MainActivity.username+" !");

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Histo.class);
                startActivity(intent);
            }
        });

        greenhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Menu.this,Add_record.class);
                startActivity(intent);
                Menu.this.finish();


            }
        });

        hydroponic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Add_record2.class);
                startActivity(intent);
                Menu.this.finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.network="";
                MainActivity.username="";
                Intent intent = new Intent(Menu.this,MainActivity.class);
                startActivity(intent);
                Menu.this.finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this,R.style.MyAlertDialogTheme);
                builder.setTitle("Enter new IP address");

                final EditText input = new EditText(Menu.this);
                input.setTextColor(Color.WHITE);
                while (dbres.moveToNext()){

                    MainActivity.network = String.format(dbres.getString(1));
                }
                input.setText(MainActivity.network);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String address=input.getText().toString();
                        if(address.isEmpty())
                        {
                            Message.message(getApplicationContext(),"Changes not saved");
                        }
                        else
                        {

                        String aa="a";
                        helper.delete(aa);
                        long id = helper.insertData(address,aa);
                        if(id<=0)
                        {
                            Message.message(getApplicationContext(),"Unsuccessful");

                        } else
                        {
                            Message.message(getApplicationContext(),"IP address has been set");
                            MainActivity.network=address;
                            ip.setText("Current Network: "+MainActivity.network);

                        }
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });



    }
    @Override
    public void onBackPressed() {
        Menu.this.finish();
    }



}
