package com.project.jobportal.controller;

import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.JobSeekerSave;
import com.project.jobportal.services.JobPostActivityService;
import com.project.jobportal.services.JobSeekerProfileService;
import com.project.jobportal.services.JobSeekerSaveService;
import com.project.jobportal.services.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {

    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobSeekerSaveController(UsersService usersService, JobSeekerProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @PostMapping("/job-details/save/{id}")
    public String save(@PathVariable("id") int id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            int userId = usersService.findByEmail(username).getId();
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(userId);
            JobPostActivity postActivity = jobPostActivityService.getOne(id);

            if (seekerProfile.isPresent() && postActivity != null) {
                jobSeekerSaveService.addNew(seekerProfile.get(), postActivity);
            } else {
                throw new RuntimeException("User not found.");
            }
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/saved-jobs")
    public String savedJobs(Model model) {

        List<JobPostActivity> jobPost = new ArrayList<>();
        JobSeekerProfile jobSeekerProfile = (JobSeekerProfile) usersService.getCurrentUserProfile();
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJobs(jobSeekerProfile);

        for (JobSeekerSave save : jobSeekerSaveList) {
            jobPost.add(save.getJob());
        }

        model.addAttribute("jobPost", jobPost);
        model.addAttribute("user", jobSeekerProfile);

        return "saved-jobs";
    }
}



















