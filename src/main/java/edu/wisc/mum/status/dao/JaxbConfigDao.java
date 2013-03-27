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
