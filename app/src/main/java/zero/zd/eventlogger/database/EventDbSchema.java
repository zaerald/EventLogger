package zero.zd.eventlogger.database;

class EventDbSchema {

    public static final class EventTable {
        public static final String NAME = "tbl_event";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String EVENT = "event";
            public static final String DATE = "date";
        }
    }

}
