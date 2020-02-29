package com.joffryferrater.opadatafilterspringbootstarter.it;

import com.joffryferrater.opadatafilterspringbootstarter.model.request.PartialRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getPets() {
        PartialRequest partialRequest = new PartialRequest();
        List<Pet> all = testRepository.findAll(partialRequest);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
