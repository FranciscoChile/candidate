package com.pancho.demo.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDTO {

    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private String salaryExpected;
    private WorkModel workModel;

}
