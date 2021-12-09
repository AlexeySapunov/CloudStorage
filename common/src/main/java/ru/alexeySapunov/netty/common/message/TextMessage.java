package ru.alexeySapunov.netty.common.message;

public class TextMessage extends Message {

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
