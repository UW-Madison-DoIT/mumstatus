package edu.wisc.mum.status.dao;

import java.util.Map;

import edu.wisc.mum.status.om.ServerInfoKey;
import edu.wisc.mum.status.om.ServerResponse;

public interface ServerResponseDao {

    void checkServer(ServerInfoKey key, String url);
    
    Map<String, ServerResponse> getResults();

}