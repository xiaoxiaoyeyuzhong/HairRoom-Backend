package com.fdt.common.utils;

import java.util.*;

public class generateWorkDays {

    public static List<Integer> generate(int num, int range) {
        Set<Integer> workDaysSet = new HashSet<>();
        Random random = new Random();

        // 在range范围内随机生成num个不重复的数字
        while (workDaysSet.size() < num) {
            int workDay = random.nextInt(range);
            workDaysSet.add(workDay+1);
        }

        // 将Set转换为List并返回
        return new ArrayList<>(workDaysSet);
    }

}
