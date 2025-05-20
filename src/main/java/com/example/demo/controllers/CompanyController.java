package com.example.demo.controllers;


import com.example.demo.entities.Company;
import com.example.demo.entities.Review;
import com.example.demo.entities.User;
import com.example.demo.services.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.example.demo.services.UserService;


import java.util.List;

@Controller
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;

    public CompanyController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
    }

//    @RequestMapping("/createCompany")
//    public String createCompany() {
//        return "createCompany";
//    }
@PostMapping("/saveCompany")
public String saveCompany(
        @RequestParam("name") String name,
        @RequestParam("industry") String industry,
        @RequestParam("description") String description) {

    Company company = new Company(name, industry);
    company.setDescription(description); // ✅ ne pas oublier !

    companyService.createCompany(company);
    return "redirect:/listCompanies";
}




    @RequestMapping("/listCompanies")
    public String listCompanies(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        userService.findByEmail(email).ifPresent(user ->
                model.addAttribute("currentUserRole", user.getRole())
        );

        List<Company> companies;
        if (keyword != null && !keyword.isEmpty()) {
            companies = companyService.searchCompaniesByName(keyword);
            model.addAttribute("keyword", keyword); // pour garder le mot-clé dans le champ de recherche
        } else {
            companies = companyService.getAllCompanies();
        }

        model.addAttribute("companies", companies);
        return "companyList";
    }


    @GetMapping("/editCompany")
    public String editCompany(@RequestParam("id") Long id, Model model) {
        Company company = companyService.getCompanyById(id);
        model.addAttribute("company", company);
        return "editCompany"; // JSP pour modifier
    }

    @PostMapping("/updateCompany")
    public String updateCompany(@ModelAttribute("company") Company updatedCompany) {
        companyService.updateCompany(updatedCompany.getId(), updatedCompany);
        return "redirect:/listCompanies";
    }

    @GetMapping("/deleteCompany")
    public String deleteCompany(@RequestParam("id") Long id) {
        companyService.deleteCompany(id);
        return "redirect:/listCompanies"; // ✅ pas "deleteCompany"
    }

    @GetMapping("/company/{id}")
    public String viewCompanyDetails(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        userService.findByEmail(email).ifPresent(user ->
                model.addAttribute("currentUserRole", user.getRole())
        );
        Company company = companyService.getCompanyById(id);

        model.addAttribute("company", company);
        model.addAttribute("jobs", company.getJobs());
        model.addAttribute("reviews", company.getReviews());

        model.addAttribute("newReview", new Review()); // pour formulaire

        return "companyDetails";
    }
    @GetMapping("/createCompany")
    public String showCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "createCompany";
    }



}
