package hei.fprog3.controller;

import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.Collectivity;
import hei.fprog3.service.CollectivityService;
import hei.fprog3.validator.CollectivityValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {
    public CollectivityService collectivityService;
    public CollectivityValidator  collectivityValidator;
    public CollectivityController(CollectivityService collectivityService,  CollectivityValidator collectivityValidator) {}

    @PostMapping
    public ResponseEntity<?> create(@RequestBody List<CreateCollectivityRequest> collectivities) {
        try {
            collectivityValidator.validate(collectivities);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.create(collectivities));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        }
    }
}