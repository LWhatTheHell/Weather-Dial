package com.leommxj.zd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.jar.Attributes;

/**
 * Created by leommxj on 2016/9/14.
 */
public class ClockView extends View{
    private Paint hourPaint;
    private Paint minPaint;
    private Paint secPaint;
    private Paint clockPaint;
    private Handler updateHandler = new Handler(){
        public void handleMessage(Message msg){
            invalidate();
        }
    };
    private Thread timeThread = new Thread(){
        public void run(){
            try{while(true){
                updateHandler.sendEmptyMessage(0);
                Thread.sleep(1000);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    };

    private void init(){
        hourPaint = new Paint();
        hourPaint.setAntiAlias(true);
        hourPaint.setStrokeWidth(15);
        minPaint = new Paint();
        minPaint.setAntiAlias(true);
        minPaint.setStrokeWidth(10);
        secPaint = new Paint();
        secPaint.setAntiAlias(true);
        secPaint.setStrokeWidth(5);
        clockPaint = new Paint();
        clockPaint.setAntiAlias(true);
        clockPaint.setFilterBitmap(true);
        clockPaint.setDither(true);
        timeThread.start();
    }
    public ClockView(Context context){
        super(context);
        init();
    }
    public ClockView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }
    public void setPaintColor(int c){
        hourPaint.setColor(c);
        minPaint.setColor(c);
        secPaint.setColor(c);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Calendar t = Calendar.getInstance();
        int hour = t.get(Calendar.HOUR);
        int min = t.get(Calendar.MINUTE);
        int sec = t.get(Calendar.SECOND);
        float hourAngel = (float)hour*30f + (float)min*0.5f + (float)sec*0.5f/60f;
        float minAngel = (float)min*6f + (float)sec*0.1f;
        float secAngel = (float)sec*6f;
        canvas.save();
        canvas.rotate(hourAngel,getWidth()/2,getHeight()/2);
        canvas.drawLine(getWidth()/2,getHeight()/2,getWidth()/2,getHeight()/2 - 120,hourPaint);
        canvas.restore();
        canvas.save();
        canvas.rotate(minAngel,getWidth()/2,getHeight()/2);
        canvas.drawLine(getWidth()/2,getHeight()/2,getWidth()/2,getHeight()/2 - 150,minPaint);
        canvas.restore();
        canvas.save();
        canvas.rotate(secAngel,getWidth()/2,getHeight()/2);
        canvas.drawLine(getWidth()/2,getHeight()/2,getWidth()/2,getHeight()/2 - 180,secPaint);
        canvas.restore();
    }
}
