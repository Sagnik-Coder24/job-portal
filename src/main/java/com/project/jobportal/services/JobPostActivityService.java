package com.project.jobportal.services;

import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.repository.JobPostActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository1) {
        this.jobPostActivityRepository = jobPostActivityRepository1;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }
}
