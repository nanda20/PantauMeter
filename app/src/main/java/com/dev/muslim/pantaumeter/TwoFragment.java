package com.dev.muslim.pantaumeter;

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

import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends Fragment {
    public RecyclerView myRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView.Adapter mAdapter;
    List<DataPojo> arrayList= new ArrayList<>();
    private Context context=null;
    private SqlPelangganHelper db;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.recycleLayout2);
        mLayoutManager = new LinearLayoutManager(context);
        myRecyclerView.setLayoutManager(mLayoutManager);
        initialize();
        return view;
    }

    @Override
    public void onResume() {
        initialize();
        super.onResume();
    }

    private void initialize(){
//        GetConnection gc =GetConnection.getInstance();
        db= new SqlPelangganHelper(context);
        arrayList.clear();
        try {
            arrayList = db.getAllDraft();
        }catch (Exception e)
        {
            Log.d("DATA_PEL","DRAF KOSONG");
        }
        Log.d("DATA_PEL","DRAF = "+db.getAllDraft().size());



        //check this from first or update
//        if(status==0) {
//            //check if data has insert or still null
//            if(arrayList.size()==0)
//            {
//                if(gc.isNetworkAvailable(context)) {
////                    loadDatabase(0);
//                    arrayList = db.getAllContacts();
//                    Log.d("DATABASE","data kosong membuat data baru");
//                }else{
//                    Toast.makeText(context,"Maaf, Tidak Ada Koneksi",Toast.LENGTH_LONG).show();
//                }
//            }else{
//                //load database
//                arrayList = db.getAllContacts();
//            }
//
//            Log.d("DATABASE","UPDATE count = "+ arrayList.size());

            mAdapter= new PelangganAdapter(arrayList,context);
            myRecyclerView.setAdapter(mAdapter);
//
//        }else {
//            db.delete();
//            loadDatabase(1);

        }

    public class PelangganAdapter extends RecyclerView.Adapter<TwoFragment.PelangganAdapter.ViewHolder>{
        Context context;
        List<DataPojo> data;
        public PelangganAdapter(List<DataPojo> data, Context context) {
            this.data= data;
            this.context= context;
        }
        @Override
        public TwoFragment.PelangganAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pelanggan_list_item,parent,false);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = myRecyclerView.getChildAdapterPosition(view);
                    if (pos >= 0 && pos < getItemCount()) {
                        Intent intent= new Intent(context,InputMeteran.class);
                        intent.putExtra("status","draf");
                        intent.putExtra("id1",arrayList.get(pos).getId1());
                        intent.putExtra("no_pel",arrayList.get(pos).getNo_Pel());
                        intent.putExtra("stand",arrayList.get(pos).getStandKini());
                        intent.putExtra("keterangan",arrayList.get(pos).getKeterangan());
                        intent.putExtra("foto",arrayList.get(pos).getFoto());

                        startActivity(intent);
                    }
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TwoFragment.PelangganAdapter.ViewHolder holder, int position) {
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

