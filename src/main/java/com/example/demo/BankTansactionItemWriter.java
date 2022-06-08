package com.example.demo;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankTansactionItemWriter implements ItemWriter<BankTransaction> {
    @Autowired
    private BankTransactionRepository bankTransactionRepository;
    //elle recevra une liste d'objet en entree et l'enregistrera dans la base de dpnee
    @Override
    public  void write(List<? extends BankTransaction> list) throws Exception {
        bankTransactionRepository.saveAll(list);//on enregistre la liste d'objet dans la base de donnee
    }
}
