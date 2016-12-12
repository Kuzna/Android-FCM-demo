package cz.kuzna.android.firebasecloudmessaging.cloudmessaging;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import cz.kuzna.android.firebasecloudmessaging.shared.prefs.SharedPrefsManager;

/**
 * @author Radek Kuznik
 */
public class FcmInstanceIDService extends FirebaseInstanceIdService {

    public static final String EXTRA_FCM_TOKEN          = "ext_fcm_token";
    public static final String ACTION_FCM_TOKEN_REFRESH = "cz.kuzna.android.fcm.token_update";

    private static final String TAG = FcmInstanceIDService.class.getName();

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "Refreshed token");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if(refreshedToken == null) {
            return;
        }

        SharedPrefsManager.setFcmToken(getApplicationContext(), refreshedToken);

        final Intent intent = new Intent(ACTION_FCM_TOKEN_REFRESH);
        intent.putExtra(EXTRA_FCM_TOKEN, refreshedToken);
        sendBroadcast(intent);
    }
}