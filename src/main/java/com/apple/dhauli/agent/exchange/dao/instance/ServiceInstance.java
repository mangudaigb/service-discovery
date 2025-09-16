package com.apple.dhauli.agent.exchange.dao.instance;

import java.util.ArrayList;
import java.util.List;

public class ServiceInstance {
    private String serviceName;
    private List<Instance> instanceList = new ArrayList<Instance>();

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<Instance> getInstanceList() {
        return instanceList;
    }

    public void setInstanceList(List<Instance> instanceList) {
        this.instanceList = instanceList;
    }
}
