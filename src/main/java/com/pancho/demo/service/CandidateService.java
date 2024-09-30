package com.pancho.demo.service;

import com.pancho.demo.config.TokenProvider;
import com.pancho.demo.model.*;
import com.pancho.demo.persistence.AuthorityRepository;
import com.pancho.demo.persistence.CandidateRepository;
import com.pancho.demo.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CandidateService {

    private static final String AUTHORITY_USER = "ROLE_USER";

    CandidateRepository candidateRepository;
    AuthorityRepository authorityRepository;
    UserRepository userRepository;
    TokenProvider tokenProvider;
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;

    public Optional<CandidateDTO> findById(Long id) {
        return candidateRepository.findById(id).map(
                c -> new CandidateDTO(
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getGender(),
                        c.getSalaryExpected(),
                        c.getWorkModel()));
    }

    public Iterable<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public CandidateDTO save(CandidateRequest cr) {

        Candidate candidate = Candidate.builder().
                id(cr.getId()).
                name(cr.getName()).
                email(cr.getEmail()).
                gender(cr.getGender()).
                salaryExpected(cr.getSalaryExpected()).
                workModel(cr.getWorkModel()).build();

        Candidate c = candidateRepository.save(candidate);

        return new CandidateDTO(
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getGender(),
                        c.getSalaryExpected(),
                        c.getWorkModel());

    }

    public void delete(Long id) {
        candidateRepository.deleteById(id);
    }



    public void load() {

        Candidate c1 = Candidate.builder().name("pancho").email("email").gender(Gender.MALE).workModel(WorkModel.REMOTE).salaryExpected("3000").build();
        candidateRepository.save(c1);

        Candidate c2 = Candidate.builder().name("antonio").email("email").gender(Gender.MALE).workModel(WorkModel.REMOTE).salaryExpected("3000").build();
        candidateRepository.save(c2);

        Candidate c3 = Candidate.builder().name("luis").email("email").gender(Gender.MALE).workModel(WorkModel.REMOTE).salaryExpected("3000").build();
        candidateRepository.save(c3);

        Candidate c4 = Candidate.builder().name("jose").email("email").gender(Gender.MALE).workModel(WorkModel.PRESENTIAL).salaryExpected("3000").build();
        candidateRepository.save(c4);

        Candidate c5 = Candidate.builder().name("vero").email("email").gender(Gender.FEMALE).workModel(WorkModel.PRESENTIAL).salaryExpected("3000").build();
        candidateRepository.save(c5);


    }

    public User registerUser(User user) {

        Optional<User> userCreated = userRepository.findByUsername(user.getUsername());
        if (userCreated.isPresent()) {
            throw new UsernameNotFoundException("User exists");
        }

        Set<Authority> authorities = new HashSet<>(Arrays.asList(getAuthority(AUTHORITY_USER)));
        user.setAuthorities(authorities);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public String loginAndGetToken(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return tokenProvider.createToken(authentication);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    private Authority getAuthority(String authority) {
        return authorityRepository.findByAuthority (authority)
                .orElse(authorityRepository.save(Authority.builder().authority(authority).build()));
    }

}
