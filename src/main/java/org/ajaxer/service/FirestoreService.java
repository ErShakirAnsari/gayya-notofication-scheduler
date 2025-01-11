package org.ajaxer.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ajaxer.common.Constant;
import org.ajaxer.dto.FcmTokenDto;
import org.ajaxer.dto.NotificationStatusDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shakir Ansari
 * @since 2025-01-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FirestoreService
{
	private final Firestore firestore;
	private final CommonService commonService;

	/**
	 * Generic method to fetch data from Firestore.
	 *
	 * @param collectionName the name of the Firestore collection
	 * @param documentId the ID of the Firestore document
	 * @param clazz the class type to map the Firestore document to
	 * @param <T> the type of the return object
	 * @return the fetched object, or null if not found
	 */
	public <T> T fetchData(String collectionName, String documentId, Class<T> clazz) throws Exception
	{
		DocumentReference docRef = firestore.collection(collectionName).document(documentId);

		ApiFuture<DocumentSnapshot> future = docRef.get();
		DocumentSnapshot document = future.get();

		if (document.exists())
			return document.toObject(clazz);

		log.warn("No such document: {}, in collection:{}", documentId, collectionName);
		return null;
	}

	// Fetch all documents in a given collection and convert them to a list of objects
	public <T> List<T> fetchData(String collectionName, Class<T> clazz) throws Exception
	{
		CollectionReference collectionReference = firestore.collection(collectionName);
		ApiFuture<QuerySnapshot> future = collectionReference.get();  // Fetch all documents in the collection

		// Get the snapshot of all documents
		QuerySnapshot querySnapshot = future.get();

		// Map each DocumentSnapshot to the desired object type
		return querySnapshot.getDocuments().stream()
		                    .map(documentSnapshot -> documentSnapshot.toObject(clazz)) // Convert to object of type T
		                    .collect(Collectors.toList());
	}

	public void fetchFcmTokens() throws Exception
	{
		String collectionName = commonService.getCollectionPrefix() + Constant.FIREBASE_COLLECTION_FCM_TOKENS;
		log.info("collectionName: {}", collectionName);

		var fcmTokenDtoList = fetchData(collectionName, FcmTokenDto.class);
		fcmTokenDtoList.forEach(fcmTokenDto -> log.info("fcmTokenDto: {}", fcmTokenDto));
	}

	public List<NotificationStatusDto> fetchNotificationStatus() throws Exception
	{
		String collectionName = commonService.getCollectionPrefix() + Constant.FIREBASE_COLLECTION_NOTIFICATION_STATUS;
		log.info("collectionName: {}", collectionName);

		var notificationStatusDtoList = fetchData(collectionName, NotificationStatusDto.class);

		return notificationStatusDtoList.stream().filter(NotificationStatusDto::isEnable).collect(Collectors.toList());
	}

}
