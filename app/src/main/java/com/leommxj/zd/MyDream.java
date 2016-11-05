package com.leommxj.zd;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.dreams.DreamService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.tts.client.SpeechSynthesizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by leommxj on 2016/11/5.
 */
public class MyDream extends DreamService {
    private Context mContext;
    private ClockView clockView;
    private Point size;
    private String tempNowStr;
    private RelativeLayout layout1;
    private ImageView[] timeCondition;
    private Calendar t;
    private TextView tempNow;
    private TextView city;
    private String cityStr;
    private TextView[] botTempText;
    private ImageView tourMenu;
    private String cityLocation = "116.467,39.9";
    private boolean isDay;
    private Bundle cityList;
    private String[] cityListStr=new String[]{"北京","上海","天津","重庆","香港","哈尔滨","齐齐哈尔","牡丹江","北安","伊春","鹤岗","鸡西","佳木斯","双鸭山","爱辉","长春","四平","辽源","通化","吉林","延吉","沈阳","朝阳","锦州","旅大","阜新","营口","本溪","鞍山","辽阳","抚顺","丹东","呼和浩特","赤峰","锡林浩特","包头","通辽","巴彦浩特","海拉尔","牙克石","石家庄","保定","唐山","承德","张家口","沧县","邢台","邯郸","太原","大同","阳泉","榆次","长治","侯马","济南","临沂","聊城","淄博","潍坊","烟台","青岛","郑州","南阳","西安","榆林","延安","铜川","商县","咸阳","宝鸡","安康","汉中","兰州","玉门","白银","定西","天水","临夏","张掖","银川","西宁","苏西克","玉树","乌鲁木齐","哈密","阿勒泰","若羌","昌吉","焉耆","塔城","博乐","伊宁","阿克苏","和田","喀什","合肥","淮南","六安","马鞍山","芜湖","安庆","屯溪","阜阳","南京","连云港","徐州","淮阴","扬州","镇江","常州","南通","无锡","苏州","杭州","湖州","宁波","金华","温州","长沙","常德","湘潭","株洲","吉首","邵阳","衡阳","黔阳","郴州","南昌","九江","庐山","景德镇","上饶","抚州","宜春","萍乡","吉安","赣州","武汉","恩施","黄石","成都","万县","达县","温江","阿坝","内江","马尔康","自贡","乐山","宜宾","南充","康定","甘孜","昭觉","西昌","涪陵","贵阳","遵义","福州","福安","南平","闽侯","三明","龙岩","泉州","漳州","厦门","台北","高雄","广州","韶关","汕头","湛江","海口","南宁","桂林","柳州","梧州","百色","玉林","昆明","大理","下关","潞西","景洪","拉萨","昌都","曼尼","日喀则","亚东","改则","多木拉","噶大克"};
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (cityStr==null||cityLocation==null){
                        new GetCityThread(handler).start();
                    }
                    cityStr = (String)msg.obj;
                    cityLocation = cityList.getString(cityStr);
                    city.setText(cityStr);
                    new GetDayWeatherThread(handler,cityLocation).start();
                    break;
                case 2:
                    for (int i = 0; i < 12; i++) {
                        String a = ((Bundle) msg.obj).getString("" + i);
                        if(a.equals("CLEAR_DAY")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.clear_day_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.clear_day_wl);
                            }
                        }else if(a.equals("CLEAR_NIGHT")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.clear_night_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.clear_night_wl);
                            }
                        }else if(a.equals("PARTLY_CLOUDY_DAY")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.partly_cloudy_day_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.partly_cloudy_day_wl);
                            }
                        }else if(a.equals("PARTLY_CLOUDY_NIGHT")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.partly_cloudy_night_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.partly_cloudy_night_wl);
                            }
                        }else if(a.equals("CLOUDY")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.cloudy_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.cloudy_wl);
                            }
                        }else if(a.equals("RAIN")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.rain_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.rain_wl);
                            }
                        }else if(a.equals("SLEET")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.sleet_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.sleet_wl);
                            }
                        }else if(a.equals("WIND")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.wind_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.wind_wl);
                            }
                        }else if(a.equals("SNOW")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.snow_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.snow_wl);
                            }
                        }else if(a.equals("FOG")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.fog_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.fog_wl);
                            }
                        }else if(a.equals("HAZE")){
                            if(isDay){
                                timeCondition[i].setImageResource(R.drawable.haze_bl);
                            }else{
                                timeCondition[i].setImageResource(R.drawable.haze_wl);
                            }
                        }
                    }
                    break;
                case 3:
                    Bundle b = (Bundle) msg.obj;
                    tempNowStr = b.getString("tempNowStr");
                    if(tempNowStr==null){
                        tempNow.setText("Waiting");
                    }else {
                        tempNow.setText(tempNowStr + "℃");
                    }
                    break;
            }
        }
    };
    private Thread getWeatherThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                String url = "https://api.caiyunapp.com/v2/T3irj2OIDnEwNdPW/" + cityLocation + "/forecast";
                Log.d("fuckt", url);
                String data = HttpRequestUtil.HttpRequest(url);
                try {
                    JSONObject whole = new JSONObject(data);
                    JSONObject r = whole.getJSONObject("result");
                    JSONObject hourly = r.getJSONObject("hourly");
                    JSONObject daily = r.getJSONObject("daily");
                    JSONArray skycon = hourly.getJSONArray("skycon");
                    String dateStr = skycon.getJSONObject(0).getString("datetime");
                    Date date = dateFormat.parse(dateStr);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int ii = calendar.get(Calendar.HOUR);
                    Bundle temp = new Bundle();
                    for (int i = 0; i < 12; i++) {
                        if (ii >= 12) {
                            ii = 0;
                        }
                        temp.putString("" + ii, skycon.getJSONObject(i).getString("value"));
                        ii++;
                    }
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = temp;
                    Log.d("fuckt", "" + msg.what);
                    handler.sendMessage(msg);
                    Thread.sleep(6000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
        setFullscreen(true);
        initCityList();
        mContext = this;
        size = new Point();
        t = Calendar.getInstance();
        int hour = t.get(Calendar.HOUR_OF_DAY);
        isDay = (hour < 6 || hour >= 18) ? false : true;
        setContentView(R.layout.activity_main);
        city = (TextView) findViewById(R.id.city);
        new GetCityThread(handler).start();
        initClock();
        getWeatherThread.start();
    }
    private void initClock() {
        layout1 = (RelativeLayout) findViewById(R.id.layout1);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int ImageSize = 60;
        clockView = (ClockView) findViewById(R.id.clockView);
        tempNow = (TextView) findViewById(R.id.tempNow);
        timeCondition = new ImageView[12];
        timeCondition[0] = (ImageView) findViewById(R.id.time0);
        timeCondition[1] = (ImageView) findViewById(R.id.time1);
        timeCondition[2] = (ImageView) findViewById(R.id.time2);
        timeCondition[3] = (ImageView) findViewById(R.id.time3);
        timeCondition[4] = (ImageView) findViewById(R.id.time4);
        timeCondition[5] = (ImageView) findViewById(R.id.time5);
        timeCondition[6] = (ImageView) findViewById(R.id.time6);
        timeCondition[7] = (ImageView) findViewById(R.id.time7);
        timeCondition[8] = (ImageView) findViewById(R.id.time8);
        timeCondition[9] = (ImageView) findViewById(R.id.time9);
        timeCondition[10] = (ImageView) findViewById(R.id.time10);
        timeCondition[11] = (ImageView) findViewById(R.id.time11);
        for (int i = 0; i < 12; i++) {
            timeCondition[i].setX((float) (width / 2 + Math.sin(Math.PI * 30 * i / 180) * width / 2.5 - ImageSize * 1.5));
            timeCondition[i].setY((float) (height / 2 - Math.cos(Math.PI * 30 * i / 180) * width / 2.5 - ImageSize * 1.5));
        }

        if (isDay) {
            layout1.setBackground(getDrawable(R.drawable.day));
            clockView.setPaintColor(Color.BLACK);
            tempNow.setTextColor(Color.BLACK);
            city.setTextColor(Color.BLACK);
        } else {
            layout1.setBackground(getDrawable(R.drawable.night));
            clockView.setPaintColor(Color.WHITE);
            tempNow.setTextColor(Color.WHITE);
            city.setTextColor(Color.WHITE);
        }
    }
    private void initCityList(){
        cityList = new Bundle();
        cityList.putString("北京", "116.467,39.9" );
        cityList.putString("上海", "121.483,31.2333" );
        cityList.putString("天津", "117.183,39.15" );
        cityList.putString("重庆", "106.533,29.5333" );
        cityList.putString("香港", "114.167,22.3" );
        cityList.putString("哈尔滨", "126.683,45.75" );
        cityList.putString("齐齐哈尔", "123.9,47.3167" );
        cityList.putString("牡丹江", "129.567,44.5833" );
        cityList.putString("北安", "126.5,48.2333" );
        cityList.putString("伊春", "128.917,47.7" );
        cityList.putString("鹤岗", "130.267,47.3833" );
        cityList.putString("鸡西", "130.967,45.2833" );
        cityList.putString("佳木斯", "130.367,46.8167" );
        cityList.putString("双鸭山", "131.35,46.6" );
        cityList.putString("爱辉", "127.517,50.1667" );
        cityList.putString("长春", "125.317,43.8667" );
        cityList.putString("四平", "124.333,43.1833" );
        cityList.putString("辽源", "125.083,42.9167" );
        cityList.putString("通化", "125.883,41.7667" );
        cityList.putString("吉林", "126.533,43.8667" );
        cityList.putString("延吉", "129.483,42.95" );
        cityList.putString("沈阳", "123.4,41.8333" );
        cityList.putString("朝阳", "120.417,41.5333" );
        cityList.putString("锦州", "121.083,41.1167" );
        cityList.putString("旅大", "121.567,38.8833" );
        cityList.putString("阜新", "121.717,42.05" );
        cityList.putString("营口", "122.2,40.6833" );
        cityList.putString("本溪", "123.783,41.3" );
        cityList.putString("鞍山", "122.933,41.1333" );
        cityList.putString("辽阳", "123.167,41.2833" );
        cityList.putString("抚顺", "123.883,41.8333" );
        cityList.putString("丹东", "124.367,40.1333" );
        cityList.putString("呼和浩特", "111.8,40.8167" );
        cityList.putString("赤峰", "119.7,16");
        cityList.putString("锡林浩特", "116.1,43.95" );
        cityList.putString("包头", "109.967,40.5833" );
        cityList.putString("通辽", "122.217,33.65" );
        cityList.putString("巴彦浩特", "105.683,38.8167" );
        cityList.putString("海拉尔", "119.667,49.25" );
        cityList.putString("牙克石", "120.683,49.2833" );
        cityList.putString("石家庄", "114.467,38.0333" );
        cityList.putString("保定", "115.467,38.8667" );
        cityList.putString("唐山", "118.2,39.6167" );
        cityList.putString("承德", "117.85,40.95" );
        cityList.putString("张家口", "114.883,40.8333" );
        cityList.putString("沧县", "116.85,38.3" );
        cityList.putString("邢台", "114.483,37.05" );
        cityList.putString("邯郸", "114.45,36.5833" );
        cityList.putString("太原", "112.567,37.8667" );
        cityList.putString("大同", "113.267,40.0833" );
        cityList.putString("阳泉", "113.6,37.8833" );
        cityList.putString("榆次", "112.733,37.6667" );
        cityList.putString("长治", "113.1,36.1667" );
        cityList.putString("侯马", "111.333,35.6167" );
        cityList.putString("济南", "117.6,38");
        cityList.putString("临沂", "118.367,38.8667" );
        cityList.putString("聊城", "115.95,36.45" );
        cityList.putString("淄博", "117.833,36.5" );
        cityList.putString("潍坊", "119.05,36.7" );
        cityList.putString("烟台", "121.333,37.55" );
        cityList.putString("青岛", "120.3,36.0667" );
        cityList.putString("郑州", "113.7,34.8" );
        cityList.putString("南阳", "112.517,33.0167" );
        cityList.putString("西安", "108.9,34.2667" );
        cityList.putString("榆林", "109.75,38.2833" );
        cityList.putString("延安", "109.467,36.6" );
        cityList.putString("铜川", "109.133,35.05" );
        cityList.putString("商县", "109.917,33.9333" );
        cityList.putString("咸阳", "108.7,34.35" );
        cityList.putString("宝鸡", "107.133,34.3833" );
        cityList.putString("安康", "109.533,42");
        cityList.putString("汉中", "107.017,33.05" );
        cityList.putString("兰州", "103.817,36.05" );
        cityList.putString("玉门", "97.7667,39.85" );
        cityList.putString("白银", "104.15,36.6833" );
        cityList.putString("定西", "104.6,35.5833" );
        cityList.putString("天水", "105.683,34.2333" );
        cityList.putString("临夏", "103.183,35.5833" );
        cityList.putString("张掖", "100.467,38.9167" );
        cityList.putString("银川", "106.267,38.3333" );
        cityList.putString("西宁", "101.75,36.6333" );
        cityList.putString("苏西克", "94.1333,38.4" );
        cityList.putString("玉树", "96.65,33.0167" );
        cityList.putString("乌鲁木齐", "87.6,43.8" );
        cityList.putString("哈密", "93.5333,42.8167" );
        cityList.putString("阿勒泰", "88.1167,47.9167" );
        cityList.putString("若羌", "88.15,39");
        cityList.putString("昌吉", "87.3167,44.0333" );
        cityList.putString("焉耆", "86.5167,42.05" );
        cityList.putString("塔城", "83.7667,45");
        cityList.putString("博乐", "82.0833,44.8833" );
        cityList.putString("伊宁", "81.4333,43.95" );
        cityList.putString("阿克苏", "80.3,41.15" );
        cityList.putString("和田", "79.9333,37.0833" );
        cityList.putString("喀什", "76.65,31");
        cityList.putString("合肥", "117.3,31.85" );
        cityList.putString("淮南", "117.35,51");
        cityList.putString("六安", "116.5,31.7333" );
        cityList.putString("马鞍山", "118.467,31.7" );
        cityList.putString("芜湖", "118.367,31.35" );
        cityList.putString("安庆", "117.017,30.5167" );
        cityList.putString("屯溪", "118.267,29.7167" );
        cityList.putString("阜阳", "115.8,32.9167" );
        cityList.putString("南京", "118.833,32.0333" );
        cityList.putString("连云港", "119.2,34.65" );
        cityList.putString("徐州", "117.183,34.25" );
        cityList.putString("淮阴", "119.017,33.5667" );
        cityList.putString("扬州", "119.417,32.3833" );
        cityList.putString("镇江", "119.4,32.2" );
        cityList.putString("常州", "119.967,31.8" );
        cityList.putString("南通", "120.883,32.05" );
        cityList.putString("无锡", "120.283,31.5833" );
        cityList.putString("苏州", "120.617,31.3" );
        cityList.putString("杭州", "120.15,30.2333" );
        cityList.putString("湖州", "120.067,20.8667" );
        cityList.putString("宁波", "121.517,29.8667" );
        cityList.putString("金华", "119.65,29.1" );
        cityList.putString("温州", "120.65,28.0167" );
        cityList.putString("长沙", "113.467,11");
        cityList.putString("常德", "111.683,29.05" );
        cityList.putString("湘潭", "112.9,27.8667" );
        cityList.putString("株洲", "113.167,27.8333" );
        cityList.putString("吉首", "109.717,28.4833" );
        cityList.putString("邵阳", "111.45,27.2" );
        cityList.putString("衡阳", "112.583,26.9333" );
        cityList.putString("黔阳", "110.117,27.3333" );
        cityList.putString("郴州", "112.983,25.8" );
        cityList.putString("南昌", "115.867,28.6833" );
        cityList.putString("九江", "115.967,29.7167" );
        cityList.putString("庐山", "115.967,29.55" );
        cityList.putString("景德镇", "117.183,29.3" );
        cityList.putString("上饶", "117.967,28.45" );
        cityList.putString("抚州", "116.317,28.0167" );
        cityList.putString("宜春", "114.383,27.8167" );
        cityList.putString("萍乡", "113.817,27.6" );
        cityList.putString("吉安", "114.983,27.0833" );
        cityList.putString("赣州", "114.917,25.8833" );
        cityList.putString("武汉", "114.35,30.6167" );
        cityList.putString("恩施", "109.483,30.2667" );
        cityList.putString("黄石", "115.067,30.2" );
        cityList.putString("成都", "104.083,30.65" );
        cityList.putString("万县", "108.333,30.8" );
        cityList.putString("达县", "107.483,31.2167" );
        cityList.putString("温江", "103.917,30.7333" );
        cityList.putString("阿坝", "101.717,32.8833" );
        cityList.putString("内江", "105.05,29.5833" );
        cityList.putString("马尔康", "102.333,31.7833" );
        cityList.putString("自贡", "104.75,29.3833" );
        cityList.putString("乐山", "103.717,29.5833" );
        cityList.putString("宜宾", "104.6,28.7667" );
        cityList.putString("南充", "106.067,30.8" );
        cityList.putString("康定", "101.967,30.05" );
        cityList.putString("甘孜", "99.9667,31.6333" );
        cityList.putString("昭觉", "102.85,28.05" );
        cityList.putString("西昌", "102.267,27.9167" );
        cityList.putString("涪陵", "107.367,29.7" );
        cityList.putString("贵阳", "106.7,26.5833" );
        cityList.putString("遵义", "106.883,27.7" );
        cityList.putString("福州", "119.3,26.0833" );
        cityList.putString("福安", "119.667,27.1167" );
        cityList.putString("南平", "118.15,26.6333" );
        cityList.putString("闽侯", "119.3,26");
        cityList.putString("三明", "117.6,26.2167" );
        cityList.putString("龙岩", "117.033,25.1333" );
        cityList.putString("泉州", "118.667,24.9833" );
        cityList.putString("漳州", "117.65,24.5333" );
        cityList.putString("厦门", "118.1,24.4833" );
        cityList.putString("台北", "121.517,25.05" );
        cityList.putString("高雄", "120.317,22.6167" );
        cityList.putString("广州", "113.25,23.1333" );
        cityList.putString("韶关", "113.667,24.8833" );
        cityList.putString("汕头", "116.667,23.3667" );
        cityList.putString("湛江", "110.383,21.1833" );
        cityList.putString("海口", "110.333,20.0333" );
        cityList.putString("南宁", "108.333,22.8" );
        cityList.putString("桂林", "110.25,25.3" );
        cityList.putString("柳州", "109.383,24.3167" );
        cityList.putString("梧州", "111.333,23.5" );
        cityList.putString("百色", "106.6,23.9167" );
        cityList.putString("玉林", "110.15,22.65" );
        cityList.putString("昆明", "102.683,25");
        cityList.putString("大理", "100.167,25.7167" );
        cityList.putString("下关", "100.217,25.5833" );
        cityList.putString("潞西", "98.5333,24.4" );
        cityList.putString("景洪", "100.783,21.95" );
        cityList.putString("拉萨", "91.1667,29.6667" );
        cityList.putString("昌都", "97.2333,31.0833" );
        cityList.putString("曼尼", "87.1667,34.7667" );
        cityList.putString("日喀则", "88.8833,29.3167" );
        cityList.putString("亚东", "88.85,27.4167" );
        cityList.putString("改则", "85.3333,32.1167" );
        cityList.putString("多木拉", "82.4333,34.15" );
        cityList.putString("噶大克", "80.35,31.7333" );
    }
}
