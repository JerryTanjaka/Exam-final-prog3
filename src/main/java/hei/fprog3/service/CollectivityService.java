package hei.fprog3.service;

import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.collectivity.CollectivityResponse;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.repository.CollectivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectivityService {
    private CollectivityRepository collectivityRepository;

    public CollectivityService(CollectivityRepository collectivityRepository) {
        this.collectivityRepository = collectivityRepository;
    }

    public List<CollectivityResponse> create(List<CreateCollectivityRequest> collectivities) throws NotFoundException {
        return collectivityRepository.create(collectivities);
    }

    public CollectivityResponse updateInformation(String id, CollectivityInformation collectivityInformation) throws NotFoundException {
        return collectivityRepository.updateCollectivityIdentity(id, collectivityInformation);
    }

}
