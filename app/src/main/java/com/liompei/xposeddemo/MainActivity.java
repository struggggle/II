package com.liompei.xposeddemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private TextView tvTest;
    private ImageView iv;
    public BitmapUtils bitmapUtils=new BitmapUtils();
    private Button button = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTest = findViewById(R.id.tv_test);
        tvTest.setText("准备修改的值11");

        Runtime rt=Runtime.getRuntime();
        long maxMemory=rt.maxMemory();
        long startMemory=rt.totalMemory();
        System.out.println("start Memory size: "+Long.toString(startMemory/(1024*1024)));
        System.out.println("Memory size: "+Long.toString(maxMemory/(1024*1024)));



        //System.out.println("memorysize>>>>>>>>:"+Runtime.getRuntime().maxMemory()+"   "+Runtime.getRuntime().maxMemory()/6);

        obtainPermission();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        iv = (ImageView) findViewById(R.id.iv);

        Bitmap bmp1;
        int arg1=0x7f060056;//对应了drawable文件夹中新建一个"img2.png"或"img22.png"的图片
        bmp1=decoding1(arg1);
        System.out.println("lwj>>>>>>>>>>>>>>>>>>"+bmp1.getHeight()+"   "+bmp1.getWidth());

        iv.setImageBitmap(bmp1);
