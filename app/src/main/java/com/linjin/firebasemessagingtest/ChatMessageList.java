package com.linjin.firebasemessagingtest;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by evan on 29/09/16.
 */

public class ChatMessageList extends ArrayAdapter<ChatMessage> {
    private final static String TAG = "ChatMessageList";
    private final static int changeHeadingTime = 60000*5; // In (ms)
    private Activity context;
    private List<ChatMessage> messages;

    public ChatMessageList(Activity context, int resource, ArrayList<ChatMessage> objects) {
        super(context, resource, objects);
        this.context = context;
        this.messages = objects;
    }

    public View getView(int position, View contentView, ViewGroup parent) {

        // Get the message we are displaying.
        ChatMessage message = messages.get(position);
        ChatMessage prevmsg = messages.get(position == 0 ? 0 : position - 1);
        // Get the inflater and inflate the XML layout for each item.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view;

        // START [ Get correct message 'xml' file ]
        if (message.uid.equals(MessagingActivity.name)) {
            view = inflater.inflate(R.layout.msg_delegate_right, parent, false);
        } else {
            view = inflater.inflate(R.layout.msg_delegate_left, parent, false);
        }
        // END [ Get correct message 'xml' file ]

        TextView time_label = (TextView) view.findViewById(R.id.time_label);
        TextView time_label2 = (TextView) view.findViewById(R.id.time_label2);
        TextView message_label = (TextView) view.findViewById(R.id.message_label);
        SpannableString msgText;

        // START [ Set time label ]
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
        String dateStr = format.format(new Date(message.epoch));
        String prvdStr = format.format(new Date(prevmsg.epoch));
        if (!dateStr.equals(prvdStr) || position == 0) {
            time_label.setVisibility(View.VISIBLE);
            time_label.setText(dateStr);
        }
        format = new SimpleDateFormat("HH:mm");
        time_label2.setText(format.format(new Date(message.epoch)));
        // END [ Set time label ]

        if (message.uid.equals(MessagingActivity.name)) {
            msgText = new SpannableString(String.valueOf(message.msg));
        }
        else {
            msgText = new SpannableString(String.valueOf(message.uid + "\n" + message.msg));
            msgText.setSpan(
                    new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, message.uid.length(), 0
            );
//            view.findViewById(R.id.icon).setVisibility(View.GONE);
        }
        message_label.setText(msgText);
        return view;
    }
}