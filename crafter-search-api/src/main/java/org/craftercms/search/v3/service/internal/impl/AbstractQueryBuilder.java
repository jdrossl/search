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

package org.craftercms.search.v3.service.internal.impl;

import java.util.ArrayDeque;
import java.util.Deque;

import groovy.lang.Closure;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.search.v3.service.internal.QueryBuilder;

/**
 * Base implementation for all {@link QueryBuilder}s
 * @author joseross
 */
public abstract class AbstractQueryBuilder implements QueryBuilder {

    protected StringBuilder sb = new StringBuilder();

    protected Deque<String> operators = new ArrayDeque<>();

    protected String openGroupChar;
    protected String closeGroupChar;

    protected String notOperator;
    protected String andOperator;
    protected String orOperator;
    protected String defaultOperator;

    public AbstractQueryBuilder(final String openGroupChar, final String closeGroupChar, final String notOperator,
                                final String andOperator, final String orOperator) {
        this.openGroupChar = openGroupChar;
        this.closeGroupChar = closeGroupChar;
        this.notOperator = notOperator;
        this.andOperator = andOperator;
        this.orOperator = orOperator;
        defaultOperator = andOperator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void or(Closure<Void> statements) {
        addOperatorIfNeeded();
        groupRules(statements, orOperator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void and(Closure<Void> statements) {
        addOperatorIfNeeded();
        groupRules(statements, andOperator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void not(Closure<Void> statements) {
        addOperatorIfNeeded();
        sb.append(format(" %s ", notOperator));
        groupRules(statements, orOperator);
    }

    protected void addOperatorIfNeeded() {
        if(StringUtils.isNotEmpty(sb) && !StringUtils.endsWith(sb, openGroupChar)) {
            append(" %s ", operators.peekLast());
        }
    }

    protected void processClosure(Closure<Void> closure) {
        closure.setDelegate(this);
        closure.run();
    }

    protected void groupRules(Closure<Void> rules, String operator) {
        operators.addLast(operator);
        sb.append(openGroupChar);
        processClosure(rules);
        sb.append(closeGroupChar);
        operators.removeLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryBuilder addStatements(Closure<Void> statements) {
        operators.addLast(defaultOperator);
        processClosure(statements);
        operators.removeLast();
        return this;
    }

    protected String format(String format, Object... args) {
        return String.format(format, args);
    }

    protected void append(String format, Object... args) {
        sb.append(format(format, args));
    }

    @Override
    public String toString() {
        return sb.toString();
    }

}
