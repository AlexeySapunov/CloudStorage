package ru.alexeySapunov.netty.common.message;

public class FileMessage extends Message {

    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
