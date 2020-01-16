package com.example.hasee_pc.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class MScre extends Service {
    public MScre() {

        Log.v("TAG","第一次判断是否存在1？"+isFileExist("e-note"));
        Log.v("TAG","创建是否成功"+createFile("e-note"));
        Log.v("TAG","第二次判断是否存在？"+isFileExist("e-note"));
        Log.v("TAG","My S C");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //public static String director = "e-note";
    public static boolean isFileExist(String director) {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + director);
        return file.exists();
    }

    /*
     * create multiple director
     * @param path
     * @return
     */
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
}
