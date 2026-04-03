package com.example.accountservice.service;

import com.example.accountservice.event.AccountEvent;
import com.example.accountservice.model.Account;
import com.example.accountservice.repository.EventStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final EventStore eventStore;

    public Account createAccount(String accountId, String owner) {
        AccountEvent event = new AccountEvent(
            UUID.randomUUID().toString(),
            accountId,
            "ACCOUNT_CREATED",
            0.0,
            Instant.now(),
            "Account created for " + owner
        );
        eventStore.append(event);
        log.info("Event sourcing: ACCOUNT_CREATED for {}", accountId);
        return rebuildAccount(accountId);
    }

    public Account deposit(String accountId, Double amount) {
        validateAccountExists(accountId);
        AccountEvent event = new AccountEvent(
            UUID.randomUUID().toString(),
            accountId,
            "MONEY_DEPOSITED",
            amount,
            Instant.now(),
            "Deposited $" + amount
        );
        eventStore.append(event);
        log.info("Event sourcing: MONEY_DEPOSITED ${} to {}", amount, accountId);
        return rebuildAccount(accountId);
    }

    public Account withdraw(String accountId, Double amount) {
        Account current = rebuildAccount(accountId);
        if (current.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }
        AccountEvent event = new AccountEvent(
            UUID.randomUUID().toString(),
            accountId,
            "MONEY_WITHDRAWN",
            amount,
            Instant.now(),
            "Withdrew $" + amount
        );
        eventStore.append(event);
        log.info("Event sourcing: MONEY_WITHDRAWN ${} from {}", amount, accountId);
        return rebuildAccount(accountId);
    }

    public Account getAccount(String accountId) {
        validateAccountExists(accountId);
        return rebuildAccount(accountId);
    }

    public List<AccountEvent> getAccountEvents(String accountId) {
        return eventStore.getEventsForAccount(accountId);
    }

    // Core of Event Sourcing: rebuild current state by replaying all events
    private Account rebuildAccount(String accountId) {
        List<AccountEvent> accountEvents = eventStore.getEventsForAccount(accountId);
        Account account = new Account(accountId, "", 0.0, "UNKNOWN", 0);

        for (AccountEvent event : accountEvents) {
            switch (event.getEventType()) {
                case "ACCOUNT_CREATED" -> {
                    account.setOwner(event.getDescription().replace("Account created for ", ""));
                    account.setBalance(0.0);
                    account.setStatus("ACTIVE");
                }
                case "MONEY_DEPOSITED" -> account.setBalance(account.getBalance() + event.getAmount());
                case "MONEY_WITHDRAWN" -> account.setBalance(account.getBalance() - event.getAmount());
            }
            account.setEventCount(account.getEventCount() + 1);
        }
        return account;
    }

    private void validateAccountExists(String accountId) {
        List<AccountEvent> events = eventStore.getEventsForAccount(accountId);
        if (events.isEmpty()) {
            throw new RuntimeException("Account not found: " + accountId);
        }
    }
}
