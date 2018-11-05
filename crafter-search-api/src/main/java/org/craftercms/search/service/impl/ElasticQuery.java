package org.craftercms.search.service.impl;

import java.util.Map;

import org.craftercms.search.service.Query;

public class ElasticQuery extends QueryParams {

    public ElasticQuery() {
        setParam("offset", "0");
        setParam("rows", "10");
        setParam("fields", "localId");
    }

    public ElasticQuery(final Map<String, String[]> params) {
        super(params);
    }

    @Override
    public Query setOffset(final int offset) {
        setParam("offset", Integer.toString(offset));
        return this;
    }

    @Override
    public int getOffset() {
        return Integer.parseInt(getParam("offset")[0]);
    }

    @Override
    public Query setNumResults(final int numResults) {
        setParam("rows", Integer.toString(numResults));
        return this;
    }

    @Override
    public int getNumResults() {
        return Integer.parseInt(getParam("rows")[0]);
    }

    @Override
    public Query setFieldsToReturn(final String... fieldsToReturn) {
        addParam("fields", fieldsToReturn);
        return this;
    }

    @Override
    public String[] getFieldsToReturn() {
        return getParam("fields");
    }

    @Override
    public Query setQuery(final String query) {
        setParam("query", query);
        return this;
    }

    @Override
    public String getQuery() {
        return getParam("query")[0];
    }

}
