package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company fetchCompanyById(long id) {
        Optional<Company> byId = this.companyRepository.findById(id);
        return byId.orElse(null);
    }

    public Company handleCreateCompany(Company reqCompany) {
        return this.companyRepository.save(reqCompany);
    }

    public Company handleUpdateCompany(Company requestCompany) {
        Company company = this.fetchCompanyById(requestCompany.getId());
        if (company != null) {
//            "name": "hoidanit's company",
//            "description": "my first company",
//            "address": "hanoi",
//            "logo": "abc.png"
            company.setName(requestCompany.getName());
            company.setDescription(requestCompany.getDescription());
            company.setAddress(requestCompany.getAddress());
            company.setLogo(requestCompany.getLogo());
            company = this.companyRepository.save(company);
        }
        return company;
    }

}
