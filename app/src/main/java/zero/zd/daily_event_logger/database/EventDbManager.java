package zero.zd.daily_event_logger.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
}
