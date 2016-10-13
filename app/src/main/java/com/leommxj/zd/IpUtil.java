package com.leommxj.zd;

import android.util.Log;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static android.content.ContentValues.TAG;
/**
 * Created by wolf on 2016/9/21.
 *
 */

class IpUtil {

    /**
     * 获取IP地址
     * @return String[]
     *
    */
    String[] getIp() throws IOException {
        Log.d(TAG, "getIp: Start");
        String ip;
        String address;
        String[] ipAttr = new String[2];
//        String getIpUrl = "http://www.ip138.com/";
        String getIpUrl = "http://1212.ip138.com/ic.asp";
        Document doc = Jsoup.connect(getIpUrl).get();
        String webContent;
        webContent = doc.text();
        String ipStringStart = "您的IP是：[";
        String ipStringEnd = "] 来自";
        int startIP = webContent.indexOf(ipStringStart) + ipStringStart.length();
        int endIP = webContent.indexOf(ipStringEnd, startIP);
        ip = webContent.substring(startIP, endIP);
        String addressStringStart = "] 来自：";
        String addressStringEnd = " ";
        int startAddress = webContent.indexOf(addressStringStart)
                + addressStringStart.length();
        int endAddress = webContent.indexOf(addressStringEnd, startAddress);
        address = webContent.substring(startAddress, endAddress);
        ipAttr[0] = ip;
        ipAttr[1] = address;
        return ipAttr;
    }
}
