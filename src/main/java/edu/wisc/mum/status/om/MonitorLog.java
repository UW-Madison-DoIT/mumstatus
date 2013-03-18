/**
 * Copyright (c) 2000-2010, Jasig, Inc.
 * See license distributed with this file and available online at
 * https://www.ja-sig.org/svn/jasig-parent/tags/rel-10/license-header.txt
 */

package edu.wisc.mum.status.om;

import java.util.Date;

/**
 * @author Eric Dalquist
 * @version $Revision: 299 $
 */
public class MonitorLog {
    private String label;
    private Date lastSample;
    private long duration;
    private boolean success;
    
    public String getLabel() {
        return this.label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public Date getLastSample() {
        return this.lastSample;
    }
    public void setLastSample(Date lastSample) {
        this.lastSample = lastSample;
    }
    public long getDuration() {
        return this.duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public boolean isSuccess() {
        return this.success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.duration ^ (this.duration >>> 32));
        result = prime * result + ((this.label == null) ? 0 : this.label.hashCode());
        result = prime * result + ((this.lastSample == null) ? 0 : this.lastSample.hashCode());
        result = prime * result + (this.success ? 1231 : 1237);
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
        MonitorLog other = (MonitorLog) obj;
        if (this.duration != other.duration) {
            return false;
        }
        if (this.label == null) {
            if (other.label != null) {
                return false;
            }
        }
        else if (!this.label.equals(other.label)) {
            return false;
        }
        if (this.lastSample == null) {
            if (other.lastSample != null) {
                return false;
            }
        }
        else if (!this.lastSample.equals(other.lastSample)) {
            return false;
        }
        if (this.success != other.success) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "MonitorLog [label=" + this.label + ", lastSample=" + this.lastSample + ", duration=" + this.duration
                + ", success=" + this.success + "]";
    }
}
