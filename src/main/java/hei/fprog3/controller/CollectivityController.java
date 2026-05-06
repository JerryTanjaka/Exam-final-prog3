package hei.fprog3.controller;

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
                                                    @RequestParam(required = false) LocalDate from,
                                                    @RequestParam(required = false) LocalDate to) {
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
    @GetMapping("/{id}/financialAccounts")
    public ResponseEntity<?> getFinancialAccounts(
            @PathVariable String id,
            @RequestParam(required = false) LocalDate at) {
        try {
            collectivityValidator.validateFinancialAccountParameters(id, at);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.getFinancialAccounts(id, at));
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getCollectivity(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(collectivityService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<?> getMembersStatistics(@PathVariable String id,
                                                  @RequestParam(required = false) LocalDate from,
                                                  @RequestParam(required = false) LocalDate to) {
        try {
            if (from == null) {
                from = LocalDate.EPOCH;
            }
            if (to == null) {
                to = LocalDate.now();
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(collectivityService.getMemberStatistics(id, from, to));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getOverallStatistics(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        try {
            if (from == null || to == null) {
                throw new BadRequestException("from and to are required");
            }
            if (to.isBefore(from)) {
                throw new BadRequestException("to must be after from");
            }
            return ResponseEntity.ok(collectivityService.getOverallStatistics(from, to));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.getAllActivities(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
