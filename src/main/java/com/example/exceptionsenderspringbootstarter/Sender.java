package com.example.exceptionsenderspringbootstarter;

import java.util.List;

public interface Sender {
   void send(List<String> recipient, String exceptionClass);
}
