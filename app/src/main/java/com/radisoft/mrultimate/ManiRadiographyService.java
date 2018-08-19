package com.radisoft.mrultimate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class ManiRadiographyService extends Service {
    private final String customURL = "http://www.maniradiography.com/__unzipped";
    private final String customAPIURL = "http://www.maniradiography.com/api";
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private final String TAG = "com.radisoft";
    private String asset_path = "file:///android_asset/";
    private boolean success = false;
    public ManiRadiographyService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG,"Service created!");

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Log.i(TAG,"Service is still running");
                try {
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection con = (HttpURLConnection) new URL(customURL).openConnection();
                    con.setRequestMethod("HEAD");
                    System.out.println(con.getResponseCode());

                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                       Log.i(TAG,"File exist!");
                        File dir = new File(asset_path+"__unzipped");
                        if(!(dir.exists() && dir.isDirectory())) {
                            // do something here
                            success = dir.mkdir();
                        }

                        if(success==true)
                        {
                            //compare size with remote file size
                            long size = getFileSize(dir);
                            con = (HttpURLConnection) new URL(customAPIURL+"/?action=fileSize").openConnection();
                            con.setRequestMethod("GET");
                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    con.getInputStream()));
                            String inputLine;
                            inputLine = in.readLine();
                            long size2 = Long.parseLong(inputLine);
                            if(size2>size){
                                //alert user that update is available

                            }
                        }
                    } else {

                        Log.i(TAG,"File does not exist!");

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }

    public static long getFileSize(final File file)
    {
        if(file==null||!file.exists())
            return 0;
        if(!file.isDirectory())
            return file.length();
        final List<File> dirs=new LinkedList<File>();
        dirs.add(file);
        long result=0;
        while(!dirs.isEmpty())
        {
            final File dir=dirs.remove(0);
            if(!dir.exists())
                continue;
            final File[] listFiles=dir.listFiles();
            if(listFiles==null||listFiles.length==0)
                continue;
            for(final File child : listFiles)
            {
                result+=child.length();
                if(child.isDirectory())
                    dirs.add(child);
            }
        }
        return result;
    }

}
