package edu.wisc.mum.status.dao;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import edu.wisc.mum.status.om.ServerInfoKey;
import edu.wisc.mum.status.om.ServerResponse;

@Service
public class HttpServerResponseDao implements ServerResponseDao {
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
        final ResponseEntity<String> result = restOperations.getForEntity(url, String.class);
        final long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        
        final ServerResponse serverResponse = new ServerResponse();
        serverResponse.setDuration(duration);
        serverResponse.setStatusCode(result.getStatusCode().value());
        serverResponse.setStatusReasonPhrase(result.getStatusCode().getReasonPhrase());
        serverResponse.setTimestamp(new Date());
        
        results.put(key.toString(), serverResponse);
    }

}
