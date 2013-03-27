/**
 * Copyright 2012, Board of Regents of the University of
 * Wisconsin System. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Board of Regents of the University of Wisconsin
 * System licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.wisc.mum.status.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.wisc.mum.status.om.ServerInfoKey;

@Service
public class HttpServerResponseManager {
    private ConfigDao configDao;
    private ServerResponseDao serverResponseDao;

    @Autowired
    public void setConfigDao(ConfigDao configDao) {
        this.configDao = configDao;
    }
    
    @Autowired
    public void setServerResponseDao(ServerResponseDao serverResponseDao) {
        this.serverResponseDao = serverResponseDao;
    }

    @Scheduled(fixedDelay = 30000)
    public void checkAllServers() {
        final Map<String, ServerInfoKey> pollUrls = configDao.getPollUrls();
        for (final Map.Entry<String, ServerInfoKey> pollUrl : pollUrls.entrySet()) {
            serverResponseDao.checkServer(pollUrl.getValue(), pollUrl.getKey());
        }
    }

}
