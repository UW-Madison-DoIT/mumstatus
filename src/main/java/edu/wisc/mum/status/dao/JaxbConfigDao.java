/**
 * Copyright (c) 2000-2009, Jasig, Inc.
 * See license distributed with this file and available online at
 * https://www.ja-sig.org/svn/jasig-parent/tags/rel-10/license-header.txt
 */

package edu.wisc.mum.status.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import edu.wisc.mum.status.om.ServerInfoKey;
import edu.wisc.mum.status.xom.config.Config;
import edu.wisc.mum.status.xom.config.PortInfoType;
import edu.wisc.mum.status.xom.config.PortInfoType.Port;
import edu.wisc.mum.status.xom.config.ServerGroupType;
import edu.wisc.mum.status.xom.config.ServerType;

/**
 * @author Eric Dalquist
 * @version $Revision: 330 $
 */
@Repository("configDao")
public class JaxbConfigDao extends AbstractCachingJaxbLoader<Config> implements ConfigDao {
    private Map<String, ServerInfoKey> pollUrls;
    private Map<String, ServerInfoKey> monitorServers;
    
    public JaxbConfigDao() {
        super(Config.class);
    }

    @Override
    public Config getConfig() {
        return this.getUnmarshalledObject();
    }
    
    @Override
    public Map<String, ServerInfoKey> getPollUrls() {
        this.getUnmarshalledObject();
        return this.pollUrls;
    }
    
    @Override
    public Map<String, ServerInfoKey> getMonitorServers() {
        this.getUnmarshalledObject();
        return this.monitorServers;
    }

    @Override
    protected void postProcessUnmarshalling(Config config) {
        this.buildPollUrls(config);
        this.buildMonitorServers(config);
    }

    private void buildPollUrls(Config config) {
        final Map<String, ServerInfoKey> urls = new LinkedHashMap<String, ServerInfoKey>();
        
        for (final ServerGroupType serverGroup : config.getServerGroups()) {
            for (final ServerType server : serverGroup.getServers()) {
                for (final PortInfoType portInfo : serverGroup.getPortInfos()) {
                    for (final Port port : portInfo.getPorts()) {
                        final StringBuilder url = new StringBuilder("http");
                        
                        if (port.isSecure()) {
                            url.append("s");
                        }
                        
                        url.append("://").append(server.getServer());
                        
                        final int portValue = port.getValue();
                        if (portValue != 80 && portValue != 443) {
                            url.append(":").append(portValue);
                        }
                        
                        url.append(config.getCheckUrl());

                        final ServerInfoKey serverInfoKey = new ServerInfoKey(serverGroup.getName(), server.getService(), portValue);
                        urls.put(url.toString(), serverInfoKey);
                    }
                }
            }
        }
        
        this.pollUrls = urls;
    }
    


    private void buildMonitorServers(Config config) {
        final Map<String, ServerInfoKey> servers = new LinkedHashMap<String, ServerInfoKey>();
        
        for (final ServerGroupType serverGroup : config.getServerGroups()) {
            for (final ServerType server : serverGroup.getServers()) {
                final ServerInfoKey serverInfoKey = new ServerInfoKey(serverGroup.getName(), server.getService());
                servers.put(server.getService(), serverInfoKey);
            }
        }
        
        this.monitorServers = servers;
    }
}
