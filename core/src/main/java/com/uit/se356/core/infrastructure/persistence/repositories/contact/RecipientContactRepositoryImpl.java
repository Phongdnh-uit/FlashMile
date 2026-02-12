package com.uit.se356.core.infrastructure.persistence.repositories.contact;

import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.contact.RecipientContactJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.contact.RecipientContactPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RecipientContactRepositoryImpl implements RecipientContactRepository {

    private final RecipientContactJpaRepository jpaRepository;
    private final RecipientContactPersistenceMapper mapper;

    @Override
    public RecipientContact save(RecipientContact contact) {
        RecipientContactJpaEntity entity = mapper.toEntity(contact);
        RecipientContactJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<RecipientContact> findAllByOwnerId(UserId ownerId) {
        return jpaRepository.findByUserIdOrderByNameAsc(ownerId.value()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RecipientContact> findByOwnerIdAndPhone(UserId ownerId, PhoneNumber phoneNumber) {
        return jpaRepository
                .findByUserIdAndPhoneNumber(ownerId.value(), phoneNumber.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<RecipientContact> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByOwnerIdAndPhone(UserId ownerId, PhoneNumber phoneNumber) {
        return jpaRepository.existsByUserIdAndPhoneNumber(ownerId.value(), phoneNumber.value());
    }
}
