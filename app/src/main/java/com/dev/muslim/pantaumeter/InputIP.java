package com.dev.muslim.pantaumeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.net.URLConnection;

public class InputIP extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_ip);
        Button buttonNext = (Button) findViewById(R.id.btnNext);
        Button buttonCek = (Button) findViewById(R.id.btnCek);
        final TextView txtIp= (TextView)findViewById(R.id.editText);
        buttonCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

//                    URL myUrl = new URL(String.valueOf(txtIp.getText().toString()));
//                    GetConnection.IP= "http://192.168.43.116:8080";
                    URL myUrl = new URL(GetConnection.IP);
                    URLConnection connection = myUrl.openConnection();
                    connection.setConnectTimeout(1000);
                    connection.connect();
                    GetConnection.IP = txtIp.getText().toString();
                    Toast.makeText(getApplicationContext(),"Jaringan Tersambung",Toast.LENGTH_LONG).show();

                    Intent i = new Intent(InputIP.this, Tab.class);
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Jaringan Tidak Tersambung",Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
