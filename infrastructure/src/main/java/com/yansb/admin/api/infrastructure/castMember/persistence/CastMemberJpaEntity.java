package com.yansb.admin.api.infrastructure.castMember.persistence;

import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.castMember.CastMemberType;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "CastMember")
@Table(name = "cast_members")
public class CastMemberJpaEntity {
  @Id
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private CastMemberType type;

  @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant updatedAt;

  public CastMemberJpaEntity() {
  }

  private CastMemberJpaEntity(final String id, final String name, final CastMemberType type, final Instant createdAt, final Instant updatedAt) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static CastMemberJpaEntity from(final CastMember aMember) {
    return new CastMemberJpaEntity(
        aMember.getId().getValue(),
        aMember.getName(),
        aMember.getType(),
        aMember.getCreatedAt(),
        aMember.getUpdatedAt()
    );
  }

  public CastMember toAggregate() {
    return CastMember.with(
        CastMemberID.from(this.id),
        this.name,
        this.type,
        this.createdAt,
        this.updatedAt
    );
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CastMemberType getType() {
    return type;
  }

  public void setType(CastMemberType type) {
    this.type = type;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
