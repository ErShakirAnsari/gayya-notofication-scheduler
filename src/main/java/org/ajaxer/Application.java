package org.ajaxer;

import org.ajaxer.service.StarterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Shakir Ansari
 * @since 2025-01-10
 */
@SpringBootApplication
public class Application
{
	public static void main(String[] args) throws Exception
	{
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		context.getBean(StarterService.class).sendDailyReminderNotification();
	}
}
