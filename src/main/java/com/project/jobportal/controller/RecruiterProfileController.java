package com.project.jobportal.controller;

import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.UsersRepository;
import com.project.jobportal.services.RecruiterProfileService;
import com.project.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UsersRepository usersRepository;
    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UsersRepository usersRepository, RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileService = recruiterProfileService;
    }


    @GetMapping("/")
    public String recruiterProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));

            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getId());

            recruiterProfile.ifPresent(profile -> model.addAttribute("profile", profile));

            return "recruiter_profile";
        }
        return "redirect:/login";
    }

    @PostMapping("/addNew")
    public String addNew(RecruiterProfile profile, @RequestParam("image") MultipartFile multipartFile, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));

            profile.setUserId(users);
            profile.setUserAccountId(users.getId());

        }

        model.addAttribute("profile", profile);

        String filename = "";

        if (!multipartFile.getOriginalFilename().equals("")) {
            filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            profile.setProfilePhoto(filename);
        }

        RecruiterProfile savedProfile = recruiterProfileService.addNew(profile);

        String uploadDirectory = "photos/recruiter/" + savedProfile.getUserAccountId() + "/";

        try {
            FileUploadUtil.saveFile(uploadDirectory, filename, multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard";
    }

}


























