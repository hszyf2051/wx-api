package com.yif.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author admin
 */
public class test {
    /**
     * 转化格式
     * @param date
     * @return
     * @throws ParseException
     */
    public static String formatDate(String date) throws ParseException {
        String replaceDate = date.replaceAll("/", "-");
        Date newDate = new SimpleDateFormat("MM-dd").parse(replaceDate);
        String now = new SimpleDateFormat("M月d日").format(newDate);
        return now;
    }
    public static void main(String[] args) throws Exception{
//
//        String oldDate = "2022/12/17";
//        String newDate = oldDate.replaceAll("/","-");
//        System.out.println(newDate);
//        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(newDate);
//        System.out.println(date);
//        String now = new SimpleDateFormat("yyyy年M月dd日").format(date);
        String date = formatDate("09-23");

        System.out.println(date);

    }
}
