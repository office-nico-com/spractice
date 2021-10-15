package com.office_nico.spractice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.office_nico.spractice.web.dialect.CustomDialect;

@Configuration
public class ThymeleafConfiguration {

	@Bean
    CustomDialect myDialect() {
        return new CustomDialect();
    }
}
