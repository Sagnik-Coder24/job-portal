package com.project.jobportal.repository;

import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.entity.JobSeekerApply;
import com.project.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {

    List<JobSeekerApply> findByUserId(JobSeekerProfile user);

    List<JobSeekerApply> findByJob(JobPostActivity job);
}
