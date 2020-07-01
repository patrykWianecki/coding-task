package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.Element;

@Repository
public interface ElementRepository extends JpaRepository<Element, String> {
}
