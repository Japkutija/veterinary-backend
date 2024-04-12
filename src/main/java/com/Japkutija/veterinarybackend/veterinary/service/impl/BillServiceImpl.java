package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.BillMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.BillDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Bill;
import com.Japkutija.veterinarybackend.veterinary.repository.BillRepository;
import com.Japkutija.veterinarybackend.veterinary.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final BillMapper billMapper;

    @Override
    @Transactional
    public Bill createBill(BillDTO billDTO) {
        var bill = billMapper.toBill(billDTO);

        return saveBill(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public Bill getBillByUuid(UUID uuid) {
        var bill = billRepository.findByUuid(uuid);

        return bill.orElseThrow(() -> new EntityNotFoundException(Bill.class, uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public Bill getBillByBillNumber(String billNumber) {

        var bill = billRepository.findByBillNumber(billNumber);

        return bill.orElseThrow(() -> new EntityNotFoundException(Bill.class, billNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bill> getBillsByOwnerUuid(String ownerUuid) {

        var bills = billRepository.findAllByOwnerUuid(ownerUuid);

        if (bills.isEmpty()) {
            return List.of();
        }

        return bills;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bill> getAllBills() {
        var bills = billRepository.findAll();

        if (bills.isEmpty()) {
            return List.of();
        }

        return bills;
    }

    @Override
    @Transactional
    public Bill saveBill(Bill bill) {
        try {
            return billRepository.save(bill);
        } catch (Exception ex) {
            throw new EntitySavingException(Bill.class, ex);
        }
    }

    @Override
    @Transactional
    public Bill updateBill(UUID uuid, BillDTO billDTO) {

        var bill = getBillByUuid(uuid);

        var updatedBill = billMapper.updateBillFromDto(billDTO, bill);

        return saveBill(updatedBill);
    }

    @Override
    @Transactional
    public void deleteBill(UUID uuid) {

        var bill = getBillByUuid(uuid);

        billRepository.delete(bill);

    }
}
