package br.com.pegasus.api.restful.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
public class OpenApiInfoController {

    @GetMapping(value = "/openapi.yaml", produces = "application/yaml")
    public byte[] getContract() throws IOException {
        return Files.readAllBytes(new ClassPathResource("openapi/openapi.yaml").getFile().toPath());
    }
}
