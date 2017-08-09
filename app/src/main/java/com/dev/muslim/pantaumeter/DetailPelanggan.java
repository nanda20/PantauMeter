package com.dev.muslim.pantaumeter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


public class DetailPelanggan extends AppCompatActivity {
    private TextView txtIdPelanggan,txtNama,txtAlamat,txtNoTiang,txtLatLong,txtKodeBaca,txtStatus;
    private Button buttonMaps,buttonBack,buttonNext;
    Intent intent=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pelanggan);

        txtIdPelanggan = (TextView)findViewById(R.id.txtIdPelanggan);
        txtNama = (TextView)findViewById(R.id.txtNama);
        txtAlamat = (TextView)findViewById(R.id.txtAlamat);
        txtNoTiang = (TextView)findViewById(R.id.txtNoTiang);
        txtLatLong = (TextView)findViewById(R.id.txtLatLong);
        txtKodeBaca = (TextView)findViewById(R.id.txtKodeBaca);
        buttonMaps = (Button)findViewById(R.id.buttonMaps);
        txtStatus= (TextView)findViewById(R.id.txtStatus);
        buttonBack = (Button)findViewById(R.id.btnBack);
        buttonNext = (Button)findViewById(R.id.btnNext);

        intent= getIntent();
        txtIdPelanggan.setText(String.valueOf(intent.getIntExtra("no_pel",0)));
        txtNama.setText(String.valueOf(intent.getStringExtra("nama")));
        txtAlamat.setText(String.valueOf(intent.getStringExtra("alamat")));
        txtNoTiang.setText(String.valueOf(intent.getStringExtra("no_tiang")));
        txtLatLong.setText(String.valueOf(intent.getStringExtra("lat")+" , "+intent.getStringExtra("long")));
        txtKodeBaca.setText(String.valueOf(intent.getStringExtra("kode_baca")));
        txtStatus.setText(String.valueOf(intent.getStringExtra("status")));

        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat =intent.getStringExtra("lat");
                String lng =intent.getStringExtra("long");
                Log.d("LAT",lat+" , "+lng);
                String uri = "geo:"+lat+","+lng+"?z=17&q="+lat+","+lng;

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DetailPelanggan.this, ListPelanggan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent1= new Intent(getApplicationContext(),InputMeteran.class);
              intent1.putExtra("id1",Integer.valueOf(intent.getIntExtra("id1",0)));
              intent1.putExtra("no_pel",Integer.valueOf(intent.getIntExtra("no_pel",0)));
               intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
              startActivity(intent1);
            }
        });

    }
}
