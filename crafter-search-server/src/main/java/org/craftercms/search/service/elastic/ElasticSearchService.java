package org.craftercms.search.service.elastic;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.craftercms.search.exception.SearchException;
import org.craftercms.search.service.SearchService;
import org.craftercms.search.service.impl.ElasticQuery;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ElasticSearchService implements SearchService<ElasticQuery> {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    //TODO: Extract values
    protected String defaltIndex = "default";
    protected String defaultType = "descriptor";

    protected RestHighLevelClient client;
    protected ElasticDocumentBuilder documentBuilder;

    @Required
    public void setClient(final RestHighLevelClient client) {
        this.client = client;
    }

    @Required
    public void setDocumentBuilder(final ElasticDocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
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
        SearchSourceBuilder source = new SearchSourceBuilder();
        source.query(QueryBuilders.queryStringQuery(query.getQuery()));
        source.from(query.getOffset());
        source.size(query.getNumResults());
        source.fetchSource(query.getFieldsToReturn(), new String[]{});

        SearchRequest request = new SearchRequest(indexId);
        request.source(source);

        try {
            SearchResponse response = client.search(request);
            return toMap(response);
        } catch (Exception e) {
            throw new SearchException("Error searching", e);
        }
    }

    public Map<String, Object> toMap(SearchResponse response) {
        Map<String, Object> result = new HashMap<>();

        result.put("aggregations", response.getAggregations());
        result.put("hits", response.getHits());
        result.put("suggest", response.getSuggest());

        return result;
    }

    @Override
    public void update(final String indexId, final String site, final String id, final String xml, final boolean
        ignoreRootInFieldNames) throws SearchException {
        String finalId = site + ":" + id;
        IndexRequest request = new IndexRequest(StringUtils.isEmpty(indexId)? defaltIndex : indexId, defaultType,
                                                finalId);
        try {
            Map map = documentBuilder.build(site, finalId, xml, ignoreRootInFieldNames);
            request.source(map);
            IndexResponse response = client.index(request);
            logger.debug("Update for {} result: {}", id, response.getResult());
        } catch (IOException e) {
            throw new SearchException("Error indexing document", e);
        }
    }

    @Override
    public void delete(final String indexId, final String site, final String id) throws SearchException {
        String finalId = site + ":" + id;
        DeleteRequest request = new DeleteRequest(StringUtils.isEmpty(indexId)? defaltIndex : indexId, defaultType,
                                                    finalId);
        try {
            DeleteResponse response = client.delete(request);
            logger.debug("Delete for {} result: {}", finalId, response.getResult());
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
            FlushRequest request = new FlushRequest(StringUtils.isEmpty(indexId)? defaltIndex : indexId);
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
