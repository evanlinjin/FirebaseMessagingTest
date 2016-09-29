package com.linjin.firebasemessagingtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity {

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

        // Instantiate 'textView' and 'button'.
        textView = (EditText) findViewById(R.id.msg_editText);
        button = (Button) findViewById(R.id.msg_sendButton);

        // Instantiate 'messageArray', 'adapter' and 'listView'.
        messageArray = new ArrayList<>();
        adapter = new ChatMessageList(this, 0, messageArray);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // Setup Firebase Persistence on First Run.
        if (firstRun) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // Enable Local Storage.
            firstRun = false;
        }
        // Get Firebase Reference.
        chatRef = FirebaseDatabase.getInstance().getReference().child("chat");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Instantiate Firebase database and database reference.
        chatRef.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        messageArray.add(dataSnapshot.getValue(ChatMessage.class));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        messageArray.remove(dataSnapshot.getValue(ChatMessage.class));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    public void sendMessage(View view) {
        enableSending(false);
        gotoListBottom();
        String messageString = textView.getText().toString();
        if (messageString.isEmpty()) {enableSending(true); return;}

        String key = chatRef.child("posts").push().getKey();

        ChatMessage chatMessage = new ChatMessage("evanlinjin", messageString);
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
        listView.setSelection(listView.getCount()-1);
    }

    public void gotoListBottom(View view) {
        listView.setSelection(listView.getCount()-1);
    }
}
