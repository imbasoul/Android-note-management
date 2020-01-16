package com.example.gallery;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.hasee_pc.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class GrilViewActivity extends AppCompatActivity {
    public SQLiteDatabase sqLiteDatabase;
    private List<ImageAndText> list = new ArrayList<ImageAndText>();
    private File root;
    private void getname(){
        int i=1;
        list.clear();
        File files[] = root.listFiles();
        if(files != null){
            for (File f : files){
                String path=f.getAbsolutePath();

                if(path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")){
                    String pz="";
                    Cursor cursor=sqLiteDatabase.query(name,null,"path IN('"+path+"');",null,null,null,null);
                    if(cursor.moveToNext()){
                        pz=cursor.getString(cursor.getColumnIndex("pizhu"));
                   }
                    list.add(new ImageAndText(path, pz));
                    //i++;
                }else{
                    //System.out.println(f);
                }
            }
        }
    }
    private  void getrankname(){
        list.clear();
        Cursor cursor=sqLiteDatabase.query(name,null,"rank > -100",null,null,null,"rank desc");
        while(cursor.moveToNext()){
            list.add(new ImageAndText(cursor.getString(cursor.getColumnIndex("path")),cursor.getString(cursor.getColumnIndex("pizhu"))));
        }
    }
    public ImageAndTextListAdapter imageAndTextListAdapter;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//指定Toolbar上的视图文件
        final MenuItem item = menu.findItem(R.id.ab_search);
        final MenuItem item1=menu.findItem(R.id.action_item1);
        item1.setTitle("智能推荐");
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="智能推荐"){
                    SQL.updaterank(sqLiteDatabase,name);
                    menuItem.setTitle("取消智能推荐");
                    getrankname();
                    imageAndTextListAdapter.notifyDataSetChanged();
                }
                else{
                    menuItem.setTitle("智能推荐");
                    getname();
                    imageAndTextListAdapter.notifyDataSetChanged();
                }
                //Toast.makeText(GrilViewActivity.this,"点了",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list.clear();
                Cursor cursor=sqLiteDatabase.query(name,null,"pizhu like '%"+newText+"%' and path like '%"+root.getAbsolutePath().toString()+"%'",null,null,null,null);
                while(cursor.moveToNext()){
                    list.add(new ImageAndText(cursor.getString(cursor.getColumnIndex("path")),cursor.getString(cursor.getColumnIndex("pizhu"))));
                }
                imageAndTextListAdapter.notifyDataSetChanged();
                //Toast.makeText(GrilViewActivity.this,newText,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                this.finish();//真正实现回退功能的代码
            default:break;

        }
        return super.onOptionsItemSelected(item);
    }
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gril_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        GridView gridView=(GridView)findViewById(R.id.gridview);
        //List<ImageAndText> list = new ArrayList<ImageAndText>();
        Intent intent=getIntent();
        name=intent.getStringExtra("data");
        root=new File(Environment.getExternalStorageDirectory()+ File.separator + "e-note"+File.separator+name);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
        sqLiteDatabase=this.openOrCreateDatabase("Test",MODE_PRIVATE,null);
        /*String[] paths=new String[15];
        for(int i=0;i<15;i++){
            int index=i;
            paths[i]="/sdcard/"+String.valueOf(index+1)+".jpg";//自己动手向SD卡添加15张图片，如：1.jpg
        }
        for(int i=0;i<15;i++){
            list.add(new ImageAndText(paths[i], String.valueOf(i)));
        }*/
        getname();
        imageAndTextListAdapter=new ImageAndTextListAdapter(this, list, gridView);
        gridView.setAdapter(imageAndTextListAdapter);
        this.registerForContextMenu(gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(GrilViewActivity.this,""+i,Toast.LENGTH_SHORT).show();
                ImageAndText imageAndText=list.get(i);
                String s=imageAndText.getImageUrl();
                File file=new File(s);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                //ContentValues contentValues = new ContentValues(1);
                //contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                //Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                //Toast.makeText(GrilViewActivity.this,""+uri.toString(),Toast.LENGTH_SHORT).show();
                //Uri uri = FileProvider.getUriForFile(GrilViewActivity.this,GrilViewActivity.this.getPackageName()+".provider",file);
                Uri uri=ProviderUtil.getImageContentUri(GrilViewActivity.this,file);
                intent.setDataAndType(uri, "image/*");
                SQL.timesup(sqLiteDatabase,name,s);
                //SQL.updaterank(sqLiteDatabase,name);
                startActivity(intent);
            }
        });
    }
    public class ImageAndText {
        private String imageUrl;
        private String text;

        public ImageAndText(String imageUrl, String text) {
            this.imageUrl = imageUrl;
            this.text = text;
        }
        public String getImageUrl() {
            return imageUrl;
        }
        public String getText() {
            return text;
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("菜单");
        //menu.setHeaderIcon(R.drawable.a4c);
        menu.add(0, 0, Menu.NONE, "修改注释");
        menu.add(0, 1, Menu.NONE, "删除");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                //Toast.makeText(this,"0"+menuInfo.position, Toast.LENGTH_SHORT).show();
                final EditText editText=new EditText(this);
                editText.setText(list.get(menuInfo.position).getText());
                new AlertDialog.Builder(this).setTitle("请输入注释").setIcon(android.R.drawable.ic_dialog_info).setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str=editText.getText().toString();
                        ImageAndText imageAndText1=list.get(menuInfo.position);
                        ImageAndText imageAndText2=new ImageAndText(imageAndText1.getImageUrl(),str);
                        Cursor cursor=sqLiteDatabase.query(name,null,"path='"+imageAndText1.getImageUrl()+"'",null,null,null,null);
                        if(cursor.moveToNext()){
                            sqLiteDatabase.execSQL("update "+name+" set pizhu=? where path=?",new Object[]{str,imageAndText1.getImageUrl()});
                        }
                        else{
                            Date date=new Date();
                            sqLiteDatabase.execSQL("insert into "+name+" values('"+imageAndText1.getImageUrl()+"','"+str+"',"+0+","+date.getTime()+",0,0);");
                        }
                        //sqLiteDatabase.execSQL("update student set pizhu=? where path=?",new Object[]{str,imageAndText1.getImageUrl()});
                        list.set(menuInfo.position,imageAndText2);
                        imageAndTextListAdapter.notifyDataSetChanged();
                        //查看list在适配器中为引用调用
                    }
                }).setNegativeButton("取消", null).show();
                break;
            case 1:
                //Toast.makeText(this, menuInfo.position, Toast.LENGTH_SHORT).show();
                new  AlertDialog.Builder(this).setTitle("提示").setMessage("确认要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ImageAndText imageAndText=list.get(menuInfo.position);
                        String p=imageAndText.getImageUrl();
                        //Toast.makeText(GrilViewActivity.this,p,Toast.LENGTH_SHORT).show();
                        File file=new File(p);
                        file.delete();
                        //deleteFile(p);
                        sqLiteDatabase.execSQL("delete from "+name+" where path=?",new Object[]{p});
                        list.remove(menuInfo.position);
                        imageAndTextListAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消",null).show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        imageAndTextListAdapter.notifyDataSetChanged();
        return true;
    }

}
