package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    @ApiMessage("Create new skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Skill newSkill = skillService.createSkill(skill);
        return ResponseEntity.ok(newSkill);
    }


    @PutMapping()
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skillDetails) throws IdInvalidException {
        Skill updatedSkill = skillService.updateSkill(skillDetails);
        return ResponseEntity.ok(updatedSkill);
    }

    @GetMapping
    @ApiMessage("Get all skills")
    public ResponseEntity<PaginatedResultDTO> getAllSkills(@Filter Specification<Skill> spec, Pageable pageable) {
        PaginatedResultDTO allSkills = skillService.getAllSkills(spec, pageable);
        return ResponseEntity.ok(allSkills);
    }

    @GetMapping("/{id}")
    @ApiMessage("Get a skill by Id")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) throws IdInvalidException {
        Skill skill = skillService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) throws IdInvalidException {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
