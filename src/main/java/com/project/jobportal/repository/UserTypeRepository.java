package com.project.jobportal.repository;

import com.project.jobportal.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UsersType, Integer> {
}
