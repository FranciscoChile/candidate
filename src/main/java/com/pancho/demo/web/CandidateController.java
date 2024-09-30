package com.pancho.demo.web;

import com.pancho.demo.model.APIResponse;
import com.pancho.demo.model.CandidateRequest;
import com.pancho.demo.model.User;
import com.pancho.demo.service.CandidateService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<APIResponse> findById(@PathVariable String id) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setData(candidateService.findById(Long.valueOf(id)));
        apiResponse.setResponseCode(HttpStatus.OK);
        apiResponse.setMessage("Successfully executed");

        return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<APIResponse> findAll() {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setData(candidateService.findAll());
        apiResponse.setResponseCode(HttpStatus.OK);
        apiResponse.setMessage("Successfully executed");

        return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<APIResponse> save(@RequestBody CandidateRequest candidateRequest) {

        APIResponse apiResponse = new APIResponse();
        apiResponse.setData(candidateService.save(candidateRequest));
        apiResponse.setResponseCode(HttpStatus.OK);
        apiResponse.setMessage("Successfully executed");

        return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<APIResponse> delete(@PathVariable String id) {
        candidateService.delete(Long.valueOf(id));
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(HttpStatus.OK);
        apiResponse.setMessage("Successfully executed");

        return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());
    }

    @PostMapping("/load")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<APIResponse> load() {

        candidateService.load();
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(HttpStatus.OK);
        apiResponse.setMessage("Successfully executed");

        return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return candidateService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Token> authenticate(@RequestBody User user) {
        String token = candidateService.loginAndGetToken(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(new Token(token), HttpStatus.OK);
    }

}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Token {
    private String token;
}