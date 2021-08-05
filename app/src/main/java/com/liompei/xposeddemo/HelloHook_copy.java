
package com.liompei.xposeddemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.InputStream;
import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

//编码修改日期：2020-10-25
public class HelloHook_copy implements IXposedHookLoadPackage {
    CompareBitmap compareBitmap=new CompareBitmap();
    CompareDrawable compareDrawable=new CompareDrawable();

    BitmapUtils bitmapUtils=new BitmapUtils();
    int nameCount=1;

    //保存要测试的app的包名
    String testAppNames= "org.wordpress.android" +
                    "com.newsblur+com.moez.QKSMS"+
                    "org.ligi.passandroid"+
                    "fr.neamar.kiss";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("===包名===" + lpparam.packageName);
//        Log.d("HelloHook", "===包名===" + lpparam.packageName);//log无法打印出输出到app，不知道到logcat中是否能打印出来。
        String targetApp="com.liompei.xposeddemo";
        //String targetApp="org.wordpress.android";//以后需要写一个配置文件然后进行信息读取。

        /**
         * 对于参数替换，这部分还需要进行进一步的分析和实现
         *这部分可能需要添加静态分析，感觉不会那么容易。
         * */
        String targetClass="com.liompei.xposeddemo.MainActivity";
        String targetMethod="onCreate";
        if (testAppNames.contains(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod(targetClass, lpparam.classLoader, targetMethod, Bundle.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("HelloHook----afterHookedMethod");
                    //thisObject，代表调用该方法的对象实例，如果是静态方法
                    //的话，返回一个Null，比如这里调用onCreate()方法的是MainActivity，获得
                    //的自然是MainActivity实例
                    Class aClass = param.thisObject.getClass();
                    Field[] fields = aClass.getDeclaredFields();

                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        XposedBridge.log("HelloHook-----参数名: " + field.getName() + ",类型: " + field.getType());
                    }

                    Field field = aClass.getDeclaredField("tvTest");
                    field.setAccessible(true);
                    TextView textView = (TextView) field.get(param.thisObject);
                    textView.setText("成功修改参数11");//这里是直接对value进行修改就可以了。

                }
            });


            String targetMethod1="decoding1";
            //这里注意修改findAndHookMethod中涉及的参数，根据要监控的app的method所涉及的参数类型分析得到
            XposedHelpers.findAndHookMethod(targetClass, lpparam.classLoader, targetMethod1,int.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decoding1");
                    Bitmap bmp= (Bitmap) param.getResult();
                    compareBitmap.bitmapSet.add(bmp);
                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    XposedBridge.log("helloHook----bitmapSet:   "+compareBitmap.bitmapSet.size());
                }
            });

            String targetMethod2="decoding2";
            XposedHelpers.findAndHookMethod(targetClass, lpparam.classLoader, targetMethod2,int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decoding1");
                    XposedBridge.log("helloHook----beforeHookedMethod");
                    int arg=(int) param.args[0];
                    XposedBridge.log("helloHook----arg[0]:  "+arg);

                    param.args[0]=0x7f060057;//这里用来直接修改参数的值
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decoding2");
                    Bitmap bmp= (Bitmap) param.getResult();


                    compareBitmap.bitmapSet.add(bmp);
                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    XposedBridge.log("helloHook----bitmapSet:   "+compareBitmap.bitmapSet.size());
                }
            });


            //下面开始直接监控decodeSource方法的调用
            String targetClass3="android.graphics.BitmapFactory";
            String targetMethod3="decodeFile";
            XposedHelpers.findAndHookMethod(targetClass3, lpparam.classLoader, targetMethod3,String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decodeFile(String pathName)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });

            String targetClass4="android.graphics.BitmapFactory";
            String targetMethod4="decodeFile";
            XposedHelpers.findAndHookMethod(targetClass4, lpparam.classLoader, targetMethod4,String.class, BitmapFactory.Options.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decodeFile(String pathName, BitmapFactory.Options opts)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });


            String targetClass5="android.graphics.BitmapFactory";
            String targetMethod5="decodeFileDescriptor";
            XposedHelpers.findAndHookMethod(targetClass5, lpparam.classLoader, targetMethod5,FileDescriptor.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decodeFileDescriptor(FileDescriptor fd)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });

            String targetClass6="android.graphics.BitmapFactory";
            String targetMethod6="decodeFileDescriptor";
            XposedHelpers.findAndHookMethod(targetClass6, lpparam.classLoader, targetMethod6,FileDescriptor.class,Rect.class,BitmapFactory.Options.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decodeFileDescriptor(FileDescriptor fd, Rect outPadding, BitmapFactory.Options opts)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });


            String targetClass7="android.graphics.BitmapFactory";
            String targetMethod7="decodeByteArray";
            XposedHelpers.findAndHookMethod(targetClass7, lpparam.classLoader, targetMethod7,byte[].class,int.class,int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decodeByteArray(byte[] data, int offset, int length)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });

            String targetClass8="android.graphics.BitmapFactory";
            String targetMethod8="decodeByteArray";
            XposedHelpers.findAndHookMethod(targetClass8, lpparam.classLoader, targetMethod8,byte[].class,int.class,int.class,BitmapFactory.Options.class
                    , new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decodeByteArray(byte[] data, int offset, int length, BitmapFactory.Options opts)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });


            String targetClass9="android.graphics.BitmapFactory";
            String targetMethod9="decodeStream";
            XposedHelpers.findAndHookMethod(targetClass9, lpparam.classLoader, targetMethod9,InputStream.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("helloHook----decodeStream(InputStream is)");
                            Bitmap bmp= (Bitmap) param.getResult();

                            XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                            long timeStamp=System.currentTimeMillis();
                            //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                            nameCount++;
                        }
             });


            String targetClass10="android.graphics.BitmapFactory";
            String targetMethod10="decodeStream";
            XposedHelpers.findAndHookMethod(targetClass10, lpparam.classLoader, targetMethod10,InputStream.class,Rect.class,BitmapFactory.Options.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });


            //TODO:  这里是否需要考虑decodeRegion?????
            String targetClass11="android.graphics.BitmapRegionDecoder";
            String targetMethod11="decodeRegion";
            XposedHelpers.findAndHookMethod(targetClass11, lpparam.classLoader, targetMethod11,Rect.class,BitmapFactory.Options.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----decodeRegion (Rect rect, BitmapFactory.Options options)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });

            String targetClass12="android.graphics.drawable.Drawable";
            String targetMethod12="createFromPath";
            XposedHelpers.findAndHookMethod(targetClass12, lpparam.classLoader, targetMethod12,String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----createFromPath (String pathName)");
                    Drawable bmp= (Drawable) param.getResult();
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveDrawable(nameCount+"-"+timeStamp+"-"+"Drawable",bmp);
                    nameCount++;
                }
            });

            String targetClass13="android.graphics.drawable.Drawable";
            String targetMethod13="createFromStream";
            XposedHelpers.findAndHookMethod(targetClass13, lpparam.classLoader, targetMethod13,InputStream.class,String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----createFromStream (InputStream is,String srcName)");
                    Drawable bmp= (Drawable) param.getResult();
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveDrawable(nameCount+"-"+timeStamp+"-"+"Drawable",bmp);
                    nameCount++;

                }
            });

            String targetClass14="android.widget.ImageView";
            String targetMethod14="setImageURI";
            XposedHelpers.findAndHookMethod(targetClass14, lpparam.classLoader, targetMethod14,Uri.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----setImageURI (Uri uri)");
                    XposedBridge.log("afterHookedMethod uri:" + param.args[0]);
                    long timeStamp=System.currentTimeMillis();
                    bitmapUtils.saveUrl(timeStamp+"-"+param.args[0].toString(),param.args[0].toString());

                }
            });

            String targetClass15="android.widget.RemoteViews";
            String targetMethod15="setImageViewUri";
            XposedHelpers.findAndHookMethod(targetClass15, lpparam.classLoader, targetMethod15,int.class, Uri.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("helloHook----setImageViewUri (int viewId, Uri uri)");
                    XposedBridge.log("afterHookedMethod uri:" + param.args[1]);
                    long timeStamp=System.currentTimeMillis();
                    bitmapUtils.saveUrl(timeStamp+"-"+param.args[1].toString(),param.args[1].toString());
                }
            });



            //下面开始直接监控decodeSource方法的调用
            String targetClass33="android.graphics.BitmapFactory";
            String targetMethod33="decodeResource";
            XposedHelpers.findAndHookMethod(targetClass33, lpparam.classLoader, targetMethod33,Resources.class, int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("liwenjie---decodeResource(Resources res, int id)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });

            //下面开始直接监控decodeSource方法的调用
            String targetClass34="android.graphics.BitmapFactory";
            String targetMethod34="decodeResource";
            XposedHelpers.findAndHookMethod(targetClass34, lpparam.classLoader, targetMethod34,Resources.class, int.class,BitmapFactory.Options.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("liwenjie---decodeResource(Resources, int, BitmapFactory.Options)");
                    Bitmap bmp= (Bitmap) param.getResult();

                    XposedBridge.log("helloHook----bitmapSet:   "+bmp.getWidth()+"   "+bmp.getHeight());
                    long timeStamp=System.currentTimeMillis();
                    //bitmapUtils.saveBitmap(nameCount+"-"+timeStamp+"-"+"Bitmap",bmp);
                    nameCount++;
                }
            });


        }
    }
}