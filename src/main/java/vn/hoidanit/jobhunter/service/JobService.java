package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;


    public JobService(JobRepository jobRepository, SkillRepository skillRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public ResCreateJobDTO createJob(Job job) {
        List<Long> reqSkills = null;
        if (job.getSkills() != null) {
            reqSkills = job.getSkills().stream().map(Skill::getId).toList();
        }
        List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
        job.setSkills(dbSkills);

        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                job.setCompany(companyOptional.get());
            }
        }
        Job currentJob = this.jobRepository.save(job);

        return this.convertToResCreateJobDTO(currentJob);
    }

    private ResCreateJobDTO convertToResCreateJobDTO(Job currentJob) {
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());
        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(Skill::getName).toList();
            dto.setSkills(skills);
        }
        return dto;
    }

    public ResUpdateJobDTO updateJob(Job reqJob) throws IdInvalidException {
        Job dbJob = jobRepository.findById(reqJob.getId())
                .orElseThrow(() -> new IdInvalidException("Job not found"));

        List<Skill> dbSkills = null;
        if (reqJob.getSkills() != null) {
            List<Long> reqSkillsIds = reqJob.getSkills().stream()
                    .map(Skill::getId)
                    .toList();
            dbSkills = skillRepository.findByIdIn(reqSkillsIds);
        }
        dbJob.setSkills(dbSkills);
        if (reqJob.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(reqJob.getCompany().getId());
            companyOptional.ifPresent(dbJob::setCompany);
        }

        dbJob.setName(reqJob.getName());
        dbJob.setLocation(reqJob.getLocation());
        dbJob.setSalary(reqJob.getSalary());
        dbJob.setQuantity(reqJob.getQuantity());
        dbJob.setLevel(reqJob.getLevel());
        dbJob.setDescription(reqJob.getDescription());
        dbJob.setStartDate(reqJob.getStartDate());
        dbJob.setEndDate(reqJob.getEndDate());
        dbJob.setActive(reqJob.isActive());

        Job updatedJob = jobRepository.save(dbJob);

        return this.convertToResUpdateJobDTO(updatedJob);
    }

    private ResUpdateJobDTO convertToResUpdateJobDTO(Job currentJob) {
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setUpdatedAt(currentJob.getUpdatedAt());
        dto.setUpdatedBy(currentJob.getUpdatedBy());
        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream()
                    .map(Skill::getName)
                    .toList();
            dto.setSkills(skills);
        }
        return dto;
    }


    public PaginatedResultDTO fetchAll(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageUser = this.jobRepository.findAll(spec, pageable);
        PaginatedResultDTO rs = new PaginatedResultDTO();
        PaginatedResultDTO.Meta mt = new PaginatedResultDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());
        return rs;
    }

    // Get a job by ID
    public Job getJobById(Long id) throws IdInvalidException {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Job not found"));
    }

    public void deleteJob(Long id) throws IdInvalidException {
        Job j = jobRepository.findById(id).orElseThrow(() -> new IdInvalidException("Job not found"));
        jobRepository.delete(j);
    }
}
