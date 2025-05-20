package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Job;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiterId(Long recruiterId);
    List<Job> findByCategoryId(Long categoryId);
    List<Job> findByCompanyId(Long companyId);
    List<Job> findAllByOrderByCreatedAtDesc();
    List<Job> findAllByOrderByCreatedAtAsc();

    List<Job> findByCategoryIdOrderByCreatedAtDesc(Long categoryId);
    List<Job> findByCategoryIdOrderByCreatedAtAsc(Long categoryId);


}
