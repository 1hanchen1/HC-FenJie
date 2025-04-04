package com.hanchen.hcfenjie.util;

import java.text.DecimalFormat;

public class ChangeUtil {
    public static boolean check(double d) {
        DecimalFormat df = new DecimalFormat("######0.000");
        return Math.random() - Double.parseDouble(df.format(d)) < 0.0d;
    }
}
