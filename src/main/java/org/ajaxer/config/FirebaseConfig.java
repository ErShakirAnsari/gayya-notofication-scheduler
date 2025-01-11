package org.ajaxer.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.ajaxer.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Shakir Ansari
 * @since 2025-01-10
 */
@Configuration
@RequiredArgsConstructor
public class FirebaseConfig
{
	private final EnvironmentService environmentService;

	private void checkAllEnvironmentVars()
	{
		String firebaseServiceAccountJsonFile = environmentService.getFirebaseServiceAccountJsonFile();
		if (firebaseServiceAccountJsonFile == null)
			throw new IllegalStateException("firebaseServiceAccountJsonFile environment variable is not set");


		String firebaseDatabaseUrl = environmentService.getFirebaseDatabaseUrl();
		if (firebaseDatabaseUrl == null)
			throw new IllegalStateException("firebaseDatabaseUrl environment variable is not set");


		String envMode = environmentService.getEnvMode();
		if (envMode == null)
			throw new IllegalStateException("envMode [DEV/PROD] environment variable is not set");

	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public FirebaseApp firebaseApp() throws IOException
	{
		checkAllEnvironmentVars();

		String serviceAccountKey = environmentService.getFirebaseServiceAccountJsonFile();
		String firebaseDatabaseUrl = environmentService.getFirebaseDatabaseUrl();

		ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(serviceAccountKey.getBytes());

		FirebaseOptions options = FirebaseOptions.builder()
		                                         .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
		                                         .setDatabaseUrl(firebaseDatabaseUrl)
		                                         .build();

		return FirebaseApp.initializeApp(options);
	}

	@Bean
	public DatabaseReference firebaseDatabaseReference(@Autowired FirebaseApp firebaseApp)
	{
		return FirebaseDatabase.getInstance(firebaseApp).getReference();
	}

	@Bean
	public Firestore firestore(@Autowired FirebaseApp firebaseApp)
	{
		return FirestoreClient.getFirestore(firebaseApp);
	}

	@Bean
	public FirebaseMessaging firebaseMessaging(@Autowired FirebaseApp firebaseApp)
	{
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}
