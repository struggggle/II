package com.liompei.xposeddemo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import static com.liompei.xposeddemo.CompareBitmap.bitmapSet;

public class CompareDrawable {
    public static List<Drawable> drawableSet = new ArrayList<Drawable>();



    public boolean compare(Drawable bmp,List<Drawable> drawableSet){
        boolean result=false;
        boolean resultTmp;
        for(int i=0;i<drawableSet.size();i++){
            Drawable tmpBmp=drawableSet.get(i);
            resultTmp=compareResult(bmp,tmpBmp);
            if(resultTmp){
                result=true;
                break;
            }
        }
        return result;
    }

    public boolean compareResult(Drawable bmp1, Drawable bmp2){
        boolean result;
        result=bmp1.getConstantState().equals(bmp2.getConstantState());
        return result;
    }
}
