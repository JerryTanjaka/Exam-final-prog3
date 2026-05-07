package hei.fprog3.controller;

import hei.fprog3.dto.member.CreateMemberRequest;
import hei.fprog3.dto.payment.PaymentRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.exception.UnauthorizedException;
import hei.fprog3.service.MemberService;
import hei.fprog3.service.PaymentService;
import hei.fprog3.validator.MemberValidator;
import hei.fprog3.validator.PayementValidator;
import hei.fprog3.validator.RequestKeyValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final PayementValidator payementValidator;
    private final RequestKeyValidator requestKeyValidator;
    public MemberService memberService;
    public MemberValidator memberValidator;

    public PaymentService paymentService;

    public MemberController(MemberService memberService, MemberValidator memberValidator, PaymentService paymentService, PayementValidator payementValidator, RequestKeyValidator requestKeyValidator) {
        this.memberService = memberService;
        this.memberValidator = memberValidator;
        this.paymentService = paymentService;
        this.payementValidator = payementValidator;
        this.requestKeyValidator = requestKeyValidator;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                    @RequestBody List<CreateMemberRequest> members) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            memberValidator.validate(members);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(memberService.create(members));
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

    @PostMapping("/{id}/payments")
    public ResponseEntity<?> createPayments(@RequestHeader(required = false, value = "x-api-key") String apiKey,
                                            @PathVariable(name = "id") String id,
                                            @RequestBody List<PaymentRequest> paymentRequests) {
        try {
            requestKeyValidator.isAuthorized(apiKey);
            payementValidator.validate(paymentRequests);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(paymentService.create(id, paymentRequests));
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
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        }
    }
}
