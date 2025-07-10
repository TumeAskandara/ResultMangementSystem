package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.FeePayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeePaymentRepository extends MongoRepository<FeePayment, String> {
    List<FeePayment> findByStudentId(String studentId);
    List<FeePayment> findByFeeId(String feeId);
    List<FeePayment> findByStatus(FeePayment.PaymentStatus status);
}