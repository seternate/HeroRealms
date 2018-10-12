package com.seternate.herorealms.networking.messages;

public interface Message<T> {
    T getData();

    String getMessage();
}
