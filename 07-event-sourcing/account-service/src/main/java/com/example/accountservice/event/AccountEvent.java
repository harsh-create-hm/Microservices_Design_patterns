package com.example.accountservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEvent {
    private String eventId;
    private String accountId;
    private String eventType;  // ACCOUNT_CREATED, MONEY_DEPOSITED, MONEY_WITHDRAWN
    private Double amount;
    private Instant occurredAt;
    private String description;
}
