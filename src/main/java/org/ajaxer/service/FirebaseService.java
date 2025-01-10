package org.ajaxer.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ajaxer.dto.NotificationStatusDto;
import org.springframework.stereotype.Service;

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
		CompletableFuture<DataSnapshot> future = fetchData("/notification_status");
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
}
