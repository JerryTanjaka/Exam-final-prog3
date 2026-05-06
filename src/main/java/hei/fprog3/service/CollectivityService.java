package hei.fprog3.service;

import hei.fprog3.dto.activity.ActivityCreate;
import hei.fprog3.dto.attendance.AttendanceRequest;
import hei.fprog3.dto.attendance.AttendanceResponse;
import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.collectivity.CollectivityResponse;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.dto.fee.FeeRequest;
import hei.fprog3.dto.statistic.CollectivityOverallStatistics;
import hei.fprog3.dto.statistic.MemberStatistic;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.Activity;
import hei.fprog3.model.Fee;
import hei.fprog3.model.FinancialAccount;
import hei.fprog3.model.Transaction;
import hei.fprog3.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CollectivityService {
    private final AttendanceRepository attendanceRepository;
    private CollectivityRepository collectivityRepository;
    private TransactionRepository transactionRepository;
    private FeeRepository feeRepository;
    private AccountRepository accountRepository;
    private StatisticRepository statisticRepository;
    private ActivityRepository activityRepository;

    public CollectivityService(CollectivityRepository collectivityRepository,
                               TransactionRepository transactionRepository,
                               FeeRepository feeRepository,
                               AccountRepository accountRepository,
                               StatisticRepository statisticRepository,
                               ActivityRepository activityRepository, AttendanceRepository attendanceRepository) {
        this.collectivityRepository = collectivityRepository;
        this.transactionRepository = transactionRepository;
        this.feeRepository = feeRepository;
        this.accountRepository= accountRepository;
        this.statisticRepository = statisticRepository;
        this.activityRepository = activityRepository;
        this.attendanceRepository = attendanceRepository;
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

    public List<MemberStatistic> getMemberStatistics(String collectivityId, LocalDate from, LocalDate to) throws NotFoundException {
        collectivityRepository.exists(collectivityId);
        return statisticRepository.getCollectivityMemberStatistic(collectivityId, from, to);
    }


    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        return statisticRepository.getOverallStatistics(from, to);
    }

    public List<Activity> getAllActivities(String id) throws NotFoundException {
        collectivityRepository.exists(id);
        return activityRepository.getAllActivities(id);
    }

    public List<Activity> createActivities(String id, List<ActivityCreate> newActivities) throws NotFoundException {
        collectivityRepository.exists(id);
        return activityRepository.createActivityAndReturn(id, newActivities);
    }

    public List<AttendanceResponse> createAttendances(String id, String activityId, List<AttendanceRequest> attendances) throws BadRequestException, NotFoundException {
        collectivityRepository.exists(id);
        activityRepository.exists(activityId);
        return attendanceRepository.createAndReturn(activityId, attendances);
    }

    public List<AttendanceResponse> getActivityAttendance(String id, String activityId) throws NotFoundException {
        collectivityRepository.exists(id);
        activityRepository.exists(activityId);
        return attendanceRepository.getActivityAttendance(activityId);
    }
}
