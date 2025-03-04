package com.project.jobportal.controller;

import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.Skills;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.UsersRepository;
import com.project.jobportal.services.JobSeekerProfileService;
import com.project.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("job-seeker-profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final UsersRepository usersRepository;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UsersRepository usersRepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/")
    public String jobSeekerProfile(Model model) {

        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills = new ArrayList<>();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users users = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
            Optional<JobSeekerProfile> seeker = jobSeekerProfileService.getOne(users.getId());
            if (seeker.isPresent()) {
                jobSeekerProfile = seeker.get();
                if (jobSeekerProfile.getSkills().isEmpty()) {
                    skills.add(new Skills());
                    jobSeekerProfile.setSkills(skills);
                }
            }

            model.addAttribute("skills", skills);
            model.addAttribute("profile", jobSeekerProfile);
        }

        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNew(@ModelAttribute("profile") JobSeekerProfile profile, @RequestParam("image") MultipartFile image, @RequestParam("pdf") MultipartFile pdf, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users users = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
            profile.setUserAccountId(users.getId());
            profile.setUserId(users);
        }

        List<Skills> list = profile.getSkills().stream().filter(skills -> !skills.getName().isEmpty()).toList();
        profile.setSkills(list);

        for (Skills skills : profile.getSkills()) {
            skills.setJobSeekerProfile(profile);
        }

        String imageName = "";
        String resumeName = "";

        if (!image.getOriginalFilename().isEmpty()) {
            imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            profile.setProfilePhoto(imageName);
        }

        if (!pdf.getOriginalFilename().isEmpty()) {
            resumeName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            profile.setResume(resumeName);
        }

        JobSeekerProfile saved = jobSeekerProfileService.addNew(profile);

        try {
            String uploadDirectory = "photos/candidate/" + saved.getUserAccountId();
            if (!image.getOriginalFilename().isEmpty()) {
                FileUploadUtil.saveFile(uploadDirectory, imageName, image);
            }
            if (!pdf.getOriginalFilename().isEmpty()) {
                FileUploadUtil.saveFile(uploadDirectory, resumeName, pdf);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/dashboard";
    }
}
