package hei.fprog3.controller;

import hei.fprog3.dto.collectivity.CollectivityIdentity;
import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.dto.fee.FeeRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.service.CollectivityService;
import hei.fprog3.validator.CollectivityValidator;
import hei.fprog3.validator.FeeValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {
    public CollectivityService collectivityService;
    public CollectivityValidator  collectivityValidator;
    public FeeValidator  feeValidator;
    public CollectivityController(CollectivityService collectivityService,  CollectivityValidator collectivityValidator,  FeeValidator feeValidator) {
        this.collectivityService = collectivityService;
        this.collectivityValidator = collectivityValidator;
        this.feeValidator = feeValidator;
    }

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

    @PutMapping("/{id}/informations")
    public ResponseEntity<?> updateInformation(@PathVariable String id, @RequestBody CollectivityInformation info) {
        try {
            collectivityValidator.validate(id, info);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.updateInfromation(id, info));
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

    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getTransactionsBetween(@PathVariable String id,
                                                    @RequestParam LocalDate from,
                                                    @RequestParam LocalDate to) {
        try {
            collectivityValidator.validateTransactionParameters(id, from, to);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.getTransactionBetween(id, from, to));
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

    @GetMapping("/{id}/membershipFees")
    public ResponseEntity<?> getMembershipFees(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(collectivityService.getAllFees(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/membershipFees")
    public ResponseEntity<?> getMembershipFees(@PathVariable String id, @RequestBody List<FeeRequest> feeRequests) {
        try {
            feeValidator.validate(feeRequests);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(collectivityService.createFee(id, feeRequests));
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
