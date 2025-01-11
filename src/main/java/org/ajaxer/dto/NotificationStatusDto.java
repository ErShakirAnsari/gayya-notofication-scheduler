package org.ajaxer.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Shakir Ansari
 * @since 2025-01-10
 */
@Getter
@Setter
@ToString
public class NotificationStatusDto
{
	private int hour;
	private int minute;
	private boolean enable;
	private String username;
}
