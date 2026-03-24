package com.lttbdd.hrmsystem.utils;

import java.util.Calendar;

public class SalaryCalculator {

    public static final float OT_MULTIPLIER = 1.5f;

    public static float calculate(float baseSalary, int workingDays, int standardDays,
                                  float overtimeHours, float bonus, float deduction) {
        if (standardDays <= 0) {
            return 0f;
        }

        float daily = baseSalary / standardDays;
        float hourly = daily / 8f;
        float earned = daily * workingDays;
        float otPay = hourly * overtimeHours * OT_MULTIPLIER;
        return Math.max(0f, earned + otPay + bonus - deduction);
    }

    public static int getStandardWorkingDays(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int count = 0;
        for (int d = 1; d <= days; d++) {
            cal.set(year, month - 1, d);
            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                count++;
            }
        }
        return count;
    }
}
