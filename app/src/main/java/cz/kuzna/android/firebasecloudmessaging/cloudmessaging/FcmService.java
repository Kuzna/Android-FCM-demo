package cz.kuzna.android.firebasecloudmessaging.cloudmessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import cz.kuzna.android.firebasecloudmessaging.MainActivity;
import cz.kuzna.android.firebasecloudmessaging.R;
import cz.kuzna.android.firebasecloudmessaging.chat.domain.ChatMessage;
import cz.kuzna.android.firebasecloudmessaging.chat.ui.ChatActivity;
import cz.kuzna.android.firebasecloudmessaging.shared.prefs.SharedPrefsManager;

/**
 * @author Radek Kuznik
 */

public class FcmService extends FirebaseMessagingService {

    /** Constants */
    private static final String TAG = FcmService.class.getName();
    public static final int NOTIFICATION_ID = 988714;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        Log.d(TAG, "Cloud message received");
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, ": Message data payload: " + remoteMessage.getData());

            if("/topics/chat".equals(remoteMessage.getFrom())) {
                notifyChatMessage(remoteMessage);
                return;
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification());
        }

    }

    private void sendNotification(RemoteMessage.Notification notification) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_firebase)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void sendNotificationChatMessage(final String message) {
        final Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_firebase)
                .setContentTitle("New chat message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void notifyChatMessage(RemoteMessage remoteMessage) {

        final Map<String, String> data = remoteMessage.getData();

        if(data != null && data.containsKey("sender") && data.containsKey("message")) {
            final String sender = data.get("sender");
            final String message = data.get("message");

            if(sender.equals(SharedPrefsManager.getUuid(getApplicationContext()))) {
                return;
            }

            sendNotificationChatMessage(message);

            final ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(false);
            chatMessage.setMessage(message);
            chatMessage.setTimestamp(System.currentTimeMillis());

            final Intent intent = new Intent(ChatActivity.ACTION_CHAT_MSG);
            intent.putExtra(ChatActivity.EXTRA_CHAT_MSG, chatMessage);
            sendBroadcast(intent);
        }
    }
}