package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @ApiMessage("Create a new job")
    @PostMapping
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job) {
        ResCreateJobDTO newJob = this.jobService.createJob(job);
        return ResponseEntity.ok(newJob);
    }

    @PutMapping()
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        ResUpdateJobDTO updatedJob = this.jobService.updateJob(job);
        return ResponseEntity.ok(updatedJob);
    }

    @GetMapping
    @ApiMessage("Get all jobs")
    public ResponseEntity<PaginatedResultDTO> getAllJob(
            @Filter Specification<Job> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.jobService.fetchAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Get job by Id")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) throws IdInvalidException {
        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a job by id")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) throws IdInvalidException {
        this.jobService.deleteJob(id);
        return ResponseEntity.ok(null);
    }
}
