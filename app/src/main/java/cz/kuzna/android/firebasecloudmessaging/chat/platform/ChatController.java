package cz.kuzna.android.firebasecloudmessaging.chat.platform;

import cz.kuzna.android.firebasecloudmessaging.chat.dataaccess.ChatMessageDto;
import cz.kuzna.android.firebasecloudmessaging.shared.prefs.SharedPrefsManager;
import cz.kuzna.android.firebasecloudmessaging.shared.rest.RestModule;
import cz.kuzna.android.firebasecloudmessaging.shared.topic.dataaccess.TopicRestService;
import cz.kuzna.android.firebasecloudmessaging.shared.topic.inject.TopicModule;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Radek Kuznik
 */

public class ChatController {

    private TopicRestService topicRestService;

    private static final String KEY = "key=AAAA5oRnBws:APA91bFYjsKC1GUc4dqIUPENUsVtjxkrZb_lf81J-1jyKOxzWxb8U1sdWswGvQIBsMejQ_ZDeQV9E-QH9LPI5uuZe5Hz3Cit1Enz5ot8fzpp4JhVzAt1QH6qr2a9gg9lgdem4G6pWSMdq43pzwbsZFBOhWnzZhBqwQ";

    private SharedPrefsManager prefsController;

    public ChatController() {
        this.prefsController = new SharedPrefsManager();
        this.topicRestService = TopicModule.provideRestService(
                TopicModule.provideRestApi(RestModule.provideFcmRetrofit(
                        RestModule.provideOkHttpClient())));
    }

    public Observable<Boolean> sendMessageToTopic(final String topic, final String sender,
                                                  final String message) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                final ChatMessageDto chatMessageDto = new ChatMessageDto();
                chatMessageDto.setMessage(message);
                chatMessageDto.setSender(sender);

                final Boolean result = topicRestService.sendToTopic(KEY, topic, chatMessageDto);

                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }
}
