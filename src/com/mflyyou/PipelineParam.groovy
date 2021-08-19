package com.mflyyou

class PipelineParam implements Serializable {
    String serviceName;
    String branchName;

    PipelineParam(String serviceName, String branchName) {
        this.serviceName = serviceName
        this.branchName = branchName
    }

    String getBranchName() {
        return branchName
    }

    void setBranchName(String branchName) {
        this.branchName = branchName
    }

    String getServiceName() {
        return serviceName
    }

    void setServiceName(String serviceName) {
        this.serviceName = serviceName
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        PipelineParam that = (PipelineParam) o

        if (branchName != that.branchName) return false
        if (serviceName != that.serviceName) return false

        return true
    }

    int hashCode() {
        int result
        result = (serviceName != null ? serviceName.hashCode() : 0)
        result = 31 * result + (branchName != null ? branchName.hashCode() : 0)
        return result
    }

    @Override
    public String toString() {
        return "PipelineParam{" +
                "serviceName='" + serviceName + '\'' +
                ", branchName='" + branchName + '\'' +
                '}';
    }
}
