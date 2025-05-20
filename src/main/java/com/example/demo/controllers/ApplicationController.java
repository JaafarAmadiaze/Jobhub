package com.example.demo.controllers;
import com.example.demo.entities.Application;
import com.example.demo.entities.Job;
import com.example.demo.entities.User;
import com.example.demo.services.ApplicationService;
import com.example.demo.services.JobService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;
    private final UserService userService;

    public ApplicationController(ApplicationService applicationService, JobService jobService, UserService userService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.userService = userService;
    }
    @GetMapping("/apply/{jobId}")
    public String showApplicationForm(@PathVariable Long jobId, Model model) {
        Job job = jobService.getJobById(jobId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User candidate = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        model.addAttribute("job", job);
        model.addAttribute("application", new Application());
        model.addAttribute("candidate", candidate);  // ✅ ajoute le candidat

        return "applyJob";
    }

    @PostMapping("/apply")
    public String submitApplication(@ModelAttribute Application application, @RequestParam Long jobId) {
        Job job = jobService.getJobById(jobId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User candidate = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifie doublon
        if (applicationService.hasAlreadyApplied(job.getId(), candidate.getId())) {
            return "redirect:/listJobs?error=alreadyApplied";
        }

        application.setJob(job);
        application.setCandidate(candidate);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus("PENDING");

        applicationService.createApplication(application);
        return "redirect:/listJobs?success=applied";
    }




   @GetMapping("/myApplications")
   public String listMyApplications(Model model) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String email = auth.getName();
       User candidate = userService.findByEmail(email)
               .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

       model.addAttribute("currentUserRole", candidate.getRole());

       List<Application> applications = applicationService.getApplicationsByCandidateId(candidate.getId());
       model.addAttribute("applications", applications);
       return "myApplications";
   }


    @PostMapping("/cancelApplication/{id}")
    public String cancelApplication(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        applicationService.deleteApplication(id);
        redirectAttributes.addFlashAttribute("successMessage", "Candidature annulée !");
        return "redirect:/myApplications";
    }


    @GetMapping("/applicationsReceived")
    public String viewApplicationsReceived(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User recruiter = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        model.addAttribute("currentUserRole", recruiter.getRole());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        List<Application> applications = applicationService.searchApplicationsForRecruiter(recruiter.getId(), keyword, status);
        model.addAttribute("applications", applications);

        Map<String, Long> stats = applicationService.getRecruiterApplicationStats(recruiter.getId());
        model.addAttribute("stats", stats);

        return "applicationsReceived";
    }

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/updateStatus")
    public String updateApplicationStatus(@RequestParam Long id, @RequestParam String status, RedirectAttributes redirectAttributes) {
        Application app = applicationService.getApplicationById(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String recruiterEmail = auth.getName();

        User recruiter = userService.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!app.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vous n'avez pas le droit de modifier cette candidature.");
            return "redirect:/applicationsReceived";
        }

        // Mise à jour du statut
        applicationService.updateStatus(id, status);

        // ✅ Notification par email
        String candidateEmail = app.getCandidate().getEmail();
        String jobTitle = app.getJob().getTitle();
        String message = "Bonjour,\n\nVotre candidature pour le poste '" + jobTitle + "' a été " +
                (status.equals("APPROVED") ? "acceptée ✅" : "refusée ❌") +
                ".\n\nMerci pour votre intérêt.\n\nL'équipe JobHub";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(candidateEmail);
        email.setSubject("Mise à jour de votre candidature");
        email.setText(message);
        mailSender.send(email);  // ← injecte JavaMailSender dans ce contrôleur

        redirectAttributes.addFlashAttribute("successMessage", "Statut mis à jour avec succès.");
        return "redirect:/applicationsReceived";
    }






}

