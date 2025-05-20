package com.example.demo.services;

import com.example.demo.entities.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {
    List<Company> getAllCompanies();
    Company getCompanyById(Long id);
    Company createCompany(Company company);
    Company updateCompany(Long id, Company updatedCompany);
    void deleteCompany(Long id);
    List<Company> searchCompaniesByName(String keyword);

}
