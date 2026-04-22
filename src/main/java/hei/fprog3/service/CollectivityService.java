package hei.fprog3.service;

import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.collectivity.CollectivityResponse;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.Transaction;
import hei.fprog3.repository.CollectivityRepository;
import hei.fprog3.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CollectivityService {
    private CollectivityRepository collectivityRepository;
    private TransactionRepository transactionRepository;

    public CollectivityService(CollectivityRepository collectivityRepository, TransactionRepository transactionRepository) {
        this.collectivityRepository = collectivityRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<CollectivityResponse> create(List<CreateCollectivityRequest> collectivities) throws NotFoundException {
        return collectivityRepository.create(collectivities);
    }

    public CollectivityResponse updateInfromation(String id, CollectivityInformation information) throws NotFoundException {
        return collectivityRepository.updateCollectivityInformation(id, information);
    }

    public List<Transaction> getTransactionBetween(String id, LocalDate from, LocalDate to) throws NotFoundException {
        return transactionRepository.getTransactionBetween(id, from, to);
    }
}
