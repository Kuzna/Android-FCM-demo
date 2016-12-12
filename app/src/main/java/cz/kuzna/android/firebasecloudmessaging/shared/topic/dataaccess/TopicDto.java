package cz.kuzna.android.firebasecloudmessaging.shared.topic.dataaccess;

/**
 * @author Radek Kuznik
 */

public class TopicDto {

    private String to;
    private Object data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
