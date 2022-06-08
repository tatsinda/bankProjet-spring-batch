package com.example.demo;

import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

//@Component //disant que c'est un composant spring
//class permettante de creer la configuration de ItemProcessor avec etat et donne l'entree et la sortie
//dans ce processor on effectue un traitement de calcul
public class BankTransactionItemAnalyticsProcessor implements ItemProcessor<BankTransaction,BankTransaction> {
   @Getter private double totalDebit;
   @Getter private double totalCredit;
    //on effectue un traitement sur la donee en entree
    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        if (bankTransaction.getTransactionType().equals("D")) totalDebit+=bankTransaction.getAmount();
        else if (bankTransaction.getTransactionType().equals("C")) totalCredit+=bankTransaction.getAmount();

        return bankTransaction;
    }
}
