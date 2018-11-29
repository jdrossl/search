/*
 * Copyright (C) 2007-2018 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.search.v3.service.impl;

import org.apache.commons.lang3.StringUtils;

import groovy.lang.Closure;
import org.craftercms.search.v3.service.QueryBuilder;

/**
 * Utility class to easily generate Solr queries as strings.
 * @author joseross
 */
public class SolrQueryBuilder extends AbstractQueryBuilder {

    public static final String OPEN_GROUP_CHAR = "(";
    public static final String CLOSE_GROUP_CHAR = ")";

    public static final String OPERATOR_NOT = "NOT";
    public static final String OPERATOR_AND = "AND";
    public static final String OPERATOR_OR = "OR";

    public SolrQueryBuilder() {
        super(OPEN_GROUP_CHAR, CLOSE_GROUP_CHAR, OPERATOR_NOT, OPERATOR_AND, OPERATOR_OR);
    }

    @Override
    public QueryBuilder type(String contentType) {
        addOperatorIfNeeded();
        append("content-type:%s", quote(contentType));
        return this;
    }

    @Override
    public SolrQueryBuilder id(String objectId) {
        addOperatorIfNeeded();
        append("objectId:%s", objectId);
        return this;
    }

    @Override
    public SolrQueryBuilder field(String name) {
        addOperatorIfNeeded();
        append("%s:", name);
        return this;
    }

    @Override
    public SolrQueryBuilder matches(Object value) {
        sb.append(value);
        return this;
    }

    @Override
    public SolrQueryBuilder hasPhrase(String text) {
        sb.append(quote(text));
        return this;
    }

    @Override
    public SolrQueryBuilder hasAny(Object... values) {
        sb.append("(").append(StringUtils.join(values, format(" %s ", orOperator))).append(")");
        return this;
    }

    @Override
    public SolrQueryBuilder hasAll(Object... values) {
        sb.append("(").append(StringUtils.join(values, format(" %s ", andOperator))).append(")");
        return this;
    }

    @Override
    public SolrQueryBuilder gt(Object value) {
        append("{%s TO *}", value);
        return this;
    }

    @Override
    public SolrQueryBuilder gte(Object value) {
        append("[%s TO *]", value);
        return this;
    }

    @Override
    public SolrQueryBuilder lt(Object value) {
        append("{* TO %s}", value);
        return this;
    }

    @Override
    public SolrQueryBuilder lte(Object value) {
        append("[* TO %s]", value);
        return this;
    }

    @Override
    public SolrQueryBuilder btw(Object start, Object end) {
        append("{%s TO %s}", start, end);
        return this;
    }

    @Override
    public SolrQueryBuilder btwe(Object start, Object end) {
        append("[%s TO %s]", start, end);
        return this;
    }

    @Override
    public void andBoosting(Number value) {
        append("^%s", value);
    }

    @Override
    public void andProximity(int value) {
        append("~%s", value);
    }

    @Override
    public void andSimilarity(int value) {
        andProximity(value);
    }

    @Override
    public String boost(Object value, Number boosting) {
        return format("%s^%s", value, boosting);
    }

    @Override
    public String quote(Object value) {
        return format("\"%s\"", value);
    }

    @Override
    public DateExpression date() {
        return new SolrDateExpression();
    }

    public class SolrDateExpression implements DateExpression {

        protected StringBuilder sb = new StringBuilder();

        @Override
        public DateExpression now() {
            sb.append("NOW");
            return this;
        }

        @Override
        public DateExpression plus(final Number number) {
            sb.append("+").append(number);
            return this;
        }

        @Override
        public DateExpression minus(final Number number) {
            sb.append("-").append(number);
            return this;
        }

        @Override
        public DateExpression roundingTo() {
            sb.append("/");
            return this;
        }

        @Override
        public DateExpression hours() {
            sb.append("HOURS");
            return this;
        }

        @Override
        public String done() {
            return sb.toString();
        }

    }

}