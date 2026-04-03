package com.example.accountservice.repository;

import com.example.accountservice.event.AccountEvent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class EventStore {

    // In production, this would be a database table
    private final List<AccountEvent> events = new CopyOnWriteArrayList<>();

    public void append(AccountEvent event) {
        events.add(event);
    }

    public List<AccountEvent> getEventsForAccount(String accountId) {
        return events.stream()
            .filter(e -> e.getAccountId().equals(accountId))
            .collect(Collectors.toList());
    }

    public List<AccountEvent> getAllEvents() {
        return List.copyOf(events);
    }
}
