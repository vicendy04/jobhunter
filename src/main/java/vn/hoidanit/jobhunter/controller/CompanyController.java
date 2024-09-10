package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.service.CompanyService;

import java.util.List;


@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company reqCompany) { // @Valid sẽ chạy vào GlobalException nếu bị lỗi
        Company company = this.companyService.handleCreateCompany(reqCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable("id") Long id) {
        Company foundCompany = this.companyService.fetchCompanyById(id);
        return ResponseEntity.status(HttpStatus.OK).body(foundCompany);
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getCompanies() {
        List<Company> companyList = this.companyService.fetchAllCompanies();
//        return ResponseEntity.status(HttpStatus.OK).body(companyList);
        return ResponseEntity.ok(companyList);
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
        Company updatedCompany = this.companyService.handleUpdateCompany(reqCompany);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
        this.companyService.handleDeleteCompany(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
        return ResponseEntity.ok(null);
    }
}