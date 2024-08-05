package com.babelbeats.server.controller;

import com.babelbeats.server.model.AppUser;
import com.babelbeats.server.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appusers")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllAppUsers() {
        List<AppUser> appUsers = appUserService.getAllAppUsers();
        return ResponseEntity.ok(appUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getAppUserById(@PathVariable Long id) {
        AppUser appUser = appUserService.getAppUserById(id);
        return ResponseEntity.ok(appUser);
    }

    @GetMapping("/by-provider/{providerId}")
    public ResponseEntity<AppUser> getAppUserById(@PathVariable String providerId) {
        AppUser appUser = appUserService.getAppUserByProviderId(providerId);
        return ResponseEntity.ok(appUser);
    }

    @PostMapping
    public ResponseEntity<AppUser> createAppUser(@RequestBody AppUser appUser) {
        AppUser createdAppUser = appUserService.createAppUser(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAppUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateAppUser(@PathVariable Long id, @RequestBody AppUser appUserDetails) {
        AppUser updatedAppUser = appUserService.updateAppUser(id, appUserDetails);
        return ResponseEntity.ok(updatedAppUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AppUser> partialUpdateAppUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        AppUser updatedAppUser = appUserService.partialUpdateAppUser(id, updates);
        return ResponseEntity.ok(updatedAppUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppUser(@PathVariable Long id) {
        appUserService.deleteAppUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAppUsers() {
        appUserService.deleteAllAppUsers();
        return ResponseEntity.noContent().build();
    }
}
