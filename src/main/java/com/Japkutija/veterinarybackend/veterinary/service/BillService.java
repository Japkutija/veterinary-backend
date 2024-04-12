package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.BillDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Bill;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillService {

    Bill createBill(BillDTO billDTO);

    Bill getBillByUuid(UUID uuid);


    Bill getBillByBillNumber(String billNumber);

    List<Bill> getBillsByOwnerUuid(String ownerUuid);

    List<Bill> getAllBills();

    Bill saveBill(Bill bill);

    Bill updateBill(UUID uuid, BillDTO billDTO);

    void deleteBill(UUID uuid);


}
