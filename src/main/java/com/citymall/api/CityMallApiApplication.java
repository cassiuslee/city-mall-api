package com.citymall.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author cqkir
 */
@MapperScan("com.citymall.api.module.**.mapper")
@SpringBootApplication
public class CityMallApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(CityMallApiApplication.class, args);
  }
}
