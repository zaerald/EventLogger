package zero.zd.daily_event_logger;

import java.util.Date;

public class Event {

    private String mEvent;
    private Date mDate;

    public Event(String event, Date date) {
        mEvent = event;
        mDate = date;
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
