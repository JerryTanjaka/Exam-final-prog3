package hei.fprog3.service;

import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.collectivity.CollectivityResponse;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.dto.fee.FeeRequest;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.Fee;
import hei.fprog3.model.FinancialAccount;
import hei.fprog3.model.Transaction;
import hei.fprog3.repository.AccountRepository;
import hei.fprog3.repository.CollectivityRepository;
import hei.fprog3.repository.FeeRepository;
import hei.fprog3.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CollectivityService {
    private CollectivityRepository collectivityRepository;
    private TransactionRepository transactionRepository;
    private FeeRepository feeRepository;
    private AccountRepository accountRepository; // ← ajout


    public CollectivityService(CollectivityRepository collectivityRepository, TransactionRepository transactionRepository, FeeRepository feeRepository, AccountRepository accountRepository) {
        this.collectivityRepository = collectivityRepository;
        this.transactionRepository = transactionRepository;
        this.feeRepository = feeRepository;
        this.accountRepository= accountRepository;
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

    public List<Fee> getAllFees(String id) throws NotFoundException {
        return feeRepository.getAllCollectivityFees(id);
    }

    public List<Fee> createFee(String id, List<FeeRequest> feeRequests) throws NotFoundException {
        return feeRepository.create(id, feeRequests);
    }

    public CollectivityResponse findById(String id) throws NotFoundException {
        return collectivityRepository.findById(id);
    }
    public List<FinancialAccount> getFinancialAccounts(String id, LocalDate at) throws NotFoundException {
        collectivityRepository.exists(id);
        return accountRepository.findByCollectivityId(id, at);
    }
}
