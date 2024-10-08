package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company fetchCompanyById(long id) {
        Optional<Company> byId = this.companyRepository.findById(id);
        return byId.orElse(null);
    }

    public Company handleCreateCompany(Company reqCompany) {
        return this.companyRepository.save(reqCompany);
    }

    public PaginatedResultDTO fetchAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> userPage = this.companyRepository.findAll(spec, pageable);

        PaginatedResultDTO.Meta meta = new PaginatedResultDTO.Meta();
        meta.setPage(userPage.getNumber() + 1);
        meta.setPageSize(userPage.getSize());
        meta.setPages(userPage.getTotalPages());
        meta.setTotal(userPage.getTotalElements());

        PaginatedResultDTO paginatedResultDTO = new PaginatedResultDTO();
        paginatedResultDTO.setMeta(meta);
        paginatedResultDTO.setResult(userPage.getContent());

        return paginatedResultDTO;
    }

    public void handleDeleteCompany(long id) throws IdInvalidException {
        Company company = this.companyRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại công ty với id bạn nhập"));
        List<User> users = this.userRepository.findByCompany(company);
        this.userRepository.deleteAll(users);
        this.companyRepository.deleteById(id);
    }

    public Company handleUpdateCompany(Company requestCompany) {
        Optional<Company> optionalCompany = this.companyRepository.findById(requestCompany.getId());
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
//            "name": "hoidanit's company",
//            "description": "my first company",
//            "address": "hanoi",
//            "logo": "abc.png"
            company.setName(requestCompany.getName());
            company.setDescription(requestCompany.getDescription());
            company.setAddress(requestCompany.getAddress());
            company.setLogo(requestCompany.getLogo());
            company = this.companyRepository.save(company);
            return company;
        }
        return null;
    }

}
