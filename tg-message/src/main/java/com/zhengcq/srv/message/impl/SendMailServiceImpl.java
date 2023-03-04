package com.zhengcq.srv.message.impl;

import com.alibaba.fastjson.JSONObject;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.util.LunarUtil;
import com.nlf.calendar.util.SolarUtil;
import com.zhengcq.srv.message.util.SendmailUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


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

         // 1. birth day notify
        BirthDateNotify();
        // 2. Gpt question
        GptQuestion();

    }
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(new Date());
    }

    public static void GptQuestion() {
        String email = "1318692162@qq.com";
        String title = String.format("Today Gpt response: %s", getCurrentDate());
//        File gptQuestionFile = new File("D://gptQuestion.txt");
        File gptQuestionFile = new File("/opt/gptQuestion.txt");
        try {
            FileReader reader = new FileReader(gptQuestionFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            String url = "https://api.openai.com/v1/completions";
            while(line != null){
                System.out.println(line);
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = null;
                try {
                    HttpPost httpPost = new HttpPost(url);
                    String jsonParam = "{\n" +
                            "\t\"prompt\":\"%s\",\n" +
                            "\t\"model\":\"text-davinci-003\",\n" +
                            "\t\"max_tokens\":4000\n" +
                            "}";
                    jsonParam = String.format(jsonParam, line);
                    System.out.println(jsonParam);

                    httpPost.setHeader("Authorization", "");

                    StringEntity stringEntity = new StringEntity(jsonParam, ContentType.APPLICATION_JSON);
                    httpPost.setEntity(stringEntity);
                    response = client.execute(httpPost);
                    String  responseStr = EntityUtils.toString(response.getEntity(), "utf-8");

                    System.out.println(responseStr);

                    GptMessage gptMessage = JSONObject.parseObject(responseStr, GptMessage.class);
                    if (gptMessage != null && gptMessage.getId() != null){
                        String gptResp = gptMessage.getChoices()[0].getText();
                        System.out.println(gptResp);

                        String resp = String.format("gpt:%s \n answer:%s", line, gptResp);

                        SendmailUtil.sendEmail(email, title, resp);
                    } else {
                        System.out.println("GptQuestion fail");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally
                {

                    try {
                        if (response != null) {
                            response.close();
                        }
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void BirthDateNotify() {
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
        if (flag > 0) {
            // 农历
            int curMonth = lunar.getMonth();
            int curDay = lunar.getDay();
            int curDays = calendar.get(Calendar.DAY_OF_YEAR);
            if (month == curMonth && day == curDay) {
                return 0;
            }
            int yearDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            System.out.println(yearDays);
            Lunar tmpLun = new Lunar(lunar.getYear(), month, day);
            Calendar tmpCal = tmpLun.getSolar().getCalendar();
            int days = tmpCal.get(Calendar.DAY_OF_YEAR);
            if (tmpCal.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
                days += yearDays;
            }

            return curDays - days;
        } else {
            // 新历

            int curMonth = calendar.get(Calendar.MONTH) + 1;
            int curDay = calendar.get(Calendar.DAY_OF_MONTH);
            int curDays = calendar.get(Calendar.DAY_OF_YEAR);
            if (month == curMonth && day == curDay) {
                return 0;
            }
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.set(Calendar.MONTH, month);
            tmpCal.set(Calendar.DAY_OF_MONTH, day);
            int days = tmpCal.get(Calendar.DAY_OF_YEAR);
            return curDays - days;
        }
    }
}
