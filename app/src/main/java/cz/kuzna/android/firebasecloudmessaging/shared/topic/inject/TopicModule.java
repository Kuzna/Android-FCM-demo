package cz.kuzna.android.firebasecloudmessaging.shared.topic.inject;

import cz.kuzna.android.firebasecloudmessaging.shared.topic.dataaccess.TopicRestApi;
import cz.kuzna.android.firebasecloudmessaging.shared.topic.dataaccess.TopicRestService;
import retrofit2.Retrofit;

/**
 * @author Radek Kuznik
 */

public class TopicModule {

    public static TopicRestApi provideRestApi(final Retrofit retrofit) {
        return retrofit.create(TopicRestApi.class);
    }

    public static TopicRestService provideRestService(final TopicRestApi restApi) {
        return new TopicRestService(restApi);
    }
}
