package com.project.jobportal.controller;

import com.project.jobportal.entity.*;
import com.project.jobportal.services.JobPostActivityService;
import com.project.jobportal.services.JobSeekerApplyService;
import com.project.jobportal.services.JobSeekerSaveService;
import com.project.jobportal.services.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {

    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @GetMapping("/dashboard")
    public String searchJobs(
            Model model,
            @RequestParam(name = "job", required = false) String job,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "partTime", required = false) String partTime,
            @RequestParam(name = "fullTime", required = false) String fullTime,
            @RequestParam(name = "freelance", required = false) String freelance,
            @RequestParam(name = "remoteOnly", required = false) String remoteOnly,
            @RequestParam(name = "officeOnly", required = false) String officeOnly,
            @RequestParam(name = "partialRemote", required = false) String partialRemote,
            @RequestParam(name = "today", required = false) boolean today,
            @RequestParam(name = "days7", required = false) boolean days7,
            @RequestParam(name = "days30", required = false) boolean days30
    ) {

        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));
        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial Remote"));
        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);
        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial Remote";
            type = false;
        }

        if (!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPost = jobPostActivityService.getAll();
        } else {
            jobPost = jobPostActivityService.search(
                    job, location, Arrays.asList(partTime, fullTime, freelance), Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate
            );
            System.out.println(jobPost);
        }


        Object currentUserProfile = usersService.getCurrentUserProfile();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                List<RecruiterJobsDto> recruiterJobs = jobPostActivityService.getRecruiterJobs(((RecruiterProfile) currentUserProfile).getUserAccountId());
                model.addAttribute("jobPost", recruiterJobs);
            } else {
                List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);
                List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);

                for (JobPostActivity singleJob : jobPost) {
                    singleJob.setIsActive(
                            jobSeekerApplyList
                                    .stream()
                                    .anyMatch(jobSeekerApply -> Objects.equals(
                                            jobSeekerApply.getJob().getJobPostId(), singleJob.getJobPostId())
                                    )
                    );

                    singleJob.setIsSaved(
                            jobSeekerSaveList
                                    .stream()
                                    .anyMatch(jobSeekerSave -> Objects.equals(
                                            jobSeekerSave.getJob().getJobPostId(), singleJob.getJobPostId())
                                    )
                    );
                }

                model.addAttribute("jobPost", jobPost);
            }
        }

        model.addAttribute("user", currentUserProfile);

        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model) {

        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "add-jobs";
    }

    @PostMapping("/dashboard/addNew")
    public String addNew(@ModelAttribute("jobPostActivity") JobPostActivity jobPostActivity, Model model) {

        Users user = usersService.getCurrentUser();
        if (user != null) {
            jobPostActivity.setPostedBy(user);
        }
        jobPostActivity.setPostedDate(new Date());

        model.addAttribute("jobPostActivity", jobPostActivity);
        JobPostActivity saved = jobPostActivityService.addNew(jobPostActivity);

        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/edit/{id}")
    public String editJobs(@PathVariable("id") int id, Model model) {

        JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);

        model.addAttribute("jobPostActivity", jobPostActivity);
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "add-jobs";
    }

    @PostMapping("/dashboard/deleteJob/{id}")
    public String deleteJob(@PathVariable("id") int id) {

        jobPostActivityService.deleteAJob(id);

        return "redirect:/dashboard";
    }

    @GetMapping("/global-search")
    public String globalSearch(
            Model model,
            @RequestParam(name = "job", required = false) String job,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "partTime", required = false) String partTime,
            @RequestParam(name = "fullTime", required = false) String fullTime,
            @RequestParam(name = "freelance", required = false) String freelance,
            @RequestParam(name = "remoteOnly", required = false) String remoteOnly,
            @RequestParam(name = "officeOnly", required = false) String officeOnly,
            @RequestParam(name = "partialRemote", required = false) String partialRemote,
            @RequestParam(name = "today", required = false) boolean today,
            @RequestParam(name = "days7", required = false) boolean days7,
            @RequestParam(name = "days30", required = false) boolean days30
    ) {

        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));
        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial Remote"));
        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);
        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial Remote";
            type = false;
        }

        if (!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPost = jobPostActivityService.getAll();
        } else {
            jobPost = jobPostActivityService.search(
                    job, location, Arrays.asList(partTime, fullTime, freelance), Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate
            );
            System.out.println(jobPost);
        }

        model.addAttribute("jobPost", jobPost);

        return "global-search";
    }
}







