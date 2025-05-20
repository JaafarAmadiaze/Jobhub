package com.example.demo.services;

import com.example.demo.entities.Company;
import com.example.demo.repositories.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;


    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Override
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }




    @Override
    public Company updateCompany(Long id, Company updatedCompany) {
        Company existing = getCompanyById(id);
        existing.setName(updatedCompany.getName());
        existing.setIndustry(updatedCompany.getIndustry());
        existing.setDescription(updatedCompany.getDescription()); // ✅ manquant !
        System.out.println("🛠 Mise à jour entreprise: " + existing.getName());
        System.out.println("➡️ Nouvelle description: " + updatedCompany.getDescription());

        return companyRepository.save(existing);
    }


    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);

    }

    public List<Company> searchCompaniesByName(String keyword) {
        return companyRepository.findByNameContainingIgnoreCase(keyword);
    }

}
