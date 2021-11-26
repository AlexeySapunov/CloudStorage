package ru.alexeySapunov.netty.common.message;

import java.util.Date;

public class DateMessage extends Message {

    private final Date date;

    public DateMessage(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
