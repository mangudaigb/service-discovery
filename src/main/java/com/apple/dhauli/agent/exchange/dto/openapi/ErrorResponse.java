package com.apple.dhauli.agent.exchange.dto.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    @JsonProperty("errors")
    private List<ErrorItem> errorItemList = new ArrayList<>();

    public List<ErrorItem> addErrorItem(Integer status, String message, String description) {
        ErrorItem errorItem = new ErrorItem(status, message, description);
        errorItemList.add(errorItem);
        return errorItemList;
    }

    public List<ErrorItem> getErrorItemList() {
        return errorItemList;
    }

    public void setErrorItemList(List<ErrorItem> errorItemList) {
        this.errorItemList = errorItemList;
    }

    public static class ErrorItem {
        private Integer status;
        private String message;
        private String description;

        public ErrorItem(Integer status, String message, String description) {
            this.status = status;
            this.message = message;
            this.description = description;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer code) {
            this.status = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
