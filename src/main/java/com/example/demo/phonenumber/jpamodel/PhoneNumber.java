package com.example.demo.phonenumber.jpamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "PhoneNumbers")
@Table(name = "PHONE_NUMBERS", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = { "PREFIX", "PHONE_NUMBER" },
                name = "PHONE_NUMBERS_UNIQUE_CONSTRAINT_PREFIX_PHONE_NUMBER"
        )
})
public class PhoneNumber {

    @Id
    @Column(name = "PHONE_NUMBER_ID", nullable = false, updatable = false)
    @SequenceGenerator(name = "PHONE_NUMBER_SEQUENCE", sequenceName = "PHONE_NUMBER_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHONE_NUMBER_SEQUENCE")
    private Long phoneNumberId;

    @Column(name = "OWNER_NAME", nullable = false)
    private String ownerName;

    @Size(min = 2, max = 4)
    @NotEmpty
    @Column(name = "PREFIX", nullable = false)
    private String prefix;

    @Size(min = 5, max = 13)
    @NotEmpty
    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Column(name = "ENCRYPTED_PIN")
    @JsonIgnore
    private String encryptedPin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhoneNumber that = (PhoneNumber) o;
        return phoneNumberId != null && Objects.equals(phoneNumberId, that.phoneNumberId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
