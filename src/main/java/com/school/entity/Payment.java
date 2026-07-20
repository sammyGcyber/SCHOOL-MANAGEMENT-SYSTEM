package com.school.entity;
import jakarta.persistence.*;
import java.math.BigDecimal; import java.time.LocalDate;
@Entity public class Payment extends BaseEntity { @ManyToOne(optional=false) private Student student; @Column(nullable=false,precision=12,scale=2) private BigDecimal amount; private LocalDate paymentDate=LocalDate.now(); @Column(unique=true) private String receiptNumber; protected Payment(){} public Payment(Student s,BigDecimal a,String r){student=s;amount=a;receiptNumber=r;} public BigDecimal getAmount(){return amount;} public LocalDate getPaymentDate(){return paymentDate;} public String getReceiptNumber(){return receiptNumber;} }
