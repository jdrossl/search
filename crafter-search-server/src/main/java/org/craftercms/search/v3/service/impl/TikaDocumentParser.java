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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.craftercms.search.exception.SearchException;
import org.craftercms.search.v3.service.internal.DocumentParser;
import org.craftercms.search.v3.service.internal.impl.AbstractDocumentParser;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static org.apache.tika.metadata.TikaCoreProperties.CREATED;
import static org.apache.tika.metadata.TikaCoreProperties.CREATOR;
import static org.apache.tika.metadata.TikaCoreProperties.DESCRIPTION;
import static org.apache.tika.metadata.TikaCoreProperties.KEYWORDS;
import static org.apache.tika.metadata.TikaCoreProperties.MODIFIED;
import static org.apache.tika.metadata.TikaCoreProperties.TITLE;

/**
 * Implementation of {@link DocumentParser} that uses Apache Tika
 * @author joseross
 */
public class TikaDocumentParser extends AbstractDocumentParser {

    /**
     * The maximum number of characters to parse from the document
     */
    protected int charLimit;

    /**
     * Jackson {@link ObjectMapper} instance
     */
    protected ObjectMapper objectMapper = new XmlMapper();

    /**
     * Apache {@link Tika} instance
     */
    protected Tika tika = new Tika();

    @Required
    public void setCharLimit(final int charLimit) {
        this.charLimit = charLimit;
    }

    public void setObjectMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setTika(final Tika tika) {
        this.tika = tika;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String parseToXml(final InputStream content, final MultiValueMap<String, String> additionalFields) {
        Metadata metadata = new Metadata();
        try {
            return buildXmlDocument(tika.parseToString(content, metadata, charLimit), metadata, additionalFields);
        } catch (Exception e) {
            throw new SearchException("Error parsing file", e);
        }
    }

    /**
     * Prepares the document to be indexed
     * @param content the content of the parsed file
     * @param metadata the metadata of the parsed file
     * @param additionalFields additional fields to be added
     * @return the XML ready to be indexed
     */
    protected String buildXmlDocument(String content, Metadata metadata,
                                      MultiValueMap<String, String> additionalFields) {
        Map<String, Object> map = new HashMap<>();
        map.put(fieldNameContent, content);
        map.put(fieldNameAuthor, metadata.get(CREATOR));
        map.put(fieldNAmeTitle, metadata.get(TITLE));
        map.put(fieldNameKeywords, metadata.get(KEYWORDS));
        map.put(fieldNameDescription, metadata.get(DESCRIPTION));
        map.put(fieldNameContentType, Metadata.CONTENT_TYPE);
        map.put(fieldNameCreated, metadata.get(CREATED));
        map.put(fieldNameModified, metadata.get(MODIFIED));
        map.putAll(additionalFields);
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new SearchException("Error writing document as XML", e);
        }
    }

}
