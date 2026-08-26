package com.arjun.library_service.bookstore.library.organization;

import com.arjun.library_service.bookstore.library.domain.OrganizationEntity;
import com.arjun.library_service.bookstore.library.domain.OrganizationRepository;
import com.arjun.library_service.bookstore.library.exception.OrganizationNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public OrganizationEntity getById(Long id) {
        return organizationRepository
                .findById(id)
                .orElseThrow(() -> OrganizationNotFoundException.forId(id));
    }
}
