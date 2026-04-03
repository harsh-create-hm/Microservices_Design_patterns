package com.example.accountservice.controller;

import com.example.accountservice.event.AccountEvent;
import com.example.accountservice.model.Account;
import com.example.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Account createAccount(@RequestBody Map<String, String> request) {
        return accountService.createAccount(request.get("accountId"), request.get("owner"));
    }

    @PostMapping("/{accountId}/deposit")
    public Account deposit(@PathVariable String accountId, @RequestBody Map<String, Double> request) {
        return accountService.deposit(accountId, request.get("amount"));
    }

    @PostMapping("/{accountId}/withdraw")
    public Account withdraw(@PathVariable String accountId, @RequestBody Map<String, Double> request) {
        return accountService.withdraw(accountId, request.get("amount"));
    }

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        return accountService.getAccount(accountId);
    }

    @GetMapping("/{accountId}/events")
    public List<AccountEvent> getAccountEvents(@PathVariable String accountId) {
        return accountService.getAccountEvents(accountId);
    }
}
