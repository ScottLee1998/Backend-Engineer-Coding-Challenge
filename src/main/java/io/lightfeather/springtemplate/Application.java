package io.lightfeather.springtemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lightfeather.springtemplate.model.Supervisor;


@SpringBootApplication
@RestController
public class Application {

  @RequestMapping("/")
  public String home() {
    return "Hello World";
  }

  @GetMapping("/api/supervisors")
  public List<String> getMethodName() throws URISyntaxException, MalformedURLException, IOException {

    // make get request
    URI uri = new URI("https://o3m5qixdng.execute-api.us-east-1.amazonaws.com/api/managers");
    HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
    con.setRequestMethod("GET");
    InputStream inputStream = con.getInputStream();

    // read input as string
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder result = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
        result.append(line);
    }

    // map input to list, filter, and sort
    ObjectMapper objectMapper = new ObjectMapper();
    List<Supervisor> supervisors = objectMapper.readValue(result.toString(), new TypeReference<Set<Supervisor>>() {})
                                               .stream()
                                               .filter(s -> !s.isJurisdictionNumeric())
                                               .sorted()
                                               .toList();

    // return list of strings
    List<String> output = new ArrayList<>();
    for(Supervisor supervisor : supervisors) {
      output.add(supervisor.toString()); 
    }
    return output;
  } 

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
