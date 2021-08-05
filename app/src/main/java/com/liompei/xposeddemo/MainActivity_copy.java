package com.liompei.xposeddemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 这里是包含所有尝试的代码版本
* **/
public class MainActivity_copy extends AppCompatActivity {

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

        iv = (ImageView) findViewById(R.id.iv);

        Bitmap bmp1;
        int arg1=0x7f060056;
        bmp1=decoding1(arg1);
        System.out.println("imagesize1: "+bmp1.getWidth()+","+bmp1.getHeight());

        iv.setImageBitmap(bmp1);
        iv.setBackgroundColor(Color.RED);

        int[] re= getBitmapPositionInsideImageView(iv);
        System.out.println("position: "+re[0]+"--"+re[1]);



        System.out.println("获得坐标：");
        Rect viewRect = new Rect();
        iv.getGlobalVisibleRect(viewRect);
        System.out.println(viewRect.toString());

        //输出设备的像素密度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        System.out.println("dm.density:"+dm.density);
        System.out.println("dm.densityDpi:"+dm.densityDpi);

        //-----------这里是imageView的
        Drawable drawable = iv.getDrawable();
        Matrix matrix = iv.getImageMatrix();
        Rect rect=drawable.getBounds();
        System.out.println("bounds:  "+rect.bottom+","+rect.left+","+rect.right+","+rect.top);
        System.out.println("test: "+drawable.getIntrinsicHeight());

        if (drawable != null) {
            RectF rectf = new RectF();
            rectf.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            System.out.println("getIntrinsicWidth:"+drawable.getIntrinsicWidth()+","+drawable.getIntrinsicHeight());
            Context context = getApplicationContext();
            matrix.mapRect(rectf);     //最关键的一句
            System.out.println("imageview---  " + rectf.left + "  " + rectf.top + "  " + rectf.right + "  " + rectf.bottom);
        }

//        Rect rect=iv.getDrawable().getBounds();
//        //可见image的宽高
//        int scaledHeight = rect.height();
//        int scaledWidth = rect.width();
//        System.out.println("scaledHeight: "+scaledHeight+"--"+scaledWidth);


        int arg2=0x7f060056;
        Bitmap bmp2;
        bmp2=decoding2(arg2);
        System.out.println("imagesize2: "+bmp2.getWidth()+","+bmp2.getHeight());
        System.out.println("liwenjie--------Begin to compare");
        if(bmp1.sameAs(bmp2)){
            System.out.println("liwenjie--------The same bimtap");
        }else{
            System.out.println("liwenjie--------Not the same bimtap");
        }

        //点击监听
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new buttonListener());
        Drawable bd= new BitmapDrawable(bmp2);
        button.setBackground(bd);

        //TODO 可见image的宽高(图片实际的尺寸)------这里是button的
        Drawable drawable1 = button.getBackground();
        Matrix matrix1 = button.getMatrix();
        if (drawable1 != null) {
            RectF rectf = new RectF();
            rectf.set(0, 0, drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight());
            System.out.println(drawable1.getIntrinsicWidth()+","+drawable1.getIntrinsicHeight());
            Context context = getApplicationContext();
            matrix1.mapRect(rectf);     //最关键的一句
            System.out.println("button--  " + rectf.left + "  " + rectf.top + "  " + rectf.right + "  " + rectf.bottom);

        }

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
//        String img="/Users/wenjieli/Desktop/match/WechatIMG211.jpeg ";
//        bmp=BitmapFactory.decodeFile(img);
        //bitmapUtils.saveBitmap("decoding2",bmp);

