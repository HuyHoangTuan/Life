package com.example.Life;

import com.example.Life.test.entity.test;
import com.example.Life.test.repo.testRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.FileSystems;
import java.util.List;

@SpringBootApplication

public class LifeApplication
{
	public static final String defaultDataDir = FileSystems.getDefault().getPath("").toAbsolutePath()+
			"\\src\\main\\java\\com\\example\\Life\\data\\static";
	public static final int ADMIN = 0;
	public static final int ARTIST = 1;
	public static final int USER = 2;

	public static void main(String[] args)
	{
		SpringApplication.run(LifeApplication.class, args);
	}

}
