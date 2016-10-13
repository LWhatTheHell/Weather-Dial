package com.leommxj.zd;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

/**
 * Created by leommxj on 2016/10/9.
 */
public class GetCityThread extends Thread {
    private Handler handler;
    public GetCityThread(Handler h){
        handler = h;
    }
    @Override
    public void run() {
        super.run();
        IpUtil ipUtil = new IpUtil();
        try {
            String[] cityTemp = ipUtil.getIp();
            String[] cityT = cityTemp[1].split("市");
            Message msg = new Message();
            msg.what = 1;
            msg.obj = cityT[0];
            handler.sendMessage(msg);
            Log.d("fuckt", "t1");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
