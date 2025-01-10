package org.ajaxer.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
public class FirebaseConfig
{
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
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

	@Bean
	public Firestore firestore()
	{
		return FirestoreClient.getFirestore();
	}
}
