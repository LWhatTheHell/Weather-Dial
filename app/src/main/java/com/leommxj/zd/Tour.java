package com.leommxj.zd;

/**
 * Created by wolf on 2016/9/21.
 *
 */

class Tour {
    private String title;
    private String address;
    private String intro;
    private String time;
    Tour(String title1, String address1, String intro1, String time1){
        title = title1;
        address = address1;
        intro = intro1;
        time = time1;
    }

    String getTitle() {
        return title;
    }

    String getAddress() {
        return address;
    }

    String getIntro() {
        return intro;
    }

    String getTime() { return time;}
}
