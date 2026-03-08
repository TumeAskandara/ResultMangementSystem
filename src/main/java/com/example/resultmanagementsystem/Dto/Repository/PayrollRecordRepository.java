package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.PayrollRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRecordRepository extends MongoRepository<PayrollRecord, String> {

    List<PayrollRecord> findByStaffId(String staffId);

    Page<PayrollRecord> findByMonthAndYear(int month, int year, Pageable pageable);

    List<PayrollRecord> findByPaymentStatus(PayrollRecord.PaymentStatus paymentStatus);

    Optional<PayrollRecord> findByStaffIdAndMonthAndYear(String staffId, int month, int year);
}
