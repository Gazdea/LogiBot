package ru.tutko.micro.logibot.telegram.service.mongo

import com.mongodb.client.result.UpdateResult
import org.bson.Document

/**
 * Сервис для работы с MongoDB коллекциями.
 */
interface DynamicCollectionService {

    /**
     * Сохраняет новый документ в указанной коллекции.
     *
     * @param collectionName имя коллекции, в которую будет сохранён документ.
     * @param data данные, которые будут сохранены в документе в виде пары "ключ-значение".
     * @return сохранённый документ, представленный экземпляром [Document].
     */
    fun saveDocument(collectionName: String, data: Map<String, Any>): Document

    /**
     * Обновляет документ в указанной коллекции, удовлетворяющий фильтру, новыми значениями.
     *
     * @param collectionName имя коллекции, в которой будет выполнено обновление.
     * @param filter фильтр для поиска документов, которые будут обновлены. Пара "ключ-значение".
     * @param update новые значения, которые будут установлены в найденные документы. Пара "ключ-значение".
     * @return результат выполнения операции обновления в виде [UpdateResult], который содержит количество обновлённых документов.
     */
    fun updateDocument(collectionName: String, filter: Map<String, Any>, update: Map<String, Any>): UpdateResult

    /**
     * Получает список документов из указанной коллекции, которые соответствуют фильтру.
     *
     * @param collectionName имя коллекции, из которой будут извлечены документы.
     * @param filter фильтр для поиска документов. Пара "ключ-значение".
     * @return список документов [Document], удовлетворяющих фильтру.
     */
    fun getDocuments(collectionName: String, filter: Map<String, Any>): List<Document>

    /**
     * Получает один документ из коллекции, который соответствует заданному фильтру.
     *
     * @param collectionName имя коллекции, из которой будет извлечён документ.
     * @param query фильтр для поиска документа. Пара "ключ-значение".
     * @return документ [Document], который соответствует фильтру, или `null`, если такой документ не найден.
     */
    fun getDocument(collectionName: String, query: Map<String, Any>): Document?
}
