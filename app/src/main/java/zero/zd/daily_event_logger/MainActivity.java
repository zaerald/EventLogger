package zero.zd.daily_event_logger;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_about:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class EventArrayAdapater extends ArrayAdapter<Event> {

        private Context mContext;
        private int mResource;
        private List<Event> mEventList;

        public EventArrayAdapater(Context context, int resource, List<Event> eventList) {
            super(context, resource, eventList);
            mContext = context;
            mResource = resource;
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
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mResource, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.eventTextView = (TextView) findViewById(R.id.text_event);
                viewHolder.dateTextView = (TextView) findViewById(R.id.text_date);

                convertView.setTag(viewHolder);
            } else viewHolder = (ViewHolder) convertView.getTag();

            Event event = mEventList.get(position);

            viewHolder.eventTextView.setText(event.getEvent());
            viewHolder.dateTextView.setText(event.getDate().toString());

            return convertView;
        }
    }

    private class ViewHolder {
        private TextView eventTextView;
        private TextView dateTextView;
    }
}
