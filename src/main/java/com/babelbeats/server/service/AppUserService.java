package com.babelbeats.server.service;

import com.babelbeats.server.exception.ResourceNotFoundException;
import com.babelbeats.server.exception.BadRequestException;
import com.babelbeats.server.model.AppUser;
import com.babelbeats.server.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> getAllAppUsers() {
        return appUserRepository.findAll();
    }

    public AppUser getAppUserById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public AppUser getAppUserByProviderId(String providerId) {
        return appUserRepository.findByProviderId(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with providerId: " + providerId));
    }

    public AppUser createAppUser(AppUser appUser) {
        if (appUser.getName() == null) {
            throw new BadRequestException("Name is required field.");
        }
        return appUserRepository.save(appUser);
    }

    public AppUser updateAppUser(Long id, AppUser appUser) {
        if (!appUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("AppUser not found with id " + id);
        }
        appUser.setId(id);
        return appUserRepository.save(appUser);
    }

    public AppUser partialUpdateAppUser(Long id, Map<String, Object> updates) {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
        if (optionalAppUser.isEmpty()) {
            throw new ResourceNotFoundException("AppUser not found with id " + id);
        }

        AppUser appUser = optionalAppUser.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(AppUser.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, appUser, value);
            }
        });
        return appUserRepository.save(appUser);
    }

    public void deleteAppUser(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        appUserRepository.deleteById(id);
    }

    public void deleteAllAppUsers() {
        appUserRepository.deleteAll();
    }
}

