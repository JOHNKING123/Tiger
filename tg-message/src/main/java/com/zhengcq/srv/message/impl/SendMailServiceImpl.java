package com.zhengcq.srv.message.impl;

import com.nlf.calendar.Lunar;
import com.nlf.calendar.util.LunarUtil;
import com.nlf.calendar.util.SolarUtil;
import com.zhengcq.srv.message.util.SendmailUtil;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * @ClassName: SendMailServiceImpl
 * @Description: todo
 * @Company: 广州市两棵树网络科技有限公司
 * @Author: zhengcq
 * @Date: 2020/12/2
 */
public class SendMailServiceImpl {

    public static Calendar calendar = Calendar.getInstance();

    public static Lunar lunar = new Lunar();
    public static void main(String[] args) {

//        File birthFile = new File("D://birth.txt");
        File birthFile = new File("/opt/birth.txt");
        try {
            // 生日通知
            FileReader reader = new FileReader(birthFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while(line != null){
                System.out.println(line);
                String[] tmpStrs = line.split("[ \t]+");
                if (tmpStrs.length < 4) {
                    line = bufferedReader.readLine();
                    continue;
                }
                int flag = Integer.parseInt(tmpStrs[0]);
                int month = Integer.parseInt(tmpStrs[1]);
                int day = Integer.parseInt(tmpStrs[2]);
                String msg = tmpStrs[3];
                Integer dayNote = null;
                if (tmpStrs.length >= 5) {
                    dayNote = Integer.parseInt(tmpStrs[4]);
                }
                String userEmail = null;
                if (tmpStrs.length >= 6) {
                    userEmail = tmpStrs[5];
                }
                int dayDiff = getDiffDay(flag, month, day);
                notifyEmail(dayDiff, msg, dayNote, userEmail);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            reader.close();


            // 传统节假日通知
            Set<String> lunarFestival = LunarUtil.FESTIVAL.keySet();
            for (String festival : lunarFestival) {
                String[] tmpStrs = festival.split("-");
                if (tmpStrs.length != 2) {
                    continue;
                }
                int month = Integer.parseInt(tmpStrs[0]);
                int day = Integer.parseInt(tmpStrs[1]);
                String msg = LunarUtil.FESTIVAL.get(festival);
                int dayDiff = getDiffDay(1, month, day);
                notifyEmail(dayDiff, msg, null, null);
            }


            // 西方节假日通知
            Set<String> solarFestival = SolarUtil.FESTIVAL.keySet();
            // 去除一些节假日
//            this.put("3-8", "妇女节");
//            this.put("3-12", "植树节");
//            this.put("3-15", "消费者权益日");
            solarFestival.remove("3-8");
            solarFestival.remove("3-12");
            solarFestival.remove("3-15");
            for (String festival : solarFestival) {
                String[] tmpStrs = festival.split("-");
                if (tmpStrs.length != 2) {
                    continue;
                }
                int month = Integer.parseInt(tmpStrs[0]);
                int day = Integer.parseInt(tmpStrs[1]);
                String msg = SolarUtil.FESTIVAL.get(festival);
                int dayDiff = getDiffDay(0, month, day);
                notifyEmail(dayDiff, msg, null, null);
            }

            // 西方节日，周概念的节日
            Set<String> weekSolarFestival = SolarUtil.WEEK_FESTIVAL.keySet();
            for (String festival : weekSolarFestival) {
                String[] tmpStrs = festival.split("-");
                if (tmpStrs.length != 3) {
                    continue;
                }
                int month = Integer.parseInt(tmpStrs[0]);
                int week = Integer.parseInt(tmpStrs[1]);
                int weekDay = Integer.parseInt(tmpStrs[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.WEEK_OF_MONTH, week);
                calendar.set(Calendar.DAY_OF_WEEK, weekDay + 1);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String msg = SolarUtil.WEEK_FESTIVAL.get(festival);
                int dayDiff = getDiffDay(0, month, day);
                notifyEmail(dayDiff, msg, null, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void notifyEmail(int dayDiff, String msg, Integer dayNote, String userEmail) throws Exception {
        String email = "1318692162@qq.com";
        if (userEmail != null && !userEmail.equals("")) {
            email = userEmail;
        }
        String title = "test";
        String content = "test";
        boolean notifyFlag = notify(dayDiff, dayNote);
        if (notifyFlag) {
            if (dayDiff == 0) {
                title = String.format("今天 %s 通知", msg);
                content = String.format("今天%s 通知", msg);
            } else {
                title = String.format("%s 通知，将在%s 天后进行", msg, (0 - dayDiff));
                content = String.format("%s 通知，将在%s 天后进行", msg, (0 - dayDiff));
            }
            SendmailUtil.sendEmail(email, title, content);
        }
        System.out.println(notifyFlag + "  " + msg);
    }

    public static boolean notify(int dayDiff, Integer dayNote) {
        if (dayNote == null) {
            if (dayDiff == 0 || dayDiff == -7) {
                return true;
            }
        } else {
            if (dayDiff == 0 || dayDiff == dayNote) {
                return true;
            }
        }
        return false;
    }

    public static int getDiffDay(int flag, int month, int day) {
        int dayDiff = 0;
        if (flag > 0) {
            // 农历
            int curMonth = lunar.getMonth();
            int curDay = lunar.getDay();
            int curDays = calendar.get(Calendar.DAY_OF_YEAR);
            int curYear = calendar.get(Calendar.YEAR);
            if (month == curMonth && day == curDay) {
                return 0;
            }
            Lunar tmpLun = new Lunar(lunar.getYear(), month, day);
            Calendar tmpCal = tmpLun.getSolar().getCalendar();
            int tmpYear = tmpCal.get(Calendar.YEAR);
            if (curYear != tmpYear && curYear == (tmpYear + 1)) {
                tmpLun = new Lunar(lunar.getYear() + 1, month, day);
                tmpCal = tmpLun.getSolar().getCalendar();
            }
            int days = tmpCal.get(Calendar.DAY_OF_YEAR);
            dayDiff =  curDays - days;
            if (dayDiff > 0) {
                tmpCal.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                dayDiff = getDistanceOfTwoDate(tmpCal.getTime(), calendar.getTime()).intValue();
            }
        } else {
            // 新历

            int curMonth = calendar.get(Calendar.MONTH) + 1;
            int curDay = calendar.get(Calendar.DAY_OF_MONTH);
            int curDays = calendar.get(Calendar.DAY_OF_YEAR);
            if (month == curMonth && day == curDay) {
                return 0;
            }
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.set(Calendar.MONTH, month - 1);
            tmpCal.set(Calendar.DAY_OF_MONTH, day);
            int days = tmpCal.get(Calendar.DAY_OF_YEAR);
            dayDiff =  curDays - days;
            if (dayDiff > 0) {
                tmpCal.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                dayDiff = getDistanceOfTwoDate(tmpCal.getTime(), calendar.getTime()).intValue();
            }
        }
        return dayDiff;
    }


    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static Long getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }
}
