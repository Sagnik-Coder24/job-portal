package com.project.jobportal.services;

import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users addNew(Users user) {
        user.setActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));

        // TODO: Bcrypt password

        return usersRepository.save(user);
    }

    public Optional<Users> getUsersByEmail(String email){
        return usersRepository.findByEmail(email);
    }
}
