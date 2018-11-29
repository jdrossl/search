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

package org.craftercms.search.v3.service;

import groovy.lang.Closure;

/**
 * @author joseross
 */
public interface QueryBuilder {

    QueryBuilder type(String contentType);

    QueryBuilder id(String objectId);

    QueryBuilder field(String name);

    QueryBuilder matches(Object value);

    QueryBuilder hasPhrase(String text);

    QueryBuilder hasAny(Object... values);

    QueryBuilder hasAll(Object... values);

    QueryBuilder gt(Object value);

    QueryBuilder gte(Object value);

    QueryBuilder lt(Object value);

    QueryBuilder lte(Object value);

    QueryBuilder btw(Object start, Object end);

    QueryBuilder btwe(Object start, Object end);

    void or(Closure<Void> rules);

    void and(Closure<Void> rules);

    void not(Closure<Void> rules);

    void andBoosting(Number value);

    void andProximity(int value);

    void andSimilarity(int value);

    String boost(Object value, Number boosting);

    String quote(Object value);

    DateExpression date();

    String done();

    default void processRules(Closure<Void> rules) {
        rules.setDelegate(this);
        rules.run();
    }

    interface DateExpression {

        DateExpression now();

        DateExpression plus(Number number);

        DateExpression minus(Number number);

        DateExpression roundingTo();

        DateExpression hours();

        String done();

    }

}
