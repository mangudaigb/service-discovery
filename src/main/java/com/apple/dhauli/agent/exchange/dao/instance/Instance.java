package com.apple.dhauli.agent.exchange.dao.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Instance {
    private String id;
    private String fqdn;

    private String protocol;
    private String ip;
    private Integer port;

    private String topicName;
    private List<Integer> partitionList = new ArrayList<Integer>();
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<Integer> getPartitionList() {
        return partitionList;
    }

    public void setPartitionList(List<Integer> partitionList) {
        this.partitionList = partitionList;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getUrl() {
        return protocol + "://" + ip + ":" + port;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getFqdnUrl() {
        return protocol + "://" + fqdn + ":" + port;
    }
}
