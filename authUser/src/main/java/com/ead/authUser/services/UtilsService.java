package com.ead.authUser.services;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {
    String createURL(UUID userId, Pageable pageable);
}
