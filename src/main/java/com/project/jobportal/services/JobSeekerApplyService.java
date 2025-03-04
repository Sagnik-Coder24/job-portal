package com.project.jobportal.services;

import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.entity.JobSeekerApply;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerApplyRepository.findByUserId(jobSeekerProfile);
    }

    public List<JobSeekerApply> getJobCandidates(JobPostActivity jobPostActivity) {
        return jobSeekerApplyRepository.findByJob(jobPostActivity);
    }
}
