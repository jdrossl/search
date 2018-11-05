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
 */
package org.craftercms.search.service.impl.v2;

import java.util.Map;

import org.craftercms.search.service.SearchService;
import org.craftercms.search.service.impl.SolrQuery;

/**
 * Solr based REST client implementation of {@link SearchService} for Search REST API v2.
 *
 * @author Alfonso Vásquez
 */
public class SolrRestClientSearchService extends AbstractSearchRestClient<SolrQuery> {

    @Override
    public SolrQuery createQuery() {
        return new SolrQuery();
    }

    @Override
    public SolrQuery createQuery(Map<String, String[]> params) {
        return new SolrQuery(params);
    }

}
