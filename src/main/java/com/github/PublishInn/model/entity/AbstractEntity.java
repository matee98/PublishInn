package com.github.PublishInn.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EqualsAndHashCode
public abstract class AbstractEntity {

    @Getter
    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Getter
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Getter
    @Setter
    @JoinColumn(name = "created_by", updatable = false)
    private Long createdBy;

    @Getter
    @Setter
    @JoinColumn(name = "modified_by")
    private Long modifiedBy;

    @Getter
    @Version
    private Long version;

    @PrePersist
    private void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        modifiedOn = LocalDateTime.now();
    }

    public abstract Long getId();

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder
                .append("creationDate: ")
                .append(createdOn)
                .append("modificationDate: ")
                .append(modifiedOn)
                .append("version: ")
                .append(version)
                .append("createdBy")
                .append(createdBy == null ? "" : createdBy);
        if (modifiedBy != null) {
            toStringBuilder
                    .append("modifiedBy")
                    .append(modifiedBy);
        }
        return toStringBuilder.toString();
    }
}
