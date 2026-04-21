package hei.fprog3.controller;

import hei.fprog3.dto.member.CreateMemberRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.service.MemberService;
import hei.fprog3.validator.MemberValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    public MemberService memberService;
    public MemberValidator memberValidator;
    public MemberController(MemberService memberService, MemberValidator memberValidator) {}

    @PostMapping
    // IMPORTANT: Member occupation are not fetched from database so its value is null
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
}
