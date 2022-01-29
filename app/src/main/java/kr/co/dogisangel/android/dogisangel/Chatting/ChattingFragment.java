package kr.co.dogisangel.android.dogisangel.Chatting;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import kr.co.dogisangel.android.dogisangel.R;


/**
 * 실제 채팅이 이루어 지고 있는 곳
 */
public class ChattingFragment extends Fragment {

    private static final int REQUEST_LOGIN = 0;

    private RecyclerView mMessagesView;
    private EditText mInputMessageView;

    //실제 채팅메세지가 모여져 있는 곳
    private List<Message> mMessages = new ArrayList<Message>();
    //채팅메세지 어댑터
    private RecyclerView.Adapter mAdapter;

    //현재 상대방이 입력중임을 채팅상대들에게 보여주기 위함.
    private boolean mTyping = false;

    //타이핑을 핸들링하기 위한 Handler
    private Handler mTypingHandler = new Handler();

    //Handler delay time
    private static final int TYPING_TIMER_LENGTH = 600;
    //내 이름(별명)
    private String mUsername;
    /*
     이 EngineIO(Socket.io의 자바버전)는 반드시 Main Thread에서
     생성해야 한다.
     */
    private Socket mSocket;
    {
        try {
            //채팅하고자 하는 http URL을 설정하고 소켓을 얻어온다.
            //IO#Options 내부클래스를 이용하여 다양한 소켓옵션을 선언할 수 있다(https등)
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public ChattingFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //더미로 어댑터를 채운다.
        mAdapter = new MessageAdapter(activity, mMessages);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        /*
          on을 진행한다.
          여기서는 총 7개의 온(Emitter설정)을 진행한다.
          Socket.EVENT_* 내부에 설정된 on Event설정을 의미
          나머지는 on(StringKeyName, Listener)을 등록한다.
          Server(여기선 Node.js)를 통해 socket.emit(StringKeyName,서버에서 보낸 데이터(보통JSON))으로
          넘어오면 해당 Listener에 구현된 Runnable을 실행한다.
          on 메소드에 listener등록은 반드시 Main Thread에서 진행해야 함.
         */
        //소켓의 예기치 못한 에러나 소켓타임아웃이 발생하면 onconnectError이벤트를 진행
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        //새로운 메세지(채팅)가 emit("new message", args..)으로 넘어온 업무를 실행
        mSocket.on("new message", onNewMessage);

        //새 사용자가 추가되었을때 실행될 이벤트를 정의
        mSocket.on("user joined", onUserJoined);

        //해당 사용자가 채팅방을 나갔을때 실행
        mSocket.on("user left", onUserLeft);

        //사용자 또는 내가 타이핑 중일때 사용자에게 보낼 타이핑중 메세지
        mSocket.on("typing", onTyping);

        //타이핑 멈춤
        mSocket.on("stop typing", onStopTyping);
        //소켓과 스트림을 연결(연결에 문제발생시, on에 등록된 리스너가 실행됨)
        mSocket.connect();

        //서버와 기본정보를 이용해 로그인 한다(보통 채팅방을 만들거나, 일대일 채팅화면을 구현한다)
        startSignIn();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chatting_fragment, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect(); //소켓 연결을 종료함

        //등록된 Emitt를 off시킨다.
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessagesView = (RecyclerView) view.findViewById(R.id.chatting_view);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesView.setAdapter(mAdapter);

        mInputMessageView = (EditText) view.findViewById(R.id.chatting_input);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null == mUsername) return;
                if (!mSocket.connected()) return;

                if (!mTyping) {
                    mTyping = true;
                    mSocket.emit("typing");
                }

                ////타이핑을 핸들링하기 위한 Handler를 취소 와 통지를 진행
                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageButton sendButton = (ImageButton) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            getActivity().finish();
            return;
        }

        mUsername = data.getStringExtra("username");
        int numUsers = data.getIntExtra("numUsers", 1);

        addLog(getResources().getString(R.string.message_welcome));
        addParticipantsLog(numUsers);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_leave) {
            leave();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //로그인한 사용자를 추가
    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }
    //참가자 수에 따라 다른 리소스를 선택하여 치환
    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }
    //채팅메세지를 추가
    private void addMessage(String username, String message) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }
    //타이핑중일때의 메세지를 출력
    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }
    //다른 사용자가 타이핑을 끝낼때 호출
    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }
    private void attemptSend() {
        if (null == mUsername) return;
        if (!mSocket.connected()) return;

        mTyping = false;

        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }

        mInputMessageView.setText("");
        addMessage(mUsername, message);

        // 서버로 메세지를 보냄
        mSocket.emit("new message", message);
    }

    //채팅입장
    private void startSignIn() {
        mUsername = null;
        Intent intent = new Intent(getActivity(), Chatting_main.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    //채팅을 떠날때 커넥션을 끝내고 다시 채팅할 때
    private void leave() {
        mUsername = null;
        mSocket.disconnect();
        //여기선 다시 별칭을 입력하는 쪽으로 포워딩함
        mSocket.connect();
        startSignIn();
    }
    //RecyclerView를 맨밑으로 스크롤링
    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }
    //커넥션 에러가 발생시 호출
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    //채팅메세지 출력
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(username);
                    addMessage(username, message);
                }
            });
        }
    };
    //사용자가 들어왔을때(Server에서 emit(,,을 호출했을 때))
    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_joined, username));
                    addParticipantsLog(numUsers);
                }
            });
        }
    };
    //떠났을 때 현재 채팅인원 및 사용자가 서버에서 넘어옴
    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }
                    addLog(getResources().getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    removeTyping(username);
                }
            });
        }
    };
    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing");
        }
    };
}