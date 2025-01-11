package org.ajaxer.service;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FirestoreServiceTest
{

	@Mock
	private Firestore firestore;

	@Mock
	private DocumentReference documentReference;

	@Mock
	private DocumentSnapshot documentSnapshot;

	@Mock
	private CollectionReference collectionReference;

	@InjectMocks
	private FirestoreService firestoreService;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetDocument_Success() throws Exception
	{
		// Arrange
		String collectionName = "users";
		String documentId = "user1";
		User expectedUser = new User();
		expectedUser.setId("user1");
		expectedUser.setName("John Doe");
		expectedUser.setEmail("johndoe@example.com");

		// Mock Firestore interactions
		when(firestore.collection(collectionName)).thenReturn(collectionReference);
		when(collectionReference.document(documentId)).thenReturn(documentReference);
		ApiFuture<DocumentSnapshot> future = ApiFutures.immediateFuture(documentSnapshot);
		when(documentReference.get()).thenReturn(future);
		when(documentSnapshot.exists()).thenReturn(true);  // Simulating document found
		when(documentSnapshot.toObject(User.class)).thenReturn(expectedUser);

		// Act
		User result = firestoreService.fetchData(collectionName, documentId, User.class);

		// Assert
		assertNotNull(result);
		assertEquals(expectedUser.getId(), result.getId());
		assertEquals(expectedUser.getName(), result.getName());
		assertEquals(expectedUser.getEmail(), result.getEmail());

		verify(firestore).collection(collectionName);
		verify(firestore).document(documentId);
		verify(documentReference).get();
		verify(documentSnapshot).exists();
		verify(documentSnapshot).toObject(User.class);
	}

	@Test
	void testGetDocument_NotFound() throws Exception
	{
		// Arrange
		String collectionName = "users";
		String documentId = "non-existing-user";

		// Mock Firestore interactions
		when(firestore.collection(collectionName)).thenReturn(collectionReference);
		when(collectionReference.document(documentId)).thenReturn(documentReference);
		ApiFuture<DocumentSnapshot> future = ApiFutures.immediateFuture(documentSnapshot);
		when(documentReference.get()).thenReturn(future);
		when(documentSnapshot.exists()).thenReturn(true);  // Simulating document found


		// Act
		User result = firestoreService.fetchData(collectionName, documentId, User.class);

		// Assert
		assertNull(result);

		verify(firestore).collection(collectionName);
		verify(firestore).document(documentId);
		verify(documentReference).get();
		verify(documentSnapshot).exists();
	}

	@Test
	void testGetDocument_Exception() throws Exception
	{
		// Arrange
		String collectionName = "users";
		String documentId = "user1";

		// Mock Firestore interaction to throw an exception
		// Mock Firestore interactions
		when(firestore.collection(collectionName)).thenReturn(collectionReference);
		when(collectionReference.document(documentId)).thenReturn(documentReference);
		when(documentReference.get()).thenThrow(new RuntimeException("Firestore error"));

		// Act
		User result = firestoreService.fetchData(collectionName, documentId, User.class);

		// Assert
		assertNull(result);

		verify(firestore).collection(collectionName);
		verify(firestore).document(documentId);
		verify(documentReference).get();
	}
}