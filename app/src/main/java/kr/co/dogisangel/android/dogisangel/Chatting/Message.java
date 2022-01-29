package kr.co.dogisangel.android.dogisangel.Chatting;

/**
 * Created by ccei on 2016-08-26.
 */
public class Message {
    //일반채팅메세지
    public static final int TYPE_MESSAGE = 0;
    //입장
    public static final int TYPE_LOG = 1;
    //타이핑 유/무
    public static final int TYPE_ACTION = 2;

    private int mType;//위의 셋중 하나
    private String mMessage;//쳇메세지
    private String mUsername;

    private Message() {}

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };


    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            return message;
        }
    }

}


