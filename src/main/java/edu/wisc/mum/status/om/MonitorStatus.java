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
public class MonitorStatus {
    private long minimumDuration;
    private double averageDuration;
    private long maximumDuration;
    private String status;
    
    public long getMinimumDuration() {
        return this.minimumDuration;
    }
    public void setMinimumDuration(long minimumDuration) {
        this.minimumDuration = minimumDuration;
    }
    public double getAverageDuration() {
        return this.averageDuration;
    }
    public void setAverageDuration(double averageDuration) {
        this.averageDuration = averageDuration;
    }
    public long getMaximumDuration() {
        return this.maximumDuration;
    }
    public void setMaximumDuration(long maximumDuration) {
        this.maximumDuration = maximumDuration;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
    public Status getDurationStatus() {
        if (this.averageDuration < 2000 && this.maximumDuration < 10000) {
            return Status.OK;
        }
        
        if (this.averageDuration < 5000 && this.maximumDuration < 30000) {
            return Status.WARN;
        }
        
        return Status.FAIL;
    }
    public Status getMonitorStatus() {
        if ("UP".equals(status)) {
            return Status.OK;
        }
        else if ("OUT_UP".equals(status) || "OUT_DOWN".equals(status)) {
            return Status.WARN;
        }
        
        return Status.FAIL;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.averageDuration);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (int) (this.maximumDuration ^ (this.maximumDuration >>> 32));
        result = prime * result + (int) (this.minimumDuration ^ (this.minimumDuration >>> 32));
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
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
        MonitorStatus other = (MonitorStatus) obj;
        if (Double.doubleToLongBits(this.averageDuration) != Double.doubleToLongBits(other.averageDuration)) {
            return false;
        }
        if (this.maximumDuration != other.maximumDuration) {
            return false;
        }
        if (this.minimumDuration != other.minimumDuration) {
            return false;
        }
        if (this.status == null) {
            if (other.status != null) {
                return false;
            }
        }
        else if (!this.status.equals(other.status)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "MonitorStatus [averageDuration=" + this.averageDuration + ", maximumDuration=" + this.maximumDuration
                + ", minimumDuration=" + this.minimumDuration + ", status=" + this.status + "]";
    }
}
