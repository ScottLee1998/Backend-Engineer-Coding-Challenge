package io.lightfeather.springtemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lightfeather.springtemplate.model.Supervisor;


@SpringBootApplication
@RestController
public class Application {

  @Value("${managers.source.endpoint}")
  private String source;

  @RequestMapping("/")
  public String home() {
    return "Hello World";
  }

  @GetMapping("/api/supervisors")
  public List<String> getMethodName() throws URISyntaxException, IOException {

    // make get request
    URI uri = new URI(source);
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

  @PostMapping("/api/submit")
  public ResponseEntity<String> createUser(@RequestParam(required = true) String firstName, @RequestParam(required = true) String lastName, @RequestParam(required = false) String email, @RequestParam(required = false) String phoneNumber, @RequestParam(required = true) String supervisor) {
    StringBuilder stringBuilder = new StringBuilder();

    // return error status code if required parameters are empty
    if(firstName.isBlank() || lastName.isBlank() || supervisor.isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required data");
    }
    // write params to console if present
    else {
      stringBuilder.append("First Name: " + firstName);
      stringBuilder.append(", Last Name: " + lastName);
      stringBuilder.append(", Supervisor: " + supervisor);
      if(email != null && !email.isBlank()) {
        stringBuilder.append(", Email: " + email);
      }
      if(phoneNumber != null && !phoneNumber.isBlank()) {
        stringBuilder.append(", Phone Number: " + phoneNumber);
      }
      System.out.println(stringBuilder.toString());

      // return success status code
      return ResponseEntity.status(HttpStatus.OK).body(stringBuilder.toString());
    }
  }


  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
