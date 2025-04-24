package cn.edu.usst.cs.campusAid.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class User {
    long id;
    String name;
    long phone_number;
    float balance;
}
