package org.craftercms.search.service.elastic;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.craftercms.search.exception.SearchException;
import org.craftercms.search.service.SearchService;
import org.craftercms.search.service.impl.ElasticQuery;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ElasticSearchService implements SearchService<ElasticQuery> {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    protected RestHighLevelClient client;
    protected XmlMapper mapper;

    @Required
    public void setClient(final RestHighLevelClient client) {
        this.client = client;
    }

    @PostConstruct
    public void init() {
        mapper = new XmlMapper();
    }

    @PreDestroy
    public void destroy() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            // do nothing...
        }
    }

    @Override
    public Map<String, Object> search(final String indexId, final ElasticQuery query) throws SearchException {
        // TODO: CHECK SEARCH API
        return null;
    }

    @Override
    public void update(final String indexId, final String site, final String id, final String xml, final boolean
        ignoreRootInFieldNames) throws SearchException {
        IndexRequest request = new IndexRequest(indexId, "doc", id);
        try {
            Map map = mapper.readValue(xml, Map.class);
            request.source(map);
            IndexResponse response = client.index(request);
            logger.debug("Update for {} result: {}", id, response.getResult());
        } catch (IOException e) {
            throw new SearchException("Error indexing document", e);
        }
    }

    @Override
    public void delete(final String indexId, final String site, final String id) throws SearchException {
        DeleteRequest request = new DeleteRequest(indexId, "doc", id);
        try {
            DeleteResponse response = client.delete(request);
            logger.debug("Delete for {} result: {}", id, response.getResult());
        } catch (IOException e) {
            throw new SearchException("Error deleting document", e);
        }
    }

    @Override
    public void updateContent(final String indexId, final String site, final String id, final File file, final
    Map<String, List<String>> additionalFields) throws SearchException {
        // TODO: NOT SUPPORTED?
    }

    @Override
    public void commit(final String indexId) throws SearchException {
        // TODO: CHECK IF THIS IS REALLY NEEDED?
        try {
            FlushRequest request = new FlushRequest(indexId);
            FlushResponse response = client.indices().flush(request);
            logger.debug("Flush for {} result: {}", indexId, response.getSuccessfulShards());
        } catch (IOException e) {
            throw new SearchException("Error flushing index", e);
        }
    }

    @Override
    public ElasticQuery createQuery() {
        return new ElasticQuery();
    }

    @Override
    public ElasticQuery createQuery(final Map<String, String[]> params) {
        return new ElasticQuery(params);
    }

}
