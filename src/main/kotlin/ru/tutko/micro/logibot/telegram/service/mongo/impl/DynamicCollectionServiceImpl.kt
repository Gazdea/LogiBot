package ru.tutko.micro.logibot.telegram.service.mongo.impl

import com.mongodb.client.result.UpdateResult
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import ru.tutko.micro.logibot.telegram.service.mongo.DynamicCollectionService
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

@Service
class DynamicCollectionServiceImpl(private val mongoTemplate: MongoTemplate): DynamicCollectionService {

    override fun saveDocument(collectionName: String, data: Map<String, Any>): Document {
        val document = Document(data)
        return mongoTemplate.save(document, collectionName)
    }

    override fun updateDocument(collectionName: String, filter: Map<String, Any>, update: Map<String, Any>): UpdateResult {
        val query = Query(buildCriteria(filter))
        val updateObj = Update().apply {
            update.forEach { (key, value) -> set(key, value) }
        }
        return mongoTemplate.updateFirst(query, updateObj, collectionName)
    }

    override fun getDocuments(collectionName: String, filter: Map<String, Any>): List<Document> {
        val query = Query(buildCriteria(filter))
        return mongoTemplate.find(query, Document::class.java, collectionName)
    }

    override fun getDocument(collectionName: String, query: Map<String, Any>): Document? {
        val criteriaQuery = Query(buildCriteria(query))
        return mongoTemplate.findOne(criteriaQuery, Document::class.java, collectionName)
    }

    private fun buildCriteria(filter: Map<String, Any>): Criteria {
        val criteria = filter.entries.map { Criteria.where(it.key).`is`(it.value) }
        return if (criteria.isNotEmpty()) Criteria().andOperator(*criteria.toTypedArray()) else Criteria()
    }

}
