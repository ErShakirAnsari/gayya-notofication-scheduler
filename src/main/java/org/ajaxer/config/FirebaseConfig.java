package org.ajaxer.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Shakir Ansari
 * @since 2025-01-10
 */
@Configuration
public class FirebaseConfig
{
	@Bean
	public FirebaseApp firebaseApp() throws IOException
	{
		String serviceAccountKey = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON_FILE");
		if (serviceAccountKey == null)
			throw new IllegalStateException("FIREBASE_SERVICE_ACCOUNT_KEY environment variable is not set");

		ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(serviceAccountKey.getBytes());

		FirebaseOptions options = FirebaseOptions.builder()
		                                         .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
		                                         .setDatabaseUrl("https://gayya0000.firebaseio.com/")
		                                         .build();

		return FirebaseApp.initializeApp(options);
	}

	@Bean
	public DatabaseReference firebaseDatabaseReference(FirebaseApp firebaseApp)
	{
		return FirebaseDatabase.getInstance(firebaseApp).getReference();
	}
}
