package com.fdt.common.utils;

import com.fdt.common.api.ResultUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateToWeekUtil {

    // 根据字符串日期获取时间类型日期
    public static LocalDate getDate(String date) {
        // 按照 . 切分字符串
        // 第一个\表示第二个\是普通的反斜杠，而这个反斜杠代表正则表达式的转义字符转义点号 ，表示一个普通的 .
        String[] parts = date.split("\\.");

        String yearPart = "";
        String monthPart = "";
        String dayPart = "";

        // 根据切分结果分配年、月、日
        if (parts.length == 3) {
            // 格式为 yyyy.M.d
            yearPart = parts[0];
            monthPart = parts[1];
            dayPart = parts[2];
        } else if (parts.length == 2) {
            // 格式为 M.d
            monthPart = parts[0];
            dayPart = parts[1];
        }

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        // 补全年份逻辑
        int year;
        if (yearPart.isEmpty()) {
            // 如果年份为空
            int month = Integer.parseInt(monthPart);
            if (month == currentMonth) {
                // 月份与当前月份相同，使用当前年份
                year = currentYear;
            } else if (month < currentMonth) {
                // 月份小于当前月份，使用当前年份 + 1
                year = currentYear + 1;
            } else {
                // 月份大于当前月份，使用当前年份
                year = currentYear;
            }
        } else {
            // 如果年份不为空，直接使用输入的年份
            year = Integer.parseInt(yearPart);
        }

        date = year + "." + monthPart + "." + dayPart;


        // 日期匹配格式只写一个M（小写m代表分钟）和一个d，就可以匹配不加零和加零的月，日了
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.M.d");

        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        return localDate;
    }

    public static String getWeek(LocalDate localDate) {
        // todo 只允许预约包括当前日期的7天

        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        int dayOfWeekValue = dayOfWeek.getValue();

        return String.valueOf(dayOfWeekValue);
    }
}
