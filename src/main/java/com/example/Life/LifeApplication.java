package com.example.Life;

import com.example.Life.test.entity.test;
import com.example.Life.test.repo.testRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class LifeApplication {

	@Autowired
	///private static testRepo repo;
	public static void main(String[] args)
	{
		SpringApplication.run(LifeApplication.class, args);
		///List<test> list = repo.findByUsername("Fankychop");
		///System.out.println(list.size());
	}

}
