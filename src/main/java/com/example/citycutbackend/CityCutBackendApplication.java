package com.example.citycutbackend;

import com.example.citycutbackend.Test030525.IAmTestingMyself;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CityCutBackendApplication {


    public static void main(String[] args) {
        SpringApplication.run(CityCutBackendApplication.class, args);

        //lombok test
        IAmTestingMyself iam = new IAmTestingMyself();
        iam.setName("Mette");
        System.out.println("Navn: "+iam.getName());
        System.out.println("Beregning: "+iam.mathing());


    }



}
