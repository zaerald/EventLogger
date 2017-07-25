package zero.zd.eventlogger;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import zero.zd.eventlogger.adapter.EventArrayAdapter;
import zero.zd.eventlogger.database.EventDbManager;

public class MainActivity extends AppCompatActivity {

    private static final String FRAG_TAG_TIME_PICKER = "FRAG_TAG_TIME_PICKER";
    private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";
    private static final int STATE_ADD_EVENT = 0;
    private static final int STATE_MODIFY_EVENT = 1;

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
        updateListView();
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
                showInfoDialog();
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
        mEventArrayAdapter = new EventArrayAdapter(this, mEventList);
        listView.setAdapter(mEventArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEventDialog(mEventList.get(position));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEventDialog();
            }
        });
    }

    private void updateListView() {
        sortEvents();
        mEventArrayAdapter.notifyDataSetChanged();
    }

    private void sortEvents() {
        Collections.sort(mEventList, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }

    private void showInfoDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_info)
                .setMessage(R.string.msg_info)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showEventDialog() {
        final Event event = new Event();
        showEventDialog(event, STATE_ADD_EVENT);
    }

    private void showEventDialog(final Event event) {
        showEventDialog(event, STATE_MODIFY_EVENT);
    }

    private void showEventDialog(final Event event, final int state) {
        ViewGroup dialogRootView = (ViewGroup) findViewById(R.id.root_dialog_create_event);
        final View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_event, dialogRootView);
        final EditText eventEditText = dialogView.findViewById(R.id.edit_event);
        eventEditText.setText(event.getEvent());

        Button dateButton = dialogView.findViewById(R.id.button_date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view, event);
            }
        });
        dateButton.setText(event.getStringDate());

        Button timeButton = dialogView.findViewById(R.id.button_time);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view, event);
            }
        });
        timeButton.setText(event.getStringTime());

        int dialogTitle = 0;
        int positiveButtonText = 0;

        switch (state) {
            case STATE_ADD_EVENT:
                dialogTitle = R.string.title_event_dialog;
                positiveButtonText = R.string.action_save;
                break;

            case STATE_MODIFY_EVENT:
                dialogTitle = R.string.title_event_modify;
                positiveButtonText = R.string.action_update;
                break;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setView(dialogView)
                .setCancelable(false)
                .setNeutralButton(R.string.action_discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String eventText = eventEditText.getText().toString();
                        if (eventText.isEmpty()) {
                            showSnackBar(R.string.err_input_event);
                            return;
                        }

                        eventText = formatText(eventText);
                        event.setEvent(eventText);

                        switch (state) {
                            case STATE_ADD_EVENT:
                                addEvent(event);
                                break;

                            case STATE_MODIFY_EVENT:
                                updateEvent(event);
                                showSnackBar(R.string.msg_event_updated);
                                break;
                        }
                        dialog.dismiss();
                    }
                });

        if (state == STATE_MODIFY_EVENT) {
            dialogBuilder.setNegativeButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showConfirmDeleteDialog(event);
                }
            });
        }

        AlertDialog eventDialog = dialogBuilder.create();
        eventDialog.show();
    }

    private void showDatePickerDialog(View view, final Event event) {
        final Button dateButton = (Button) view;
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        if (isDateValid(year, monthOfYear, dayOfMonth)) {
                            event.setDate(getDateFromDatePicker(year, monthOfYear, dayOfMonth));
                            dateButton.setText(event.getStringDate());
                        } else {
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.msg_error_time_date_input),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDoneText("Save")
                .setCancelText("Discard");
        cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    private boolean isDateValid(int year, int monthOfYear, int dayOfMonth) {
        Calendar pickedCalendar = Calendar.getInstance();
        pickedCalendar.set(year, monthOfYear, dayOfMonth);

        return pickedCalendar.compareTo(Calendar.getInstance()) <= 0;
    }

    private void showTimePickerDialog(View view, final Event event) {
        final Button timeButton = (Button) view;
        RadialTimePickerDialogFragment timePickerDialog = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment dialog,
                                          int hourOfDay, int minute) {
                        if (isTimeValid(event, hourOfDay, minute) ) {
                            event.setDate(getDateFromTimePicker(hourOfDay, minute));
                            timeButton.setText(event.getStringTime());
                        } else {
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.msg_error_time_date_input),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setStartTime(getCurrentEventHour(event), getCurrentEventMinute(event))
                .setDoneText("Save")
                .setCancelText("Discard");
        timePickerDialog.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
    }

    private boolean isTimeValid(Event event, int hourOfDay, int minute) {
        Calendar pickedCalendar = Calendar.getInstance();
        pickedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        pickedCalendar.set(Calendar.MINUTE, minute);

        return pickedCalendar.compareTo(Calendar.getInstance()) <= 0 ||
                isDateBefore(event);
    }

    private boolean isDateBefore(Event event) {
        Calendar pickedCalendar = Calendar.getInstance();
        pickedCalendar.setTime(event.getDate());

        return pickedCalendar.compareTo(Calendar.getInstance()) < 0;
    }

    private void showConfirmDeleteDialog(final Event event) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_delete_dialog)
                .setMessage(R.string.msg_delete)
                .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEvent(event);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.action_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showSnackBar(int msg) {
        View rootView = findViewById(R.id.root_main);
        Snackbar.make(rootView, msg,
                Snackbar.LENGTH_SHORT).show();
    }

    private Date getDateFromDatePicker(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return calendar.getTime();
    }

    private Date getDateFromTimePicker(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    private int getCurrentEventHour(Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getDate());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int getCurrentEventMinute(Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getDate());
        return calendar.get(Calendar.MINUTE);
    }

    private void addEvent(Event event) {
        mEventList.add(event);
        mEventDbManager.addEvent(event);
        updateListView();
    }

    private void updateEvent(Event event) {
        mEventDbManager.updateEvent(event);
        updateListView();
    }

    private void deleteEvent(Event event) {
        mEventList.remove(event);
        mEventDbManager.deleteEvent(event);
        updateListView();
    }

    private String formatText(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
