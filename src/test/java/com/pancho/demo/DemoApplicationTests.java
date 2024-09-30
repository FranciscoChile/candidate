package com.pancho.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.pancho.demo.model.Candidate;
import com.pancho.demo.model.CandidateRequest;
import com.pancho.demo.service.CandidateService;
import com.pancho.demo.web.CandidateController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pancho.demo.model.APIResponse;

@WebMvcTest(value = CandidateController.class)
class DemoApplicationTests {

	@Autowired
    private MockMvc mvc;

    @MockBean
    private CandidateService candidateService;

    @Test
    public void listAll_whenGetMethod()
            throws Exception {

        Candidate ro = new Candidate();
        Iterable<Candidate> list = List.of(ro);
        when(candidateService.findAll() ).thenReturn(list);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
				.andReturn();

        assertNotNull(result.getResponse().getContentAsString());

    }


		@Test
    	public void create_whenPostMethod()
            throws Exception {

        CandidateRequest candidateRequest = CandidateRequest.builder().name("").email("").build();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson=ow.writeValueAsString(candidateRequest);

            APIResponse apiResponse = new APIResponse();
            apiResponse.setData("response");
            apiResponse.setResponseCode(HttpStatus.OK);
            apiResponse.setMessage("Successfully executed");

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/candidate")
				.content(requestJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().is2xxSuccessful())        
				.andExpect(jsonPath("$.message", Matchers.is("Successfully executed")))
				.andReturn();
					
        assertNotNull(result.getResponse().getContentAsString());
				
        }


        @Test
    	public void deleteById_whenDeleteMethod()
            throws Exception {

        Candidate ro = Candidate.builder().id(1L).build();
        List<Candidate> list = List.of(ro);
        when(candidateService.findAll() ).thenReturn(list);

        APIResponse apiResponse = new APIResponse();
        apiResponse.setData("response");
        apiResponse.setResponseCode(HttpStatus.OK);
        apiResponse.setMessage("Successfully executed");
        ResponseEntity<APIResponse> response = new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/candidate/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.message", Matchers.is("Successfully executed")))
				.andReturn();
					
        assertNotNull(result.getResponse().getContentAsString());
				
        }


}
