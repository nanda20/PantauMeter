package com.dev.muslim.pantaumeter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ListPelanggan extends AppCompatActivity {
    public RecyclerView myRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView.Adapter mAdapter;
    List<DataPojo> arrayList= new ArrayList<>();


    private String URL="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pelanggan);

        myRecyclerView = (RecyclerView)findViewById(R.id.recycleLayout);
        mLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(mLayoutManager);


        loadDatabase();
    }

    public void loadDatabase(){
        final ProgressDialog dialog = new ProgressDialog(ListPelanggan.this);

        dialog.setMessage("Get Data...");
        dialog.setCancelable(false);
        dialog.show();
        final AsyncHttpClient client= new AsyncHttpClient();

//      arrayList.clear();
//        URL="http://10.0.3.2:8080/manajemen_pelanggan/API/pelanggan";
        URL="http://192.168.1.9:8080/manajemen_pelanggan/API/pelanggan";
        client.setConnectTimeout(10000);
        client.get(getApplicationContext(), URL,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    JSONArray jsonArray= response.getJSONArray("hasil");
                    Log.d("hasil"," Count = " + jsonArray.length() + " Status" + statusCode);
                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject object= jsonArray.getJSONObject(i);
                        DataPojo data= new DataPojo(Integer.valueOf(object.getString("id_pel")),Integer.valueOf(object.getString("no_tiang")),object.getString("nama"),object.getString("alamat"),object.getString("lat"),object.getString("long"),object.getString("kode_baca"));
                        arrayList.add(data);
                    }
                    mAdapter= new PelangganAdapter (arrayList,getApplicationContext());
                    myRecyclerView.setAdapter(mAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

        });

    }

    public class PelangganAdapter extends RecyclerView.Adapter<PelangganAdapter.ViewHolder>{
        Context context;
        List<DataPojo> data;
        public PelangganAdapter(List<DataPojo> data, Context context) {
            this.data= data;
            this.context= context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pelanggan_list_item,parent,false);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = myRecyclerView.getChildAdapterPosition(view);
                    if (pos >= 0 && pos < getItemCount()) {
                        Intent intent= new Intent(context,InputMeteran.class);
                        intent.putExtra("no_pel",arrayList.get(pos).getNo_Pel());
                        Log.d("Hasil","Awal - "+ arrayList.get(pos).getNo_Pel());
                        intent.putExtra("no_tiang",arrayList.get(pos).getNo_tiang());
                        intent.putExtra("nama",arrayList.get(pos).getNama());
                        intent.putExtra("alamat",arrayList.get(pos).getAlamat());
                        intent.putExtra("lat",arrayList.get(pos).getLat());
                        intent.putExtra("long",arrayList.get(pos).getLng());
                        intent.putExtra("kode_baca",arrayList.get(pos).getKode_baca());
                        startActivity(intent);
                    }
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DataPojo pl= data.get(position);
            holder.id.setText(String.valueOf(pl.getNo_Pel()));
            holder.nama.setText(String.valueOf(pl.getNama()));

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView id,nama;
            public ViewHolder(View itemView) {
                super(itemView);

                id =(TextView)itemView.findViewById(R.id.id);
                nama=(TextView)itemView.findViewById(R.id.nama);
            }
        }
    }

}
