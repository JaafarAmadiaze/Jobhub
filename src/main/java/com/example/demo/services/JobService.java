package com.example.demo.services;


import com.example.demo.entities.Job;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {

        List<Job> getAllJobs();
        Job getJobById(Long id);
        Job createJob(Job job);
        Job updateJob(Long id, Job updatedJob);
        void deleteJob(Long id);
        List<Job> getJobsByCategoryId(Long categoryId);
        public List<Job> getJobsByCategoryIdSorted(Long categoryId, String sort);
        public List<Job> getAllJobsSorted(String sort);


}

