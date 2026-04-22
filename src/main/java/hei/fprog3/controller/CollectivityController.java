package hei.fprog3.controller;

import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.dto.fees.CreateMembershipFee;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.service.CollectivityService;
import hei.fprog3.service.MembershipFeeService;
import hei.fprog3.validator.CollectivityValidator;
import hei.fprog3.validator.MembershipFeeValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {
    public CollectivityService collectivityService;
    public CollectivityValidator  collectivityValidator;
    private final MembershipFeeService membershipFeeService;
    private final MembershipFeeValidator membershipFeeValidator;
    public CollectivityController(CollectivityService cs, CollectivityValidator cv,
                                  MembershipFeeService mfs, MembershipFeeValidator mfv) {
        this.collectivityService = cs;
        this.collectivityValidator = cv;
        this.membershipFeeService = mfs;
        this.membershipFeeValidator = mfv;
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
            collectivityValidator.validateUpdate(id,info);
            return ResponseEntity.ok(collectivityService.updateInformation(id, info));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/{id}/membershipFees")
    public ResponseEntity<?> getFees(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(membershipFeeService.getFeesByCollectivity(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/membershipFees")
    public ResponseEntity<?> createFees(@PathVariable String id, @RequestBody List<CreateMembershipFee> fees) {
        try {
            membershipFeeValidator.validate(fees);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(membershipFeeService.createFees(id, fees));

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
