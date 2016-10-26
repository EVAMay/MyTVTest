package com.myapp.mytvtest.mytvtest.util;

/**
 * Created by lenovo on 2016/10/26.
 */

public class TimeUtil {
    public String time(int curationProgress){
        String time="";
        int minute=curationProgress/60000;
        int second=curationProgress/1000%60;
        if (minute<10){
            time+="0";
        }
        time+=minute+":";
        if (second<10){
            time+="0";
        }
        time+=second;

        return time;
    }
}
