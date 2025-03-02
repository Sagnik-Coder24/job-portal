package com.project.jobportal.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "job_company")
public class JobCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String logo;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobCompany", targetEntity = JobPostActivity.class)
    private List<JobPostActivity> jobPostActivities;

    public JobCompany() {
    }

    public JobCompany(int id, String logo, String name) {
        this.id = id;
        this.logo = logo;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JobPostActivity> getJobPostActivities() {
        return jobPostActivities;
    }

    public void setJobPostActivities(List<JobPostActivity> jobPostActivities) {
        this.jobPostActivities = jobPostActivities;
    }

    @Override
    public String toString() {
        return "JobCompany{" +
                "id=" + id +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
