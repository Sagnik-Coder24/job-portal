package com.project.jobportal.services;

import com.project.jobportal.entity.UsersType;
import com.project.jobportal.repository.UserTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;

    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public List<UsersType> getAll() {
        return userTypeRepository.findAll();
    }
}
