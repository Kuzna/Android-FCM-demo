package cz.kuzna.android.firebasecloudmessaging.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Radek Kuznik
 */
public final class DateUtil {

    public static final SimpleDateFormat FORMATTER_dMHHmm= new SimpleDateFormat("d. M. H:mm", Locale.getDefault());

    private DateUtil() {}


    public static String toString(Date date, SimpleDateFormat formatter) {
        return formatter.format(date);
    }
}