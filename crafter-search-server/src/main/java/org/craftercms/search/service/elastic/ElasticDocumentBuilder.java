package org.craftercms.search.service.elastic;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ElasticDocumentBuilder {

    protected XmlMapper mapper;

    @PostConstruct
    public void init() {
        mapper = new XmlMapper();
        mapper.registerModule(new RepeatingGroupsModule());
    }

    @SuppressWarnings("unchecked")
    public Map build(final String site, final String id, final String xml, final boolean ignoreRootInFieldNames) throws IOException {

        String finalId = site + ":" + id;
        Map map = mapper.readValue(xml, MixedMap.class);

        String now = DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now(ZoneId.of("UTC")));

        map.put("id", finalId);
        map.put("crafterSite", site);
        map.put("rootId", finalId);
        map.put("localId", id);
        map.put("crafterPublishedDate", now);
        map.put("crafterPublishedDate_dt", now);

        return map;
    }

}
