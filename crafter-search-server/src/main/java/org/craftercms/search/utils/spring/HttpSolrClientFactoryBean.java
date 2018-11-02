/*
 * Copyright (C) 2007-2018 Crafter Software Corporation. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.craftercms.search.utils.spring;

import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.FactoryBean;

/**
 * Spring {@code FactoryBean} that uses a {@code HttpSolrClient.Builder} to create an instance of a
 * {@code SolrClient}. The properties of this bean correspond to the builder methods of the {@code Builder}.
 *
 * @author avasquez
 */
public class HttpSolrClientFactoryBean implements FactoryBean<HttpSolrClient> {

    private String baseSolrUrl;
    private HttpClient httpClient;
    private ResponseParser responseParser;
    private Boolean compression;
    private String kerberosDelegationToken;
    private ModifiableSolrParams invariantParams;

    public void setBaseSolrUrl(String baseSolrUrl) {
        this.baseSolrUrl = baseSolrUrl;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setResponseParser(ResponseParser responseParser) {
        this.responseParser = responseParser;
    }

    public void setCompression(Boolean compression) {
        this.compression = compression;
    }

    public void setKerberosDelegationToken(String kerberosDelegationToken) {
        this.kerberosDelegationToken = kerberosDelegationToken;
    }

    public void setInvariantParams(ModifiableSolrParams invariantParams) {
        this.invariantParams = invariantParams;
    }

    @Override
    public HttpSolrClient getObject() throws Exception {
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder();
        if (baseSolrUrl != null) {
            builder.withBaseSolrUrl(baseSolrUrl);
        }
        if (httpClient != null) {
            builder.withHttpClient(httpClient);
        }
        if (responseParser != null) {
            builder.withResponseParser(responseParser);
        }
        if (compression != null) {
            builder.allowCompression(compression);
        }
        if (kerberosDelegationToken != null) {
            builder.withKerberosDelegationToken(kerberosDelegationToken);
        }
        if (invariantParams != null) {
            builder.withInvariantParams(invariantParams);
        }

        return builder.build();
    }

    @Override
    public Class<?> getObjectType() {
        return HttpSolrClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
