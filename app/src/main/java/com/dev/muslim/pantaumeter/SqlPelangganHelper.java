package com.dev.muslim.pantaumeter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by owner on 8/2/2017.
 */

public class SqlPelangganHelper extends SQLiteOpenHelper {
    private  static final int DB_VERSION=3;
    private static final String DB_NAME = "listPelanggan";
    private static final String TABLE_CONTACT = "pelanggan";
    private static final String ID1 = "id";
    private static final String ID_PEL = "id_pel";
    private static final String NO_TIANG = "no_tiang";
    private static final String NAMA = "nama";
    private static final String ALAMAT = "alamat";
    private static final String LAT = "lat";
    private static final String LNG = "long";
    private static final String KODE_BACA = "kode_baca";
    private static final String STATUS = "status";
    private  static SqlPelangganHelper sInstance;
    Context context;

    public  static synchronized SqlPelangganHelper getsInstance(Context context){
        if(sInstance==null){
            sInstance=new SqlPelangganHelper(context.getApplicationContext());
        }
        return  sInstance;
    }

    public SqlPelangganHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        File database=context.getDatabasePath("listPelanggan.db");


        if (!database.exists()) {
            String CREATE_CONTACT_TABLE="CREATE TABLE "+ TABLE_CONTACT+
                    "("+ID1+" INTEGER PRIMARY KEY, "+ ID_PEL+" TEXT,"+ NAMA +" TEXT,"+ALAMAT+" TEXT,"+NO_TIANG+" TEXT,"+ LAT +" TEXT, "+LNG +" TEXT, "+ KODE_BACA +" TEXT ,"+ STATUS +" TEXT "+")";

            sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);
            Toast.makeText(context,"On Create",Toast.LENGTH_SHORT).show();

            Log.i("Database", "Not Found");
        } else {

            Log.i("Database", "Found");
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public  void  addContact(DataPojo plgn){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(ID1,plgn.getId1());
        values.put(ID_PEL,plgn.getNo_Pel());
        values.put(NO_TIANG,plgn.getNo_tiang());
        values.put(NAMA,plgn.getNama());
        values.put(ALAMAT,plgn.getAlamat());
        values.put(LAT,plgn.getLat());
        values.put(LNG,plgn.getLng());
        values.put(KODE_BACA,plgn.getKode_baca());
        values.put(STATUS,plgn.getStatus());
        db.insert(TABLE_CONTACT,null,values);
        db.close();
        Log.d("DATABASE","Add Data "+plgn.getNama());
    }

    public List<DataPojo> getAllContacts(){
        List<DataPojo> pelangganList = new ArrayList<DataPojo>();
        String selectQuery ="SELECT * FROM "+ TABLE_CONTACT;
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do {
                DataPojo dataPojo= new DataPojo();
                dataPojo.setId1(Integer.parseInt(cursor.getString(0)));
                dataPojo.setId_pel(Integer.parseInt(cursor.getString(1)));
                dataPojo.setNama(cursor.getString(2));
                dataPojo.setAlamat(cursor.getString(3));
                dataPojo.setNo_tiang(cursor.getString(4));
                dataPojo.setLat(cursor.getString(5));
                dataPojo.setLng(cursor.getString(6));
                dataPojo.setKode_baca(cursor.getString(7));
                dataPojo.setStatus(cursor.getString(8));
                //Adding contact to list
                pelangganList.add(dataPojo);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("DATABASE","SELECT * FROM TABEL = " + pelangganList.size());
        return  pelangganList;
    }

    public void delete() {
            SQLiteDatabase db= this.getWritableDatabase();
            db.delete(TABLE_CONTACT,null,null);
            db.close();
            Log.d("DATABASE","TRUNCATE TABLE");

    }
}
