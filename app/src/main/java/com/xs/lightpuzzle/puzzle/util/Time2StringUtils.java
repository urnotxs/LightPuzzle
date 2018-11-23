package com.xs.lightpuzzle.puzzle.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xs on 2018/04/27
 * 时间转字符串工具类
 */
public class Time2StringUtils {

    public static String getTime(String str) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(calendar.YEAR);
        int hour12 = calendar.get(calendar.HOUR);//12小时制
        int hour24 = calendar.get(calendar.HOUR_OF_DAY);//24小时制
        int minute = calendar.get(calendar.MINUTE);//4
        int am_pm = calendar.get(calendar.AM_PM);//早上0，下午1

        String dayStr = day + "";
        String monthStr = month + "";
        String yearStr = year + "";
        String hour12Str = hour12 + "";
        String hour24Str = hour24 + "";
        String minStr = minute + "";

        int num;
        String time = null;
        String mouth = null;
        if (str.equals("time_d1")) {
            time = day + "";//28
        } else if (str.equals("time_time1")) { //9:01 PM 12小时制
            String tempMinStr = minStr;
            if (minStr.length() == 1) {
                tempMinStr = "0" + minStr;
            }
            String temphour12Str = hour12Str;
            if (hour12Str.equals("0")) {
                if (hour24Str.equals("12")) {
                    temphour12Str = "12";
                } else {
                    temphour12Str = "00";
                }
            }
            if (am_pm == 1) {
                time = temphour12Str + ":" + tempMinStr + " PM";
            } else {
                time = temphour12Str + ":" + tempMinStr + " AM";
            }
        } else if (str.equals("time_time2")) { //09：01  24小时制
            String tempMinStr = minStr;
            if (minStr.length() == 1) {
                tempMinStr = "0" + minStr;
            }
            String temphour12Str = hour12Str;
            if (hour12Str.length() == 1) {
                temphour12Str = "0" + hour12Str;
            }
            if (am_pm == 1) {
                time = setTime("HH:mm");
            } else {
                if (temphour12Str.equals("00")) {
                    time = temphour12Str + ":" + tempMinStr;
                } else {
                    time = setTime("hh:mm");
                }
            }
        } else if (str.equals("time_time3")) {    //09：01  PM   12小时制
            String tempMinStr = minStr;
            if (minStr.length() == 1) {
                tempMinStr = "0" + minStr;
            }
            String temphour12Str = hour12Str;
            if (hour12Str.length() == 1) {
                temphour12Str = "0" + hour12Str;
            }
            if (am_pm == 1) {
                time = temphour12Str + ":" + tempMinStr + " PM";
            } else {
                if (temphour12Str.equals("00")) {
                    time = temphour12Str + ":" + tempMinStr + " AM";
                } else {
                    time = setTime("hh:mm") + " AM";
                }
            }
        } else if (str.equals("time_time4")) {    //9：01   24小时制
            String tempMinStr = minStr;
            if (minStr.length() == 1) {
                tempMinStr = "0" + minStr;
            }
            String temphour12Str = hour12Str;
            if (hour12Str.equals("0")) {
                temphour12Str = "00";
            }
            if (am_pm == 1) {
                time = setTime("HH:mm");
            } else {
                time = temphour12Str + ":" + tempMinStr;
            }

        } else if (str.equals("time_y1_m1_d1")) {
            time = yearStr + "." + monthStr + "." + dayStr;// 2015.3.8
        } else if (str.equals("time_d1_m1_y1")) {
            time = dayStr + "/" + monthStr + "/" + yearStr;// 8/3/2015
        } else if (str.equals("time_d2_m2_y2")) {
            time = dayStr + " " + getMouth() + setTime("/yyyy"); //  8 Mar/2015
        } else if (str.equals("time_y2_m2_d2")) {
            time = yearStr + "年" + monthStr + "月" + dayStr + "日"; // 2015年3月8日
        } else if (str.equals("time_y3_m3_d3")) {
            time = getYearCn(year + "", true) + "年" + getMouthCn(month) + "月" + getDayCn(day) + "日"; //二零一五年三月十五日
        } else if (str.equals("time_y4_m4_d4")) {
            time = getYearCn(year + "", false) + "年" + getMouthCn(month) + "月" + getDayCn(day) + "日"; //二〇一五年三月十五日
        } else if (str.equals("time_y1_m1")) {
            time = yearStr + "." + monthStr;// 2015.3
        } else if (str.equals("time_y2_m2")) {
            time = setTime("yyyy.MM");// 2015.03
        } else if (str.equals("time_y3_m3")) {
            time = setTime("MM/yyyy");// 03/2015
        } else if (str.equals("time_y4_m4")) {
            time = monthStr + "/" + yearStr;// 3/2015
        } else if (str.equals("time_y5_m5")) {
            time = getMouth() + " " + setTime("yyyy");// Mar 2015
        } else if (str.equals("time_m1_f1_y1")) {
            time = getMouth() + "/" + setTime("yyyy");// Mar/2015
        } else if (str.equals("time_y7_m7")) {
            time = yearStr + "年" + monthStr + "月";// 2015年3月
        } else if (str.equals("time_y8_m8")) {
            time = getYearCn(year + "", false) + "年" + getMouthCn(month) + "月";// 二〇一五年三月
        } else if (str.equals("time_m1_d1")) {
            time = monthStr + "." + dayStr;//  3.8
        } else if (str.equals("time_m2_d2")) {
            time = setTime("MM.dd");//03.08
        } else if (str.equals("time_m3_d3")) {
            time = dayStr + "/" + monthStr;//  8/3
        } else if (str.equals("time_m4_d4")) {
            time = setTime("dd/MM");// 08/03
        } else if (str.equals("time_m5_d5")) {
            time = dayStr + " " + getMouth();//  8 Mar
        } else if (str.equals("time_m6_d6")) {
            time = monthStr + "月" + dayStr + "日";//  3月8日
        } else if (str.equals("time_week1")) {
            time = getWeek(dayOfWeek);//星期一
        } else if (str.equals("time_week2")) {
            time = getWeek2(dayOfWeek);//Mon
        } else if (str.equals("time_week3")) {
            time = getWeek3(dayOfWeek);//Monday
        }

