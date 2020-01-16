package com.example.hasee_pc.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.service.autofill.FillEventHistory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.example.gallery.album;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.os.Environment.MEDIA_MOUNTED;

import static android.R.id.input;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public Uri uri;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    findViewById(R.id.layout2).setVisibility(View.GONE);
                    findViewById(R.id.layout3).setVisibility(View.GONE);
                    mTextMessage.setText(R.string.title_home);
                    findViewById(R.id.layout1).setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    findViewById(R.id.layout1).setVisibility(View.GONE);
                    findViewById(R.id.layout3).setVisibility(View.GONE);
                    mTextMessage.setText(R.string.title_dashboard);
                    findViewById(R.id.layout2).setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    findViewById(R.id.layout1).setVisibility(View.GONE);
                    findViewById(R.id.layout2).setVisibility(View.GONE);
                    mTextMessage.setText(R.string.title_notifications);
                    findViewById(R.id.layout3).setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }

    };
    private List<String> list = new ArrayList<String>();
    //private TextView myTextView;
    private Spinner mySpinner;
    private ArrayAdapter<String> adapter;
    private File root=new File(Environment.getExternalStorageDirectory()+ File.separator + "e-note");
    private void getname(){
        list.clear();
        File files[] = root.listFiles();
        if(files != null){
            for (File f : files){
                if(f.isDirectory()){
                    list.add(f.getName());
                }else{
                    //System.out.println(f);
                }
            }
        }
    }
    private void updatespanner(){
        //spanner事件
        //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
        getname();
        //myTextView = (TextView)findViewById(R.id.TextView_city);
        mySpinner = (Spinner)findViewById(R.id.spinnerxj);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        mySpinner.setAdapter(adapter);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                /* 将所选mySpinner 的值带入myTextView 中*/
                //myTextView.setText("您选择的是："+ adapter.getItem(arg2));
                /* 将mySpinner 显示*/
                arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("NONE");
                arg0.setVisibility(View.VISIBLE);
            }
        });
        /*下拉菜单弹出的内容选项触屏事件处理*/
        mySpinner.setOnTouchListener(new Spinner.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                /**
                 *
                 */
                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
    }
    public SQLiteDatabase sqLiteDatabase;
    String path;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        startService(new Intent(this,MScre.class));

        sqLiteDatabase=this.openOrCreateDatabase("Test",MODE_PRIVATE,null);
        String CREATE_TABLE="create table if not exists student(path varchar(100),pizhu varchar(100),star smallint);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL("create table if not exists record(date varchar(100),read smallint,write smallint,time smallint,number smallint);");
        date=new Date();
        Record.writerecord(sqLiteDatabase);
        updatespanner();

        //相机响应事件
        Button buttonxj=(Button)findViewById(R.id.buttonxj);
        buttonxj.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Toast.makeText(MainActivity.this,"111", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                            path=getPhotopath();
                                            File f=new File(path);
                                            uri = Uri.fromFile(f);
                                            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                                            Log.e("currentapiVersion","currentapiVersion====>"+currentapiVersion);
                                            if (currentapiVersion<24){
                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                                startActivityForResult(intent, 0);
                                            }else {
                                                ContentValues contentValues = new ContentValues(1);
                                                contentValues.put(MediaStore.Images.Media.DATA, f.getAbsolutePath());
                                                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                                startActivityForResult(intent, 0);
                                            }
                                            //Toast.makeText(MainActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                                            // 获取拍照后未压缩的原图片，并保存在uri路径中
                                            //intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(f));
                                            //intent.putExtra(MediaStore.Images.Media.ORIENTATION, 180);
                                            //startActivityForResult(intent, 0);
                                        }
                                    }
        );
    }
    private String getPhotopath() {
        // 照片全路径
        String fileName = "";
        if (!createFile("e-note")) {
            return fileName;
        }
        TextView ProvinceTxt = (TextView) mySpinner.getSelectedView();
        String codeString = ProvinceTxt.getText().toString();
        // 文件夹路径
        String pathUrl = Environment.getExternalStorageDirectory()+ File.separator + "e-note"+File.separator +codeString ;
        SimpleDateFormat   formatter   =   new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        pathUrl=pathUrl+ File.separator + str+".jpg";
        return pathUrl;
    }
    public static boolean isFileExist(String director) {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + director);
        return file.exists();
    }
    public static boolean createFile(String director) {

        if (isFileExist(director)) {
            return true;
        } else {
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + director);
            if (!file.mkdirs()) {
                return false;
            }
            return true;
        }
    }
    public File writetoSDcard(String directory, String fileName,Bitmap bitmap){
        File file = null;
        OutputStream os = null;
        try {
            if (!createFile(directory)) {
                return file;
            }
            file = new File(Environment.getExternalStorageDirectory() + File.separator + directory + File.separator + fileName+".jpg");
            file.createNewFile();
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os) ;
            //Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
            os.flush();
        } catch (Exception e) {
            Log.e("FileUtil", "" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    private File chooseimg;
    private  String imgpath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==0&&resultCode==-1)
        {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
            EditText editText=(EditText)findViewById(R.id.editTextpz);
            RatingBar ratingBar=(RatingBar)findViewById(R.id.ratingBar2);
            TextView ProvinceTxt = (TextView) mySpinner.getSelectedView();
            String codeString = ProvinceTxt.getText().toString();
            Date date=new Date();
            sqLiteDatabase.execSQL("insert into "+codeString+" values('"+path+"','"+editText.getText().toString()+"',"+(int)ratingBar.getRating()+","+date.getTime()+",0,0);");
            Record.biji(sqLiteDatabase);
            //Bitmap mBitmap = BitmapFactory.decodeFile(getPhotopath());
            /////////Bundle Extras = data.getExtras();
            /////////Bitmap mBitmap = (Bitmap)Extras.get("data");
            //SimpleDateFormat   formatter   =   new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            //Date curDate =  new Date(System.currentTimeMillis());
            //String   str   =   formatter.format(curDate);
            //writetoSDcard("e-note",str,mBitmap);
            //sqLiteDatabase.close();

        }
        else if(requestCode==0&&resultCode==0)
        {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(this, "您没有拍摄照片！", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            super.onActivityResult(requestCode, resultCode, data);
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imgpath = c.getString(columnIndex);
            //chooseimg = new File(imagePath);

            final TextView tv=(TextView)findViewById(R.id.editTextaddcourse);
            final String str="你确认要添加 "+tv.getText().toString()+" 课程吗？";
            final String de="e-note"+File.separator+tv.getText().toString();
            new AlertDialog.Builder(this).setTitle(str)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“确认”后的操作
                            createFile(de);
                            Bitmap bm = BitmapFactory.decodeFile(imgpath);
                            File file=new File(Environment.getExternalStorageDirectory() + File.separator+"e-note"+File.separator+tv.getText().toString()+".jpg");
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            OutputStream os = null;
                            try {
                                os =new  FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            bm.compress(Bitmap.CompressFormat.JPEG,20,os) ;
                            updatespanner();
                            sqLiteDatabase.execSQL("create table if not exists "+tv.getText().toString()+"(path varchar(100),pizhu varchar(100),star smallint,lasttime INTEGER,times smallint,rank INTEGER);");
                            Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“返回”后的操作,这里不设置没有任何操作
                        }
                    }).show();
            c.close();
        }
        else if(requestCode==1){
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode==2&&resultCode == Activity.RESULT_OK && data != null){
            super.onActivityResult(requestCode, resultCode, data);
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imgpath = c.getString(columnIndex);
            //chooseimg = new File(imagePath);
            TextView ProvinceTxt = (TextView) mySpinner.getSelectedView();
            final String codeString = ProvinceTxt.getText().toString();
            final TextView tv=(TextView)findViewById(R.id.editTextaddcourse);
            final String str="你确认要添加这张照片到 "+codeString+" 课程吗？";
            //final String de="e-note"+File.separator+tv.getText().toString();
            new AlertDialog.Builder(this).setTitle(str)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“确认”后的操作
                                    //createFile(de);
                                    Bitmap bm = BitmapFactory.decodeFile(imgpath);
                                    File file=new File(getPhotopath());
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    OutputStream os = null;
                                    try {
                                        os =new  FileOutputStream(file);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    bm.compress(Bitmap.CompressFormat.JPEG,100,os) ;
                                    EditText editText=(EditText)findViewById(R.id.editTextpz);
                                    RatingBar ratingBar=(RatingBar)findViewById(R.id.ratingBar2);
                                    TextView ProvinceTxt = (TextView) mySpinner.getSelectedView();
                                    String codeString = ProvinceTxt.getText().toString();
                                    Date date=new Date();
                                    sqLiteDatabase.execSQL("insert into "+codeString+" values('"+file.getAbsolutePath().toString()+"','"+editText.getText().toString()+"',"+(int)ratingBar.getRating()+","+date.getTime()+",0,0);");
                                    //updatespanner();
                                    Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    Record.biji(sqLiteDatabase);
                                }
                            })
                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“返回”后的操作,这里不设置没有任何操作
                        }
                    }).show();
            c.close();
        }
    }
    public void jisuan(View v){
        EditText a=(EditText) findViewById(R.id.Texta);
        EditText b=(EditText) findViewById(R.id.Textb);
        int c= Integer.parseInt( a.getText().toString())+Integer.parseInt( b.getText().toString());
        TextView view=(TextView) findViewById(R.id.textView);
        view.setText(Integer.toString(c));
    }
    public  void  tianjia(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

        /*final TextView tv=(TextView)findViewById(R.id.editTextaddcourse);
        final String str="你确认要添加 "+tv.getText().toString()+" 课程吗？";
        final String de="e-note"+File.separator+tv.getText().toString();
        new AlertDialog.Builder(this).setTitle(str)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        createFile(de);
                        updatespanner();
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();*/
    }

    public void chakan(View v){
        Intent intent=new Intent(this,album.class);
        startActivity(intent);
    }
    public void tianjiazhaopian(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }
    public void rizhi(View view){
        Intent intent=new  Intent(this,Record.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this,MScre.class));
    }
}
