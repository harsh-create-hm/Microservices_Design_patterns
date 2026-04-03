package com.example.userservice.controller;

import com.example.userservice.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> userStore = new ConcurrentHashMap<>();

    public UserController() {
        userStore.put(1L, new User(1L, "alice", "alice@example.com", "ADMIN"));
        userStore.put(2L, new User(2L, "bob", "bob@example.com", "USER"));
        userStore.put(3L, new User(3L, "carol", "carol@example.com", "USER"));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return List.copyOf(userStore.values());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User user = userStore.get(id);
        if (user == null) {
            throw new RuntimeException("User not found: " + id);
        }
        return user;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        userStore.put(user.getId(), user);
        return user;
    }
}
