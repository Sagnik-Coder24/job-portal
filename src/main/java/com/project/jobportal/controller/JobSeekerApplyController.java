package com.project.jobportal.controller;

import com.project.jobportal.entity.*;
import com.project.jobportal.services.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UsersService usersService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService, RecruiterProfileService recruiterProfileService, JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("/job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {

        JobPostActivity jobPost = jobPostActivityService.getOne(id);
        List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getJobCandidates(jobPost);
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getJobCandidates(jobPost);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                RecruiterProfile user = recruiterProfileService.getCurrentRecruiterProfile();
                if (user != null) {
                    model.addAttribute("applyList", jobSeekerApplyList);
                }
            } else {
                JobSeekerProfile user = jobSeekerProfileService.getCurrentSeekerProfile();
                if (user != null) {
                    boolean exists = false;
                    boolean saved = false;

                    if (jobSeekerApplyList.stream().anyMatch(jobSeekerApply -> Objects.equals(jobSeekerApply.getUserId().getUserAccountId(), user.getUserAccountId()))) {
                        exists = true;
                    }

                    if (jobSeekerSaveList.stream().anyMatch(jobSeekerSave -> Objects.equals(jobSeekerSave.getUserId().getUserAccountId(), user.getUserAccountId()))) {
                        saved = true;
                    }

                    model.addAttribute("alreadyApplied", exists);
                    model.addAttribute("alreadySaved", saved);
                }
            }
        }

//        JobSeekerApply jobSeekerApply = new JobSeekerApply();
//        model.addAttribute("applyJob", jobSeekerApply);

        model.addAttribute("jobDetails", jobPost);
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "job-details";
    }

    @PostMapping("/job-details/apply/{id}")
    public String per(@PathVariable("id") int id, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Job Seeker"))) {
                String username = authentication.getName();
                Users users = usersService.findByEmail(username);
                Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(users.getId());
                JobPostActivity postActivity = jobPostActivityService.getOne(id);

                JobSeekerApply jobSeekerApply = null;

                if (seekerProfile.isPresent() && postActivity != null) {
                    jobSeekerApply = new JobSeekerApply();
                    jobSeekerApply.setUserId(seekerProfile.get());
                    jobSeekerApply.setJob(postActivity);
                    jobSeekerApply.setApplyDate(new Date());
                } else {
                    throw new RuntimeException("User not found.");
                }

                JobSeekerApply saved = jobSeekerApplyService.addNew(jobSeekerApply);
            }
        }

        return "redirect:/dashboard";
    }
}

































