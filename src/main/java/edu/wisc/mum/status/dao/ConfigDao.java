/**
 * Copyright (c) 2000-2009, Jasig, Inc.
 * See license distributed with this file and available online at
 * https://www.ja-sig.org/svn/jasig-parent/tags/rel-10/license-header.txt
 */

package edu.wisc.mum.status.dao;

import java.util.Map;

import edu.wisc.mum.status.om.ServerInfoKey;
import edu.wisc.mum.status.xom.config.Config;

/**
 * @author Eric Dalquist
 * @version $Revision: 298 $
 */
public interface ConfigDao {
    public Config getConfig();
    
    public Map<String, ServerInfoKey> getPollUrls();
    
    public Map<String, ServerInfoKey> getMonitorServers();
}
