package com.linjin.firebasemessagingtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity {
    private List<String> messageArray;
    ArrayAdapter adapter;
    ListView listView;

    EditText textView;
    Button button;

    FirebaseDatabase database;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        // Instantiate 'textView' and 'button'.
        textView = (EditText) findViewById(R.id.msg_editText);
        button = (Button) findViewById(R.id.msg_sendButton);

        // Instantiate 'messageArray', 'adapter' and 'listView'.
        messageArray = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.activity_message_list_delegate, messageArray);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // Instantiate Firebase database and database reference.
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        dbRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        messageArray.clear();

                        Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                        while(itr.hasNext()) {
                            ChatMessage chatMessage = itr.next().getValue(ChatMessage.class);
                            messageArray.add(chatMessage.msg);
                        }

                        adapter.notifyDataSetInvalidated();
                        gotoListBottom();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    public void sendMessage(View view) {
        enableSending(false);
        String messageString = textView.getText().toString();
        if (messageString.isEmpty()) {enableSending(true); return;}

        String key = dbRef.child("posts").push().getKey();

        ChatMessage chatMessage = new ChatMessage("evanlinjin", messageString);
        Map<String, Object> new_message = new HashMap<>();

        new_message.put("/" + key, chatMessage);
        dbRef.updateChildren(new_message);

        textView.setText("");
        enableSending(true);
    }

    private void enableSending(boolean status) {
        //textView.setEnabled(status);
        button.setEnabled(status);
        gotoListBottom();
    }

    public void gotoListBottom() {
        listView.setSelection(listView.getCount()-1);
    }

    public void gotoListBottom(View view) {
        listView.setSelection(listView.getCount()-1);
    }
}
