package com.mflyyou

class PipelineParam implements Serializable{
    String serviceName;

    PipelineParam(String serviceName) {
        this.serviceName = serviceName
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

        if (serviceName != that.serviceName) return false

        return true
    }

    int hashCode() {
        return (serviceName != null ? serviceName.hashCode() : 0)
    }


    @Override
    public String toString() {
        return "PipelineParam{" +
                "serviceName='" + serviceName + '\'' +
                '}';
    }
}
