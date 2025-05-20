package com.example.demo.services;


import com.example.demo.entities.Application;
import com.example.demo.repositories.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    @Override
    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public Application updateApplication(Long id, Application updatedApplication) {
        Application existing = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        return applicationRepository.save(existing);
    }

    @Override
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    public boolean hasAlreadyApplied(Long jobId, Long candidateId) {
        return applicationRepository.existsByJobIdAndCandidateId(jobId, candidateId);
    }


    @Override
    public List<Application> getApplicationsByCandidateId(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    @Override
    public List<Application> getApplicationsForRecruiter(Long recruiterId) {
        return applicationRepository.findByJobRecruiterId(recruiterId);
    }
    @Override
    public Application updateStatus(Long id, String newStatus) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature introuvable"));
        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }
    @Override
    public Map<String, Long> getRecruiterApplicationStats(Long recruiterId) {
        List<Application> apps = applicationRepository.findByJobRecruiterId(recruiterId);

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) apps.size());
        stats.put("PENDING", apps.stream().filter(a -> "PENDING".equals(a.getStatus())).count());
        stats.put("APPROVED", apps.stream().filter(a -> "APPROVED".equals(a.getStatus())).count());
        stats.put("REJECTED", apps.stream().filter(a -> "REJECTED".equals(a.getStatus())).count());

        return stats;
    }
    @Override
    public List<Application> searchApplicationsForRecruiter(Long recruiterId, String keyword, String status) {
        if ((keyword == null || keyword.isBlank()) && (status == null || status.isBlank())) {
            return applicationRepository.findByJobRecruiterId(recruiterId);
        }

        return applicationRepository.searchForRecruiter(recruiterId, keyword, status);
    }



}
