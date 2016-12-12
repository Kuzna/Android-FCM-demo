package cz.kuzna.android.firebasecloudmessaging.shared.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Radek Kuznik
 */

public class SharedPrefsManager {

    private static final String PREF_NAME              = "fcm_chat_demo";
    private static final String PREF_UUID              = "pref_uuid";
    private static final String PREF_FCM_TOKEN         = "pref_fcm_token";
    private static final String PREF_TOPIC_SUBSCRIBED  = "pref_topic_subscribed";

    private static SharedPreferences getSharedPreferences(final Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setUuid(final Context context, final String uuid) {
        getSharedPreferences(context)
                .edit()
                .putString(PREF_UUID, uuid)
                .apply();
    }

    public static String getUuid(final Context context) {
        return getSharedPreferences(context)
                .getString(PREF_UUID, "");
    }

    public static boolean hasUuid(final Context context) {
        return getSharedPreferences(context)
                .getString(PREF_UUID, null) != null;
    }

    public static void setFcmToken(final Context context, final String token) {
        getSharedPreferences(context)
                .edit()
                .putString(PREF_FCM_TOKEN, token)
                .apply();
    }

    public static String getFcmToken(final Context context) {
        return getSharedPreferences(context)
                .getString(PREF_FCM_TOKEN, "");
    }

    public static boolean isTopicSubscribed(final Context context) {
        return getSharedPreferences(context)
                .getBoolean(PREF_TOPIC_SUBSCRIBED, false);
    }

    public static void setTopicSubscription(final Context context, boolean subscribed) {
        getSharedPreferences(context)
                .edit()
                .putBoolean(PREF_TOPIC_SUBSCRIBED, subscribed)
                .apply();
    }
}
