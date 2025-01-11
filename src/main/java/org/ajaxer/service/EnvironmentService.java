package org.ajaxer.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Shakir Ansari
 * @since 2025-01-11
 */
@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class EnvironmentService
{
	@Value("${GAYYA_FIREBASE_SERVICE_ACCOUNT_JSON_FILE}")
	private String firebaseServiceAccountJsonFile;

	@Value("${GAYYA_FIREBASE_DATABASE_URL}")
	private String firebaseDatabaseUrl; //https://<project-name>.firebaseio.com/

	@Value("${GAYYA_ENV_MODE}")
	private String envMode; // DEV/PROD

	@Value("${GAYYA_DAILY_REMINDER_CHANNEL_ID}")
	private String dailyReminderChannelId;

	@Value("${GAYYA_IST_TIME}")
	private String istTime; //"%Y-%m-%d %H:%M:%S" || yyyy-MM-dd HH:mm:ss

	public LocalDateTime getIstTimeDateTime()
	{
		log.info("istTime is {}", istTime);

		if (istTime == null)
			return LocalDateTime.now();


		// Define the formatter
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// Parse string to LocalDateTime
		return LocalDateTime.parse(istTime, formatter);
	}
}
