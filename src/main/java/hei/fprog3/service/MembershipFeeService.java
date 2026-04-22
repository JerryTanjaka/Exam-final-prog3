package hei.fprog3.service;

import hei.fprog3.dto.fees.CreateMembershipFee;
import hei.fprog3.dto.fees.MembershipFeeResponse;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.MembershipFee;
import hei.fprog3.model.enums.Frequency;
import hei.fprog3.repository.CollectivityRepository;
import hei.fprog3.repository.MembershipFeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class MembershipFeeService {
    private final MembershipFeeRepository repository;
    private final CollectivityRepository collectivityRepository;

    public List<MembershipFeeResponse> getFeesByCollectivity(String id) throws NotFoundException {
        collectivityRepository.findById(id);

        List<MembershipFee> models = repository.findByCollectivityId(id);

        return models.stream().map(this::mapToResponse).toList();
    }

    public List<MembershipFeeResponse> createFees(String id, List<CreateMembershipFee> dtos) throws NotFoundException {
        collectivityRepository.findById(id);

        List<MembershipFee> modelsToSave = dtos.stream().map(dto -> {
            MembershipFee m = new MembershipFee();
            m.setLabel(dto.getLabel());
            m.setAmount(dto.getAmount());
            m.setFrequency(Frequency.valueOf(dto.getFrequency()));
            m.setEligibleFrom(dto.getEligibleFrom());
            return m;
        }).toList();

        repository.saveAll(id, modelsToSave);

        return getFeesByCollectivity(id);
    }

    private MembershipFeeResponse mapToResponse(MembershipFee model) {
        MembershipFeeResponse res = new MembershipFeeResponse();
        res.setId(model.getId());
        res.setLabel(model.getLabel());
        res.setAmount(model.getAmount());
        res.setFrequency(String.valueOf(model.getFrequency()));
        res.setEligibleFrom(model.getEligibleFrom());
        res.setStatus(model.getStatus());
        return res;
    }
}