package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.PayrollGenerateDTO;
import com.example.resultmanagementsystem.Dto.PayslipDTO;
import com.example.resultmanagementsystem.Dto.SalaryStructureDTO;
import com.example.resultmanagementsystem.Dto.Repository.PayrollRecordRepository;
import com.example.resultmanagementsystem.Dto.Repository.SalaryStructureRepository;
import com.example.resultmanagementsystem.Dto.Repository.StaffRepository;
import com.example.resultmanagementsystem.model.PayrollRecord;
import com.example.resultmanagementsystem.model.SalaryStructure;
import com.example.resultmanagementsystem.model.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PayrollService {

    private final PayrollRecordRepository payrollRecordRepository;
    private final SalaryStructureRepository salaryStructureRepository;
    private final StaffRepository staffRepository;

    public PayrollRecord generatePayroll(PayrollGenerateDTO dto) {
        Staff staff = staffRepository.findById(dto.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + dto.getStaffId()));

        Optional<PayrollRecord> existing = payrollRecordRepository.findByStaffIdAndMonthAndYear(
                dto.getStaffId(), dto.getMonth(), dto.getYear());
        if (existing.isPresent()) {
            throw new RuntimeException("Payroll already generated for staff " + dto.getStaffId()
                    + " for month " + dto.getMonth() + "/" + dto.getYear());
        }

        SalaryStructure salaryStructure = salaryStructureRepository.findByDesignation(staff.getDesignation())
                .orElse(null);

        double basicSalary = salaryStructure != null ? salaryStructure.getBasicSalary() : staff.getSalary();

        Map<String, Double> allowances = new HashMap<>();
        Map<String, Double> deductions = new HashMap<>();

        if (salaryStructure != null) {
            allowances.put("Housing", salaryStructure.getHousingAllowance());
            allowances.put("Transport", salaryStructure.getTransportAllowance());
            allowances.put("Medical", salaryStructure.getMedicalAllowance());
            deductions.put("Tax", salaryStructure.getTaxDeduction());
            deductions.put("Pension", salaryStructure.getPensionDeduction());
            deductions.put("Insurance", salaryStructure.getInsuranceDeduction());
        }

        double totalAllowances = allowances.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalDeductions = deductions.values().stream().mapToDouble(Double::doubleValue).sum();
        double grossSalary = basicSalary + totalAllowances;
        double netSalary = grossSalary - totalDeductions;

        PayrollRecord payrollRecord = PayrollRecord.builder()
                .staffId(staff.getId())
                .staffName(staff.getFirstName() + " " + staff.getLastName())
                .month(dto.getMonth())
                .year(dto.getYear())
                .basicSalary(basicSalary)
                .allowances(allowances)
                .deductions(deductions)
                .totalAllowances(totalAllowances)
                .totalDeductions(totalDeductions)
                .grossSalary(grossSalary)
                .netSalary(netSalary)
                .paymentStatus(PayrollRecord.PaymentStatus.PENDING)
                .generatedAt(LocalDateTime.now())
                .build();

        return payrollRecordRepository.save(payrollRecord);
    }

    public List<PayrollRecord> generateBulkPayroll(int month, int year) {
        List<Staff> activeStaff = staffRepository.findByEmploymentStatus(Staff.EmploymentStatus.ACTIVE);
        List<PayrollRecord> generatedRecords = new ArrayList<>();

        for (Staff staff : activeStaff) {
            Optional<PayrollRecord> existing = payrollRecordRepository.findByStaffIdAndMonthAndYear(
                    staff.getId(), month, year);
            if (existing.isEmpty()) {
                PayrollGenerateDTO dto = PayrollGenerateDTO.builder()
                        .staffId(staff.getId())
                        .month(month)
                        .year(year)
                        .build();
                try {
                    generatedRecords.add(generatePayroll(dto));
                } catch (RuntimeException e) {
                    // Skip staff members that fail payroll generation
                }
            }
        }

        return generatedRecords;
    }

    public PayrollRecord getPayrollById(String id) {
        return payrollRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found with ID: " + id));
    }

    public List<PayrollRecord> getPayrollByStaff(String staffId) {
        return payrollRecordRepository.findByStaffId(staffId);
    }

    public Page<PayrollRecord> getPayrollByMonth(int month, int year, Pageable pageable) {
        return payrollRecordRepository.findByMonthAndYear(month, year, pageable);
    }

    public PayrollRecord processPayment(String id) {
        PayrollRecord payrollRecord = getPayrollById(id);

        if (payrollRecord.getPaymentStatus() != PayrollRecord.PaymentStatus.PENDING) {
            throw new RuntimeException("Payroll is not in PENDING status. Current status: " + payrollRecord.getPaymentStatus());
        }

        payrollRecord.setPaymentStatus(PayrollRecord.PaymentStatus.PROCESSED);
        payrollRecord.setPaymentDate(LocalDateTime.now());
        return payrollRecordRepository.save(payrollRecord);
    }

    public PayslipDTO getPayslip(String id) {
        PayrollRecord record = getPayrollById(id);

        return PayslipDTO.builder()
                .payrollId(record.getId())
                .staffId(record.getStaffId())
                .staffName(record.getStaffName())
                .month(record.getMonth())
                .year(record.getYear())
                .basicSalary(record.getBasicSalary())
                .allowances(record.getAllowances())
                .deductions(record.getDeductions())
                .totalAllowances(record.getTotalAllowances())
                .totalDeductions(record.getTotalDeductions())
                .grossSalary(record.getGrossSalary())
                .netSalary(record.getNetSalary())
                .paymentStatus(record.getPaymentStatus().name())
                .paymentDate(record.getPaymentDate())
                .paymentMethod(record.getPaymentMethod())
                .bankReference(record.getBankReference())
                .build();
    }

    public SalaryStructure createSalaryStructure(SalaryStructureDTO dto) {
        SalaryStructure salaryStructure = SalaryStructure.builder()
                .designation(dto.getDesignation())
                .basicSalary(dto.getBasicSalary())
                .housingAllowance(dto.getHousingAllowance())
                .transportAllowance(dto.getTransportAllowance())
                .medicalAllowance(dto.getMedicalAllowance())
                .taxDeduction(dto.getTaxDeduction())
                .pensionDeduction(dto.getPensionDeduction())
                .insuranceDeduction(dto.getInsuranceDeduction())
                .effectiveFrom(dto.getEffectiveFrom())
                .isActive(dto.isActive())
                .createdAt(LocalDateTime.now())
                .build();

        return salaryStructureRepository.save(salaryStructure);
    }

    public List<SalaryStructure> getSalaryStructures() {
        return salaryStructureRepository.findByIsActive(true);
    }

    public SalaryStructure getSalaryStructureByDesignation(String designation) {
        return salaryStructureRepository.findByDesignation(designation)
                .orElseThrow(() -> new RuntimeException("Salary structure not found for designation: " + designation));
    }
}
