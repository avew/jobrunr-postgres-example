package com.example.demo.service.csv;

import io.github.avew.CsvewParser;
import io.github.avew.CsvewResultReader;
import io.github.avew.reader.CsvewReader;

import java.io.InputStream;

public class CsvUserReader extends CsvewReader<CsvUserValueDTO> {

    public static final String[] HEADER = {
            "username",
            "email",
            "firstname",
            "lastname"
    };


    @Override
    public CsvewResultReader<CsvUserValueDTO> process(int startAt, InputStream is) {
        CsvewParser parse = new CsvewParser();
        return read(startAt, is, HEADER, ";", (line, columns, validations, value) -> {
            parse.parseString(line, 0, HEADER[0], columns[0], true, validations, value::setUsername);
            parse.parseEmail(line, 1, HEADER[1], columns[1], true, validations, value::setEmail);
            parse.parseString(line, 2, HEADER[2], columns[2], true, validations, value::setFirstname);
            parse.parseString(line, 3, HEADER[3], columns[3], true, validations, value::setLastname);
        });
    }

}
