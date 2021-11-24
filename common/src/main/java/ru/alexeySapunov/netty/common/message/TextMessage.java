package ru.alexeySapunov.netty.common.message;

public class TextMessage extends Message {

    private final String text;

    public TextMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
