package hei.fprog3.controller;

import hei.fprog3.dto.member.CreateMemberRequest;
import hei.fprog3.dto.payment.PaymentRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.service.MemberService;
import hei.fprog3.service.PaymentService;
import hei.fprog3.validator.MemberValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    public MemberService memberService;
    public MemberValidator memberValidator;

    public PaymentService paymentService;

    public MemberController(MemberService memberService, MemberValidator memberValidator, PaymentService paymentService) {
        this.memberService = memberService;
        this.memberValidator = memberValidator;
        this.paymentService = paymentService;
    }

    @PostMapping
    // IMPORTANT: Occupation is set to the latest attributed one
    public ResponseEntity<?> create(@RequestBody List<CreateMemberRequest> members) {
        try {
            memberValidator.validate(members);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(memberService.create(members));
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
    public ResponseEntity<?> createPayments(@PathVariable(name = "id") String id,
                                            @RequestBody List<PaymentRequest> paymentRequests) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(paymentService.create(id, paymentRequests));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .header("Content-Type", "application/json")
                    .body(e.getMessage());
        }
    }
}
