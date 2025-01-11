package org.ajaxer.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ajaxer.common.Constant;
import org.ajaxer.dto.FcmTokenDto;
import org.ajaxer.dto.NotificationStatusDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Shakir Ansari
 * @since 2025-01-10
 */
@SuppressWarnings("LoggingSimilarMessage")
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

	public void fetchNotificationTime()
	{
		log.info("Fetching notification time");
		CompletableFuture<DataSnapshot> future = fetchData("/test_notification_status");
		future.thenAccept(dataSnapshot -> {
			for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren())
			{
				NotificationStatusDto dto = dataSnapshotChild.getValue(NotificationStatusDto.class);
				log.info("Fetched notification status from firebase database: {}", dto);
				if (dto != null && dto.isEnable())
					log.info("sending notification to: {}", dto);
			}
		});
	}

	public <T> List<T> fetchAllDocumentsFromCollection(String collection, Class<T> type) throws Exception
	{
		CollectionReference collectionRef = firestore.collection(collection);

		ApiFuture<QuerySnapshot> querySnapshotApiFuture = collectionRef.get();

		QuerySnapshot queryDocumentSnapshots = querySnapshotApiFuture.get();

		List<QueryDocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

		List<T> result = new ArrayList<>();

		for (QueryDocumentSnapshot document : documents)
		{
			String id = document.getId();
			log.info("documentId: {}", id);

			T object = document.toObject(type);
			log.debug("object: {}", object);

			result.add(object);
		}

		return result;
	}
}
