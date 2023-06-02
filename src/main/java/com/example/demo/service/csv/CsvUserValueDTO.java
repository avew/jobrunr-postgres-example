package com.example.demo.service.csv;

import io.github.avew.CsvValue;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsvUserValueDTO extends CsvValue {

    private String username;
    private String email;
    private String firstname;
    private String lastname;

    @Override
    public String toString() {
        return "CsvUserValueDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", line='" + getLine() + '\'' +
                '}';
    }
}
