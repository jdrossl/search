package org.craftercms.search.service.elastic;

import java.util.Map;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class RepeatingGroupsModule extends SimpleModule {

    public RepeatingGroupsModule() {
        addAbstractTypeMapping(Map.class, MixedMap.class);
    }

}
