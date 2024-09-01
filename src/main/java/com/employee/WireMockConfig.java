package com.employee;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class WireMockConfig {

    private Process wireMockProcess;

    @PostConstruct
    public void startWireMock() throws URISyntaxException, IOException, InterruptedException {
   
            URL jarUrl = getClass().getClassLoader().getResource("wiremock-standalone-3.9.1.jar");
            if (jarUrl == null) {
                throw new IllegalStateException("WireMock jar not found in classpath.");
            }
            String jarPath = Paths.get(jarUrl.toURI()).toString();
            
            URL mappingsUrl = getClass().getClassLoader().getResource("wiremock");
            if (mappingsUrl == null) {
                throw new IllegalStateException("Mappings directory not found in classpath.");
            }
            String mappingsPath = Paths.get(mappingsUrl.toURI()).toString();

            ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", jarPath, "--port", "8089", "--root-dir", mappingsPath
            );
            processBuilder.inheritIO();  // To capture output in the main process console
            wireMockProcess = processBuilder.start();
        
    }

    @PreDestroy
    public void stopWireMock() {
        if (wireMockProcess != null) {
            wireMockProcess.destroy();
        }
    }
}
