package ru.alexeySapunov.netty.common.message;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        property = "type"
)

public abstract class Message {
}
