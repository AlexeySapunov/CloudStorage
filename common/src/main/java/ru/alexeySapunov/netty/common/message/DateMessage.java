package ru.alexeySapunov.netty.common.message;

import java.util.Date;

public class DateMessage extends Message {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}