//        System.out.println("lwj----：");
//        Rect viewRect = new Rect();
//        iv.getGlobalVisibleRect(viewRect);
//        System.out.println(viewRect.toString());
//
//        int[] posXY = new int[2];
//        iv.getLocationOnScreen(posXY);
//        int x = posXY[0];
//        int y = posXY[1];
//        System.out.println(x+"-----"+y);


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

    public static int[] getRealImgShowSize(Button imageview){
        Rect rect=imageview.getBackground().getBounds();
        //可见image的宽高
        int scaledHeight = rect.height();
        int scaledWidth = rect.width();
        System.out.println("image:   "+scaledHeight+","+scaledWidth);
        //获得ImageView中Image的变换矩阵
        Matrix matrix= imageview.getMatrix();
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

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
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


    public class buttonListener implements View.OnClickListener {

        public void onClick(View v)
        {
            Rect rect=iv.getDrawable().getBounds();
        //可见image的宽高
        int scaledHeight = rect.height();
        int scaledWidth = rect.width();
        System.out.println("scaledHeight: "+scaledHeight+"--"+scaledWidth);

            Rect rect1=button.getBackground().getBounds();
            //可见image的宽高
            int scaledHeight1 = rect1.height();
            int scaledWidth1 = rect1.width();
            System.out.println("scaledHeight1: "+scaledHeight1+"--"+scaledWidth1);


            //获得显示的图片的大小
//            Drawable drawable = iv.getDrawable();
//            Matrix matrix = iv.getImageMatrix();
//            Rect rect=drawable.getBounds();
//            System.out.println("bounds---:  "+rect.bottom+","+rect.left+","+rect.right+","+rect.top);
//
//            int[] result=getRealImgShowSize(iv);
//            System.out.println("result--: "+result[0]+","+result[1]);
//
//            int[] result1=getRealImgShowSize(button);
//            System.out.println("result1--: "+result[0]+","+result[1]);



            //这里获得的是控件的信息
            int[] location = new int[2];
            iv.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            System.out.println("x:"+x+"  y:"+y);
            System.out.println("图片各个角Left："+iv.getLeft()+"  Right："+iv.getRight()+"  Top："+iv.getTop()+"  Bottom："+iv.getBottom());
        }
    }

    //在用户看到 Activity 之前会调用 onStart()方法。当 Activity 进入前台时, Android在调用 onStart()之后就会调用onResume()
    //dm.density:2.625  这个是缩放比例
    //是不是可以记住每一个widget对象的name，然后直接以name作为参数来进行。
    @Override
    protected void onResume() {
        super.onResume();

        iv.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                System.out.println("dm.density:"+dm.density);
                System.out.println("dm.densityDpi:"+dm.densityDpi);

                Drawable drawable = iv.getDrawable();
                //实际的图片大小
                System.out.println("onresume image size:" + drawable.getBounds().top/dm.density+","+drawable.getBounds().left/dm.density+"--"+drawable.getBounds().bottom/dm.density+","+drawable.getBounds().right/dm.density);
                //控件大小
                int[] location = new int[2];
                iv.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                System.out.println("x:"+x+"  y:"+y);
                //TODO 这里输出的不是在屏幕的位置，而是widget的实际大小。
                System.out.println("onresume imageview size："+iv.getLeft()+"  Right："+iv.getRight()+"  Top："+iv.getTop()+"  Bottom："+iv.getBottom());

                //显示的图片在UI上面的大小
                int[] re= getBitmapPositionInsideImageView(iv);
                System.out.println("position: "+re[0]+"--"+re[1]+"--"+re[2]+"--"+re[3]);

                //这应该是控件的坐标
                System.out.println("获得坐标：");
                Rect viewRect = new Rect();
                iv.getGlobalVisibleRect(viewRect);
                System.out.println(viewRect.toString());

                Drawable drawable1 = button.getBackground();
                //实际的图片大小
                System.out.println("onresume image width=" + drawable.getBounds().top/dm.density+","+drawable.getBounds().left/dm.density+"--"+drawable.getBounds().bottom/dm.density+","+drawable.getBounds().right/dm.density);

                //控件大小
                int[] location1 = new int[2];
                button.getLocationOnScreen(location1);
                int x1 = location1[0];
                int y1 = location1[1];
                System.out.println("x:"+x1+"  y:"+y1);
                System.out.println("onresume imageview size："+button.getLeft()+"  Right："+button.getRight()+"  Top："+button.getTop()+"  Bottom："+button.getBottom());

                //显示的图片在UI上面的大小
                int[] re1= getBitmapPositionInsideButton(button);
                System.out.println("position: "+re1[0]+"--"+re1[1]+"--"+re1[2]+"--"+re1[3]);


                int[] result=getRealImgShowSize(iv);
                System.out.println("result--: "+result[0]+","+result[1]);
            }
        });
    }
}
