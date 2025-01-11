package org.ajaxer.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ajaxer.dto.NotificationMessageDto;
import org.springframework.stereotype.Service;

/**
 * @author Shakir Ansari
 * @since 2025-01-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseNotificationService
{
	private final FirebaseMessaging firebaseMessaging;

	public void sendNotification(String fcmToken, String channelId, NotificationMessageDto messageDto)
	{
		try
		{
			var notification = Notification.builder().setTitle(messageDto.getTitle()).setBody(messageDto.getBody()).build();

			Message message = Message.builder()
			                         .setToken(fcmToken)
//			                         .setNotification(notification) // with this notification will hidden when app is in background
			                         .putData("channel_id", channelId)
			                         .putData("title", messageDto.getTitle())
			                         .putData("body", messageDto.getBody())
			                         .build();

			// Send the notification
			String response = firebaseMessaging.send(message);
			log.info("Notification sent successfully: {}", response);

		} catch (Exception e)
		{
			log.error("Failed to send notification:", e);
		}
	}
}
