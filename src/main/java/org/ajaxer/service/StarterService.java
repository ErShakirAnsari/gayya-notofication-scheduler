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
		//log.info(environmentService.toString());
		List<NotificationStatusDto> notificationStatusDtoList = firestoreService.fetchNotificationStatus();
		log.debug("notificationStatusDtoList: {}", notificationStatusDtoList);

		if (CollectionUtils.isEmpty(notificationStatusDtoList))
		{
			log.error("Notification status list is empty");
			return;
		}

		String dailyReminderChannelId = environmentService.getDailyReminderChannelId();
		log.debug("dailyReminderChannelId: {}", dailyReminderChannelId);

		String notificationMessage = commonService.getPrefixedCollectionName(Constant.FIREBASE_COLLECTION_NOTIFICATION_MESSAGES);
		log.debug("notificationMessageCollection: {}", notificationMessage);

		var messageDto = firestoreService.fetchData(notificationMessage, dailyReminderChannelId, NotificationMessageDto.class);
		log.debug("messageDto: {}", messageDto);

		for (NotificationStatusDto dto : notificationStatusDtoList)
		{
			String fcmTokenCollection = commonService.getPrefixedCollectionName(Constant.FIREBASE_COLLECTION_FCM_TOKENS);
			log.debug("fcmTokenCollection: {}", fcmTokenCollection);

			FcmTokenDto fcmTokenDto = firestoreService.fetchData(fcmTokenCollection, dto.getUsername(), FcmTokenDto.class);
			log.debug("fcmTokenDto: {}", fcmTokenDto);

			if (!StringUtils.hasText(fcmTokenDto.getToken()))
				continue;

			LocalDateTime localDateTime = environmentService.getIstTimeDateTime();
			log.info("localDateTime: {}", localDateTime);

			if (localDateTime.getHour() == dto.getHour())
			{
				int savedMinutes = dto.getMinute() * 15;
				if (isMinuteMatched(localDateTime.getMinute(), savedMinutes))
					firebaseNotificationService.sendNotification(fcmTokenDto.getToken(), dailyReminderChannelId, messageDto);
			}
		}
	}

	public static boolean isMinuteMatched(int currentMinute, int savedMinute)
	{
		if (savedMinute == 0 && currentMinute < 15)
			return true;

		if (savedMinute == 15)
			if (currentMinute >= 15 && currentMinute < 30)
				return true;

		if (savedMinute == 30)
			if (currentMinute >= 30 && currentMinute < 45)
				return true;

		return savedMinute == 45 && currentMinute >= 45;
	}
}
