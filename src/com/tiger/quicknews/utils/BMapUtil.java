package com.tiger.quicknews.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;

public class BMapUtil {
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromViews(View view) {
//        view.destroyDrawingCache();
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        view.setDrawingCacheEnabled(true);
//        Bitmap bitmap = view.getDrawingCache(true);
//        return bitmap;  
		int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);  
        view.measure(spec, spec);  
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());  
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),  
                Bitmap.Config.ARGB_8888); 
        Canvas c = new Canvas(b);  
        c.translate(-view.getScrollX(), -view.getScrollY());  
        view.draw(c);  
        view.setDrawingCacheEnabled(true);  
        Bitmap cacheBmp = view.getDrawingCache();  
        Bitmap bitmap = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);  
        view.destroyDrawingCache();  
        return bitmap;
	}
	
	  //将Drawable转化为Bitmap  
    public static Bitmap drawableToBitmap(Drawable drawable){  
           int width = drawable.getIntrinsicWidth();  
           int height = drawable.getIntrinsicHeight();  
           Bitmap bitmap = Bitmap.createBitmap(width, height,  
                   drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                           : Bitmap.Config.RGB_565);  
           Canvas canvas = new Canvas(bitmap);  
           drawable.setBounds(0,0,width,height);  
           drawable.draw(canvas);  
           return bitmap;  
             
       }  
      
    //获得圆角图片的方法  
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){  
         
       Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap  
               .getHeight(), Config.ARGB_8888);  
       Canvas canvas = new Canvas(output);  
  
       final int color = 0xff424242;  
       final Paint paint = new Paint();  
       final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
       final RectF rectF = new RectF(rect);  
  
       paint.setAntiAlias(true);  
       canvas.drawARGB(0, 0, 0, 0);  
       paint.setColor(color);  
       canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
  
       paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
       canvas.drawBitmap(bitmap, rect, rect, paint);  
  
       return output;  
   }  
}
