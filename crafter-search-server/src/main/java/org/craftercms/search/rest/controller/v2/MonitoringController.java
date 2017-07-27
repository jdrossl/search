/*
 * Copyright (C) 2007-2013 Crafter Software Corporation.
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
package org.craftercms.search.rest.controller.v2;

import org.craftercms.commons.monitoring.MemoryMonitor;
import org.craftercms.commons.monitoring.StatusMonitor;
import org.craftercms.commons.monitoring.VersionMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.jar.Manifest;

@RestController
@RequestMapping(MonitoringController.URL_ROOT)
public class MonitoringController {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringController.class);

    public final static String URL_ROOT = "/api/2/monitoring";
    public final static String MEMORY_URL = "/memory";
    public final static String STATUS_URL = "/status";
    public final static String VERSION_URL = "/version";

    @GetMapping(MEMORY_URL)
    public ResponseEntity<List<MemoryMonitor>> getMemoryStats() {
        return new ResponseEntity<>(MemoryMonitor.getMemoryStats(), HttpStatus.OK);
    }


    @GetMapping(STATUS_URL)
    public ResponseEntity<StatusMonitor> getCurrentStatus() {
        return new ResponseEntity<>(StatusMonitor.getCurrentStatus(), HttpStatus.OK);
    }

    @GetMapping(VERSION_URL)
    public ResponseEntity<VersionMonitor> getCurrentVersion() throws Exception {
        try {
            return new ResponseEntity<>(VersionMonitor.getVersion(MonitoringController.class), HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Unable to read manifest file", e);
            throw new IOException("Unable to read manifest file", e);
        }
    }

}
