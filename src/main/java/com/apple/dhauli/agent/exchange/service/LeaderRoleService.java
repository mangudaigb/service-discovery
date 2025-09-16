package com.apple.dhauli.agent.exchange.service;

import com.apple.dhauli.agent.exchange.watcher.ServiceWatcherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LeaderRoleService {
    private static final Logger logger = LoggerFactory.getLogger(LeaderRoleService.class);

    private final ServiceWatcherService serviceWatcherService;

    public LeaderRoleService(ServiceWatcherService serviceWatcherService) {
        this.serviceWatcherService = serviceWatcherService;
    }

    public void startLeadership() throws Exception {
        logger.info("Starting leadership");
        serviceWatcherService.startWatcher();
    }

    public void stopLeadership() throws Exception {
        logger.info("Stopping leadership");
        serviceWatcherService.stopWatcher();
    }
}
