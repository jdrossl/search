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

import java.util.ArrayDeque;
import java.util.Deque;

import groovy.lang.Closure;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.search.v3.service.QueryBuilder;

/**
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

    public AbstractQueryBuilder(final String openGroupChar, final String closeGroupChar, final String notOperator,
                                final String andOperator, final String orOperator) {
        this.openGroupChar = openGroupChar;
        this.closeGroupChar = closeGroupChar;
        this.notOperator = notOperator;
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }

    @Override
    public void or(Closure<Void> rules) {
        addOperatorIfNeeded();
        groupRules(rules, orOperator);
    }

    @Override
    public void and(Closure<Void> rules) {
        addOperatorIfNeeded();
        groupRules(rules, andOperator);
    }

    @Override
    public void not(Closure<Void> rules) {
        addOperatorIfNeeded();
        sb.append(format(" %s ", notOperator));
        groupRules(rules, orOperator);
    }

    protected void addOperatorIfNeeded() {
        if(StringUtils.isNotEmpty(sb) && !StringUtils.endsWith(sb, openGroupChar)) {
            append(" %s ", operators.peekLast());
        }
    }

    protected void groupRules(Closure<Void> rules, String operator) {
        operators.addLast(operator);
        sb.append(openGroupChar);
        processRules(rules);
        sb.append(closeGroupChar);
        operators.removeLast();
    }

    public void addRules(Closure<Void> rules) {
        operators.addLast(andOperator);
        processRules(rules);
        operators.removeLast();
    }

    protected String format(String format, Object... args) {
        return String.format(format, args);
    }

    protected void append(String format, Object... args) {
        sb.append(format(format, args));
    }

    @Override
    public String done() {
        return sb.toString();
    }

}
