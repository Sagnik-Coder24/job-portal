package com.project.jobportal.services;

import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.entity.JobSeekerApply;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.JobSeekerSave;
import com.project.jobportal.repository.JobSeekerApplyRepository;
import com.project.jobportal.repository.JobSeekerSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {

    private final JobSeekerSaveRepository jobSeekerSaveRepository;

    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<JobSeekerSave> getCandidatesJobs(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerSaveRepository.findByUserId(jobSeekerProfile);
    }

    public List<JobSeekerSave> getJobCandidates(JobPostActivity jobPostActivity) {
        return jobSeekerSaveRepository.findByJob(jobPostActivity);
    }

}
