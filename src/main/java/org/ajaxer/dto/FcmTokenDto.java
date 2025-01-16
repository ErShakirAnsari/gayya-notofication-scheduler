package org.ajaxer.dto;

import lombok.Data;

/**
 * @author Shakir Ansari
 * @since 2025-01-11
 */
@Data
public class FcmTokenDto
{
	private String username;
	private String token;

	@Override
	public String toString()
	{
		String escapedToken = token == null ? "null" : "**** ****";
		return "FcmTokenDto{" +
		       "username='" + username + '\'' +
		       ", token='" + escapedToken + '\'' +
		       '}';
	}
}
