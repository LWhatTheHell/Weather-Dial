package com.leommxj.zd;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by leommxj on 2016/10/9.
 */
public class GetDayWeatherThread extends Thread {
    private Handler handler;
    private String cityLocation;
    public GetDayWeatherThread(Handler h,String c){
        handler=h;
        cityLocation =c;
        Log.d("hereisproblem",cityLocation);
        Log.d("hereisproblem2",c);
    }
    @Override
    public void run() {
        super.run();
        Log.d("hereisproblem3",cityLocation);
        String url = "https://api.caiyunapp.com/v2/T3irj2OIDnEwNdPW/" + cityLocation + "/forecast";
        String urlRealtime = "https://api.caiyunapp.com/v2/T3irj2OIDnEwNdPW/" + cityLocation + "/realtime.json";
        String data = HttpRequestUtil.HttpRequest(url);
        String dataRealtime = HttpRequestUtil.HttpRequest(urlRealtime);
        try {
            JSONObject whole = new JSONObject(data);
            JSONObject r = whole.getJSONObject("result");
            JSONObject daily = r.getJSONObject("daily");
            JSONObject hourly = r.getJSONObject("hourly");
            JSONArray temperature = daily.getJSONArray("temperature");
            JSONArray skycon = daily.getJSONArray("skycon");
            JSONArray aqi = daily.getJSONArray("aqi");
            JSONArray wind = daily.getJSONArray("wind");
            JSONObject wholeRealtime = new JSONObject(dataRealtime);
            JSONObject rRealtime = wholeRealtime.getJSONObject("result");
            Bundle temp = new Bundle();
            temp.putString("tempNowStr",rRealtime.getString("temperature"));
            temp.putString("tempMaxStr",temperature.getJSONObject(0).getString("max"));
            temp.putString("tempMinStr",temperature.getJSONObject(0).getString("min"));
            for (int i=0;i<3;i++){
                temp.putString("day"+i+"wind_direction",wind.getJSONObject(i).getJSONObject("avg").getString("direction"));
                temp.putString("day"+i+"wind_speed",wind.getJSONObject(i).getJSONObject("avg").getString("speed"));
            }
            for (int i = 0; i < 3; i++) {
                temp.putString("day" + i + "temperature", temperature.getJSONObject(i).getString("min") + "℃~" + temperature.getJSONObject(i).getString("max") + "℃");
                Log.d("fuckdaily_temperature", temp.getString("day" + i + "temperature"));
            }
            for (int i = 0; i < 3; i++) {
                temp.putString("day" + i + "skycon", skycon.getJSONObject(i).getString("value"));
                Log.d("fuckdaily_skycon", temp.getString("day" + i + "skycon"));
            }
            for (int i = 0; i < 3; i++) {
                temp.putString("day" + i + "aqi", aqi.getJSONObject(i).getString("avg"));
                Log.d("fuckdaily_aqi", temp.getString("day" + i + "aqi"));
            }
            temp.putString("description",hourly.getString("description"));
            Message msg = new Message();
            msg.what = 3;
            msg.obj = temp;
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
