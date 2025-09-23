package com.apple.dhauli.agent.exchange.controller;

import com.apple.dhauli.agent.exchange.dao.instance.AgentInstance;
import com.apple.dhauli.agent.exchange.dao.instance.MCPServerInstance;
import com.apple.dhauli.agent.exchange.dao.instance.ServiceInstance;
import com.apple.dhauli.agent.exchange.service.AgentDiscoveryCacheService;
import com.apple.dhauli.agent.exchange.service.McpServerDiscoveryCacheService;
import com.apple.dhauli.agent.exchange.service.ServiceDiscoveryCacheService;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusController {

    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

    private final AgentDiscoveryCacheService agentDiscoveryCacheService;
    private final ServiceDiscoveryCacheService serviceDiscoveryCacheService;
    private final McpServerDiscoveryCacheService mcpServerDiscoveryCacheService;
    private final Validator validator;

    public StatusController(Validator validator,
                            AgentDiscoveryCacheService agentDiscoveryCacheService,
                            ServiceDiscoveryCacheService serviceDiscoveryCacheService,
                            McpServerDiscoveryCacheService mcpServerDiscoveryCacheService) {
        this.validator = validator;
        this.agentDiscoveryCacheService = agentDiscoveryCacheService;
        this.serviceDiscoveryCacheService = serviceDiscoveryCacheService;
        this.mcpServerDiscoveryCacheService = mcpServerDiscoveryCacheService;
    }

    @GetMapping("/services")
    public ResponseEntity<?> services(@RequestParam(name = "query", required = false) String query) {
        List<ServiceInstance> serviceInstanceList = serviceDiscoveryCacheService.getAllServices();
        return ResponseEntity.status(HttpStatus.OK).body(serviceInstanceList);
    }

    @GetMapping("/agents")
    public ResponseEntity<?> agents(@RequestParam(name = "query", required = false) String query) {
        List<AgentInstance> agentInstanceList = agentDiscoveryCacheService.getAllAgents();
        return ResponseEntity.status(HttpStatus.OK).body(agentInstanceList);
    }

    @GetMapping("/mcp")
    public ResponseEntity<?> mcp(@RequestParam(name = "query", required = false) String query) {
        List<MCPServerInstance> mcpServerInstanceList = mcpServerDiscoveryCacheService.getAllMcpServers();
        return ResponseEntity.status(HttpStatus.OK).body(mcpServerInstanceList);
    }
}
