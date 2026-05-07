package hei.fprog3.controller;

import hei.fprog3.dto.activity.ActivityCreate;
import hei.fprog3.dto.attendance.AttendanceRequest;
import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.dto.fee.FeeRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.exception.UnauthorizedException;
import hei.fprog3.service.CollectivityService;
import hei.fprog3.validator.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {
    private final ActivityValidator activityValidator;
    private final AttendanceValidator attendanceValidator;
    private final RequestKeyValidator requestKeyValidator;
    public CollectivityService collectivityService;
    public CollectivityValidator  collectivityValidator;
    public FeeValidator  feeValidator;
    public CollectivityController(CollectivityService collectivityService,
                                  CollectivityValidator collectivityValidator,
                                  FeeValidator feeValidator,
                                  ActivityValidator activityValidator,
                                  AttendanceValidator attendanceValidator,
                                  RequestKeyValidator requestKeyValidator) {
        this.collectivityService = collectivityService;
        this.collectivityValidator = collectivityValidator;
        this.feeValidator = feeValidator;
        this.activityValidator = activityValidator;
        this.attendanceValidator = attendanceValidator;
        this.requestKeyValidator = requestKeyValidator;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                    @RequestBody List<CreateCollectivityRequest> collectivities) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            collectivityValidator.validate(collectivities);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.create(collectivities));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
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
    public ResponseEntity<?> updateInformation(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                               @PathVariable String id,
                                               @RequestBody CollectivityInformation info) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            collectivityValidator.validate(id, info);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.updateInfromation(id, info));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
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
    public ResponseEntity<?> getTransactionsBetween(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                                    @PathVariable String id,
                                                    @RequestParam(required = false) LocalDate from,
                                                    @RequestParam(required = false) LocalDate to) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            collectivityValidator.validateTransactionParameters(id, from, to);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.getTransactionBetween(id, from, to));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
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
    public ResponseEntity<?> getMembershipFees(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                               @PathVariable String id) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(collectivityService.getAllFees(id));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/membershipFees")
    public ResponseEntity<?> getMembershipFees(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                               @PathVariable String id,
                                               @RequestBody List<FeeRequest> feeRequests) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            feeValidator.validate(feeRequests);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(collectivityService.createFee(id, feeRequests));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
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
            @RequestHeader(required = false, value = "x-api-key") String apiKey,
            @PathVariable String id,
            @RequestParam(required = false) LocalDate at) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            collectivityValidator.validateFinancialAccountParameters(id, at);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.getFinancialAccounts(id, at));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
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
    public ResponseEntity<?> getCollectivity(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                             @PathVariable String id) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(collectivityService.findById(id));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<?> getMembersStatistics(@RequestHeader(required = false, value = "x-api-key") String apiKey, @PathVariable String id,
                                                  @RequestParam(required = false) LocalDate from,
                                                  @RequestParam(required = false) LocalDate to) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            if (from == null || to == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .header("Content-Type", "application/json")
                        .body("From and to cannot be null");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(collectivityService.getMemberStatistics(id, from, to));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
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
            @RequestHeader(required = false, value = "x-api-key") String apiKey,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            if (from == null || to == null) {
                throw new BadRequestException("from and to are required");
            }
            if (to.isBefore(from)) {
                throw new BadRequestException("to must be after from");
            }
            return ResponseEntity.ok(collectivityService.getOverallStatistics(from, to));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<?> getActivities(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                           @PathVariable String id) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.getAllActivities(id));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<?> createActivities(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                              @PathVariable String id,
                                              @RequestBody List<ActivityCreate> activities) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            activityValidator.validate(activities);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.createActivities(id, activities));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> createActivities(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                              @PathVariable String id,
                                              @PathVariable String  activityId,
                                              @RequestBody List<AttendanceRequest> attendances) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            attendanceValidator.validate(attendances);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.createAttendances(id, activityId, attendances));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> getActivityAttendance(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                                   @PathVariable String id,
                                                   @PathVariable String  activityId) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(collectivityService.getActivityAttendance(id, activityId));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
