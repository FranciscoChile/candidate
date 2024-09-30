package com.pancho.demo.persistence;

import com.pancho.demo.model.Candidate;
import org.springframework.data.repository.CrudRepository;

public interface CandidateRepository extends CrudRepository<Candidate, Long>  {

}
