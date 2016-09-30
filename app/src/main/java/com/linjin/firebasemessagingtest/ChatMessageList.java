package com.linjin.firebasemessagingtest;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by evan on 29/09/16.
 */

public class ChatMessageList extends ArrayAdapter<ChatMessage> {
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

        // Get the inflater and inflate the XML layout for each item.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_message_list_delegate, null);

        TextView time_label = (TextView) view.findViewById(R.id.time_label);
        time_label.setText(
                getStringFromEpoch(message.epoch, position == 0 ? 0 : messages.get(position - 1).epoch)
        );
        if (time_label.getText().toString().isEmpty()) {
            ViewGroup.LayoutParams params = time_label.getLayoutParams();
            params.height = 0;
            time_label.setLayoutParams(params);
        } else {
            time_label.setPadding(0, getPxFromDp(5), 0, 0);
        }

        TextView message_label = (TextView) view.findViewById(R.id.message_label);
        SpannableString msgText;

        if (message.uid.equals(MessagingActivity.name)) {
//            message_label.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            msgText = new SpannableString(String.valueOf(message.msg));
            view.findViewById(R.id.bubble).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            view.findViewById(R.id.icon).setVisibility(View.INVISIBLE);
            message_label.setBackgroundResource(R.drawable.colored_sqircle);
            message_label.setTextColor(Color.WHITE);
        }
        else {
            msgText = new SpannableString(String.valueOf(message.uid + "\n" + message.msg));
            msgText.setSpan(
                    new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, message.uid.length(), 0
            );
            view.findViewById(R.id.icon).setVisibility(View.GONE);
        }
        message_label.setText(msgText);
        return view;
    }

    private String getStringFromEpoch(long epoch, long epoch_old) {
        if (epoch - epoch_old > changeHeadingTime) {
            Date date = new Date(epoch);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm (dd/MM/yy)");
            return format.format(date);
        }
        return "";
    }

    private int getPxFromDp(int dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}