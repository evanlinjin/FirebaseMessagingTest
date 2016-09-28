package com.linjin.firebasemessagingtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity {
    private List<String> messageArray;
    ArrayAdapter adapter;
    ListView listView;

    FirebaseDatabase database;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        messageArray = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.activity_message_list_delegate, messageArray);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        dbRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            messageArray.add(dataSnapshot.getValue(String.class));
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }

        );
    }

    public void sendMessage(View view) {
        EditText textView = (EditText) findViewById(R.id.msg_editText);
        String msg = textView.getText().toString();

        String key = dbRef.child("posts").push().getKey();

        // Create a map.
        Map m1 = new HashMap();
        m1.put("message", msg);
//        Post post = new Post("0000", "Evan Lin", "My man.", "Test body.");
//        Map<String, Object> postValues = post.toMap();

//        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key, message);

//        database.updateChildren(childUpdates);

        dbRef.setValue(msg);
        textView.setText("");
    }
}
