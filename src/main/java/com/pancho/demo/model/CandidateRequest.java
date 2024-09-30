package com.pancho.demo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateRequest {

    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private String salaryExpected;
    private WorkModel workModel;

}
