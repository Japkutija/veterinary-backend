package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.mapper.BillMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.BillDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Bill;
import com.Japkutija.veterinarybackend.veterinary.repository.BillRepository;
import com.Japkutija.veterinarybackend.veterinary.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final BillMapper billMapper;

    @Override
    public Bill createBill(BillDTO billDTO) {
        var bill = billMapper.toBill(billDTO);

        return saveBill(bill);
    }

    @Override
    public Bill getBillByUuid(UUID uuid) {
        var bill = billRepository.findByUuid(uuid);

        return bill.orElseThrow(() -> new EntityNotFoundException(Bill.class, uuid));
    }

    @Override
    public Bill getBillByBillNumber(String billNumber) {

        var bill = billRepository.findByBillNumber(billNumber);

        return bill.orElseThrow(() -> new EntityNotFoundException(Bill.class, billNumber));
    }

    @Override
    public List<Bill> getBillsByOwnerUuid(String ownerUuid) {

        var bills = billRepository.findAllByOwnerUuid(ownerUuid);

        if (bills.isEmpty()) {
            return List.of();
        }

        return bills;
    }

    @Override
    public List<Bill> getAllBills() {
        var bills = billRepository.findAll();

        if (bills.isEmpty()) {
            return List.of();
        }

        return bills;
    }

    @Override
    public Bill saveBill(Bill bill) {
        try {
            return billRepository.save(bill);
        } catch (Exception ex) {
            throw new EntitySavingException(Bill.class, ex);
        }
    }

    @Override
    public Bill updateBill(UUID uuid, BillDTO billDTO) {

        var bill = getBillByUuid(uuid);

        billMapper.updateBillFromDto(billDTO, bill);

        return saveBill(bill);
    }

    @Override
    public void deleteBill(UUID uuid) {

        var bill = getBillByUuid(uuid);

        billRepository.delete(bill);

    }
}
