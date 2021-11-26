package ru.alexeySapunov.netty.common.message;

public class FileMessage extends Message {

    private byte[] content;
    private long startPosition;

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
