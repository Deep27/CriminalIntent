package com.romanso.criminalintent.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WithDate {

    public static String convertDate(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, y");
        return df.format(date);
    }
}
