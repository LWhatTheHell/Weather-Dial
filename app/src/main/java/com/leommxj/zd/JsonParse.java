package com.leommxj.zd;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * Created by wolf on 2016/9/21.
 *
 */

class JsonParse {

    /**
     * 解析Json数据(Tour)
     * @param urlPath String
     * @return List<Tour>
     * @throws Exception
     */
    static List<Tour> getListTour(String urlPath) throws Exception {
        List<Tour> mlists = new ArrayList<>();
        byte[] data = readParse(urlPath);
        JSONObject object = new JSONObject(new String(data));
//        Log.d(TAG, "getListTour: json Tour "+object);
        for (int i = 0; i < object.length(); i++) {
            JSONObject item = object.getJSONObject(String.valueOf(i));
//            Log.d(TAG, "getListTour: item Tour" + item);
            String name = item.getString("spot");
            String address = item.getString("address");
            String intro = item.getString("introduce");
            String time = item.getString("recommand_time");
            mlists.add(new Tour(name, address, intro, time));
        }
        return mlists;
    }

    /**
     * 解析Json数据(Weather)
     * @param urlPath String
     * @return Weather
     * @throws Exception
     */
    static Weather getListWeather(String urlPath) throws Exception {
//        byte[] data = readParse(urlPath);
        String data = HttpRequestUtil.HttpRequest(urlPath);
//        Log.d(TAG, "getListWeather: "+data);
        JSONObject object = new JSONObject(data);
//        Log.d(TAG, "getListWeather:object_ "+ object);
        JSONObject item = object.getJSONObject("result");
//        Log.d(TAG, "getListWeather:item_ "+ item);
        JSONObject hourly = item.getJSONObject("hourly");
//        Log.d(TAG, "getListWeather:hourly_ "+ hourly);
        JSONArray temperature = hourly.getJSONArray("temperature");
//        Log.d(TAG, "getListWeather:temperature_ "+ temperature);
        double curTem = temperature.getJSONObject(0).getDouble("value");
//        Log.d(TAG, "getListWeather:curTem_ "+ curTem);

        JSONArray wind = hourly.getJSONArray("wind");
        Log.d(TAG, "getListWeather:wind_ "+ wind);
        double curWindSpeed = wind.getJSONObject(0).getDouble("speed");
        Log.d(TAG, "getListWeather:curWindSpeed_ "+ curWindSpeed);
        double curWindLevel = getWindLevel(curWindSpeed);
        Log.d(TAG, "getListWeather:curWindLevel_ "+ curWindLevel);

        JSONArray skycon = hourly.getJSONArray("skycon");
//        Log.d(TAG, "getListWeather:skycon_ "+ skycon);
        String[] weather = new String[12];
        for(int i = 0; i < 12; i++){
            weather[i] = skycon.getJSONObject(i).getString("value");
        }
//        Log.d(TAG, "getListWeather:weather_ "+ weather);

        JSONObject daily = item.getJSONObject("daily");
//        Log.d(TAG, "getListWeather:daily_ "+ daily);
        float rainSum = 0;
        int isSnow = 0;
        for (int i = 0; i <12;i++){
            if ((Objects.equals(weather[i], "RAIN")) || (Objects.equals(weather[i], "SLEET")) || (Objects.equals(weather[i], "SNOW"))){
                if(Objects.equals(weather[i], "SNOW")){
                    isSnow = 1;
                }
                rainSum += 1;
            }else if (Objects.equals(weather[i], "CLOUDY")){
                rainSum += 0.5;
            }else if ((Objects.equals(weather[i], "PARTLY_CLOUDY_DAY")) ||  (Objects.equals(weather[i], "PARTLY_CLOUDY_NIGHT"))){
                rainSum += 0.2;
            }
        }
        int rain = (int)((rainSum / 12  + isSnow) * 100);

        JSONArray temperatureD = daily.getJSONArray("temperature");
        int[] temD = new int[3];
        for (int i = 0;i < 3;i++){
            temD[i] = temperatureD.getJSONObject(i).getInt("avg");
        }

        return new Weather(curTem, curWindLevel, rain, weather, temD);
    }

    /**
     * 从指定的url中获取字节数组
     * @param urlPath String
     * @return byte[]
     * @throws Exception
     */
    private static byte[] readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 计算风力等级
     * @param windSpeed double
     * @return byte[]
     */
    private static double getWindLevel(double windSpeed){
//        int windLevel;
//        if (windSpeed <= 0.2){
//            windLevel = 0;
//        }else if (windSpeed <= 1.6){
//            windLevel = 1;
//        }else if (windSpeed <= 3.4){
//            windLevel = 2;
//        }else if (windSpeed <= 5.5){
//            windLevel = 3;
//        }else if (windSpeed <= 8.0){
//            windLevel = 4;
//        }else if (windSpeed <= 10.8){
//            windLevel = 5;
//        }else if (windSpeed <= 13.9){
//            windLevel = 6;
//        }else if (windSpeed <= 17.2){
//            windLevel = 7;
//        }else if (windSpeed <= 20.8){
//            windLevel = 8;
//        }else if (windSpeed <= 24.5){
//            windLevel = 9;
//        }else if (windSpeed <= 28.5){
//            windLevel = 10;
//        }else if (windSpeed <= 32.6){
//            windLevel = 11;
//        }else{
//            windLevel = 12;
//        }
        return windSpeed;
    }

}
