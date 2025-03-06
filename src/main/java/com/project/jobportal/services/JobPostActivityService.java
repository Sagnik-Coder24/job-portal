package com.project.jobportal.services;

import com.project.jobportal.entity.*;
import com.project.jobportal.repository.JobPostActivityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository1, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.jobPostActivityRepository = jobPostActivityRepository1;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobsDto> getRecruiterJobs(int recruiter) {

        List<IRecruiterJobs> recruiterJobsDtos = jobPostActivityRepository.getRecruiterJobs(recruiter);

        List<RecruiterJobsDto> recruiterJobsDtoList = new ArrayList<>();

        for (IRecruiterJobs recruiterJobs : recruiterJobsDtos) {
            recruiterJobsDtoList.add(new RecruiterJobsDto(
                    recruiterJobs.getTotalCandidates(),
                    recruiterJobs.getJobPostId(),
                    recruiterJobs.getJobTitle(),
                    new JobLocation(recruiterJobs.getLocationId(), recruiterJobs.getCity(), recruiterJobs.getCountry(), recruiterJobs.getState()),
                    new JobCompany(recruiterJobs.getCompanyId(), null, recruiterJobs.getName())
            ));
        }

        return recruiterJobsDtoList;
    }

    public JobPostActivity getOne(int id) {
        return jobPostActivityRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found."));
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {

        System.out.println(job + " | " + location + " | " + type + " | " + remote + " | " + searchDate);

//        type = type.stream().filter(Objects::nonNull).toList();
//        remote = remote.stream().filter(Objects::nonNull).toList();

        if (searchDate == null) {
            return jobPostActivityRepository.searchWithoutDate(job, location, type, remote);
        } else {
            return jobPostActivityRepository.search(job, location, type, remote, searchDate);
        }
    }

    public void deleteAJob(int id) {
        jobPostActivityRepository.deleteById(id);
    }
}
