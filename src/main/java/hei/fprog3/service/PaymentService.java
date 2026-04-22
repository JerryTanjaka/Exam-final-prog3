package hei.fprog3.service;

import hei.fprog3.dto.payment.PaymentRequest;
import hei.fprog3.model.Payment;
import hei.fprog3.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> create(List<PaymentRequest> paymentRequests) {
        return paymentRepository.create(paymentRequests);
    }
}
