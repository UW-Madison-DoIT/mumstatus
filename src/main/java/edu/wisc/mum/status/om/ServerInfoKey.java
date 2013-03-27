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
package edu.wisc.mum.status.om;

/**
 * @author Eric Dalquist
 * @version $Revision: 298 $
 */
public class ServerInfoKey {
    private final String groupName;
    private final String serviceName;
    private final Integer port;
    
    public ServerInfoKey(String groupName, String serviceName) {
        this.groupName = groupName;
        this.serviceName = serviceName;
        this.port = null;
    }

    public ServerInfoKey(String groupName, String serviceName, int port) {
        this.groupName = groupName;
        this.serviceName = serviceName;
        this.port = port;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public int getPort() {
        return this.port;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.groupName == null) ? 0 : this.groupName.hashCode());
        result = prime * result + ((this.port == null) ? 0 : this.port.hashCode());
        result = prime * result + ((this.serviceName == null) ? 0 : this.serviceName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ServerInfoKey other = (ServerInfoKey) obj;
        if (this.groupName == null) {
            if (other.groupName != null) {
                return false;
            }
        }
        else if (!this.groupName.equals(other.groupName)) {
            return false;
        }
        if (this.port == null) {
            if (other.port != null) {
                return false;
            }
        }
        else if (!this.port.equals(other.port)) {
            return false;
        }
        if (this.serviceName == null) {
            if (other.serviceName != null) {
                return false;
            }
        }
        else if (!this.serviceName.equals(other.serviceName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (this.port != null) {
            return this.groupName + "." + this.serviceName + "." + this.port;
        }
        
        return this.groupName + "." + this.serviceName;
    }
}
