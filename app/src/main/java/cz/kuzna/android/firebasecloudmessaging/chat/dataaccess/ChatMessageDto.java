package cz.kuzna.android.firebasecloudmessaging.chat.dataaccess;

/**
 * @author Radek Kuznik
 */

public class ChatMessageDto {

    private String sender;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
