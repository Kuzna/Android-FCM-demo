package cz.kuzna.android.firebasecloudmessaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.kuzna.android.firebasecloudmessaging.chat.ui.ChatActivity;
import cz.kuzna.android.firebasecloudmessaging.cloudmessaging.FcmInstanceIDService;
import cz.kuzna.android.firebasecloudmessaging.shared.prefs.SharedPrefsManager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_fcm_token) TextView tvFcmToken;
    @BindView(R.id.btn_subscribe_topic) Button btnSubscribeTopic;
    @BindView(R.id.btn_unsubscribe_topic) Button btnUnsubscribeTopic;
    @BindView(R.id.btn_chat) Button btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateUuid();

        ButterKnife.bind(this);
    }

    public void onResume() {
        super.onResume();
        if(SharedPrefsManager.isTopicSubscribed(getApplicationContext())) {
            enableUnsubscribeTopicViews();
        } else {
            enableSubscribeTopicViews();
        }

        showToken(SharedPrefsManager.getFcmToken(getApplicationContext()));
        registerReceiver(receiver, new IntentFilter(FcmInstanceIDService.ACTION_FCM_TOKEN_REFRESH));
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void showToken(final String token) {
        if(token == null) {
            return;
        }

        tvFcmToken.setText(token);
        Log.i("MainActivity", token);
    }

    @OnClick(R.id.btn_subscribe_topic)
    public void onClickSubscribeTopic(View view) {
        FirebaseMessaging.getInstance().subscribeToTopic(ChatActivity.TOPIC_CHAT);
        SharedPrefsManager.setTopicSubscription(getApplicationContext(), true);
        enableUnsubscribeTopicViews();
    }

    @OnClick(R.id.btn_unsubscribe_topic)
    public void onClickUnsubscribeTopic(View view) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(ChatActivity.TOPIC_CHAT);
        SharedPrefsManager.setTopicSubscription(getApplicationContext(), false);
        enableSubscribeTopicViews();
    }

    @OnClick(R.id.btn_chat)
    public void onClickOpenChat(View view) {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra(FcmInstanceIDService.EXTRA_FCM_TOKEN)) {
                showToken(intent.getStringExtra(FcmInstanceIDService.EXTRA_FCM_TOKEN));
            }
        }
    };

    private void generateUuid() {
        if(!SharedPrefsManager.hasUuid(getApplicationContext())) {
            SharedPrefsManager.setUuid(getApplicationContext(), UUID.randomUUID().toString());
        }
    }

    private void enableSubscribeTopicViews() {
        btnUnsubscribeTopic.setEnabled(false);
        btnChat.setEnabled(false);
        btnSubscribeTopic.setEnabled(true);
    }

    private void enableUnsubscribeTopicViews() {
        btnUnsubscribeTopic.setEnabled(true);
        btnChat.setEnabled(true);
        btnSubscribeTopic.setEnabled(false);
    }
}