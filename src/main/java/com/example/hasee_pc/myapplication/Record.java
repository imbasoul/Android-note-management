package com.example.hasee_pc.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Record extends AppCompatActivity {
    public static int lianxu;
    public static SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Date now=new Date();
        java.text.SimpleDateFormat matter1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String nowtime=matter1.format(now);
        setContentView(R.layout.activity_record);
        sqLiteDatabase=this.openOrCreateDatabase("Test",MODE_PRIVATE,null);
        Cursor cursor=sqLiteDatabase.query("sys",null,null,null,null,null,null);
        cursor.moveToNext();
        TextView textViewlianxu=(TextView)findViewById(R.id.textViewlianxu);
        textViewlianxu.setText(""+cursor.getInt(cursor.getColumnIndex("time")));
        Cursor cursor1=sqLiteDatabase.query("record",null,"date IN('"+nowtime+"');",null,null,null,null);
        cursor1.moveToNext();
        TextView textViewbiji=(TextView)findViewById(R.id.textViewbiji);
        textViewbiji.setText(""+cursor1.getInt(cursor1.getColumnIndex("write")));
        TextView textViewliulan=(TextView)findViewById(R.id.textViewliulan);
        textViewliulan.setText(""+cursor1.getInt(cursor1.getColumnIndex("read")));



        ListView list = (ListView) findViewById(R.id.listView);
        //生成动态数组，并且转载数据
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        int count=cursor.getInt(cursor.getColumnIndex("number"));
        for(int i=count-1;i>=1;i--)
        {
            Cursor c=sqLiteDatabase.query("record",null,"number IN("+i+");",null,null,null,null);
            c.moveToNext();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle",c.getString(c.getColumnIndex("date")));
            map.put("Itembiji", "这天你记录了"+c.getInt(c.getColumnIndex("write"))+"页笔记");
            map.put("Itemliulan", "这天你浏览了"+c.getInt(c.getColumnIndex("read"))+"页笔记");
            mylist.add(map);
            c.close();
        }
        //生成适配器，数组===》ListItem
        SimpleAdapter mSchedule = new SimpleAdapter(this,
                mylist,//数据来源
                R.layout.my_listitem,//ListItem的XML实现

                //动态数组与ListItem对应的子项
                new String[] {"ItemTitle", "Itembiji","Itemliulan"},

                //ListItem的XML文件里面的两个TextView ID
                new int[] {R.id.ItemTitle,R.id.Itembiji,R.id.Itemliulan});
        //添加并且显示
        list.setAdapter(mSchedule);

    }

    /*public static void writerecord(){




        Date now=new Date();
        Date pr = new Date(new Date().getTime()-24*60*60*1000);
        java.text.SimpleDateFormat matter1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String prtime = matter1.format(pr);
        String nowtime=matter1.format(now);



        Cursor c=sqLiteDatabase.query("record",null,"number=0",null,null,null,null);
        if(!c.moveToNext()){
            sqLiteDatabase.execSQL("insert into record values('"+nowtime+"',"+1+","+1+","+1+","+0+");");
            sqLiteDatabase.execSQL("insert into record values('"+nowtime+"',"+0+","+0+","+0+","+1+");");
        }
        ///////////全是错的全是错的



        Cursor cursor=sqLiteDatabase.query("record",null,"date IN('"+nowtime+"');",null,null,null,null);
        if(!cursor.moveToNext()){
            int num;
            Cursor cursor1=sqLiteDatabase.query("record",null,"number=0",null,null,null,null);
            cursor1.moveToNext();
            num=cursor1.getInt(cursor1.getColumnIndex("time"))+1;
            String data=cursor1.getString(cursor1.getColumnIndex("date"));
            if(data==prtime){
                lianxu=cursor1.getInt(cursor1.getColumnIndex("read"))+1;
            }
            else{
                lianxu=1;
            }
            sqLiteDatabase.execSQL("update record set date=? where number=?",new Object[]{nowtime,0});//当前日期
            sqLiteDatabase.execSQL("update record set read=? where number=?",new Object[]{lianxu,0});//lianxu存在read中
            sqLiteDatabase.execSQL("update record set time=? where number=?",new Object[]{num,0});//time存的是一共有几个记录
            sqLiteDatabase.execSQL("insert into record values('"+nowtime+"',"+0+","+0+","+0+","+num+");");
        }
        //System.out.println(time);
    }*/
    public static void writerecord(SQLiteDatabase sqLiteDatabase){

        sqLiteDatabase.execSQL("create table if not exists sys(date varchar(100),time smallint,number smallint);");
        Date now=new Date();
        Date pr = new Date(new Date().getTime()-24*60*60*1000);
        java.text.SimpleDateFormat matter1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String prtime = matter1.format(pr);
        String nowtime=matter1.format(now);
        Cursor cursor=sqLiteDatabase.query("sys",null,null,null,null,null,null);
        if(!cursor.moveToNext()){
            sqLiteDatabase.execSQL("insert into sys values('"+nowtime+"',1,1);");
            sqLiteDatabase.execSQL("insert into record values('"+nowtime+"',"+0+","+0+","+0+","+1+");");
        }
        else{
            Log.e("nowtime",""+nowtime);
            Log.e("pretime",""+prtime);
            Cursor cursor1=sqLiteDatabase.query("record",null,"date IN('"+nowtime+"');",null,null,null,null);
            if(!cursor1.moveToNext()){
                //Log.e("123",""+nowtime);
                int lianxu;
                int num=cursor.getInt(cursor.getColumnIndex("number"))+1;
                String data=cursor.getString(cursor.getColumnIndex("date"));
                if(data.equals(prtime)){
                    lianxu=cursor.getInt(cursor.getColumnIndex("time"))+1;
                }
                else {
                    lianxu=1;
                }
                sqLiteDatabase.execSQL("update sys set time=? where date=?",new Object[]{lianxu,data});//lianxu存在read中
                sqLiteDatabase.execSQL("update sys set number=? where date=?",new Object[]{num,data});//time存的是一共有几个记录
                sqLiteDatabase.execSQL("update sys set date=? where date=?",new Object[]{nowtime,data});//当前日期
                sqLiteDatabase.execSQL("insert into record values('"+nowtime+"',"+0+","+0+","+0+","+num+");");
                //Toast.makeText(, ""+lianxu,Toast.LENGTH_SHORT).show();
                Log.e("lianxu",""+lianxu);
                Log.e("number",""+num);
            }
        }
    }
    public static void biji(SQLiteDatabase sqLiteDatabase){
        Date now=new Date();
        java.text.SimpleDateFormat matter1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String nowtime=matter1.format(now);
        Cursor cursor=sqLiteDatabase.query("record",null,"date IN('"+nowtime+"');",null,null,null,null);
        cursor.moveToNext();//如果隔天打开可能出事情
        int time=cursor.getInt(cursor.getColumnIndex("write"))+1;
        sqLiteDatabase.execSQL("update record set write=? where date=?",new Object[]{time,nowtime});
        cursor.close();
    }
    public static void liulan(SQLiteDatabase sqLiteDatabase){
        Date now=new Date();
        java.text.SimpleDateFormat matter1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String nowtime=matter1.format(now);
        Cursor cursor=sqLiteDatabase.query("record",null,"date IN('"+nowtime+"');",null,null,null,null);
        cursor.moveToNext();//如果隔天打开可能出事情
        int time=cursor.getInt(cursor.getColumnIndex("read"))+1;
        sqLiteDatabase.execSQL("update record set read=? where date=?",new Object[]{time,nowtime});
        cursor.close();
    }
}
