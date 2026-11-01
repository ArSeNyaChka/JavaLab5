package org.example.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SMS-сообщение, формируемое для подписчиков рассылки.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessage {

    private String phoneNumber;
    private String message;
}
