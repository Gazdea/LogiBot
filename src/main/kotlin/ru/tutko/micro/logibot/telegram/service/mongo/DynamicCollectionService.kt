package ru.tutko.micro.logibot.telegram.service.mongo

import com.mongodb.client.result.UpdateResult
import org.bson.Document

interface DynamicCollectionService {
    fun saveDocument(collectionName: String, data: Map<String, Any>): Document
    fun updateDocument(collectionName: String, filter: Map<String, Any>, update: Map<String, Any>): UpdateResult
    fun getDocuments(collectionName: String, filter: Map<String, Any>): List<Document>
    fun getDocument(collectionName: String, query: Map<String, Any>): Document?
}