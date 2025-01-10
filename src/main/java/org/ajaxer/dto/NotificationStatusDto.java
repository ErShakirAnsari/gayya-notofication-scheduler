package org.ajaxer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shakir Ansari
 * @since 2025-01-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStatusDto
{
	private int hour;
	private int minute;
	private boolean status;
	private String user;


	private String fcmToken;
}
