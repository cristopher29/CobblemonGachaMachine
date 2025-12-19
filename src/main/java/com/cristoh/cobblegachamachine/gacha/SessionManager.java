package com.cristoh.cobblegachamachine.gacha;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final Map<UUID, Session> SESSIONS = new HashMap<>();

    public static Session getOrCreate(UUID playerId) {
        return SESSIONS.computeIfAbsent(playerId, id -> new Session());
    }

    public static Session get(UUID playerId) {
        return SESSIONS.get(playerId);
    }

    public static void remove(UUID playerId) {
        SESSIONS.remove(playerId);
    }
}

