package com.example.exceptionsenderspringbootstarter;

import java.util.List;

public class EmailSender implements Sender {
    @Override
    public void send(List<String> recipient, String exceptionClass) {
        recipient.forEach(r -> System.out.printf("send message to %s exception class : %s \n", r, exceptionClass));

    }
}
