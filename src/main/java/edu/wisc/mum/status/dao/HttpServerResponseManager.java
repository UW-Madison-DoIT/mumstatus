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

    @Scheduled(fixedDelay = 30)
    public void checkAllServers() {
        final Map<String, ServerInfoKey> pollUrls = configDao.getPollUrls();
        for (final Map.Entry<String, ServerInfoKey> pollUrl : pollUrls.entrySet()) {
            serverResponseDao.checkServer(pollUrl.getValue(), pollUrl.getKey());
        }
    }

}
