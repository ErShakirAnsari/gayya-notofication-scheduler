package org.ajaxer.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ajaxer.dto.NotificationStatusDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Shakir Ansari
 * @since 2025-01-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseService
{
	private final DatabaseReference databaseReference;
	private final Firestore firestore;

	public CompletableFuture<DataSnapshot> fetchData(final String path)
	{
		CompletableFuture<DataSnapshot> future = new CompletableFuture<>();
		databaseReference.child(path).addListenerForSingleValueEvent(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				future.complete(dataSnapshot);
			}

			@Override
			public void onCancelled(DatabaseError databaseError)
			{
				future.completeExceptionally(new RuntimeException(databaseError.getMessage()));
			}
		});
		return future;
	}

	@PostConstruct
	public void fetchNotificationTime()
	{
		log.info("Fetching notification time");
		CompletableFuture<DataSnapshot> future = fetchData("/test_notification_status");
		future.thenAccept(dataSnapshot -> {
			for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren())
			{
				NotificationStatusDto dto = dataSnapshotChild.getValue(NotificationStatusDto.class);
				log.info("Fetched notification status from firebase database: {}", dto);
				if (dto != null && dto.isStatus())
					log.info("sending notification to: {}", dto);
			}
		});
	}

	@PostConstruct
	public List<Map<String, Object>> fetchAllDocumentsFromCollection() throws Exception
	{
		CollectionReference collectionRef = firestore.collection("test_fcm_tokens");
		ApiFuture<QuerySnapshot> querySnapshotApiFuture = collectionRef.get();

		QuerySnapshot queryDocumentSnapshots = querySnapshotApiFuture.get();

		List<QueryDocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

		List<Map<String, Object>> result = new ArrayList<>();
		// Iterate over the documents
		for (QueryDocumentSnapshot document : documents)
		{
			String id = document.getId();
			log.info("documentId: {}", id);
			Map<String, Object> data = document.getData();
			data.forEach((key, value) -> log.info("key: {}, value: {}", key, value));

			result.add(data);  // Get document data (key-value pairs)
		}

		return result;
	}
}
