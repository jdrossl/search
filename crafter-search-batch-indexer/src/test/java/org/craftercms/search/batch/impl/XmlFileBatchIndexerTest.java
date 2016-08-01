package org.craftercms.search.batch.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.craftercms.search.batch.utils.xml.DefaultDocumentProcessorChain;
import org.craftercms.search.batch.utils.xml.DocumentProcessor;
import org.craftercms.search.service.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link XmlFileBatchIndexer}.
 *
 * @author avasquez
 */
public class XmlFileBatchIndexerTest {

    private static final String SITE_NAME = "test";
    private static final String UPDATE_FILENAME = "test.xml";
    private static final String DELETE_FILENAME = "deleteme.xml";
    private static final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                               "<page>" +
                                               "<fileName>test.xml</fileName>" +
                                               "<title_s tokenize=\"true\">Test</title_s>" +
                                               "<date>11/10/2015 00:00:00</date>" +
                                               "<title_t tokenize=\"true\">Test</title_t>" +
                                               "</page>";

    private SearchService searchService;
    private XmlFileBatchIndexer batchIndexer;

    @Before
    public void setUp() throws Exception {
        searchService = getSearchService();
        batchIndexer = getBatchIndexer(searchService);
    }

    @Test
    public void testUpdateIndex() throws Exception {
        List<String> updatedFiles = Collections.singletonList(getUpdateFilename());
        List<String> deletedFiles = Collections.singletonList(getDeleteFilename());

        int updated = batchIndexer.updateIndex(getIndexId(), getSiteName(), getRootFolder(), updatedFiles, false);

        assertEquals(1, updated);
        verify(searchService).update(getIndexId(), getSiteName(), getUpdateFilename(), getExpectedXml(), true);

        updated = batchIndexer.updateIndex(getIndexId(), getSiteName(), getRootFolder(), deletedFiles, true);

        assertEquals(1, updated);
        verify(searchService).delete(getIndexId(), getSiteName(), getDeleteFilename());
    }

    protected DocumentProcessor getDocumentProcessor() throws Exception {
        DefaultDocumentProcessorChain processor = new DefaultDocumentProcessorChain();
        processor.setFieldMappings(Collections.singletonMap("//name", "fileName"));

        return processor;
    }

    protected SearchService getSearchService() throws Exception {
        return mock(SearchService.class);
    }

    protected XmlFileBatchIndexer getBatchIndexer(SearchService searchService) throws Exception {
        XmlFileBatchIndexer batchIndexer = new XmlFileBatchIndexer();
        batchIndexer.setDocumentProcessor(getDocumentProcessor());
        batchIndexer.setSearchService(searchService);

        return batchIndexer;
    }

    protected String getIndexId() {
        return getSiteName() + "-default";
    }

    protected String getSiteName() {
        return SITE_NAME;
    }

    protected String getUpdateFilename() {
        return UPDATE_FILENAME;
    }

    protected String getDeleteFilename() {
        return DELETE_FILENAME;
    }

    protected String getExpectedXml() {
        return EXPECTED_XML;
    }

    protected String getRootFolder() throws IOException {
        return new ClassPathResource("/docs").getFile().getAbsolutePath();
    }

}