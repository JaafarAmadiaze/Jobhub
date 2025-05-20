package com.example.demo.services;

import com.example.demo.entities.Application;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ApplicationService {
    List<Application> getAllApplications();
    Application getApplicationById(Long id);
    Application createApplication(Application application);
    Application updateApplication(Long id, Application updatedApplication);
    void deleteApplication(Long id);
    boolean hasAlreadyApplied(Long jobId, Long candidateId);
    List<Application> getApplicationsByCandidateId(Long candidateId);
    List<Application> getApplicationsForRecruiter(Long recruiterId);
    Application updateStatus(Long id, String newStatus);
    Map<String, Long> getRecruiterApplicationStats(Long recruiterId);
    List<Application> searchApplicationsForRecruiter(Long recruiterId, String keyword, String status);





}
