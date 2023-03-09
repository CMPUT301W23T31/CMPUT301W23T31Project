package com.example.cmput301w23t31project;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * This class creates an instance of the database we need for the program
 */
public class QRDatabase {
    FirebaseFirestore QRdb;
    CollectionReference collection;

    /**
     * Constructor to create a database instance
     * @param collection
     *      The name of the collection we want to access
     */
    public QRDatabase(String collection) {
        QRdb = FirebaseFirestore.getInstance();
        this.collection = QRdb.collection(collection);
    }

    /**
     * Obtains the CollectionReference object of a database collection
     * @return
     *      CollectionReference object for database collection
     */
    public CollectionReference getReference() {
        return collection;
    }


}
