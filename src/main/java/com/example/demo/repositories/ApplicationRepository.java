package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Application;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateId(Long candidateId);
    List<Application> findByJobId(Long jobId);
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    List<Application> findByJobRecruiterId(Long recruiterId);

    @Query("""
        SELECT a FROM Application a
        WHERE a.job.recruiter.id = :recruiterId
        AND (:keyword IS NULL OR LOWER(a.job.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.candidate.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:status IS NULL OR a.status = :status)
        """)
    List<Application> searchForRecruiter(@Param("recruiterId") Long recruiterId,
                                         @Param("keyword") String keyword,
                                         @Param("status") String status);



}
