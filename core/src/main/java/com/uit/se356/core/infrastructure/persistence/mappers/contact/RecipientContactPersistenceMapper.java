package com.uit.se356.core.infrastructure.persistence.mappers.contact;

import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.vo.area.ContactId;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.contact.RecipientContactJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RecipientContactPersistenceMapper {
  /**
   * Chuyển từ Database Entity -> Domain Entity Dùng khi đọc dữ liệu từ DB lên để xử lý nghiệp vụ
   */
  public RecipientContact toDomain(RecipientContactJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return RecipientContact.rehydrate(
        new ContactId(entity.getId()),
        new UserId(entity.getUserId()),
        entity.getName(),
        new PhoneNumber(entity.getPhoneNumber()),
        entity.getAddress(),
        entity.getNote());
  }

  /** Chuyển từ Domain Entity -> Database Entity Dùng khi muốn lưu (Save/Update) xuống DB */
  public RecipientContactJpaEntity toEntity(RecipientContact domain) {
    if (domain == null) {
      return null;
    }

    RecipientContactJpaEntity entity = new RecipientContactJpaEntity();

    // Map ID (nếu có - trường hợp update)
    entity.setId(domain.getId().value());

    if (domain.getOwnerId() != null) {
      entity.setUserId(domain.getOwnerId().value());
    }

    entity.setName(domain.getName());

    if (domain.getPhoneNumber() != null) {
      entity.setPhoneNumber(domain.getPhoneNumber().value());
    }

    entity.setAddress(domain.getAddress());
    entity.setNote(domain.getNote());

    return entity;
  }

  public void updateEntityFromDomain(
      RecipientContact contact, RecipientContactJpaEntity existingEntity) {
    if (contact == null || existingEntity == null) {
      return;
    }

    if (contact.getOwnerId() != null) {
      existingEntity.setUserId(contact.getOwnerId().value());
    }

    existingEntity.setName(contact.getName());

    if (contact.getPhoneNumber() != null) {
      existingEntity.setPhoneNumber(contact.getPhoneNumber().value());
    }

    existingEntity.setAddress(contact.getAddress());
    existingEntity.setNote(contact.getNote());
  }
}
