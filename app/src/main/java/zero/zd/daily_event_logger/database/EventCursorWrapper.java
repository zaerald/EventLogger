package zero.zd.daily_event_logger.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

import zero.zd.daily_event_logger.Event;
import zero.zd.daily_event_logger.database.EventDbSchema.EventTable;

public class EventCursorWrapper extends CursorWrapper {

    public EventCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Event getEvent() {
        String eventText = getString(getColumnIndex(EventTable.Cols.EVENT));
        long date = getLong(getColumnIndex(EventTable.Cols.DATE));

        return new Event(eventText, new Date(date));
    }
}
