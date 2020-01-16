package com.example.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hasee_pc.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Xiangce extends AppCompatActivity {
    private GridView gv;
    private File root;
    private List<String> list = new ArrayList<String>();
    int[] images={R.mipmap.a2cc760c9ba365fd008edd67e2282c1b,R.mipmap.ab00a226a3fc9e169bb0fd5e07271553,R.mipmap.b03df66b4dd45cb8201bc931ff51cc0d,R.mipmap.c9c859d137fd3e4506dc59b748582061};
    private void getname(){
        list.clear();
        File files[] = root.listFiles();
        if(files != null){
            for (File f : files){
                String path=f.getAbsolutePath();

                if(path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")){
                    list.add(path);
                }else{
                    //System.out.println(f);
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangce);
        Intent intent=getIntent();
        String name=intent.getStringExtra("data");
        root=new File(Environment.getExternalStorageDirectory()+ File.separator + "e-note"+File.separator+name);
        getname();
        //Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
        gv= (GridView) findViewById(R.id.gv);
        gv.setAdapter(new MyAdapter());
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        //设置单击GridView中每个item的单击事件
        /*gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //使用intend获取要交互的Activity，也就是将要跳转的界面
                Intent intent = new Intent(GridActivity.this,MainActivity.class);
                //通过intent的putExtra方法获取点击图片的下标位置（用于Activity之间数据传输）
                intent.putExtra("select",position);
                //启动要交互的Activity（通过传入包含该Activity的intent）
                startActivity(intent);
            }
        });*/
    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.griditem_layout,null);
                vh= new ViewHolder();
                vh.iv= (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(vh);
            }
            vh= (ViewHolder) convertView.getTag();
            int MIN_SIZE=500;
            Bitmap bitmap = BitmapFactory.decodeFile(list.get(position));
            Bitmap bmp1 = ThumbnailUtils.extractThumbnail(bitmap, MIN_SIZE, MIN_SIZE);
            vh.iv.setImageBitmap(bmp1);
            //vh.iv.setImageResource(images[position]);
            return convertView;
        }
        class ViewHolder{
            ImageView iv;
        }
    }
}
