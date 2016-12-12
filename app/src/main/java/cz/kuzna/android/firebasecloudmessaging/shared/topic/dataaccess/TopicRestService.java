package cz.kuzna.android.firebasecloudmessaging.shared.topic.dataaccess;

import android.support.annotation.WorkerThread;
import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author Radek Kuznik
 */

public class TopicRestService {

    private static final String TAG = "TopicRestService";

    private TopicRestApi restApi;

    public TopicRestService(final TopicRestApi restApi) {
        this.restApi = restApi;
    }

    @WorkerThread
    public boolean sendToTopic(final String authorization, final String topic, final Object data) {

        final TopicDto topicDto = new TopicDto();
        topicDto.setTo("/topics/" + topic);
        topicDto.setData(data);

        try {
            final Response<ResponseBody> response = restApi.sendToTopic(authorization, topicDto).execute();

            if (response.isSuccessful()) {
                Log.d(TAG, "Ok");
                return true;
            } else if(response.errorBody() != null) {
                Log.d(TAG, "Donut order failed: " + response.errorBody().toString());
                return false;
            } else {
                Log.d(TAG, "Message send failure");
            }
        } catch (Throwable e) {
            Log.d(TAG, "Message send failure", e);
            return false;
        }

        return false;
    }
}
