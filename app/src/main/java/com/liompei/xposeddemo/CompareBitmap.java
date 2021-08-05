package com.liompei.xposeddemo;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class CompareBitmap {

    public static List<Bitmap> bitmapSet = new ArrayList<Bitmap>();



    public boolean compare(Bitmap bmp){
        boolean result=false;
        boolean resultTmp;
        for(int i=0;i<bitmapSet.size();i++){
            Bitmap tmpBmp=bitmapSet.get(i);
            resultTmp=compareResult(bmp,tmpBmp);
            if(resultTmp){
                result=true;
                break;
            }
        }
        return result;
    }

    public boolean compareResult(Bitmap bmp1, Bitmap bmp2){
        boolean result;
        result=bmp1.sameAs(bmp2);
        return result;
    }
}
