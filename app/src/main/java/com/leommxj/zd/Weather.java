package com.leommxj.zd;

/**
 * Created by wolf on 2016/9/21.
 *
 */

class Weather {
    private double curTem;
    private double curWindLevel;
    private String[] weather = new String[20];
    private int rain;
    private int[] temD = new int[3];
    Weather(){
        curTem = 27.0;
        curWindLevel = 0.0;
        for (int i = 0; i < 20;i++){
            weather[i] = "\0";
        }
        rain = 20;
        for (int i = 0; i < 3; i++){
            temD[i] = 7;
        }
    }

    Weather(double curTem1, double curWindLevel1, int rain1, String[] weather1, int[] temD1){
        curTem = curTem1;
        curWindLevel = curWindLevel1;
        System.arraycopy(weather1, 0, weather, 0, weather1.length);
        rain = rain1;
        System.arraycopy(temD1, 0, temD, 0, 3);
    }

    void setWeather(Weather w){
        curTem = w.curTem;
        curWindLevel = w.curWindLevel;
        System.arraycopy(w.weather, 0, weather, 0, weather.length);
        rain = w.rain;
        System.arraycopy(w.temD, 0, temD, 0, 3);
    }

    double getCurTem() {
        return curTem;
    }

    double getCurWindLevel() {
        return curWindLevel;
    }

//    String[] getWeather() {
//        return weather;
//    }

    int getRain() {
        return rain;
    }

//    int[] getTemD() {
//        return temD;
//    }
}