        //新添加的的日期格式201703301203
        /*单独年份*/
        else if (str.equals("year_1")) {
            time = yearStr;// 2017
        } else if (str.equals("year_2")) {
            time = getYearCn(yearStr, false); //  二〇一七
        } else if (str.equals("year_3")) {
            time = getYearCn(yearStr, true); //  二零一七
        } else if (str.equals("year_4")) {
            time = getYearCn4(yearStr); //  贰零壹柒
        }         /*单独月份    */ else if (str.equals("time_m1")) {
            time = monthStr;                   // 1
        } else if (str.equals("time_m2")) {
            time = getDecimalFormat(monthStr);//01
        } else if (str.equals("time_m3")) {
            time = monthStr + "月";           //1月
        } else if (str.equals("time_m4")) {
            time = getMouthCn(month) + "月";  //一月
        } else if (str.equals("time_m5")) {
            time = getMouth5(); //JANUARY
        } else if (str.equals("time_m6")) {
            time = getMouth6(); //January
        }

        /*单独日期    */
        else if (str.equals("time_d1")) {
            time = dayStr;// 3
        } else if (str.equals("time_d2")) {
            time = getDecimalFormat(dayStr);//01
        } else if (str.equals("time_d3")) {
            Lunar lunar = new Lunar(calendar);
            time = lunar.getDay();
        }
        /*月+日*/
        else if (str.equals("time_m7_d7")) {
            time = getMouth() + "." + getDayEnglish(day); //Mar.3rd
        } else if (str.equals("time_m8_d8")) {
            time = getMouth6() + " " + getDayEnglish(day); //Mar.3rd
        }

        /*年+月+日*/
        else if (str.equals("time_d3_m3_y3")) {
            time = getMouth() + "." + getDayEnglish(day) + "," + yearStr;//Mar.15th,2017
        }
        /*星期*/
        else if (str.equals("time_week4")) {
            time = getWeek3(dayOfWeek).toUpperCase();//  MONDAY
        }
        return time;
    }

    public static String getWeek(int d) {
        String[] nums = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return nums[d - 1];
    }

    public static String getWeek2(int d) {
        String[] nums = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
        return nums[d - 1];
    }

    public static String getWeek3(int d) {
        String[] nums = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return nums[d - 1];
    }

    public static String getYearCn(String y, boolean style) {
        String y1, y2, y3, y4;
        if (style) {
            y1 = getNumber(Integer.parseInt(y.substring(0, 1)));
            y2 = getNumber(Integer.parseInt(y.substring(1, 2)));
            y3 = getNumber(Integer.parseInt(y.substring(2, 3)));
            y4 = getNumber(Integer.parseInt(y.substring(3, 4)));
        } else {
            y1 = getNumber2(Integer.parseInt(y.substring(0, 1)));
            y2 = getNumber2(Integer.parseInt(y.substring(1, 2)));
            y3 = getNumber2(Integer.parseInt(y.substring(2, 3)));
            y4 = getNumber2(Integer.parseInt(y.substring(3, 4)));
        }
        return y1 + y2 + y3 + y4;
    }


    public static String getYearCn4(String y) {//贰零壹柒
        String y1, y2, y3, y4;
        y1 = getNumber3(Integer.parseInt(y.substring(0, 1)));
        y2 = getNumber3(Integer.parseInt(y.substring(1, 2)));
        y3 = getNumber3(Integer.parseInt(y.substring(2, 3)));
        y4 = getNumber3(Integer.parseInt(y.substring(3, 4)));
        return y1 + y2 + y3 + y4;
    }


    public static String getMouthCn(int m) {
        String[] nums = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
        return nums[m - 1];
    }

    public static String getDayCn(int d) {
        String[] nums = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
                "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
                "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九", "三十", "三十一"};
        return nums[d - 1];
    }

    public static String getDayEnglish(int d) {
        String[] nums = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th",
                "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th", "20th",
                "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th", "30th",
                "31st"};
        return nums[d - 1];
    }


    /**
     * 1-->01  12-->12
     *
     * @param str
     * @return
     */
    private static String getDecimalFormat(String str) {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(Integer.parseInt(str));
    }

    public static String getNumber(int n) {
        String[] nums = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        return nums[n];
    }

    public static String getNumber2(int n) {
        String[] nums = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        return nums[n];
    }

    public static String getNumber3(int n) {
        String[] nums = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        return nums[n];
    }


    public static String getMouth() {
        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MONTH) + 1;
        String[] mons = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return mons[m - 1];
    }


    public static String getMouth5() {
        return getMouth6().toUpperCase();
    }


    public static String getMouth6() {
        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MONTH) + 1;
        String[] mons = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return mons[m - 1];
    }


    public static String setTime(String timestr) {
        SimpleDateFormat sdf = new SimpleDateFormat(timestr);
        String time = sdf.format(new Date());
        return time;
    }


    /**
     * 获取时间间隔
     *
     * @param millisecond Calendar.getInstance().getTimeInMillis()
     * @return 间隔毫秒数
     */
    public static String getSpaceTime(Long millisecond) {
        Calendar calendar = Calendar.getInstance();
        Long currentMillisecond = calendar.getTimeInMillis();
        Long spaceSecond = (currentMillisecond - millisecond);
        return spaceSecond.toString();
    }
}

