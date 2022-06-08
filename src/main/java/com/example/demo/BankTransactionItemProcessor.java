package com.example.demo;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

//@Component //disant que c'est un composant spring
//class permettante de creer la configuration de ItemProcessor sans etat et donne l'entree et la sortie
//on effectue un traitement pour formater la date
public class BankTransactionItemProcessor implements ItemProcessor<BankTransaction,BankTransaction> {
    private SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy-HH:mm");
    //on effectue un traitement sur la donee en entree
    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
            bankTransaction.setTransactionDate(dateFormat.parse(bankTransaction.getStrTransactionDate()));

        return bankTransaction;
    }
}
