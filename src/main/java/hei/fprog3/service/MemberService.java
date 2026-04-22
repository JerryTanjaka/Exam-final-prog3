package hei.fprog3.service;

import hei.fprog3.dto.member.CreateMemberRequest;
import hei.fprog3.dto.member.MemberResponse;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> create(List<CreateMemberRequest> members) throws BadRequestException, NotFoundException {
        for  (CreateMemberRequest createMemberRequest : members) {
            if (createMemberRequest.getReferees().stream()
                    .noneMatch(s -> memberRepository.isValidReferee(s, createMemberRequest.getCollectivityIdentifier()))) {
                throw new BadRequestException("Invalid referee");
            }
        }
        return memberRepository.create(members);
    }
}
