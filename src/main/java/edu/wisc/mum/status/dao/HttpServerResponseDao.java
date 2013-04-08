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

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import edu.wisc.mum.status.om.ServerInfoKey;
import edu.wisc.mum.status.om.ServerResponse;

@Service
public class HttpServerResponseDao implements ServerResponseDao {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final Map<String, ServerResponse> results = new ConcurrentHashMap<String, ServerResponse>();
    private final Map<String, ServerResponse> readOnlyResults = Collections.unmodifiableMap(results);
    
    private RestOperations restOperations;
    
    @Autowired
    public void setRestOperations(RestOperations restOperations) {
        this.restOperations = restOperations;
    }
    
    @Override
    public Map<String, ServerResponse> getResults() {
        return readOnlyResults;
    }

    @Override
    @Async
    public void checkServer(ServerInfoKey key, String url) {
        final long start = System.nanoTime();
        final ServerResponse serverResponse = new ServerResponse();
        try {
            final ResponseEntity<String> result = restOperations.getForEntity(url, String.class);
            final long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            
            serverResponse.setDuration(duration);
            serverResponse.setStatusCode(result.getStatusCode().value());
            serverResponse.setStatusReasonPhrase(result.getStatusCode().getReasonPhrase());
            serverResponse.setTimestamp(new Date());
        }
        catch (Exception e) {
            final long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            
            serverResponse.setDuration(duration);
            serverResponse.setStatusCode(-1);
            serverResponse.setStatusReasonPhrase(e.getMessage());
            serverResponse.setTimestamp(new Date());
        }
           
        logger.debug("Checked server {} and got {}", key, serverResponse);
        
        results.put(key.toString(), serverResponse);
    }

}
