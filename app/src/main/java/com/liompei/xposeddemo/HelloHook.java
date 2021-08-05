
package com.liompei.xposeddemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HelloHook implements IXposedHookLoadPackage {
    private static final String TAG = "HelloHook";

    CompareBitmap compareBitmap=new CompareBitmap();
    CompareDrawable compareDrawable=new CompareDrawable();

    public static Context appContext = null;

    BitmapUtils bitmapUtils=new BitmapUtils();
    int nameCount=1;

    String testAppNames=
            "org.wordpress.android" +
                    "com.newsblur"+
                    "com.moez.QKSMS"+
                    "org.ligi.passandroid"+
                    "fr.neamar.kiss"+
                    "me.ccrama.redditslide"+
                    "link.standen.michael.slideshow"+
                    "net.nurik.roman.muzei"+
                    "de.danoeh.antennapod"+
                    "de.danoeh.antennapod.debug"+
                    "com.ss.android.article.news"+
                    "com.money.manager.ex"+
                    "org.wikipedia.alpha"+
                    "com.keylesspalace.tusky"+
                    "net.cyclestreets"+
                    "org.mariotaku.twidere"+
                    "com.gelakinetic.mtgfam"+
                    "de.tap.easy_xkcd"+
                    "eu.siacs.conversations";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        String targetApp="com.liompei.xposeddemo";
        String targetClass="com.liompei.xposeddemo.MainActivity";
        String targetMethod="onCreate";
        if (testAppNames.contains(lpparam.packageName)) {
            XposedBridge.log("===开始分析app===" + lpparam.packageName);

            try {
                XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        appContext = (Context) param.thisObject;
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "handleLoadPackage: error capturing context");
            }
            String targetClass3="android.graphics.BitmapFactory";
            String targetMethod3="decodeFile";
            try {
                XposedHelpers.findAndHookMethod(targetClass3, lpparam.classLoader, targetMethod3, String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----decodeFile(String pathName)");
                        Bitmap bmp = (Bitmap) param.getResult();

                        XposedBridge.log("helloHook----bitmapSet:   " + bmp.getWidth() + "   " + bmp.getHeight());
                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        String threadName = Thread.currentThread().getName();
                        bitmapUtils.saveBitmap(nameCount + "-" + timeStamp + "-" + "Bitmap" + "-"+bmp.getWidth()+","+bmp.getHeight()+"-"+threadName+"-"+ "decodeFile(pathName)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp,stackInfo[2]);
                        nameCount++;
                    }
                });
            }catch (Exception e){
                XposedBridge.log("===not method===decodeFile(String pathName)" );
                e.printStackTrace();
            }

            String targetClass4="android.graphics.BitmapFactory";
            String targetMethod4="decodeFile";
            try {
                XposedHelpers.findAndHookMethod(targetClass4, lpparam.classLoader, targetMethod4, String.class, BitmapFactory.Options.class, new XC_MethodHook() {

                    int bHight;
                    int bWidth;
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("beforeHookedMethod----decodeFile(String pathName, BitmapFactory.Options opts)");
                        BitmapFactory.Options arg1 = (BitmapFactory.Options) param.args[1];
                        bHight=arg1.outHeight;
                        bWidth=arg1.outWidth;
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----decodeFile(String pathName, BitmapFactory.Options opts)");
                        Bitmap bmp = (Bitmap) param.getResult();

                        if(bmp!=null) {
                            XposedBridge.log("helloHook----bitmapSet:   " + bmp.getWidth() + "   " + bmp.getHeight());
                            long timeStamp = System.currentTimeMillis();
                            BitmapFactory.Options arg = (BitmapFactory.Options) param.args[1];
                            int sampleSize=arg.inSampleSize;

                            String[] stackInfo=getTraceInfo();
                            String threadName = Thread.currentThread().getName();
                            bitmapUtils.saveBitmap(nameCount + "-" + timeStamp + "-" + "Bitmap"+"-"+bWidth+","+bHight+"-"+sampleSize+"-"+ bmp.getWidth() + "," + bmp.getHeight() + "-"+threadName+"-"+ "decodeFile(pathName,opts)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                            nameCount++;
                        }else {
                            XposedBridge.log("helloHook--null--decodeFile(String pathName, BitmapFactory.Options opts)");
                        }
                    }
                });
            }catch (Exception e){
                XposedBridge.log("===not method===decodeFile(String pathName, BitmapFactory.Options opts)" );
                e.printStackTrace();
            }


            String targetClass5="android.graphics.BitmapFactory";
            String targetMethod5="decodeFileDescriptor";
            try {
                XposedHelpers.findAndHookMethod(targetClass5, lpparam.classLoader, targetMethod5, FileDescriptor.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----decodeFileDescriptor(FileDescriptor fd)");
                        Bitmap bmp = (Bitmap) param.getResult();

                        XposedBridge.log("helloHook----bitmapSet:   " + bmp.getWidth() + "   " + bmp.getHeight());
                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        String threadName = Thread.currentThread().getName();
                        bitmapUtils.saveBitmap(nameCount + "-" + timeStamp + "-" + "Bitmap" + "-"+bmp.getWidth()+","+bmp.getHeight()+"-" +threadName+"-"+ "decodeFileDescriptor(fd)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                        nameCount++;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass6="android.graphics.BitmapFactory";
            String targetMethod6="decodeFileDescriptor";
            try {
                XposedHelpers.findAndHookMethod(targetClass6, lpparam.classLoader, targetMethod6, FileDescriptor.class, Rect.class, BitmapFactory.Options.class, new XC_MethodHook() {
                    int bHight;
                    int bWidth;
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("beforeHookedMethod----decodeFileDescriptor(FileDescriptor fd, Rect outPadding, BitmapFactory.Options opts)");
                        BitmapFactory.Options arg1 = (BitmapFactory.Options) param.args[2];
                        bHight=arg1.outHeight;
                        bWidth=arg1.outWidth;
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----decodeFileDescriptor(FileDescriptor fd, Rect outPadding, BitmapFactory.Options opts)");
                        Bitmap bmp = (Bitmap) param.getResult();

                        if(bmp!=null) {
                            XposedBridge.log("helloHook----bitmapSet:   " + bmp.getWidth() + "   " + bmp.getHeight());
                            long timeStamp = System.currentTimeMillis();
                            BitmapFactory.Options arg = (BitmapFactory.Options) param.args[2];
                            int sampleSize=arg.inSampleSize;

                            String[] stackInfo=getTraceInfo();
                            String threadName = Thread.currentThread().getName();
                            bitmapUtils.saveBitmap(nameCount + "-" + timeStamp + "-" + "Bitmap"+"-"+bWidth+","+bHight+"-"+sampleSize+"-"+ bmp.getWidth() + "," + bmp.getHeight() + "-" +threadName+"-"+ "decodeFileDescriptor(fd, outPadding, opts)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                            nameCount++;
                        }else {
                            XposedBridge.log("helloHook--null--decodeFileDescriptor(FileDescriptor fd, Rect outPadding, BitmapFactory.Options opts)");

                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }


            String targetClass7="android.graphics.BitmapFactory";
            String targetMethod7="decodeByteArray";
            try {
                XposedHelpers.findAndHookMethod(targetClass7, lpparam.classLoader, targetMethod7, byte[].class, int.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----decodeByteArray(byte[] data, int offset, int length)");
                        Bitmap bmp = (Bitmap) param.getResult();

                        XposedBridge.log("helloHook----bitmapSet:   " + bmp.getWidth() + "   " + bmp.getHeight());
                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        String threadName = Thread.currentThread().getName();
                        bitmapUtils.saveBitmap(nameCount + "-" + timeStamp + "-" + "Bitmap" + "-"+bmp.getWidth()+","+bmp.getHeight()+"-" +threadName+"-"+ "decodeByteArray(data, offset,length)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                        nameCount++;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass8="android.graphics.BitmapFactory";
            String targetMethod8="decodeByteArray";
            try {
                XposedHelpers.findAndHookMethod(targetClass8, lpparam.classLoader, targetMethod8, byte[].class, int.class, int.class, BitmapFactory.Options.class
                        , new XC_MethodHook() {

                            int bHight;
                            int bWidth;
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log("beforeHookedMethod----decodeByteArray(byte[] data, int offset, int length, BitmapFactory.Options opts)");
                                BitmapFactory.Options arg1 = (BitmapFactory.Options) param.args[3];
                                bHight=arg1.outHeight;
                                bWidth=arg1.outWidth;
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log("helloHook----decodeByteArray(byte[] data, int offset, int length, BitmapFactory.Options opts)");
                                Bitmap bmp = (Bitmap) param.getResult();

                                if(bmp!=null) {
                                    XposedBridge.log("helloHook----bitmapSet:   " + bmp.getWidth() + "   " + bmp.getHeight());
                                    long timeStamp = System.currentTimeMillis();
                                    BitmapFactory.Options arg = (BitmapFactory.Options) param.args[3];
                                    int sampleSize=arg.inSampleSize;

                                    String[] stackInfo=getTraceInfo();
                                    String threadName = Thread.currentThread().getName();
                                    bitmapUtils.saveBitmap(nameCount + "-" + timeStamp + "-" + "Bitmap"+"-"+bWidth+","+bHight+"-"+sampleSize+"-"+ bmp.getWidth() + "," + bmp.getHeight() + "-" +threadName+"-"+ "decodeByteArray(data, offset, length, opts)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                                    nameCount++;
                                }else {
                                    XposedBridge.log("helloHook--null--decodeByteArray(byte[] data, int offset, int length, BitmapFactory.Options opts)");

                                }
                            }
                        });
            }catch (Exception e){
                e.printStackTrace();
            }


            String targetClass9="android.graphics.BitmapFactory";
            String targetMethod9="decodeStream";
            try {
                XposedHelpers.findAndHookMethod(targetClass9, lpparam.classLoader, targetMethod9, InputStream.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----decodeStream(InputStream is)");
                        Bitmap bmp = (Bitmap) param.getResult();


                        XposedBridge.log("helloHook----bitmapSet:   " + bmp.getWidth() + "   " + bmp.getHeight());
                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        String threadName = Thread.currentThread().getName();
                        bitmapUtils.saveBitmap(nameCount + "-" + timeStamp + "-" + "Bitmap" + "-"+bmp.getWidth()+","+bmp.getHeight()+"-" +threadName+"-"+"decodeStream(is)"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                        nameCount++;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }


            String targetClass10="android.graphics.BitmapFactory";
            String targetMethod10="decodeStream";
            try {
                XposedHelpers.findAndHookMethod(targetClass10, lpparam.classLoader, targetMethod10, InputStream.class, Rect.class, BitmapFactory.Options.class, new XC_MethodHook() {
                    int bHight;
                    int bWidth;
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("beforeHookedMethod----decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts)");
                        BitmapFactory.Options arg1 = (BitmapFactory.Options) param.args[2];
                        bHight=arg1.outHeight;
                        bWidth=arg1.outWidth;
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts)");
                        Bitmap bmp = (Bitmap) param.getResult();

                        if(bmp!=null) {
                            XposedBridge.log("helloHook----bitmapSet:   " + bmp.getWidth() + "   " + bmp.getHeight());
                            long timeStamp = System.currentTimeMillis();
                            BitmapFactory.Options arg = (BitmapFactory.Options) param.args[2];
                            int sampleSize=arg.inSampleSize;

                            String[] stackInfo=getTraceInfo();
                            String threadName = Thread.currentThread().getName();
                            bitmapUtils.saveBitmap(nameCount + "-" + timeStamp + "-" + "Bitmap" + "-"+bWidth+","+bHight+"-"+sampleSize+"-"+ bmp.getWidth() + "," + bmp.getHeight() + "-" +threadName+"-"+ "decodeStream(is, outPadding, opts)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                            nameCount++;
                        }else {
                            XposedBridge.log("helloHook--null--decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts)");

                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }


            String targetClass11="android.graphics.BitmapRegionDecoder";
            String targetMethod11="decodeRegion";
            try {
                XposedHelpers.findAndHookMethod(targetClass11, lpparam.classLoader, targetMethod11, Rect.class, BitmapFactory.Options.class, new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        XposedBridge.log("helloHook----decodeRegion (Rect rect, BitmapFactory.Options options)");
                        Bitmap bmp = (Bitmap) param.getResult();

                        if(bmp!=null) {
                            XposedBridge.log("helloHook----bitmapSet:   " + bmp.getWidth() + "   " + bmp.getHeight());
                            long timeStamp = System.currentTimeMillis();
                            String[] stackInfo=getTraceInfo();

                            Rect arg = (Rect) param.args[1];
                            BitmapFactory.Options arg1 = (BitmapFactory.Options) param.args[1];
                            int sampleSize=arg1.inSampleSize;

                            int inHight=arg.height();
                            int inWidth=arg.width();

                            String threadName = Thread.currentThread().getName();
                            bitmapUtils.saveBitmap(nameCount + "-" + timeStamp + "-" + "Bitmap" + "-"+inWidth+","+inHight+"-" +sampleSize+"-"+ bmp.getWidth() + "," + bmp.getHeight() + "-" +threadName+"-"+ "decodeRegion (rect, options)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                            nameCount++;
                        }else {
                            XposedBridge.log("helloHook--null--decodeRegion (Rect rect, BitmapFactory.Options options)");
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass12="android.graphics.drawable.Drawable";
            String targetMethod12="createFromPath";
            try {
                XposedHelpers.findAndHookMethod(targetClass12, lpparam.classLoader, targetMethod12, String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----createFromPath (String pathName)");
                        Drawable bmp = (Drawable) param.getResult();
                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        String threadName = Thread.currentThread().getName();
                        bitmapUtils.saveDrawable(nameCount + "-" + timeStamp + "-" + "Drawable" + "-"+((BitmapDrawable) bmp).getBitmap().getWidth()+","+((BitmapDrawable) bmp).getBitmap().getHeight()+"-" +threadName+"-"+ "createFromPath (pathName)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                        nameCount++;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass13="android.graphics.drawable.Drawable";
            String targetMethod13="createFromStream";
            try {
                XposedHelpers.findAndHookMethod(targetClass13, lpparam.classLoader, targetMethod13, InputStream.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----createFromStream (InputStream is,String srcName)");
                        Drawable bmp = (Drawable) param.getResult();
                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        String threadName = Thread.currentThread().getName();
                        bitmapUtils.saveDrawable(nameCount + "-" + timeStamp + "-" + "Drawable" + "-"+((BitmapDrawable) bmp).getBitmap().getWidth()+","+((BitmapDrawable) bmp).getBitmap().getHeight()+"-" +threadName+"-"+"createFromStream(is,srcName)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                        nameCount++;

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass14="android.widget.ImageView";
            String targetMethod14="setImageURI";
            try {
                XposedHelpers.findAndHookMethod(targetClass14, lpparam.classLoader, targetMethod14, Uri.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----setImageURI (Uri uri)");
                        XposedBridge.log("afterHookedMethod uri:" + param.args[0]);
                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        String threadName = Thread.currentThread().getName();
                        bitmapUtils.saveUrl(nameCount + "-" + timeStamp + "-" + "url" + "-" +threadName+"-"+ "setImageURI"+"-"+stackInfo[0]+"-"+stackInfo[1], param.args[0].toString());
                        nameCount++;

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }


            String targetClass15="android.widget.RemoteViews";
            String targetMethod15="setImageViewUri";
            try {
                XposedHelpers.findAndHookMethod(targetClass15, lpparam.classLoader, targetMethod15, int.class, Uri.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----setImageViewUri (int viewId, Uri uri)");
                        XposedBridge.log("afterHookedMethod uri:" + param.args[1]);
                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        String threadName = Thread.currentThread().getName();
                        bitmapUtils.saveUrl(nameCount + "-" + timeStamp + "-" + "url" + "-" +threadName+"-"+ "setImageViewUri"+"-"+stackInfo[0]+"-"+stackInfo[1], param.args[1].toString());
                        nameCount++;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            //TODO 这里开始获得displayed的bitmap和控件的大小
            String targetClass16="android.widget.ImageView";
            String targetMethod16="setImageDrawable";
            try {
                XposedHelpers.findAndHookMethod(targetClass16, lpparam.classLoader, targetMethod16, Drawable.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----setImageDrawable(Drawable drawable)");
                        Drawable bmp=(Drawable) param.args[0];

                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        //TODO 如何得到调用这个方面的类的信息呢？
                        ImageView imageView=(ImageView) param.thisObject;
                        float widgetW=imageView.getRight()-imageView.getLeft();
                        float widgetH=imageView.getBottom()-imageView.getTop();
                        bitmapUtils.saveDisplayedDrawable(nameCount + "-" + timeStamp + "-" + "displayingDrawable(setImageDrawable)" + "-"+widgetW+","+widgetH+ "-"+((BitmapDrawable) bmp).getBitmap().getWidth()+","+((BitmapDrawable) bmp).getBitmap().getHeight()+"-" +"createFromStream(is,srcName)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                        nameCount++;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass17="android.widget.ImageView";
            String targetMethod17="setImageBitmap";
            try {
                XposedHelpers.findAndHookMethod(targetClass17, lpparam.classLoader, targetMethod17, Bitmap.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----setImageBitmap (Bitmap bm)");
                        Bitmap bmp=(Bitmap) param.args[0];
                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();
                        //TODO 如何得到调用这个方面的类的信息呢？
                        ImageView imageView=(ImageView) param.thisObject;
                        float widgetW=imageView.getRight()-imageView.getLeft();
                        float widgetH=imageView.getBottom()-imageView.getTop();
                        bitmapUtils.saveDisplayedBitmap(nameCount + "-" + timeStamp + "-" + "displayingBitmap(setImageBitmap)" + "-"+widgetW+","+widgetH+ "-"+bmp.getWidth()+","+bmp.getHeight()+"-" +"createFromStream(is,srcName)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                        nameCount++;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass18="android.graphics.Canvas";
            String targetMethod18="drawBitmap";
            try {
                XposedHelpers.findAndHookMethod(targetClass18, lpparam.classLoader, targetMethod18,Bitmap.class, Matrix.class,Paint.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----drawBitmap (Bitmap bm, Matrix, Paint)");
                        Bitmap bmp=(Bitmap) param.args[0];

                        long timeStamp = System.currentTimeMillis();
                        String[] stackInfo=getTraceInfo();

                        Canvas canvas=(Canvas) param.thisObject;
                        float widgetW=canvas.getWidth();
                        float widgetH=canvas.getHeight();
                        bitmapUtils.saveDisplayedBitmap(nameCount + "-" + timeStamp + "-" + "displayingBitmap(drawBitmap)" + "-"+widgetW+","+widgetH+ "-"+bmp.getWidth()+","+bmp.getHeight()+"-" +"createFromStream(is,srcName)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
                        nameCount++;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

//            String targetClass19="android.widget.RemoteViews";
//            String targetMethod19="setBitmap";
//            try {
//                XposedHelpers.findAndHookMethod(targetClass19, lpparam.classLoader, targetMethod19,int.class, String.class,Bitmap.class, new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("helloHook----setBitmap ()");
//                        Bitmap bmp=(Bitmap) param.args[2];
//
//                        long timeStamp = System.currentTimeMillis();
//                        String[] stackInfo=getTraceInfo();
//
//                        RemoteViews remoteViews=(RemoteViews) param.thisObject;
//                        float widgetW=remoteViews.getLayoutId().getWidth();
//                        float widgetH=remoteViews.getHeight();
//                        bitmapUtils.saveDisplayedBitmap(nameCount + "-" + timeStamp + "-" + "displayingBitmap(setBitmap)" + "-"+widgetW+","+widgetH+ "-"+bmp.getWidth()+","+bmp.getHeight()+"-" +"createFromStream(is,srcName)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
//                        nameCount++;
//                    }
//                });
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            String targetClass20="android.widget.RemoteViews";
//            String targetMethod20="setImageViewBitmap";
//            try {
//                XposedHelpers.findAndHookMethod(targetClass20, lpparam.classLoader, targetMethod20,int.class, Bitmap.class, new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("helloHook----setImageViewBitmap ()");
//                        Bitmap bmp=(Bitmap) param.args[1];
//
//                        long timeStamp = System.currentTimeMillis();
//                        String[] stackInfo=getTraceInfo();
//
//                        Canvas canvas=(Canvas) param.thisObject;
//                        float widgetW=canvas.getWidth();
//                        float widgetH=canvas.getHeight();
//                        bitmapUtils.saveDisplayedBitmap(nameCount + "-" + timeStamp + "-" + "displayingBitmap(setImageViewBitmap)" + "-"+widgetW+","+widgetH+ "-"+bmp.getWidth()+","+bmp.getHeight()+"-" +"createFromStream(is,srcName)"+"-"+stackInfo[0]+"-"+stackInfo[1], bmp, stackInfo[2]);
//                        nameCount++;
//                    }
//                });
//            }catch (Exception e){
//                e.printStackTrace();
//            }


//            String targetClass18="android.widget.ImageView";
//            String targetMethod18="setImageIcon";

//            String targetClass19="android.widget.ImageView";
//            String targetMethod19="getBackground";
//
//            String targetClass20="android.view.View";
//            String targetMethod20="getBackground";
            //setBackground(Drawable background)


            String targetClass21="android.graphics.Bitmap";
            String targetMethod21="createBitmap";
            try {
                XposedHelpers.findAndHookMethod(targetClass21, lpparam.classLoader, targetMethod21, Bitmap.class, int.class,int.class,int.class,int.class,new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----createBitmap1 (Bitmap source, \n" +
                                "                int x, \n" +
                                "                int y, \n" +
                                "                int width, \n" +
                                "                int height)");
                        String apiName="createBitmap1";
                        Bitmap inputBitmap=(Bitmap) param.args[0];
                        long timeStamp1 = System.currentTimeMillis();
                        String[] stackInfo1=getTraceInfo();

                        String imageName1=nameCount + "-" + timeStamp1 + "-" + "inputBitmap" + "-"+"createBitmap1"+ "-"+inputBitmap.getWidth()+","+inputBitmap.getHeight()+"-"+stackInfo1[0]+"-"+stackInfo1[1];
                        bitmapUtils.saveDisplayedBitmap(imageName1, inputBitmap, stackInfo1[2]);


                        Bitmap outputBitmap = (Bitmap) param.getResult();
                        XposedBridge.log("helloHook----bitmapSet:   " + outputBitmap.getWidth() + "   " + outputBitmap.getHeight());
                        long timeStamp2 = System.currentTimeMillis();
                        String[] stackInfo2=getTraceInfo();
                        String imageName2=nameCount + "-" + timeStamp2 + "-" + "outputBitmap" + "-"+"createBitmap1"+ "-"+outputBitmap.getWidth()+","+outputBitmap.getHeight()+"-"+stackInfo2[0]+"-"+stackInfo2[1];
                        bitmapUtils.saveBitmap(imageName2, outputBitmap, stackInfo2[2]);
                        nameCount++;

                        bitmapUtils.saveTrace(nameCount+"Trace",imageName1,apiName,imageName2);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass22="android.graphics.Bitmap";
            String targetMethod22="createBitmap";
            try {
                XposedHelpers.findAndHookMethod(targetClass22, lpparam.classLoader, targetMethod22, int[].class, int.class,int.class,Bitmap.Config.class,new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----createBitmap2 (int[] colors, \n" +
                                "                int width, \n" +
                                "                int height, \n" +
                                "                Bitmap.Config config)");
                        String apiName="createBitmap2";
                        Bitmap inputBitmap=(Bitmap) param.args[0];
                        long timeStamp1 = System.currentTimeMillis();
                        String[] stackInfo1=getTraceInfo();

                        String imageName1=nameCount + "-" + timeStamp1 + "-" + "inputBitmap" + "-"+"createBitmap1"+ "-"+inputBitmap.getWidth()+","+inputBitmap.getHeight()+"-"+stackInfo1[0]+"-"+stackInfo1[1];
                        bitmapUtils.saveDisplayedBitmap(imageName1, inputBitmap, stackInfo1[2]);


                        Bitmap outputBitmap = (Bitmap) param.getResult();
                        XposedBridge.log("helloHook----bitmapSet:   " + outputBitmap.getWidth() + "   " + outputBitmap.getHeight());
                        long timeStamp2 = System.currentTimeMillis();
                        String[] stackInfo2=getTraceInfo();
                        String imageName2=nameCount + "-" + timeStamp2 + "-" + "outputBitmap" + "-"+"createBitmap1"+ "-"+outputBitmap.getWidth()+","+outputBitmap.getHeight()+"-"+stackInfo2[0]+"-"+stackInfo2[1];
                        bitmapUtils.saveBitmap(imageName2, outputBitmap, stackInfo2[2]);
                        nameCount++;

                        bitmapUtils.saveTrace(nameCount+"Trace",imageName1,apiName,imageName2);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass23="android.graphics.Bitmap";
            String targetMethod23="createBitmap";
            try {
                XposedHelpers.findAndHookMethod(targetClass23, lpparam.classLoader, targetMethod23, Bitmap.class, int.class,int.class,int.class,int.class,Matrix.class,boolean.class,new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----createBitmap3 (Bitmap source, \n" +
                                "                int x, \n" +
                                "                int y, \n" +
                                "                int width, \n" +
                                "                int height, \n" +
                                "                Matrix m, \n" +
                                "                boolean filter)");
                        String apiName="createBitmap3";
                        Bitmap inputBitmap=(Bitmap) param.args[0];
                        long timeStamp1 = System.currentTimeMillis();
                        String[] stackInfo1=getTraceInfo();

                        String imageName1=nameCount + "-" + timeStamp1 + "-" + "inputBitmap" + "-"+"createBitmap1"+ "-"+inputBitmap.getWidth()+","+inputBitmap.getHeight()+"-"+stackInfo1[0]+"-"+stackInfo1[1];
                        bitmapUtils.saveDisplayedBitmap(imageName1, inputBitmap, stackInfo1[2]);


                        Bitmap outputBitmap = (Bitmap) param.getResult();
                        XposedBridge.log("helloHook----bitmapSet:   " + outputBitmap.getWidth() + "   " + outputBitmap.getHeight());
                        long timeStamp2 = System.currentTimeMillis();
                        String[] stackInfo2=getTraceInfo();
                        String imageName2=nameCount + "-" + timeStamp2 + "-" + "outputBitmap" + "-"+"createBitmap1"+ "-"+outputBitmap.getWidth()+","+outputBitmap.getHeight()+"-"+stackInfo2[0]+"-"+stackInfo2[1];
                        bitmapUtils.saveBitmap(imageName2, outputBitmap, stackInfo2[2]);
                        nameCount++;

                        bitmapUtils.saveTrace(nameCount+"Trace",imageName1,apiName,imageName2);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass24="android.graphics.Bitmap";
            String targetMethod24="createBitmap";
            try {
                XposedHelpers.findAndHookMethod(targetClass24, lpparam.classLoader, targetMethod24, Bitmap.class,new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----createBitmap (Bitmap src)");
                        String apiName="createBitmap4";
                        Bitmap inputBitmap=(Bitmap) param.args[0];
                        long timeStamp1 = System.currentTimeMillis();
                        String[] stackInfo1=getTraceInfo();

                        String imageName1=nameCount + "-" + timeStamp1 + "-" + "inputBitmap" + "-"+"createBitmap1"+ "-"+inputBitmap.getWidth()+","+inputBitmap.getHeight()+"-"+stackInfo1[0]+"-"+stackInfo1[1];
                        bitmapUtils.saveDisplayedBitmap(imageName1, inputBitmap, stackInfo1[2]);


                        Bitmap outputBitmap = (Bitmap) param.getResult();
                        XposedBridge.log("helloHook----bitmapSet:   " + outputBitmap.getWidth() + "   " + outputBitmap.getHeight());
                        long timeStamp2 = System.currentTimeMillis();
                        String[] stackInfo2=getTraceInfo();
                        String imageName2=nameCount + "-" + timeStamp2 + "-" + "outputBitmap" + "-"+"createBitmap1"+ "-"+outputBitmap.getWidth()+","+outputBitmap.getHeight()+"-"+stackInfo2[0]+"-"+stackInfo2[1];
                        bitmapUtils.saveBitmap(imageName2, outputBitmap, stackInfo2[2]);
                        nameCount++;

                        bitmapUtils.saveTrace(nameCount+"Trace",imageName1,apiName,imageName2);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass25="android.graphics.Bitmap";
            String targetMethod25="createScaledBitmap";
            try {
                XposedHelpers.findAndHookMethod(targetClass25, lpparam.classLoader, targetMethod25, Bitmap.class, int.class,int.class,boolean.class,new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----createScaledBitmap (Bitmap src, \n" +
                                "                int dstWidth, \n" +
                                "                int dstHeight, \n" +
                                "                boolean filter)");
                        String apiName="createScaledBitmap";
                        Bitmap inputBitmap=(Bitmap) param.args[0];
                        long timeStamp1 = System.currentTimeMillis();
                        String[] stackInfo1=getTraceInfo();

                        String imageName1=nameCount + "-" + timeStamp1 + "-" + "inputBitmap" + "-"+"createBitmap1"+ "-"+inputBitmap.getWidth()+","+inputBitmap.getHeight()+"-"+stackInfo1[0]+"-"+stackInfo1[1];
                        bitmapUtils.saveDisplayedBitmap(imageName1, inputBitmap, stackInfo1[2]);


                        Bitmap outputBitmap = (Bitmap) param.getResult();
                        XposedBridge.log("helloHook----bitmapSet:   " + outputBitmap.getWidth() + "   " + outputBitmap.getHeight());
                        long timeStamp2 = System.currentTimeMillis();
                        String[] stackInfo2=getTraceInfo();
                        String imageName2=nameCount + "-" + timeStamp2 + "-" + "outputBitmap" + "-"+"createBitmap1"+ "-"+outputBitmap.getWidth()+","+outputBitmap.getHeight()+"-"+stackInfo2[0]+"-"+stackInfo2[1];
                        bitmapUtils.saveBitmap(imageName2, outputBitmap, stackInfo2[2]);
                        nameCount++;

                        bitmapUtils.saveTrace(nameCount+"Trace",imageName1,apiName,imageName2);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass26="com.bumptech.glide.load.engine.cache.MemorySizeCalculator";
            String targetMethod26="getMemoryCacheSize";
            try {
                XposedHelpers.findAndHookMethod(targetClass26, lpparam.classLoader, targetMethod26,new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----getMemoryCacheSize()");
                        int memorySize =(int) param.getResult();
                        XposedBridge.log("helloHook----memorySize:   " + memorySize);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            String targetClass27="com.bumptech.glide.load.engine.cache.MemorySizeCalculator";
            String targetMethod27="getMaxSize";
            try {
                XposedHelpers.findAndHookMethod(targetClass27, lpparam.classLoader, targetMethod27, ActivityManager.class,float.class,float.class,new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("helloHook----getMaxSize()");
                        int memorySize =(int) param.getResult();
                        XposedBridge.log("helloHook----getMaxSize:   " + memorySize);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

//            String targetClass28="com.bumptech.glide.load.engine.cache.MemorySizeCalculator";
//            String targetMethod28="isLow";
//            try {
//                XposedHelpers.findAndHookMethod(targetClass27, lpparam.classLoader, targetMethod27, ActivityManager.class,float.class,float.class,new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("helloHook----getMaxSize()");
//                        int memorySize =(int) param.getResult();
//                        XposedBridge.log("helloHook----getMaxSize:   " + memorySize);
//                    }
//                });
//            }catch (Exception e){
//                e.printStackTrace();
//            }

//            String targetClass26="android.graphics.Bitmap";
//            String targetMethod26="extractAlpha";
//            try {
//                XposedHelpers.findAndHookMethod(targetClass26, lpparam.classLoader, targetMethod26,new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("helloHook----extractAlpha ()");
//                        String apiName="createBitmap1";
//                        Bitmap inputBitmap=(Bitmap) param.args[0];
//                        long timeStamp1 = System.currentTimeMillis();
//                        String[] stackInfo1=getTraceInfo();
//
//                        String imageName1=nameCount + "-" + timeStamp1 + "-" + "inputBitmap" + "-"+"createBitmap1"+ "-"+inputBitmap.getWidth()+","+inputBitmap.getHeight()+"-"+stackInfo1[0]+"-"+stackInfo1[1];
//                        bitmapUtils.saveDisplayedBitmap(imageName1, inputBitmap, stackInfo1[2]);
//
//
//                        Bitmap outputBitmap = (Bitmap) param.getResult();
//                        XposedBridge.log("helloHook----bitmapSet:   " + outputBitmap.getWidth() + "   " + outputBitmap.getHeight());
//                        long timeStamp2 = System.currentTimeMillis();
//                        String[] stackInfo2=getTraceInfo();
//                        String imageName2=nameCount + "-" + timeStamp2 + "-" + "outputBitmap" + "-"+"createBitmap1"+ "-"+outputBitmap.getWidth()+","+outputBitmap.getHeight()+"-"+stackInfo2[0]+"-"+stackInfo2[1];
//                        bitmapUtils.saveBitmap(imageName2, outputBitmap, stackInfo2[2]);
//                        nameCount++;
//
//                        //存储一个文件信息
//                        bitmapUtils.saveTrace(nameCount+"Trace",imageName1,apiName,imageName2);
//                    }
//                });
//            }catch (Exception e){
//                e.printStackTrace();
//            }





            //TODO hook onresume()
            try {
                //XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
                XposedHelpers.findAndHookMethod(Activity.class,"onResume", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ?????????????????? <<<<<<<<<<<<<<<<<<<<<");
                        super.afterHookedMethod(param);
                        XposedBridge.log("hook onResume");
                        Activity activity = (Activity) param.thisObject;
                        FrameLayout decor = (FrameLayout) activity.getWindow().getDecorView();
                        //View decor = (View) activity.getWindow().getDecorView();
                        List<View> views=getAllChildViews(decor);
                        XposedBridge.log("view size: "+views.size());
                        for(int i=0;i<views.size();i++){
                            View view=views.get(i);
                            Log.i("ViewName", view.getClass().getName());
//                            ImageView imageView=(ImageView)views.get(i);
//                            imageView.getRight();
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "handleLoadPackage: error capturing context");
            }








//            //下面开始直接监控decodeSource方法的调用
//            String targetClass33="android.graphics.BitmapFactory";
//            String targetMethod33="decodeResource";
//            XposedHelpers.findAndHookMethod(targetClass33, lpparam.classLoader, targetMethod33,Resources.class, int.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    XposedBridge.log("liwenjie---decodeResource(Resources res, int id)");
//                    Bitmap bmp= (Bitmap) param.getResult();
//
//                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
//                    long timeStamp=System.currentTimeMillis();
//                    bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap"+"-"+"decodeResource",bmp);
//                    nameCount++;
//                }
//            });

//            //下面开始直接监控decodeSource方法的调用，
//
//            String targetClass34="android.graphics.BitmapFactory";
//            String targetMethod34="decodeResource";
//            XposedHelpers.findAndHookMethod(targetClass34, lpparam.classLoader, targetMethod34,Resources.class, int.class,BitmapFactory.Options.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    XposedBridge.log("liwenjie---decodeResource(Resources, int, BitmapFactory.Options)");
//                    Bitmap bmp= (Bitmap) param.getResult();
//
//                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
//                    long timeStamp=System.currentTimeMillis();
//                    bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap"+"-"+"decodeResource",bmp);
//                    nameCount++;
//                }
//            });


        }
    }

    public static ArrayList<View> getAllChildViews1(View v) {
        ArrayList<View> result = new ArrayList<View>();


        ViewGroup viewGroup = (ViewGroup) v;

        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildViews(child));

            result.addAll(viewArrayList);
        }
        return result;
    }


    public static ArrayList<View> getAllChildViews(View v) {

        if (v instanceof TextView) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TestView: " + (v instanceof ViewGroup));
            Log.d(TAG, v.getClass().getCanonicalName());
            Log.d(TAG, v.getClass().getName());
            Log.d(TAG, v.getClass().getSimpleName());
            Log.d(TAG, v.getClass().getDeclaringClass().getName());
        }
        if (v instanceof ImageView) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ImageView: "+(v instanceof ViewGroup));
        }

        if (!(v instanceof ViewGroup)) {

            if (v instanceof TextView) {
                Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TestView " + ((TextView) v ).getText());
            }

            XposedBridge.log("!(v instanceof ViewGroup");
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildViews(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    public static String[] getTraceInfo(){
        String[] result={"null","null","null"};
        StringBuffer sb = new StringBuffer();
        StringBuffer stes = new StringBuffer();

        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        //TODO save stack info

        for(int i=0;i<stacks.length;i++)
        {
            stes.append(stacks[i].getClassName())
                    .append(";").append(stacks[i].getMethodName())
                    .append(";").append(stacks[i].getLineNumber())
                    .append("\n");

            XposedBridge.log("stack level "+i+" :" +stacks[i].getClassName()+"--"+ stacks[i].getMethodName()+"---"+stacks[i].getLineNumber());
            if(stacks[i].getClassName().equals("com.android.internal.os.ZygoteInit")&&stacks[i].getMethodName().equals("main")){
                result[0]="UI";
            }

            if(stacks[i].getClassName().equals("java.lang.Thread")&&stacks[i].getMethodName().equals("run")){
                result[0]="Background";
            }
            System.out.println("TraceInfo22>>>>>>>"+result[0]);
        }
        sb.append(stacks[6].getClassName())
                .append(";").append(stacks[6].getMethodName())
                .append(";").append(stacks[6].getLineNumber());
        result[1]=sb.toString();
        result[2]=stes.toString();


        //判断是否位于UI
        //java.lang.Thread--run
        //com.android.internal.os.ZygoteInit--main
//        String className=stacks[stacks.length-2].getClassName();
//        String methodName=stacks[stacks.length-2].getMethodName();
        //System.out.println("TraceInfo11>>>>>>>"+"stacks length:"+stacks.length+"  "+"classname: "+className+"   "+"methodName: "+methodName);
//        if(className.equals("com.android.internal.os.ZygoteInit")&&methodName.equals("main")){
//            result[0]="UI";
//        }
//
//        if(className.equals("java.lang.Thread")&&methodName.equals("run")){
//            result[0]="Background";
//        }
//
//        System.out.println("TraceInfo22>>>>>>>"+result[0]);


        return result;
    }


//    public static String getTraceInfo(){
//        StringBuffer sb = new StringBuffer();
//
//        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
//        for(int i=0;i<stacks.length;i++)
//        {
//            XposedBridge.log("stack level "+i+" :" +stacks[i].getClassName()+"--"+ stacks[i].getMethodName()+"---"+stacks[i].getLineNumber());
//        }
//        sb.append("3---").append(stacks[3].getClassName())
//                .append(";").append(stacks[3].getMethodName())
//                .append(";").append(stacks[3].getLineNumber())
//        .append("4---").append(stacks[4].getClassName())
//                .append(";").append(stacks[4].getMethodName())
//                .append(";").append(stacks[4].getLineNumber())
//        .append("5---").append(stacks[5].getClassName())
//                .append(";").append(stacks[5].getMethodName())
//                .append(";").append(stacks[5].getLineNumber())
//        .append("6---").append(stacks[6].getClassName())
//                .append(";").append(stacks[6].getMethodName())
//                .append(";").append(stacks[6].getLineNumber());
//        return sb.toString();
//    }
    //stacks[2]:   13-1605769807405-Bitmap-(300,196-decodeStream(is, outPadding, opts);de.robv.android.xposed.DexposedBridge;handleHookedArtMethod;265
    //3---me.weishu.epic.art.entry.Entry;onHookObject;69
    // 4---me.weishu.epic.art.entry.Entry;referenceBridge;186
    // 5---android.graphics.BitmapFactory;decodeFile;516
    // 6---java.lang.reflect.Method;invoke;-2


}