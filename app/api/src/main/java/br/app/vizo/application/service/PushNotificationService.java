package br.app.vizo.application.service;

import br.app.vizo.application.dto.PushNotificationDTO;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class PushNotificationService {

    public void send(PushNotificationDTO pushNotification) {
        Notification notification = Notification.builder()
                .setTitle(pushNotification.title())
                .setBody(pushNotification.body())
                .build();

        Message message = Message.builder()
                .setToken(pushNotification.token())
                .setNotification(notification)
                .build();

        String response;

        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            System.out.println("Push notification error: " + e.getMessage());
            return;
        }

        System.out.println("Push notification sent: " + response);
    }

    public void sendAll(List<PushNotificationDTO> pushNotifications) {

        List<Message> messages = new LinkedList<>();

        for (PushNotificationDTO pushNotification : pushNotifications) {
            Notification notification = Notification.builder()
                    .setTitle(pushNotification.title())
                    .setBody(pushNotification.body())
                    .build();

            Message message = Message.builder()
                    .setToken(pushNotification.token())
                    .setNotification(notification)
                    .build();

            messages.add(message);
        }

        BatchResponse response;

        try {
            response = FirebaseMessaging.getInstance().sendEach(messages);
        } catch (FirebaseMessagingException e) {
            System.out.println("Push notification error: " + e.getMessage());
            return;
        }

        System.out.println("Push notifications sent: " + response.getResponses());
    }
}
