package org.craftercms.search.service.elastic;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MixedMap implements Map {

    protected Map values = new HashMap<>();

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return values.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return values.containsValue(value);
    }

    @Override
    public Object get(final Object key) {
        return values.get(key);
    }

    @Override
    public Object put(final Object key, final Object value) {
        if(values.containsKey(key)) {
            Object v = values.get(key);
            if(v instanceof List) {
                List original = new LinkedList();
                original.addAll((List) v);
                ((List)v).add(value);
                return original;
            } else {
                List list = new LinkedList();
                list.add(v);
                list.add(value);
                return values.put(key, list);
            }
        } else {
            return values.put(key, value);
        }
    }

    @Override
    public Object remove(final Object key) {
        return values.remove(key);
    }

    @Override
    public void putAll(final Map m) {
        values.putAll(m);
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public Set keySet() {
        return values.keySet();
    }

    @Override
    public Collection values() {
        return values.values();
    }

    @Override
    public Set<Entry> entrySet() {
        return values.entrySet();
    }

}
