package org.craftercms.search.service.impl;

import java.util.Map;

import org.craftercms.search.service.Query;

public class ElasticQuery extends QueryParams {

    public ElasticQuery() {
    }

    public ElasticQuery(final Map<String, String[]> params) {
        super(params);
    }

    @Override
    public Query setOffset(final int offset) {
        return null;
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public Query setNumResults(final int numResults) {
        return null;
    }

    @Override
    public int getNumResults() {
        return 0;
    }

    @Override
    public Query setFieldsToReturn(final String... fieldsToReturn) {
        return null;
    }

    @Override
    public String[] getFieldsToReturn() {
        return new String[0];
    }

    @Override
    public Query setQuery(final String query) {
        return null;
    }

    @Override
    public String getQuery() {
        return null;
    }

}
