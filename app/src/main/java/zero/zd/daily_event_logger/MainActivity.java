package zero.zd.daily_event_logger;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zero.zd.daily_event_logger.adapter.EventArrayAdapter;
import zero.zd.daily_event_logger.database.EventDbManager;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Event> mEventArrayAdapter;
    private List<Event> mEventList;
    private EventDbManager mEventDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initObjects();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initObjects() {
        mEventList = new ArrayList<>();
        mEventDbManager = new EventDbManager(this);

        mEventDbManager.open();
        mEventList = mEventDbManager.getEventList();

        ListView listView = (ListView) findViewById(R.id.list_event);
        mEventArrayAdapter = new EventArrayAdapter(this, R.layout.item_event, mEventList);
        listView.setAdapter(mEventArrayAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEventDialog();
            }
        });
    }

    private void showEventDialog() {
        ViewGroup dialogRootView = (ViewGroup) findViewById(R.id.root_dialog_create_event);
        final View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_event, dialogRootView);
        final EditText eventEditText = (EditText) dialogView.findViewById(R.id.edit_event);
        Button timeButton = (Button) dialogView.findViewById(R.id.button_time);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        final Event event = new Event("", new Date());
        timeButton.setText(event.getStringDate());

        AlertDialog eventDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_event)
                .setView(dialogView)
                .setCancelable(false)
                .setNegativeButton(R.string.action_discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String eventText = eventEditText.getText().toString();
                        if (eventText.isEmpty()) {
                            Toast.makeText(MainActivity.this, R.string.err_input_event,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            eventText = formatText(eventText);
                            event.setEvent(eventText);
                            addEvent(event);
                            dialog.dismiss();
                        }
                    }
                })
                .create();
        eventDialog.show();
    }

    private void showTimePickerDialog() {
        Toast.makeText(this, "Dialog Show", Toast.LENGTH_SHORT).show();
    }

    private void addEvent(Event event) {
        mEventList.add(event);
        mEventDbManager.addEvent(event);
        mEventArrayAdapter.notifyDataSetChanged();
    }

    private String formatText(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
