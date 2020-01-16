package com.example.gallery;

import com.example.gallery.view.CustomGallery;
import com.example.gallery.view.ImageUtil;
import com.example.hasee_pc.myapplication.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class album extends Activity {
    private ImageAdapter adapter;
    private SQLiteDatabase sqLiteDatabase;
    /** 图片资源数组 */
    private File root=new File(Environment.getExternalStorageDirectory()+ File.separator + "e-note");
    //private int[] imageResIDs;
    private List<String> list = new ArrayList<String>();
    private List<String> namelist= new ArrayList<String>();
    private void getname(){
        namelist.clear();
        list.clear();
        File files[] = root.listFiles();
        if(files != null){
            for (File f : files){
                if(f.isDirectory()){
                    list.add(Environment.getExternalStorageDirectory()+ File.separator + "e-note"+File.separator+f.getName()+".jpg");
                    namelist.add(f.getName());
                }else{
                    //System.out.println(f);
                }
            }
        }
    }

    SelectPicPopupWindow menuWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_album);
        getname();
        /*imageResIDs = new int[]{//
                R.mipmap.a2cc760c9ba365fd008edd67e2282c1b,//
                R.mipmap.ab00a226a3fc9e169bb0fd5e07271553,//
                R.mipmap.b03df66b4dd45cb8201bc931ff51cc0d,//
                R.mipmap.c9c859d137fd3e4506dc59b748582061,//
                R.mipmap.d6cc289f0fe59682542dc502dc097501//
        };*/
        CustomGallery customGallery = (CustomGallery) findViewById(R.id.customgallery);
        sqLiteDatabase=this.openOrCreateDatabase("Test",MODE_PRIVATE,null);
        adapter = new ImageAdapter();
        customGallery.setAdapter(adapter);
        customGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                Toast.makeText(album.this, "你选择了"+position+"号图片", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(album.this,GrilViewActivity.class);
                intent.putExtra("data",namelist.get(position));
                startActivity(intent);
            }

        });
        customGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,int position, long id) {
                TextView textView=(TextView)findViewById(R.id.textViewshow);
                textView.setText(namelist.get(position));
                //customGallery.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //这里不做响应
            }
        });
        customGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                temp=i;
                menuWindow = new SelectPicPopupWindow(album.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(album.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                return false;
            }
        });

    }
    public int temp;
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_change:

                    break;
                case R.id.btn_delete:
                    new  AlertDialog.Builder(album.this).setTitle("提示").setMessage("确认要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            File file1=new File(list.get(temp));
                            File file2=new File(root+File.separator+namelist.get(temp));
                            sqLiteDatabase.execSQL("DROP TABLE "+namelist.get(temp));
                            if(adapter.getCount()!=temp+1){
                                TextView textView=(TextView)findViewById(R.id.textViewshow);
                                textView.setText(namelist.get(temp+1));
                            }
                            else if(adapter.getCount()==1){
                                TextView textView=(TextView)findViewById(R.id.textViewshow);
                                textView.setText("请在设置中添加课程笔记");
                            }
                            list.remove(temp);
                            namelist.remove(temp);
                            adapter.notifyDataSetChanged();
                            RecursionDeleteFile(file1);
                            RecursionDeleteFile(file2);

                        }
                    }).setNegativeButton("取消",null).show();
                    break;
                default:
                    break;
            }


        }

    };
    public void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }
    public class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ImageView imageView;
            if (convertView != null) {
                imageView = (ImageView) convertView;
            } else {
                imageView = new ImageView(album.this);
            }
            //Bitmap bitmap = ImageUtil.getImageBitmap(getResources(),
                   // imageResIDs[position]);
            //Bitmap bitmap = BitmapFactory.decodeFile(list.get(position));
            Bitmap bitmap = ImageUtil.getImageBitmap(list.get(position),position);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            drawable.setAntiAlias(true); // 消除锯齿
            imageView.setImageDrawable(drawable);
            LayoutParams params = new LayoutParams(480, 640);
            imageView.setLayoutParams(params);
            return imageView;
        }

    }

}