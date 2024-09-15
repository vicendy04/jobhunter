package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    private final JobRepository jobRepository;

    public SkillService(SkillRepository skillRepository, JobRepository jobRepository) {
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
    }

    public Skill createSkill(Skill reqSkill) throws IdInvalidException {
        if (this.skillRepository.existsByName(reqSkill.getName())) {
            throw new IdInvalidException("Skill đã tồn tại");
        }
        return this.skillRepository.save(reqSkill);
    }


    public Skill updateSkill(Skill reqSkill) throws IdInvalidException {
        Skill skill = this.skillRepository.findById(reqSkill.getId()).orElseThrow(() -> new IdInvalidException("Skill với id không tồn tại"));

        if (reqSkill.getName() != null && this.skillRepository.existsByName(reqSkill.getName())) {
            throw new IdInvalidException("Skill name " + reqSkill.getName() + "đã tồn tại");
        }
        //update
        skill.setName(reqSkill.getName());
        return skillRepository.save(skill);
    }

    public PaginatedResultDTO getAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> userPage = this.skillRepository.findAll(spec, pageable);

        PaginatedResultDTO paginatedResultDTO = new PaginatedResultDTO();
        PaginatedResultDTO.Meta meta = new PaginatedResultDTO.Meta();
        meta.setPage(userPage.getNumber() + 1);
        meta.setPageSize(userPage.getSize());
        meta.setPages(userPage.getTotalPages());
        meta.setTotal(userPage.getTotalElements());
        paginatedResultDTO.setMeta(meta);
        paginatedResultDTO.setResult(userPage.getContent());

        return paginatedResultDTO;
    }

    public Skill getSkillById(Long id) throws IdInvalidException {
        return this.skillRepository.findById(id).orElseThrow(() -> new IdInvalidException("Skill với id không tồn tại"));
    }

    public void deleteSkill(Long id) throws IdInvalidException {
        Skill currentSkill = this.skillRepository.findById(id).orElseThrow(() -> new IdInvalidException("Skill với id không tồn tại"));

        List<Job> associatedJobs = currentSkill.getJobs();

        associatedJobs.forEach(job -> {
            job.getSkills().remove(currentSkill);
            this.jobRepository.save(job);
        });

        this.skillRepository.delete(currentSkill);
    }
}
