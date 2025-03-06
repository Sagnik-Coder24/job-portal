package com.project.jobportal.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "job_post_activity")
public class JobPostActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobPostId;

    @Length(max = 10000)
    private String descriptionOfJob;

    private String jobTitle;
    private String jobType;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;

    private String remote;
    private String salary;

    @ManyToOne
    @JoinColumn(name = "posted_by_id", referencedColumnName = "user_id")
    private Users postedBy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_location_id", referencedColumnName = "id")
    private JobLocation jobLocation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_company_id", referencedColumnName = "id")
    private JobCompany jobCompany;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "job")
    private List<JobSeekerApply> jobSeekerApplyList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "job")
    private List<JobSeekerSave> jobSeekerSaveList;

    @Transient
    private boolean isActive;

    @Transient
    private boolean isSaved;

    public JobPostActivity() {
    }

    public JobPostActivity(Integer jobPostId, String descriptionOfJob, String jobTitle, String jobType, Date postedDate, String remote, String salary, Users postedBy, JobLocation jobLocation, JobCompany jobCompany, List<JobSeekerApply> jobSeekerApplyList, List<JobSeekerSave> jobSeekerSaveList, boolean isActive, boolean isSaved) {
        this.jobPostId = jobPostId;
        this.descriptionOfJob = descriptionOfJob;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.postedDate = postedDate;
        this.remote = remote;
        this.salary = salary;
        this.postedBy = postedBy;
        this.jobLocation = jobLocation;
        this.jobCompany = jobCompany;
        this.jobSeekerApplyList = jobSeekerApplyList;
        this.jobSeekerSaveList = jobSeekerSaveList;
        this.isActive = isActive;
        this.isSaved = isSaved;
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public String getDescriptionOfJob() {
        return descriptionOfJob;
    }

    public void setDescriptionOfJob(String descriptionOfJob) {
        this.descriptionOfJob = descriptionOfJob;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public Users getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Users postedBy) {
        this.postedBy = postedBy;
    }

    public JobLocation getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(JobLocation jobLocation) {
        this.jobLocation = jobLocation;
    }

    public JobCompany getJobCompany() {
        return jobCompany;
    }

    public void setJobCompany(JobCompany jobCompany) {
        this.jobCompany = jobCompany;
    }

    public List<JobSeekerApply> getJobSeekerApplyList() {
        return jobSeekerApplyList;
    }

    public void setJobSeekerApplyList(List<JobSeekerApply> jobSeekerApplyList) {
        this.jobSeekerApplyList = jobSeekerApplyList;
    }

    public List<JobSeekerSave> getJobSeekerSaveList() {
        return jobSeekerSaveList;
    }

    public void setJobSeekerSaveList(List<JobSeekerSave> jobSeekerSaveList) {
        this.jobSeekerSaveList = jobSeekerSaveList;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean saved) {
        isSaved = saved;
    }

    @Override
    public String toString() {
        return "JobPostActivity{" +
                "jobPostId=" + jobPostId +
                ", descriptionOfJob='" + descriptionOfJob + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobType='" + jobType + '\'' +
                ", postedDate=" + postedDate +
                ", remote='" + remote + '\'' +
                ", salary='" + salary + '\'' +
                ", postedBy=" + postedBy +
                ", jobLocation=" + jobLocation +
                ", jobCompany=" + jobCompany +
                ", isActive=" + isActive +
                ", isSaved=" + isSaved +
                '}';
    }
}