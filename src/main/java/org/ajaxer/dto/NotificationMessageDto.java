package org.ajaxer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shakir Ansari
 * @since 2025-01-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDto
{
	String title;
	String body;
}
