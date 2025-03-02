package com.project.jobportal.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "job_location")
public class JobLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String city;
    private String country;
    private String state;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobLocation", targetEntity = JobPostActivity.class)
    private List<JobPostActivity> jobPostActivities;

    public JobLocation() {
    }

    public JobLocation(int id, String city, String country, String state) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<JobPostActivity> getJobPostActivities() {
        return jobPostActivities;
    }

    public void setJobPostActivities(List<JobPostActivity> jobPostActivities) {
        this.jobPostActivities = jobPostActivities;
    }

    @Override
    public String toString() {
        return "JobLocation{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
