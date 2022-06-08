package com.example.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;


//classe de configuration du Job
@Configuration
//@Lazy
@EnableBatchProcessing
public class SpringBatchProcessing {

    @Autowired  private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ItemReader<BankTransaction> bankTransactionItemReader; //on precise a ItemReader le type de donnee qui sera charger en entree
    @Autowired private ItemWriter<BankTransaction> bankTransactionItemWriter; //on precise a ItemWriter le type de donnee qu'on aura en sortie

    //@Autowired  private ItemProcessor<BankTransaction,BankTransaction> bankTransactionItemProcessor; //a Itemprocessor on precise le type de doneee en entree et en sortie du traitement

    //methode configuration permettant de retourner un JOB
    @Bean
    public Job myJob()
    {
        //creation du step et de ses parametres

        Step step1=stepBuilderFactory.get("step-load")  //on precise le nom du step
                .<BankTransaction,BankTransaction>chunk(100)
                .reader(bankTransactionItemReader)
                .processor(compositeItemprocessor())
                .writer(bankTransactionItemWriter)
                .build();

        return jobBuilderFactory.get("bank-data-loader-job") //cretaion du job et on precise son nom
                .start(step1).build();//on demarre le step1
    }




    //methode qui permet de donnee l'entree a l'ItemReader. entree comme les fihier txt et csv
    @Bean
    public FlatFileItemReader<BankTransaction> fileItemReader(@Value("${inputFile}") Resource inputFile)//avec l'annotation on injecte la propriete qui se trouve dans le fichier application.properties.representant le chemin vers le fichier data.csv
    {
        FlatFileItemReader<BankTransaction> fileItemReader=new FlatFileItemReader<>();
        fileItemReader.setName("FFIR1");//on precise son nom
        fileItemReader.setLinesToSkip(1);//on lira le fichier ligne par ligne
        fileItemReader.setResource(inputFile);//on donne les donnees en e ntrees
        fileItemReader.setLineMapper(lineMappe());
        return fileItemReader;
    }

    //methode por instancir le processor1
    @Bean
    public ItemProcessor<BankTransaction, BankTransaction> itemprocessor1() {
        return new BankTransactionItemProcessor();
    }
    //methode pour instancier le processort 2
    @Bean
    public ItemProcessor<BankTransaction, BankTransaction> itemprocessor2() {
        return new BankTransactionItemAnalyticsProcessor();
    }
    public ItemProcessor<BankTransaction, BankTransaction> compositeItemprocessor() {
        List<ItemProcessor<BankTransaction,BankTransaction>> itemProcessors=new ArrayList<>();
        itemProcessors.add(itemprocessor1());
        itemProcessors.add(itemprocessor2());

        CompositeItemProcessor<BankTransaction,BankTransaction> compositeItemProcessor=new
                CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(itemProcessors);
        return compositeItemProcessor;
    }
    @Bean
    public LineMapper<BankTransaction> lineMappe()
    {
        DefaultLineMapper<BankTransaction> lineMapper =new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");//on precise que les donnees sont separes par les virgules
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id","accountID","strTransactionDate","transactionType","amount");//on precise de facon ordonnee les attributs vers lesquels chaque donnee du fichier data.csv se stockera
        lineMapper.setLineTokenizer(lineTokenizer);//on charge le tokenizer dans lineMapper
        BeanWrapperFieldSetMapper fieldSetMapper=new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(BankTransaction.class);//on precise le type de variable cible apres traitement
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }


}
