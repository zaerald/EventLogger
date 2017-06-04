package zero.zd.daily_event_logger;

import java.util.Date;
import java.util.UUID;

public class Event {

    private UUID mId;
    private String mEvent;
    private Date mDate;

    public Event(String event, Date date) {
        this(event, date, UUID.randomUUID());
    }

    public Event(String event, Date date, UUID uuid) {
        mId = uuid;
        mEvent = event;
        mDate = date;
    }

    public UUID getId() {
        return mId;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(String event) {
        mEvent = event;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
