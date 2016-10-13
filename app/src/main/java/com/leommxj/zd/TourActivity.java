package com.leommxj.zd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by wolf on 2016/9/24.
 *
 */

public class TourActivity extends Activity {

    private static String urlPathW;
    private static String urlPathT;
    private static final String TAG = "TourActivity";

    private int mMonth = 1;
    public String webIp = "0";
    public String[] cityAttr = new String[3];
    public String cityGetName;
    HashMap<String, String> cityName = new HashMap<String, String>() {{
        put("天津", "Tianjin");
        put("北京", "Beijing");
    }};
    public Weather weather = new Weather();
    List<HashMap<String, String>> mDatas = new ArrayList<>();
    public List tourList =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tour);

        TextView bt = (TextView)findViewById(R.id.returnKey);
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourActivity.this,MainActivity.class);
                TourActivity.this.startActivity(intent);
                TourActivity.this.finish();
            }
        });

        setTime();
        Location loc = getLocation();
        start(loc);
    }

    /**
     * RecyclerView
    */
    // RecyclerView - start
     class TourAdapter extends RecyclerView.Adapter<TourAdapter.MyViewHolder> {
        /**
         * 创建 ViewHolder
         * @param parent ViewGroup
         * @param viewType int
         * @return MyViewHolder
         */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(
                    TourActivity.this).inflate(R.layout.item_tour, parent,
                    false));
        }

        /**
         * 绑定 ViewHolder
         * @param holder MyViewHolder
         * @param position int
         */
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Typeface faceHei = Typeface.createFromAsset(getAssets(),"fonts/hei.ttf");  //黑体
            Typeface faceSong = Typeface.createFromAsset(getAssets(),"fonts/zhongsong.ttf");  //宋体

            holder.title.setText((String) ((HashMap) mDatas.get(position)).get("title"));

            holder.address.setText((String) ((HashMap) mDatas.get(position)).get("address"));
            holder.address.setTypeface(faceHei);

            holder.time.setText((String) ((HashMap) mDatas.get(position)).get("time"));
            holder.time.setOnClickListener(new TourActivity.MyOnClickListener());

            holder.intro.setText((String) ((HashMap) mDatas.get(position)).get("intro"));
            holder.intro.setTypeface(faceSong);
            holder.intro.setOnClickListener(new TourActivity.MyOnClickListener());
        }

        /**
         * 获取列表长度
         * @return int
         */
        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView address;
            TextView time;
            TextView intro;

            private MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.tourTitle);
                intro = (TextView) view.findViewById(R.id.tourIntro);
                time = (TextView) view.findViewById(R.id.tourTime);
                address = (TextView) view.findViewById(R.id.tourAddress);
            }
        }
    }
    // RecyclerView - end

    /**
     * Handler
     */
    // Handler - start
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setTime();
                    break;
                case 1:
                    String[] temp;
                    temp = (String[]) msg.obj;
                    webIp = temp[0];
                    cityGetName = getStrings(temp[1], Pattern.compile(getString(R.string.patternCity)));
                    cityAttr[0] = cityName.get(cityGetName);
                    TextView mCurrentPlace = (TextView) findViewById(R.id.currentPlace);
                    String currentPlace = cityAttr[0];
                    mCurrentPlace.setText(currentPlace);
                    break;
                case 2:
                    Weather w = (Weather)msg.obj;
                    weather.setWeather(w);

                    // Weather
                    TextView mTemperature = (TextView) findViewById(R.id.belowDegrees);
                    int temperature;
                    temperature = (int) weather.getCurTem();
                    mTemperature.setText(temperature + "℃");

                    TextView mWind = (TextView) findViewById(R.id.belowWind);
                    double wind;
                    wind = weather.getCurWindLevel();
                    mWind.setText(wind + "km/h");

                    TextView mRain = (TextView) findViewById(R.id.belowRain);
                    int rain;
                    rain = weather.getRain() % 100;
                    mRain.setText(rain + "%");
                    break;
                case 3:
                    // Tour
                    tourList = (List)msg.obj;
                    if(tourList!=null){
                        for (int i = 0; i < tourList.size(); i++) {
                            HashMap<String, String> mapT = new HashMap<>();
                            mapT.put("title", ((Tour)tourList.get(i)).getTitle() + "");
                            mapT.put("address", "地址：" + ((Tour)tourList.get(i)).getAddress() + "");
                            mapT.put("time", "开放时间：" + ((Tour)tourList.get(i)).getTime() + "");
                            mapT.put("intro", "简介：" + ((Tour)tourList.get(i)).getIntro() + "");
                            mDatas.add(mapT);
                        }
                    }
                    //初始化适配器，并且绑定数据
                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    mRecyclerView.setAdapter(new TourAdapter());
                    break;
            }
        }
    };
    // Handler - end

    /**
     * Thread
     */
    // Thread - start
    void start(final Location location1){
        new Thread(new Runnable() {
            private int i = 0;
            @Override
            public void run() {
                while (true) {
                    // Time
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);

                    if (i % 30 == 0) {
                        // Ip
                        msg = new Message();
                        IpUtil ipUtil = new IpUtil();
                        msg.what = 1;
                        String city = "tianjin";
                        try {
                            String[] cityTemp = ipUtil.getIp();
                            String cGName = (getStrings(cityTemp[1], Pattern.compile(getString(R.string.patternCity))));
                            city = cityName.get(cGName);
                            msg.obj = cityTemp;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        handler.sendMessage(msg);
                        Log.d(TAG, "run: IP  end end end");

                        //Weather
                        msg = new Message();
                        msg.what = 2;
                        urlPathW = "http://api.caiyunapp.com/v2/T3irj2OIDnEwNdPW/" + location1.getLongitude() + "," + location1.getLatitude() + "/forecast";
                        String rainy = "sunny";
                        try {
                            Weather weatherTemp = JsonParse.getListWeather(urlPathW);
                            int rain = weather.getRain();
                            if (rain >= 1) {
                                rainy = "snowy";
                            } else if (rain >= 0.7) {
                                rainy = "rainy";
                            } else if (rain >= 0.4) {
                                rainy = "cloudy";
                            } else {
                                rainy = "sunny";
                            }
                            msg.obj = weatherTemp;
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        handler.sendMessage(msg);
                        Log.d(TAG, "run: Weather  end end end");

                        // Tour
                        msg = new Message();
                        msg.what = 3;
                        urlPathT = "http://123.206.43.159/getJson/handleData.php?month=" + mMonth + "&weather=" + rainy + "&city=" + city;
                        Log.d(TAG, "run: www    " + urlPathT);
                        try {
                            msg.obj = JsonParse.getListTour(urlPathT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.sendMessage(msg);
                        Log.d(TAG, "run: Tour end end end");
                    }

                    try {
                        Thread.sleep(30000);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    // Thread - end

    /**
     * 设置当前时间
     */
    private void setTime(){
        TextView mLocalTime = (TextView) findViewById(R.id.belowTime);
        long time=System.currentTimeMillis();
        final Calendar mCalendar=Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        mMonth = mCalendar.get(Calendar.MONTH);
        int mHour = mCalendar.get(Calendar.HOUR);
        int isPm = mCalendar.get(Calendar.AM_PM);
        String strHour = "";
        if (isPm == 1){
            mHour += 12;
            strHour +=  + mHour;
        }else{
            if (mHour < 10){
                strHour += "0" + mHour;
            }else{
                strHour += "" + mHour;
            }
        }

        int mMinutes = mCalendar.get(Calendar.MINUTE);
        String strMinutes = "";
        if (mMinutes < 10){
            strMinutes += "0" + mMinutes;
        }else{
            strMinutes += "" + mMinutes;
        }
        String localTime = strHour + " : " + strMinutes;
        mLocalTime.setText(localTime);
    }

    /**
     * 通过LocationManger获取Location
     * @return Location
     */
    public Location getLocation() {
        LocationManager locManger = (LocationManager) (getSystemService(Context.LOCATION_SERVICE));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return TODO;
        }
        Location loc = locManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null) {
            loc = locManger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return loc;
    }

    /**
     * 正则表达式 处理 城市名
     * @param str String
     * @param p Pattern
     * @return String
     */
    private String getStrings(String str, Pattern p) {
        Matcher m = p.matcher(str);
        String result = "";
        if (m.find()) {
            Pattern pattern = Pattern.compile(getString(R.string.patternCityReplace));
            Matcher matcher = pattern.matcher(m.group(0));
            if (matcher.find()){
                result = matcher.replaceAll("");
            }else{
                result = m.group(0);
            }
        }
        return result;
    }

    /**
     * 点击事件 - 处理 介绍部分
     */
    private class MyOnClickListener implements View.OnClickListener {
        Boolean flag = true;
        @Override
        public void onClick(View v) {
            TextView tv = (TextView)v;
//            Log.i("v.getLineCount()", v.getHeight() + "");
            if (flag) {
                flag = false;
                tv.setEllipsize(null); // 展开
                tv.setMaxLines(Integer.MAX_VALUE);
            } else {
                flag = true;
                tv.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                tv.setMaxLines(1);
            }
        }
    }

    /**
     * 自带按键-后退键 处理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent;
            myIntent = new Intent(TourActivity.this, MainActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}

