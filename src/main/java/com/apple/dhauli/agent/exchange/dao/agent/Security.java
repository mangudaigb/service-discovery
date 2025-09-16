package com.apple.dhauli.agent.exchange.dao.agent;

import java.util.ArrayList;
import java.util.List;

public record Security(
        AuthenticationMethod authMethod,
        List<String> scopes ) {

    public Security {
        if (scopes == null) scopes = new ArrayList<>();
    }
}
