package com.foxminded.university.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.foxminded.university.dao")
@EnableConfigurationProperties(UniversityConfigProperties.class)
@EnableTransactionManagement
public class ApplicationConfig {
    
    @Bean
    public SessionFactory sessionFactory(DataSource dataSource, Properties hibernateProperties) {
        return new LocalSessionFactoryBuilder(dataSource)
                .scanPackages("com.foxminded.university.model")
                .addProperties(hibernateProperties)
                .buildSessionFactory();
    }

    @Bean
    public PlatformTransactionManager txManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
