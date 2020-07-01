package com.app.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "elements")
public class Element {

  @Id
  private String primaryKey;
  private String name;
  private String description;
  private LocalDateTime updatedTimestamp;

  @PrePersist
  private void ensurePrimaryKey() {
    this.setPrimaryKey(primaryKey);
  }

  public void setPrimaryKey(final String primaryKey) {
    this.primaryKey = primaryKey;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Element element = (Element) o;
    return Objects.equals(primaryKey, element.primaryKey) &&
        Objects.equals(name, element.name) &&
        Objects.equals(description, element.description) &&
        Objects.equals(updatedTimestamp, element.updatedTimestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey, name, description, updatedTimestamp);
  }
}
