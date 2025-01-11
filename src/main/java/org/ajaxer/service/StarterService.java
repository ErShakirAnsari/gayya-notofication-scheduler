package org.ajaxer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ajaxer.common.Constant;
import org.ajaxer.dto.FcmTokenDto;
import org.ajaxer.dto.NotificationMessageDto;
import org.ajaxer.dto.NotificationStatusDto;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Shakir Ansari
 * @since 2025-01-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StarterService
{
	private final FirestoreService firestoreService;
	private final CommonService commonService;
	private final EnvironmentService environmentService;
	private final FirebaseNotificationService firebaseNotificationService;

	public void sendDailyReminderNotification() throws Exception
	{
		List<NotificationStatusDto> notificationStatusDtoList = firestoreService.fetchNotificationStatus();
		log.info("notificationStatusDtoList: {}", notificationStatusDtoList);

		if (CollectionUtils.isEmpty(notificationStatusDtoList))
		{
			log.error("Notification status list is empty");
			return;
		}

		String dailyReminderChannelId = environmentService.getDailyReminderChannelId();
		log.info("dailyReminderChannelId: {}", dailyReminderChannelId);

		String notificationMessage = commonService.getPrefixedCollectionName(Constant.FIREBASE_COLLECTION_NOTIFICATION_MESSAGES);
		log.info("notificationMessageCollection: {}", notificationMessage);

		var messageDto = firestoreService.fetchData(notificationMessage, dailyReminderChannelId, NotificationMessageDto.class);
		log.info("messageDto: {}", messageDto);

		for (NotificationStatusDto dto : notificationStatusDtoList)
		{
			String fcmTokenCollection = commonService.getPrefixedCollectionName(Constant.FIREBASE_COLLECTION_FCM_TOKENS);
			log.info("fcmTokenCollection: {}", fcmTokenCollection);

			FcmTokenDto fcmTokenDto = firestoreService.fetchData(fcmTokenCollection, dto.getUsername(), FcmTokenDto.class);
			log.info("fcmTokenDto: {}", fcmTokenDto);

			if (!StringUtils.hasText(fcmTokenDto.getToken()))
				continue;

			LocalDateTime localDateTime = environmentService.getIstTimeDateTime();
			log.info("localDateTime: {}", localDateTime);

			if (localDateTime.getHour() == dto.getHour())
			{
				int savedMinutes = dto.getMinute() * 15;
				if (localDateTime.getMinute() >= savedMinutes)
					firebaseNotificationService.sendNotification(fcmTokenDto.getToken(), dailyReminderChannelId, messageDto);
			}
		}
	}
}
