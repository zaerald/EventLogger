package zero.zd.daily_event_logger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import zero.zd.daily_event_logger.Event;
import zero.zd.daily_event_logger.database.EventDbSchema.EventTable;

public class EventDbManager {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private EventSQLiteOpenHelper mDbHelper;

    public EventDbManager(Context context) {
        mContext = context;
    }

    public EventDbManager open() throws SQLException {
        mDbHelper = new EventSQLiteOpenHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
        return this;
    }

    public void addEvent(Event event) {
        ContentValues values = getContentValues(event);
        mDatabase.insert(EventTable.NAME, null, values);
    }

    private ContentValues getContentValues(Event event) {
        ContentValues values = new ContentValues();
        values.put(EventTable.Cols.EVENT, event.getEvent());
        values.put(EventTable.Cols.DATE, event.getDate().getTime());

        return values;
    }
}