//        iv.setBackgroundColor(Color.RED);
//
//
//        int arg2=0x7f060056;
//        Bitmap bmp2;
//        bmp2=decoding2(arg2);
//        System.out.println("liwenjie--------Begin to compare");
//        if(bmp1.sameAs(bmp2)){
//            System.out.println("liwenjie--------The same bimtap");
//        }else{
//            System.out.println("liwenjie--------Not the same bimtap");
//        }
//
//        button = (Button)findViewById(R.id.button);
//        Drawable bd= new BitmapDrawable(bmp2);
//        button.setBackground(bd);
//
//        Context context = getApplicationContext();
//        createDumpFile(context);

    }

    public void obtainPermission(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    public static boolean createDumpFile(Context context) {
        System.out.println("call createDumpFile");
        String LOG_PATH = "/hprof/";
        boolean bool = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ssss");
        String createTime = sdf.format(new Date(System.currentTimeMillis()));
        String state = android.os.Environment.getExternalStorageState();
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + LOG_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            String hprofPath = file.getAbsolutePath();
            if (!hprofPath.endsWith("/")) {
                hprofPath += "/";
            }

            hprofPath += createTime + ".hprof";
            //hprofPath: /storage/emulated/0/hprof/2020-12-18_02.09.0043.hprof
            System.out.println("hprofPath: "+hprofPath);
            try {
                android.os.Debug.dumpHprofData(hprofPath);
                bool = true;
                Log.d("ANDROID_LAB", "create dumpfile done!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bool = false;
            Log.d("ANDROID_LAB", "nosdcard!");
        }

        return bool;
    }

    public Bitmap decoding1(int url){
        Bitmap bmp;
        bmp=BitmapFactory.decodeResource(this.getResources(), url);//0x7f060056
        //bitmapUtils.saveBitmap("decoding1",bmp);
        return bmp;
    }

    public Bitmap decoding2(int url){
        Bitmap bmp;
        bmp=BitmapFactory.decodeResource(this.getResources(), url);
        //bitmapUtils.saveBitmap("decoding2",bmp);
        return bmp;
    }

    public static int[] getRealImgShowSize(ImageView imageview){
        Rect rect=imageview.getDrawable().getBounds();
        //可见image的宽高
        int scaledHeight = rect.height();
        int scaledWidth = rect.width();
        //获得ImageView中Image的变换矩阵
        Matrix matrix= imageview.getImageMatrix();
        float[] values = new float[10];
        matrix.getValues(values);
        //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
        float sx = values[0];
        float sy = values[4];
        //计算Image在屏幕上实际绘制的宽高
        int realImgShowWidth =(int) (scaledWidth * sx);
        int realImgShowHeight =(int)( scaledHeight * sy);
        int[] size=new int[]{realImgShowWidth,realImgShowHeight};
        return size;
    }

    public static int[] getRealImgShowSize(Button button){
        Drawable drawable=button.getBackground();
        if(drawable==null){
            System.out.println("button drawable is null!");
        }else {
            System.out.println("button drawable is not null: "+drawable.getIntrinsicHeight()+","+drawable.getIntrinsicWidth());
        }

        Rect rect=button.getBackground().getBounds();

        int scaledHeight = rect.height();
        int scaledWidth = rect.width();
        Matrix matrix= button.getMatrix();
        float[] values = new float[10];
        matrix.getValues(values);
        float sx = values[0];
        float sy = values[4];
        int realImgShowWidth =(int) (scaledWidth * sx);
        int realImgShowHeight =(int)( scaledHeight * sy);
        int[] size=new int[]{realImgShowWidth,realImgShowHeight};
        return size;
    }


    /**
     * Returns the bitmap position inside an imageView.
     * @param imageView source ImageView
     * @return 0: left, 1: top, 2: width, 3: height
     */
    public static int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public static int[] getBitmapPositionInsideButton(Button imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getBackground() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getBackground();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }



    @Override
    protected void onResume() {
        super.onResume();

        iv.post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable1 = button.getBackground();
                Matrix matrix1 = button.getMatrix();
                if (drawable1 != null) {
                    RectF rectf = new RectF();
                    rectf.set(0, 0, drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight());
                    System.out.println(drawable1.getIntrinsicWidth()+","+drawable1.getIntrinsicHeight());
                    Context context = getApplicationContext();
                    matrix1.mapRect(rectf);     //最关键的一句
                    System.out.println("button image real size--  " + rectf.left + "  " + rectf.top + "  " + rectf.right + "  " + rectf.bottom);
                    float width1=rectf.right-rectf.left;
                    float higth1=rectf.bottom-rectf.top;
                    System.out.println("size: ("+width1+","+higth1+")");

                }
                DisplayMetrics dm = new DisplayMetrics();
                System.out.println("button size："+button.getLeft()+"  Right："+button.getRight()+"  Top："+button.getTop()+"  Bottom："+button.getBottom());
                float width2=button.getRight()-button.getLeft();
                float higth2=button.getBottom()-button.getTop();
                System.out.println("size: ("+width2+","+higth2+")");
                int[] result=getRealImgShowSize(button);
                System.out.println("button image's displayed size: "+result[0]+","+result[1]);
                System.out.println("size: ("+result[0]+","+result[1]+")");

                if((result[0]==0)&&(result[1]==0)){
                    System.out.println("size: ("+width2+","+higth2+")");
                }

                //TODO -------------------------------------------------------------------------------
                System.out.println("-------------------------------------------------------------------------------");

                Drawable drawable = iv.getDrawable();

                //下面的输出是：imageView image real size--  0.0  118.264465  1080.0  961.73553
//                Matrix matrix = iv.getImageMatrix();
//                if (drawable != null) {
//                    RectF rectf = new RectF();
//                    rectf.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                    System.out.println(drawable.getIntrinsicWidth()+","+drawable.getIntrinsicHeight());
//                    Context context = getApplicationContext();
//                    matrix.mapRect(rectf);     //最关键的一句
//                    System.out.println("imageView image real size--  " + rectf.left + "  " + rectf.top + "  " + rectf.right + "  " + rectf.bottom);
//                    float width1=rectf.right-rectf.left;
//                    float higth1=rectf.bottom-rectf.top;
//                    System.out.println("size: ("+width1+","+higth1+")");
//
//                }
                //实际的图片大小
                dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                System.out.println("imageView image real size--:" + drawable.getBounds().top/dm.density+","+drawable.getBounds().left/dm.density+"--"+drawable.getBounds().bottom/dm.density+","+drawable.getBounds().right/dm.density);
                float width1=drawable.getBounds().right/dm.density-drawable.getBounds().left/dm.density;
                float higth1=drawable.getBounds().bottom/dm.density-drawable.getBounds().top/dm.density;
                System.out.println("size: ("+width1+","+higth1+")");


                System.out.println("imageview size："+iv.getLeft()+"  Right："+iv.getRight()+"  Top："+iv.getTop()+"  Bottom："+iv.getBottom());
                float width4=iv.getRight()-iv.getLeft();
                float higth4=iv.getBottom()-iv.getTop();
                System.out.println("size: ("+width4+","+higth4+")");


                int[] result1=getRealImgShowSize(iv);
                System.out.println("imageview image's displayed size: "+result1[0]+","+result1[1]);
                System.out.println("size: ("+result1[0]+","+result1[1]+")");
            }
        });
    }
}
