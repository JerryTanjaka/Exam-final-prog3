package hei.fprog3.service;

import hei.fprog3.dto.financial.CreateMembershipFee;
import hei.fprog3.dto.financial.MembershipFeeResponse;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.repository.CollectivityRepository;
import hei.fprog3.repository.MembershipFeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MembershipFeeService {
    private final MembershipFeeRepository membershipFeeRepository;
    private final CollectivityRepository collectivityRepository;

    public List<MembershipFeeResponse> getFeesByCollectivity(String collectivityId) throws NotFoundException {
        collectivityRepository.findById(collectivityId);
        return membershipFeeRepository.findByCollectivityId(collectivityId);
    }

    public List<MembershipFeeResponse> createFees(String collectivityId, List<CreateMembershipFee> fees) throws NotFoundException {
        // Vérifier si la collectivité existe
        collectivityRepository.findById(collectivityId);
        return membershipFeeRepository.saveAll(collectivityId, fees);
    }
}