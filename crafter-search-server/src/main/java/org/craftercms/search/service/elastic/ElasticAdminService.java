package org.craftercms.search.service.elastic;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.craftercms.search.exception.SearchException;
import org.craftercms.search.service.AdminService;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ElasticAdminService implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticAdminService.class);

    protected RestHighLevelClient client;

    @Required
    public void setClient(final RestHighLevelClient client) {
        this.client = client;
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
    public void createIndex(final String id) throws SearchException {
        CreateIndexRequest request = new CreateIndexRequest(id);
        try {
            CreateIndexResponse response = client.indices().create(request);
            logger.debug("Creation of index {} result: {}", id, response.isAcknowledged());
        } catch (IOException e) {
            throw new SearchException("Error creating index", e);
        }
    }

    @Override
    public Map<String, Object> getIndexInfo(final String id) throws SearchException {
        // TODO: Check what goes in here?
        return null;
    }

    @Override
    public void deleteIndex(final String id, final IndexDeleteMode mode) throws SearchException {
        DeleteIndexRequest request = new DeleteIndexRequest(id);
        try {
            DeleteIndexResponse response = client.indices().delete(request);
            logger.debug("Deletion of index {} result: {}", id, response.isAcknowledged());
        } catch (IOException e) {
            throw new SearchException("Error deleting index", e);
        }
    }

}
