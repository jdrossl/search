package org.craftercms.search.service.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

public class ElasticClientFactory implements FactoryBean<RestHighLevelClient> {

    protected String host;
    protected int port;
    protected String protocol;

    @Required
    public void setHost(final String host) {
        this.host = host;
    }

    @Required
    public void setPort(final int port) {
        this.port = port;
    }

    @Required
    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    @Override
    public RestHighLevelClient getObject() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol)));
    }

    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
