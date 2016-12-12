package cz.kuzna.android.firebasecloudmessaging.chat.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Radek Kuznik
 */

public class ChatMessage implements Parcelable {

    private String message;
    private boolean sender;
    private long timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSender() {
        return sender;
    }

    public void setSender(boolean sender) {
        this.sender = sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ChatMessage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeByte((byte) (sender ? 1 : 0));
        parcel.writeLong(timestamp);
    }

    protected ChatMessage(Parcel in) {
        message = in.readString();
        sender = in.readByte() != 0;
        timestamp = in.readLong();
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };
}
