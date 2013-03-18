/**
 * Copyright (c) 2000-2009, Jasig, Inc.
 * See license distributed with this file and available online at
 * https://www.ja-sig.org/svn/jasig-parent/tags/rel-10/license-header.txt
 */

package edu.wisc.mum.status.om;

import java.util.Date;

/**
 * @author Eric Dalquist
 * @version $Revision: 298 $
 */
public class ServerResponse {
    private Date timestamp;  
    private int statusCode;
    private String statusReasonPhrase;
    private long duration;
    
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getStatusReasonPhrase() {
        return statusReasonPhrase;
    }
    public void setStatusReasonPhrase(String statusReasonPhrase) {
        this.statusReasonPhrase = statusReasonPhrase;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public Status getStatus() {
        if (this.statusCode == 200) {
            return Status.OK;
        }
        
        if (this.statusCode == 404) {
            return Status.WARN;
        }
        
        return Status.FAIL;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (duration ^ (duration >>> 32));
        result = prime * result + statusCode;
        result = prime * result + ((statusReasonPhrase == null) ? 0 : statusReasonPhrase.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
        if (!(obj instanceof ServerResponse)) {
            return false;
        }
        ServerResponse other = (ServerResponse) obj;
        if (duration != other.duration) {
            return false;
        }
        if (statusCode != other.statusCode) {
            return false;
        }
        if (statusReasonPhrase == null) {
            if (other.statusReasonPhrase != null) {
                return false;
            }
        }
        else if (!statusReasonPhrase.equals(other.statusReasonPhrase)) {
            return false;
        }
        if (timestamp == null) {
            if (other.timestamp != null) {
                return false;
            }
        }
        else if (!timestamp.equals(other.timestamp)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "ServerResponse [duration=" + duration + ", statusCode=" + statusCode + ", statusReasonPhrase="
                + statusReasonPhrase + ", timestamp=" + timestamp + "]";
    }
}
