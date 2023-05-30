package com.example.nottingbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @author Junhan LIU
 * @version 1.0
 *
 * Adapter class of Message
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private int resourceId;

    /**
     * MessageAdapter constructor
     *
     * @param context Context object
     * @param textViewResourceId the resource's id
     * @param objects a list of objects with content of Message
     */
    public MessageAdapter(Context context, int textViewResourceId, List<Message> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message Message = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (Message.getType() == Message.TYPE_RECEIVED) {
            // If it is a received message, display the message layout on the left and
            // hide the message layout on the right
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(Message.getContent());
        } else if (Message.getType() == Message.TYPE_SENT) {
            // If it is a sent message, display the message layout on the right and
            // hide the message layout on the left
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(Message.getContent());
        }
        return view;
    }

    /**
     * defines the View elements in xml
     */
    static class ViewHolder {
        TextView leftMsg;
        LinearLayout leftLayout;
        TextView rightMsg;
        LinearLayout rightLayout;

        ViewHolder(View view) {
            leftMsg = view.findViewById(R.id.left_msg);
            leftLayout = view.findViewById(R.id.left_layout);
            rightMsg = view.findViewById(R.id.right_msg);
            rightLayout = view.findViewById(R.id.right_layout);
        }
    }
}
