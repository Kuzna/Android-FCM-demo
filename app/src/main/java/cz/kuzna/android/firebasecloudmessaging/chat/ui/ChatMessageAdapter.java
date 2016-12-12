package cz.kuzna.android.firebasecloudmessaging.chat.ui;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import cz.kuzna.android.firebasecloudmessaging.R;
import cz.kuzna.android.firebasecloudmessaging.chat.domain.ChatMessage;
import cz.kuzna.android.firebasecloudmessaging.util.DateUtil;

/**
 * Chat message adapter
 *
 * @author Radek Kuznik
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>{

    private ArrayList<ChatMessage> messages = new ArrayList<>();

    @Override
    public void onBindViewHolder(ViewHolder contactViewHolder, int i) {
        final ChatMessage msg = getItem(i);


        contactViewHolder.tvText.setText(msg.getMessage());
        contactViewHolder.tvText.setBackgroundResource(msg.isSender() ? R.drawable.ic_chat_bubble_right : R.drawable.ic_chat_bubble_left);
        contactViewHolder.tvDateTime.setText(DateUtil.toString(new Date(msg.getTimestamp()), DateUtil.FORMATTER_dMHHmm));

//            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.gravity = isSender ? Gravity.RIGHT : Gravity.LEFT;
//            contactViewHolder.llWrapper.setLayoutParams(params);

        contactViewHolder.llWrapper.setGravity(msg.isSender() ? Gravity.RIGHT : Gravity.LEFT);
    }

    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_chat_msg, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public ChatMessage getItem(int position) {
        return messages.get(position);
    }

    public void add(final ChatMessage chatMessage) {
        messages.add(chatMessage);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llWrapper;
        private LinearLayout llChatMessage;
        private TextView tvText;
        private TextView tvDateTime;

        public ViewHolder(View v) {
            super(v);
            tvText =  (TextView) v.findViewById(R.id.messageText);
            tvDateTime =  (TextView) v.findViewById(R.id.messageDateTime);
            llWrapper = (LinearLayout) v.findViewById(R.id.linearLayoutWrapper);
            llChatMessage = (LinearLayout) v.findViewById(R.id.linearLayoutChatMessage);
        }
    }
}