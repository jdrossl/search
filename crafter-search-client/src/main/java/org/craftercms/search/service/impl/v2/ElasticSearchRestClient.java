package org.craftercms.search.service.impl.v2;

import java.util.Map;

import org.craftercms.search.service.impl.ElasticQuery;

public class ElasticSearchRestClient extends AbstractSearchRestClient<ElasticQuery> {

    @Override
    public ElasticQuery createQuery() {
        return new ElasticQuery();
    }

    @Override
    public ElasticQuery createQuery(final Map<String, String[]> params) {
        return new ElasticQuery(params);
    }

}
