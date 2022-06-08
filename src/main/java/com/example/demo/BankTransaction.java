package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTransaction {
    @Id
    private Long id;
    private long accountID;
    private Date transactionDate;
    @Transient
    private String strTransactionDate;//ce champs permettra de recuperer l date contenu dans le fichier qui sera dans un format quelconque puuis de le transformer dans un format donnee et le stocker dans la variable de type date
    private String transactionType;
    private double amount;
}
