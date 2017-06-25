package zero.zd.daily_event_logger.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import zero.zd.daily_event_logger.Event;
import zero.zd.daily_event_logger.R;

public class EventArrayAdapter extends ArrayAdapter<Event> {

    private Context mContext;
    private int mResource;
    private List<Event> mEventList;

    public EventArrayAdapter(Context context, List<Event> eventList) {
        super(context, R.layout.item_event, eventList);
        mContext = context;
        mResource = R.layout.item_event;
        mEventList = eventList;
    }

    @Override
    public int getCount() {
        return mEventList.size();
    }

    @Nullable
    @Override
    public Event getItem(int position) {
        return mEventList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.eventTextView = (TextView) convertView.findViewById(R.id.text_event);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.text_date);

            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        Event event = mEventList.get(position);

        viewHolder.eventTextView.setText(event.getEvent());
        viewHolder.dateTextView.setText(event.getStringDate());

        return convertView;
    }


    private static class ViewHolder {
        private TextView eventTextView;
        private TextView dateTextView;
    }
}
