package ru.alexeySapunov.netty.common.message;

public class EndFileDownloadMessage extends Message {
    public EndFileDownloadMessage() {
        System.out.println("End of file download");
    }
}
