/**
 * Copyright (c) 2000-2010, Jasig, Inc.
 * See license distributed with this file and available online at
 * https://www.ja-sig.org/svn/jasig-parent/tags/rel-10/license-header.txt
 */

package edu.wisc.mum.status.om;

/**
 * @author Eric Dalquist
 * @version $Revision: 321 $
 */
public enum Status {
    OK,
    WARN,
    FAIL;
    
    public static Status and(Status a, Status b) {
        if (OK == a && OK == b) {
            return OK;
        }
        
        if (FAIL == a || FAIL == b) {
            return FAIL;
        }
        return WARN;
    }
    
    public static Status or(Status a, Status b) {
        if (OK == a || OK == b) {
            return OK;
        }
        
        if (WARN == a || WARN == b) {
            return WARN;
        }
        
        return FAIL;
    }
}
