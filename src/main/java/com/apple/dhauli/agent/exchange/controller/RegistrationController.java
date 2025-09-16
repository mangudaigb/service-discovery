package com.apple.dhauli.agent.exchange.controller;

import com.apple.dhauli.agent.exchange.dto.openapi.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.openapitools.client.model.AgentRegistrationRequest;
import org.openapitools.client.model.McpServerRegistrationRequest;
import org.openapitools.client.model.ServiceRegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final Validator validator;

    public RegistrationController(Validator validator) {
        this.validator = validator;
    }

    @GetMapping("/services")
    public ResponseEntity<?> services(@RequestParam(name = "query", required = false) String query) {
//        serviceCache.stream().forEach((service) -> {
//            logger.info(" - {} : {}", service.getPath(),  new String(service.getData(), StandardCharsets.UTF_8));
//        });
        return ResponseEntity.status(HttpStatus.OK).body("Test");
    }

    @PostMapping("/agent")
    public ResponseEntity<?> register(@RequestBody AgentRegistrationRequest request) {
        Set<ConstraintViolation<AgentRegistrationRequest>> constraintViolations = validator.validate(request);
        ErrorResponse errorResponse = new ErrorResponse();
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<AgentRegistrationRequest> constraintViolation : constraintViolations) {
                String message = constraintViolation.getMessage();
                String field = constraintViolation.getPropertyPath().toString();
                errorResponse.addErrorItem(404, field, message);
            }
            logger.info(errorResponse.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        logger.info(request.toString());
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/mcp-server")
    public ResponseEntity<?> registerMcpServer(@RequestBody McpServerRegistrationRequest request) {
        Set<ConstraintViolation<McpServerRegistrationRequest>> constraintViolations = validator.validate(request);
        ErrorResponse errorResponse = new ErrorResponse();
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<McpServerRegistrationRequest> constraintViolation : constraintViolations) {
                String message = constraintViolation.getMessage();
                String field = constraintViolation.getPropertyPath().toString();
                errorResponse.addErrorItem(404, field, message);
            }
            logger.info(errorResponse.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        logger.info(request.toString());
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/service")
    public ResponseEntity<String> registerService(@Valid @RequestBody ServiceRegistrationRequest request) {
        return ResponseEntity.ok("Registered");
    }
}
