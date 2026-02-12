package com.uit.se356.core.infrastructure.persistence.mappers.contact;

import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.contact.RecipientContactJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RecipientContactPersistenceMapper {

    /**
     * Chuyển từ Database Entity -> Domain Entity
     * Dùng khi đọc dữ liệu từ DB lên để xử lý nghiệp vụ
     */
    public RecipientContact toDomain(RecipientContactJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new RecipientContact(
                entity.getId(),
                new UserId(entity.getUserId()),
                entity.getName(),
                new PhoneNumber(entity.getPhoneNumber()),
                entity.getNote(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Chuyển từ Domain Entity -> Database Entity
     * Dùng khi muốn lưu (Save/Update) xuống DB
     */
    public RecipientContactJpaEntity toEntity(RecipientContact domain) {
        if (domain == null) {
            return null;
        }

        RecipientContactJpaEntity entity = new RecipientContactJpaEntity();

        // Map ID (nếu có - trường hợp update)
        entity.setId(domain.getId());

        if (domain.getOwnerId() != null) {
            entity.setUserId(domain.getOwnerId().value());
        }

        entity.setName(domain.getName());

        if (domain.getPhoneNumber() != null) {
            entity.setPhoneNumber(domain.getPhoneNumber().value());
        }

        entity.setNote(domain.getNote());

        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }
}