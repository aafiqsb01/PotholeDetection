package com.example.potholedetectionappv1;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Database {
    private FirebaseFirestore db;
    private CollectionReference usersReference;
    private CollectionReference PotholeReportsReference;

    public Database() {
        db = FirebaseFirestore.getInstance();
        usersReference = db.collection("Users"); // Replace with your collection name
        PotholeReportsReference = db.collection("PotholeReports"); // Replace with your collection name

    }

    public void users_addDocument(Object object, String documentId) {
        DocumentReference documentReference = usersReference.document(documentId);
        documentReference.set(object);
    }

    public void users_deleteDocument(String documentId) {
        DocumentReference documentReference = usersReference.document(documentId);
        documentReference.delete();
    }

    public void users_updateDocument(Object object, String documentId) {
        DocumentReference documentReference = usersReference.document(documentId);
        documentReference.set(object);
    }

    public void users_getDocuments() {
        usersReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    // Process documents here
                }  // Handle error here

            }
        });
    }

    public void potholeReports_addDocument(Object object, String documentId) {
        DocumentReference documentReference = PotholeReportsReference.document(documentId);
        documentReference.set(object);
    }

    public void potholeReports_deleteDocument(String documentId) {
        DocumentReference documentReference = PotholeReportsReference.document(documentId);
        documentReference.delete();
    }

    public void potholeReports_updateDocument(Object object, String documentId) {
        DocumentReference documentReference = PotholeReportsReference.document(documentId);
        documentReference.set(object);
    }

    public void potholeReports_getDocuments() {
        PotholeReportsReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    // Process documents here
                } else {
                    // Handle error here
                }
            }
        });
    }
}
