package zero.zd.daily_event_logger.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zero.zd.daily_event_logger.database.EventDbSchema.EventTable;

class EventSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "event.db";

    public EventSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EventTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                EventTable.Cols.UUID + ", " +
                EventTable.Cols.EVENT + ", " +
                EventTable.Cols.DATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
