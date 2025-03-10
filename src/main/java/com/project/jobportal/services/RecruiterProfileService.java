package com.project.jobportal.services;

import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.RecruiterProfileRepository;
import com.project.jobportal.repository.UsersRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UsersRepository usersRepository;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UsersRepository usersRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.usersRepository = usersRepository;
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users user = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not find user with email: " + username));

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                Optional<RecruiterProfile> recruiterProfile = getOne(user.getId());
                return recruiterProfile.orElse(null);
            }
        }
        return null;
    }

    public Optional<RecruiterProfile> getOne(Integer id) {
        return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile addNew(RecruiterProfile profile) {
        return recruiterProfileRepository.save(profile);
    }
}
