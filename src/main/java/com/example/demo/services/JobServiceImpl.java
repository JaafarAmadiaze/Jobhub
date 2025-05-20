package com.example.demo.services;

import com.example.demo.entities.Job;
import com.example.demo.repositories.JobRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class JobServiceImpl implements JobService {
    private  final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Override
    public Job createJob(Job job) {
         return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, Job updatedJob) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setSalary(updatedJob.getSalary());

        if (updatedJob.getCategory() != null) {
            existingJob.setCategory(updatedJob.getCategory());
        }

        if (updatedJob.getCompany() != null) {
            existingJob.setCompany(updatedJob.getCompany());
        }

        // recruteur conservé dans le contrôleur

        return jobRepository.save(existingJob);
    }


    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);

    }
    @Override
    public List<Job> getJobsByCategoryId(Long categoryId) {
        return jobRepository.findByCategoryId(categoryId);
    }

    public List<Job> getAllJobsSorted(String sort) {
        if ("oldest".equals(sort)) {
            return jobRepository.findAllByOrderByCreatedAtAsc();
        } else {
            return jobRepository.findAllByOrderByCreatedAtDesc();
        }
    }

    public List<Job> getJobsByCategoryIdSorted(Long categoryId, String sort) {
        if ("oldest".equals(sort)) {
            return jobRepository.findByCategoryIdOrderByCreatedAtAsc(categoryId);
        } else {
            return jobRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId);
        }
    }


}
