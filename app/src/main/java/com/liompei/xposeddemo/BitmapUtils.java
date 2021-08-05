package com.liompei.xposeddemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

public class BitmapUtils {

    public void saveBitmap1(String name, Bitmap bm){
        String is = Environment.getExternalStorageState();
        if (is.equals("mounted")) {
            File sdRoot = null;
            //Environment.setUserRequired(false);
            try {
                Class<?> environmentcls = Class.forName("android.os.Environment");
                Method setUserRequiredM = environmentcls.getMethod("setUserRequired", boolean.class);
                setUserRequiredM.invoke(null, false);

            } catch (Exception e) {
                e.printStackTrace();
            }

            sdRoot = Environment.getExternalStorageDirectory();
            System.out.println("sdRoot: "+sdRoot);

            //下面的是原来的
            String dumpImages="/dumpImages/";
            String targetPath=Environment.getExternalStorageDirectory().getPath() + dumpImages;
            File file = new File(Environment.getExternalStorageDirectory().getPath() + dumpImages);
            if (!file.exists()) {
                file.mkdirs();
            }
            if(fileIsExist(targetPath)){
                Log.d("Save Bitmap","TargetPath is exist");
                File saveFile=new File(targetPath,name);
                try{
                    FileOutputStream saveImgOut=new FileOutputStream(saveFile);
                    boolean isSaved=bm.compress(Bitmap.CompressFormat.JPEG,80,saveImgOut);
                    System.out.println("is saved: "+isSaved);
                    saveImgOut.flush();
                    saveImgOut.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }else {
                System.out.println("TargetPath is still not exist");
            }

        }



    }

    /**
     * 在真机上成功，但是在模拟器上显示权限的问题（也不知道为啥）
     * */
    public void saveBitmap(String name, Bitmap bm,String stackInfo){
        saveStackInfo(name,stackInfo);
        //    /data/data/org.wordpress.android      /data/user/0/org.wordpress.android/images/

        name=name+".jpg";

        File targetPath = HelloHook.appContext.getDataDir();

        System.out.println("targetPath:"+targetPath.getAbsolutePath());
        String path=targetPath.getAbsolutePath()+"/images/";
        targetPath=new File(path);
        targetPath.mkdirs();

        if(!targetPath.exists()){
            Log.d("Save Bitmap","TargetPath isn't exist");
            System.out.println("liwenjie78");
        }
        if(targetPath.exists()){
            System.out.println("liwenjie101");
            Log.d("Save Bitmap","TargetPath is exist");
            File saveFile=new File(targetPath,name);
            Log.d("Save Bitmap", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + saveFile.exists() + ", " + saveFile.getName() + ", " + saveFile.getAbsolutePath() + ", w:" + saveFile.canWrite() + ", r:" + saveFile.canRead());
            try{
                FileOutputStream saveImgOut=new FileOutputStream(saveFile);
                boolean isSaved=bm.compress(Bitmap.CompressFormat.JPEG,80,saveImgOut);
                System.out.println("is saved: "+isSaved);
                saveImgOut.flush();
                saveImgOut.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }else {
            System.out.println("TargetPath is still not exist");
        }
    }

    public void saveDisplayedBitmap(String name, Bitmap bm,String stackInfo){
        saveStackInfo(name,stackInfo);
        //    /data/data/org.wordpress.android      /data/user/0/org.wordpress.android/images/

        name=name+".jpg";

        File targetPath = HelloHook.appContext.getDataDir();

        System.out.println("targetPath:"+targetPath.getAbsolutePath());
        String path=targetPath.getAbsolutePath()+"/displayedImages/";
        targetPath=new File(path);
        targetPath.mkdirs();

        if(!targetPath.exists()){
            Log.d("Save Bitmap","TargetPath isn't exist");
            System.out.println("liwenjie78");
        }
        if(targetPath.exists()){
            System.out.println("liwenjie101");
            Log.d("Save Bitmap","TargetPath is exist");
            File saveFile=new File(targetPath,name);
            Log.d("Save Bitmap", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + saveFile.exists() + ", " + saveFile.getName() + ", " + saveFile.getAbsolutePath() + ", w:" + saveFile.canWrite() + ", r:" + saveFile.canRead());
            try{
                FileOutputStream saveImgOut=new FileOutputStream(saveFile);
                boolean isSaved=bm.compress(Bitmap.CompressFormat.JPEG,80,saveImgOut);
                System.out.println("is saved: "+isSaved);
                saveImgOut.flush();
                saveImgOut.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }else {
            System.out.println("TargetPath is still not exist");
        }
    }

    public void saveDisplayedDrawable(String name, Drawable drawable,String stackInfo){
        saveStackInfo(name,stackInfo);
        name=name+".jpg";

        File targetPath = HelloHook.appContext.getDataDir();

        System.out.println("targetPath:"+targetPath.getAbsolutePath());
        String path=targetPath.getAbsolutePath()+"/displayedImages/";
        targetPath=new File(path);
        targetPath.mkdirs();

        if(!targetPath.exists()){
            Log.d("Save Bitmap","TargetPath isn't exist");
            System.out.println("liwenjie78");
        }


        if(targetPath.exists()){
            File saveFile=new File(targetPath,name);
            try{
                FileOutputStream saveImgOut=new FileOutputStream(saveFile);
                ((BitmapDrawable) drawable).getBitmap().compress(Bitmap.CompressFormat.JPEG,80,saveImgOut);
                saveImgOut.flush();
                saveImgOut.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void saveDrawable(String name, Drawable drawable, String stackInfo){
            saveStackInfo(name,stackInfo);

        name=name+".jpg";

        File targetPath = HelloHook.appContext.getDataDir();

        System.out.println("targetPath:"+targetPath.getAbsolutePath());
        String path=targetPath.getAbsolutePath()+"/images/";
        targetPath=new File(path);
        targetPath.mkdirs();

        if(!targetPath.exists()){
            Log.d("Save Bitmap","TargetPath isn't exist");
            System.out.println("liwenjie78");
        }


        if(targetPath.exists()){
            File saveFile=new File(targetPath,name);
            try{
                FileOutputStream saveImgOut=new FileOutputStream(saveFile);
                ((BitmapDrawable) drawable).getBitmap().compress(Bitmap.CompressFormat.JPEG,80,saveImgOut);
                saveImgOut.flush();
                saveImgOut.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    //success
    public void saveUrl(String name,String url){
        File targetPath = HelloHook.appContext.getDataDir();

        System.out.println("targetPath:"+targetPath.getAbsolutePath());
        String path=targetPath.getAbsolutePath()+"/images/";
        targetPath=new File(path);
        targetPath.mkdirs();

        if(!targetPath.exists()){
            Log.d("Save Bitmap","TargetPath isn't exist");
            System.out.println("liwenjie78");
        }


        if(targetPath.exists()){
            File checkFile = new File(targetPath + "/"+name+".txt");
            FileWriter writer = null;
            try {
                if (!checkFile.exists()) {
                    checkFile.createNewFile();
                }
                writer = new FileWriter(checkFile, true);
                writer.append(url);//写入url地址
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    if (null != writer)
                        writer.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveStackInfo(String name,String url){
        File targetPath = HelloHook.appContext.getDataDir();

        System.out.println("targetPath:"+targetPath.getAbsolutePath());
        String path=targetPath.getAbsolutePath()+"/images/";
        targetPath=new File(path);
        targetPath.mkdirs();

        if(!targetPath.exists()){
            Log.d("Save Bitmap","TargetPath isn't exist");
            System.out.println("liwenjie78");
        }


        if(targetPath.exists()){
            File checkFile = new File(targetPath + "/"+name+".txt");
            FileWriter writer = null;
            try {
                if (!checkFile.exists()) {
                    checkFile.createNewFile();
                }
                writer = new FileWriter(checkFile, true);
                writer.append(url);//写入url地址
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    if (null != writer)
                        writer.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveTrace(String name, String imageName1, String APIName,String imageName2){
        File targetPath = HelloHook.appContext.getDataDir();
        System.out.println("targetPath:"+targetPath.getAbsolutePath());
        String path=targetPath.getAbsolutePath()+"/images/";
        targetPath=new File(path);
        targetPath.mkdirs();

        if(!targetPath.exists()){
            Log.d("Save Bitmap","TargetPath isn't exist");
            System.out.println("liwenjie79");
        }


        if(targetPath.exists()){
            File checkFile = new File(targetPath + "/"+name+".txt");
            FileWriter writer = null;
            try {
                if (!checkFile.exists()) {
                    checkFile.createNewFile();
                }
                writer = new FileWriter(checkFile, true);
                writer.append(imageName1+"\n");
                writer.append(APIName+"\n");
                writer.append(imageName2+"\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    if (null != writer)
                        writer.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


//    public void saveDrawable(String name, Drawable drawable){
//        String targetPath="/sdcard/namecard/images/";
//        if(!fileIsExist(targetPath)){
//            Log.d("Save Drawable","TargetPath isn't exist");
//        }else{
//            File saveFile=new File(targetPath,name);
//            try{
//                FileOutputStream saveImgOut=new FileOutputStream(saveFile);
//                ((BitmapDrawable) drawable).getBitmap().compress(Bitmap.CompressFormat.JPEG,80,saveImgOut);
//                saveImgOut.flush();
//                saveImgOut.close();
//            }catch (IOException ex){
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    public void saveUrl(String name,String url){
//
//        String targetPath="/sdcard/namecard/images/";
//        if(!fileIsExist(targetPath)){
//            Log.d("Save Bitmap","TargetPath isn't exist");
//        }else{
//            File dir = new File(targetPath);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            File checkFile = new File(targetPath + "/"+name+".txt");
//            FileWriter writer = null;
//            try {
//                if (!checkFile.exists()) {
//                    checkFile.createNewFile();
//                }
//                writer = new FileWriter(checkFile, true);
//                writer.append(url);//写入url地址
//                writer.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try{
//                    if (null != writer)
//                        writer.close();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public boolean fileIsExist(String fileName){
        File file=new File(fileName);
        if(file.exists())
            return true;
        else{
            return file.mkdirs();
        }
    }


}
