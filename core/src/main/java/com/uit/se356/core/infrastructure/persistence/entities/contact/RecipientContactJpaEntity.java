package com.uit.se356.core.infrastructure.persistence.entities.contact;

import com.uit.se356.core.infrastructure.persistence.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recipient_contacts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "phone_number"})
})
public class RecipientContactJpaEntity extends BaseEntity {
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    private String address;

    private String note;
}
