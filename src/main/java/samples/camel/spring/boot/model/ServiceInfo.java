package samples.camel.spring.boot.model;

import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInfo implements Serializable {

    private static final long serialVersionUID = 1201862274036785594L;

    private Integer port;
    private String serviceName;
    private String ip;
    private Integer sleepSecs;
    private String transactionId;

    public ServiceInfo() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getSleepSecs() {
        return sleepSecs;
    }

    public void setSleepSecs(Integer sleepSecs) {
        this.sleepSecs = sleepSecs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.transactionId, this.ip, this.port, this.serviceName, this.sleepSecs);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceInfo other = (ServiceInfo)obj;

        return Objects.equals(this.transactionId, other.getTransactionId()) && Objects.equals(this.ip, other.getIp()) && Objects.equals(this.port, other.getPort())
               && Objects.equals(this.serviceName, other.getServiceName()) && Objects.equals(this.sleepSecs, this.getSleepSecs());
    }

    @Override
    public String toString() {
        return String.format("ServiceInfo [%s]: %s:%s - [%s] - delay of %s seconds", this.transactionId, this.ip, this.port, this.serviceName, this.sleepSecs);
    }

}
