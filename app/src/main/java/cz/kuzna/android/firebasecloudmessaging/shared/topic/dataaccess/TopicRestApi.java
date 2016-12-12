package cz.kuzna.android.firebasecloudmessaging.shared.topic.dataaccess;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author Radek Kuznik
 */

public interface TopicRestApi {

    @POST("/fcm/send")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> sendToTopic(final @Header("Authorization") String authorization, @Body final TopicDto topicDto);
}
