package com.linjin.firebasemessagingtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity {

    public static String name;
    private ArrayList<ChatMessage> messageArray;
    ChatMessageList adapter;
    ListView listView;

    EditText textView;
    Button button;

    DatabaseReference chatRef;
    static boolean firstRun = true;

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        firstRun = false;
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        // Get intent.
        Intent intent = getIntent();
        name = intent.getStringExtra(LoginActivity._NAME_);

        // Instantiate 'textView' and 'button'.
        textView = (EditText) findViewById(R.id.msg_editText);
        button = (Button) findViewById(R.id.msg_sendButton);

        textView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (
                                keyCode == KeyEvent.KEYCODE_ENTER ||
                                keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT ||
                                keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                        )) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

        // Instantiate 'messageArray', 'adapter' and 'listView'.
        messageArray = new ArrayList<>();
        adapter = new ChatMessageList(this, 0, messageArray);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // Setup Firebase  on First Run.
        if (firstRun) {
            firstRun = false;
            // Setup Firebase Persistence.
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // Enable Local Storage.
        }
        if (chatRef == null) {
            chatRef = FirebaseDatabase.getInstance().getReference().child("chat");
            chatRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    messageArray.add(dataSnapshot.getValue(ChatMessage.class));
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    messageArray.remove(dataSnapshot.getValue(ChatMessage.class));
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public void sendMessage(View view) {
        sendMessage();
    }
    public void sendMessage() {
        enableSending(false);
        gotoListBottom();
        String messageString = textView.getText().toString();
        if (messageString.isEmpty()) {enableSending(true); return;}

        String key = chatRef.child("posts").push().getKey();

        ChatMessage chatMessage = new ChatMessage(name, messageString);
        Map<String, Object> new_message = new HashMap<>();

        new_message.put("/" + key, chatMessage);
        chatRef.updateChildren(new_message);

        textView.setText("");
        enableSending(true);
    }

    private void enableSending(boolean status) {
        textView.setEnabled(status);
        button.setEnabled(status);
    }

    public void gotoListBottom() {
        listView.smoothScrollToPosition(listView.getCount()-1);
    }

    public void gotoListBottom(View view) {
        gotoListBottom();
    }
}
