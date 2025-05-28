package com.example.demo.controllers;

import com.example.demo.entities.Job;
import com.example.demo.entities.User;
import com.example.demo.services.CategoryService;
import com.example.demo.services.CompanyService;
import com.example.demo.services.JobService;
import com.example.demo.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller

public class JobController {

    private final JobService jobService;
    private final CompanyService companyService;
    private final UserService userService;
    private final CategoryService categoryService;

    public JobController(JobService jobService, CompanyService companyService, UserService userService, CategoryService categoryService) {
        this.jobService = jobService;
        this.companyService = companyService;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @GetMapping("/createJob")
    public String showJobForm(Model model) {
        model.addAttribute("job", new Job());
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("recruiters", userService.getUsersByRole("RECRUITER"));
        model.addAttribute("categories", categoryService.getAllCategories()); // ✅ ajouter
        return "createJob";
    }


    @PostMapping("/saveJob")
    public String saveJob(@ModelAttribute("job") Job job) {
        // Récupérer l'utilisateur connecté dynamiquement
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User recruiter = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruteur non trouvé"));

        // Affecter ce recruteur au job
        job.setRecruiter(recruiter);
        job.setCreatedAt(LocalDateTime.now());

        jobService.createJob(job);
        return "redirect:/listJobs";
    }

    @GetMapping("/listJobs")
    public String listJobs(@RequestParam(value = "categoryId", required = false) Long categoryId,
                           @RequestParam(value = "sort", required = false, defaultValue = "newest") String sort,
                           Model model) {
        List<Job> jobs;

        if (categoryId != null) {
            jobs = jobService.getJobsByCategoryIdSorted(categoryId, sort);
        } else {
            jobs = jobService.getAllJobsSorted(sort);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("currentUserEmail", email);

        userService.findByEmail(email).ifPresent(user -> model.addAttribute("currentUserRole", user.getRole()));

        model.addAttribute("jobs", jobs);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedSort", sort); // Pour garder le tri sélectionné

        return "listJobs";
    }


    @GetMapping("/job/{id}")
    public String viewJobDetails(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("currentUserEmail", email);

        userService.findByEmail(email).ifPresent(user -> model.addAttribute("currentUserRole", user.getRole()));
        Job job = jobService.getJobById(id);
        model.addAttribute("job", job);
        return "jobDetails";
    }
    @GetMapping("/editJob")
    public String editJob(@RequestParam("id") Long id, Model model) {
        Job job = jobService.getJobById(id);

        // If the one who's connected  est un recruteur
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!job.getRecruiter().getEmail().equals(email)) {
            return "redirect:/listJobs?error=unauthorized";
        }

        model.addAttribute("job", job);
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "editJob";
    }

    @GetMapping("/deleteJob")
    public String deleteJob(@RequestParam("id") Long id) {
        Job job = jobService.getJobById(id);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!job.getRecruiter().getEmail().equals(email)) {
            return "redirect:/listJobs?error=unauthorized";
        }

        jobService.deleteJob(id);
        return "redirect:/listJobs?deleted=true";
    }

    @PostMapping("/updateJob")
    public String updateJob(@ModelAttribute("job") Job updatedJob) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Job existingJob = jobService.getJobById(updatedJob.getId());

        if (existingJob.getRecruiter() == null || !existingJob.getRecruiter().getEmail().equals(email)) {
            return "redirect:/listJobs?error=unauthorized";
        }

        // ✅ Remettre les objets complets pour Company et Category
        if (updatedJob.getCompany() != null && updatedJob.getCompany().getId() != null) {
            updatedJob.setCompany(companyService.getCompanyById(updatedJob.getCompany().getId()));
        }

        if (updatedJob.getCategory() != null && updatedJob.getCategory().getId() != null) {
            updatedJob.setCategory(categoryService.getCategoryById(updatedJob.getCategory().getId()));
        }

        updatedJob.setRecruiter(existingJob.getRecruiter()); // conserver recruteur

        jobService.updateJob(updatedJob.getId(), updatedJob);
        System.out.println("==> Update request:");
        System.out.println("Title: " + updatedJob.getTitle());
        System.out.println("Description: " + updatedJob.getDescription());
        System.out.println("Location: " + updatedJob.getLocation());
        System.out.println("Salary: " + updatedJob.getSalary());
        System.out.println("Category ID: " + (updatedJob.getCategory() != null ? updatedJob.getCategory().getId() : "null"));
        System.out.println("Company ID: " + (updatedJob.getCompany() != null ? updatedJob.getCompany().getId() : "null"));


        return "redirect:/listJobs?updated=true";
    }



}
