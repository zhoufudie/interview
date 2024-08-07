package org.example.controller;

import org.example.permission.Role;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * org.example.controller
 * User: fd
 * Date: 2024/08/07 18:55
 * Description:
 * Version: V1.0
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Role(value = Role.GUEST)
    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.loginAdmin(username, password);
        return ResponseEntity.ok(token);
    }

    @Role(value = Role.USER)
    @GetMapping("/api/user")
    public ResponseEntity<Void> getUser(@RequestParam String id) {
        return ResponseEntity.ok().build();
    }

    @Role(value = Role.ADMIN)
    @DeleteMapping("/api/user")
    public ResponseEntity<Void> deleteUser(@RequestParam String id) {
        return ResponseEntity.ok().build();
    }
}