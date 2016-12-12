package cz.kuzna.android.firebasecloudmessaging.chat.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.kuzna.android.firebasecloudmessaging.R;
import cz.kuzna.android.firebasecloudmessaging.chat.domain.ChatMessage;
import cz.kuzna.android.firebasecloudmessaging.chat.platform.ChatController;
import cz.kuzna.android.firebasecloudmessaging.shared.prefs.SharedPrefsManager;
import rx.functions.Action1;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_CHAT_MSG   = "ext_chat_msg";
    public static final String ACTION_CHAT_MSG  = "cz.kuzna.android.fcm.chat.msg";
    public static final String TOPIC_CHAT       = "chat";

    @BindView(R.id.rv_chat_messages) protected RecyclerView rvMessages;
    @BindView(R.id.et_chat_input) protected EditText etChatInput;
    @BindView(R.id.iv_send_message) protected ImageView ivSendMessage;

    private ChatMessageAdapter chatMessageAdapter;
    private boolean hasText = false;
    private ChatController chatController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        chatController = new ChatController();

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ACTION_CHAT_MSG));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void initRecyclerView() {
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        chatMessageAdapter = new ChatMessageAdapter();
        rvMessages.setAdapter(chatMessageAdapter);
    }

    @OnTextChanged(value = { R.id.et_chat_input },
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChangedChatInput(final Editable editable) {
        final String msg = etChatInput.getText().toString().trim();

        if (msg.length() == 0) {
            ivSendMessage.setImageResource(R.drawable.ic_send_gray);
            hasText = false;
        } else if (msg.length() > 0 && !hasText) {
            ivSendMessage.setImageResource(R.drawable.ic_send_blue);
            hasText = true;
        }
    }

    @OnClick(R.id.iv_send_message)
    public void onClickSendMessage(final View view) {
        sendMessage(etChatInput.getText().toString().trim());
    }

    public void sendMessage(final String msg) {
        chatController.sendMessageToTopic(TOPIC_CHAT,
                SharedPrefsManager.getUuid(getApplicationContext()), msg)
                .subscribe(new Action1<Boolean>() {

            @Override
            public void call(final Boolean result) {
                if(result == null || !result) {
                    Toast.makeText(getApplicationContext(), "Message send failure", Toast.LENGTH_LONG).show();
                } else {
                    final ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessage(msg);
                    chatMessage.setTimestamp(System.currentTimeMillis());
                    chatMessage.setSender(true);
                    etChatInput.setText("");
                    addMessage(chatMessage);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Message send failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addMessage(final ChatMessage msg) {
        chatMessageAdapter.add(msg);
        rvMessages.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra(EXTRA_CHAT_MSG)) {
                addMessage((ChatMessage)intent.getParcelableExtra(EXTRA_CHAT_MSG));
            }
        }
    };
}
