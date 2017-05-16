package org.craftercms.search.utils;


import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.commons.lang3.StringUtils;

import groovy.lang.Closure;

/**
 * Utility class to easily generate Solr queries as strings.
 * @author joseross
 *
 */
public class SolrQueryBuilder {

    public static final String OPEN_GROUP_CHAR = "(";
    public static final String CLOSE_GROUP_CHAR = ")";

    public static final String OPERATOR_NOT = "NOT";
    public static final String OPERATOR_OR = "OR";
    public static final String OPERATOR_AND = "AND";

    protected StringBuilder sb = new StringBuilder();
    protected Deque<String> operators = new ArrayDeque<>();

    /**
     * Quickly create a new instance and get the resulting string.
     * @param rules
     * @return
     */
    public static String with(Closure<Void> rules) {
        SolrQueryBuilder builder = new SolrQueryBuilder();
        builder.addRules(rules);
        return builder.toString();
    }

    // Starting methods

    public SolrQueryBuilder type(String contentType) {
        addOperatorIfNeeded();
        append("content-type:%s", quote(contentType));
        return this;
    }

    public SolrQueryBuilder id(String objectId) {
        addOperatorIfNeeded();
        append("objectId:%s", objectId);
        return this;
    }

    public SolrQueryBuilder field(String name) {
        addOperatorIfNeeded();
        append("%s:", name);
        return this;
    }

    // Condition methods

    public SolrQueryBuilder matches(Object value) {
        sb.append(value);
        return this;
    }

    public SolrQueryBuilder hasPhrase(String text) {
        sb.append(quote(text));
        return this;
    }

    public SolrQueryBuilder hasAny(Object... values) {
        sb.append("(").append(StringUtils.join(values, f(" %s ", OPERATOR_OR))).append(")");
        return this;
    }

    public SolrQueryBuilder hasAll(Object... values) {
        sb.append("(").append(StringUtils.join(values, f(" %s ", OPERATOR_AND))).append(")");
        return this;
    }

    public SolrQueryBuilder gt(Object value) {
        append("{%s TO *}", value);
        return this;
    }

    public SolrQueryBuilder gte(Object value) {
        append("[%s TO *]", value);
        return this;
    }

    public SolrQueryBuilder lt(Object value) {
        append("{* TO %s}", value);
        return this;
    }

    public SolrQueryBuilder lte(Object value) {
        append("[* TO %s]", value);
        return this;
    }

    public SolrQueryBuilder btw(Object start, Object end) {
        append("{%s TO %s}", start, end);
        return this;
    }

    public SolrQueryBuilder btwe(Object start, Object end) {
        append("[%s TO %s]", start, end);
        return this;
    }

    // Operator methods

    public void or(Closure<Void> rules) {
        addOperatorIfNeeded();
        groupRules(rules, OPERATOR_OR);
    }

    public void and(Closure<Void> rules) {
        addOperatorIfNeeded();
        groupRules(rules, OPERATOR_AND);
    }

    public void not(Closure<Void> rules) {
        addOperatorIfNeeded();
        sb.append(f(" %s ", OPERATOR_NOT));
        groupRules(rules, OPERATOR_OR);
    }

    // Closing methods

    public void andBoosting(Number value) {
        append("^%s", value);
    }

    public void andProximity(int value) {
        append("~%s", value);
    }

    public void andSimilarity(int value) {
        andProximity(value);
    }

    // Utility methods

    public String boost(Object value, Number boosting) {
        return f("%s^%s", value, boosting);
    }

    public String quote(Object value) {
        return f("\"%s\"", value);
    }

    protected void addOperatorIfNeeded() {
        if(StringUtils.isNotEmpty(sb) && !StringUtils.endsWith(sb, OPEN_GROUP_CHAR)) {
            append(" %s ", operators.peekLast());
        }
    }

    protected void processRules(Closure<Void> rules) {
        rules.setDelegate(this);
        rules.run();
    }

    protected void groupRules(Closure<Void> rules, String operator) {
        operators.addLast(operator);
        sb.append(OPEN_GROUP_CHAR);
        processRules(rules);
        sb.append(CLOSE_GROUP_CHAR);
        operators.removeLast();
    }

    public void addRules(Closure<Void> rules) {
        operators.addLast(OPERATOR_AND);
        processRules(rules);
        operators.removeLast();
    }

    protected String f(String format, Object... args) {
        return String.format(format, args);
    }

    protected void append(String format, Object... args) {
        sb.append(f(format, args));
    }

    @Override
    public String toString() {
        return sb.toString();
    }

}
