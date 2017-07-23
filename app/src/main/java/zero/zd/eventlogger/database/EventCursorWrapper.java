package zero.zd.eventlogger.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import zero.zd.eventlogger.Event;
import zero.zd.eventlogger.database.EventDbSchema.EventTable;

class EventCursorWrapper extends CursorWrapper {

    public EventCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Event getEvent() {
        UUID uuid = UUID.fromString(getString(getColumnIndex(EventTable.Cols.UUID)));
        String eventText = getString(getColumnIndex(EventTable.Cols.EVENT));
        long date = getLong(getColumnIndex(EventTable.Cols.DATE));

        return new Event(eventText, new Date(date), uuid);
    }
}
