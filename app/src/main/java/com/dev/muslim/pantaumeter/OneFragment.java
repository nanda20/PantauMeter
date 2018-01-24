package com.dev.muslim.pantaumeter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class OneFragment extends Fragment implements View.OnClickListener {

    public RecyclerView myRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView.Adapter mAdapter;
    List<DataPojo> arrayList= new ArrayList<>();
    private Context context=null;
    private Button btnUpdateList;
    private String URL="";
    private SqlPelangganHelper db;
    private String sqlQuery="SELECT * FROM "+ SqlPelangganHelper.TABLE_CONTACT;

    public OneFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.recycleLayout1);
        mLayoutManager = new LinearLayoutManager(context);
        myRecyclerView.setLayoutManager(mLayoutManager);
        btnUpdateList = (Button) view.findViewById(R.id.btnUpdateList1);
        // Inflate the layout for this fragment
//        btnUpdateList.setOnClickListener(this);
        btnUpdateList.setOnClickListener(this);

        initialize(0);
        return view;
    }

    private void initialize(int status){
        GetConnection gc =GetConnection.getInstance();
        db= new SqlPelangganHelper(context);

        arrayList.clear();

        arrayList = db.getAllContacts();
        //check this from first or update
        if(status==0) {
            //check if data has insert or still null
            if(arrayList.size()==0)
            {
                if(gc.isNetworkAvailable(context)) {
                    loadDatabase(0);
                    arrayList = db.getAllContacts();
                    Log.d("DATABASE","data kosong membuat data baru");
                }else{
                    Toast.makeText(context,"Maaf, Tidak Ada Koneksi",Toast.LENGTH_LONG).show();
                }
            }else{
                //load database
                arrayList = db.getAllContacts();
            }

            Log.d("DATABASE","UPDATE count = "+ arrayList.size());
            mAdapter= new OneFragment.PelangganAdapter(arrayList,context);
            myRecyclerView.setAdapter(mAdapter);

        }else {
            db.delete();
            loadDatabase(1);

        }
    }


    public void loadDatabase(final int status){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Get Data...");
        dialog.setCancelable(false);
        dialog.show();
        final AsyncHttpClient client= new AsyncHttpClient();

//      arrayList.clear();
//        URL="http://10.0.3.2:8080/manajemen_pelanggan/API/pelanggan";
        URL=GetConnection.IP+"/manajemen_pelanggan/API/pelanggan";
        client.setConnectTimeout(10000);
        client.get(context, URL,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    JSONArray jsonArray= response.getJSONArray("hasil");
                    Log.d("hasil"," Count = " + jsonArray.length() + " Status" + statusCode);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        if(object.getString("petugas_cek").equals(Login.txtUser)) {
                            DataPojo data = new DataPojo(Integer.valueOf(object.getString("id")), Integer.valueOf(object.getString("id_pel")), object.getString("nama"), object.getString("alamat"), object.getString("no_tiang"), object.getString("lat"), object.getString("long"), object.getString("kode_baca"), object.getString("status"));
                            db.addContact(data);
                        }

                    }
                    if(status==1){
                        arrayList = db.getAllContacts();
                        Log.d("DATABASE","UPDATE count = "+ arrayList.size());
                        mAdapter= new OneFragment.PelangganAdapter(arrayList,context);
                        myRecyclerView.setAdapter(mAdapter);
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        Toast.makeText(getContext(),"Data Berhasil di Perbarui",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }

    @Override
    public void onClick(View view) {
        GetConnection gc = GetConnection.getInstance();
        switch (view.getId()) {
            case R.id.btnUpdateList1:
                if (gc.isNetworkAvailable(context)) {
                    initialize(1);
                }else{
                    Toast.makeText(context,"Maaf, Tidak Ada Koneksi",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    public class PelangganAdapter extends RecyclerView.Adapter<OneFragment.PelangganAdapter.ViewHolder>{
        Context context;
        List<DataPojo> data;
        public PelangganAdapter(List<DataPojo> data, Context context) {
            this.data= data;
            this.context= context;
        }
        @Override
        public OneFragment.PelangganAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pelanggan_list_item,parent,false);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = myRecyclerView.getChildAdapterPosition(view);
                    if (pos >= 0 && pos < getItemCount()) {
                        Intent intent= new Intent(context,DetailPelanggan.class);

                        intent.putExtra("id1",arrayList.get(pos).getId1());
                        intent.putExtra("no_pel",arrayList.get(pos).getNo_Pel());
                        intent.putExtra("no_tiang",arrayList.get(pos).getNo_tiang());
                        intent.putExtra("nama",arrayList.get(pos).getNama());
                        intent.putExtra("alamat",arrayList.get(pos).getAlamat());
                        intent.putExtra("lat",arrayList.get(pos).getLat());
                        intent.putExtra("long",arrayList.get(pos).getLng());
                        intent.putExtra("kode_baca",arrayList.get(pos).getKode_baca());
                        intent.putExtra("status",arrayList.get(pos).getStatus());
                        startActivity(intent);
                    }
                }
            });
            return new OneFragment.PelangganAdapter.ViewHolder(view);
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
