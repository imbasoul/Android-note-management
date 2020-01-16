package com.example.gallery;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.hasee_pc.myapplication.Record;

import java.util.Date;
import java.util.logging.Logger;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hasee-pc on 2017/07/11.
 */

public class SQL {
    public static void timesup(SQLiteDatabase sqLiteDatabase, String table, String path){
        int time;
        Date date=new Date();
        Cursor cursor=sqLiteDatabase.query(table,null,"path IN('"+path+"');",null,null,null,null);
        cursor.moveToNext();
        time=cursor.getInt(cursor.getColumnIndex("times"));
        time++;
        sqLiteDatabase.execSQL("update "+table+" set times=? where path=?",new Object[]{time,path});
        sqLiteDatabase.execSQL("update "+table+" set lasttime=? where path=?",new Object[]{date.getTime(),path});
        Log.v(""+date.getTime(),""+time);
        Record.liulan(sqLiteDatabase);/////////////////////////////////////////////新加的
    }
    public static void updaterank(SQLiteDatabase sqLiteDatabase,String table){
        int rank,star,times;
        Long lasttime,hour;
        Date date=new Date();
        String path;
        Cursor cursor=sqLiteDatabase.query(table,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            rank=0;
            star=cursor.getInt(cursor.getColumnIndex("star"));
            lasttime=cursor.getLong(cursor.getColumnIndex("lasttime"));
            times=cursor.getInt(cursor.getColumnIndex("times"));
            path=cursor.getString(cursor.getColumnIndex("path"));
            hour=(date.getTime()-lasttime)/3600000;
            if(hour>24){
                rank+=100;
            }
            else if(hour<=2&&times!=0){
                rank-=100;
            }
            rank+=(int)(date.getTime()-lasttime)/3600000+star*24-times*5;
            sqLiteDatabase.execSQL("update "+table+" set rank=? where path=?",new Object[]{rank,path});
            Log.v(""+lasttime,""+times);
            Log.v("rank",""+rank);
        }
    }
}
