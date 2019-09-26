package com.xteamstudio.exam.oms.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EntityScan(basePackages = {"com.xteamstudio.exam.oms.entity"})
@EnableJpaRepositories(basePackages =
        {"com.xteamstudio.exam.oms.lottery.repository"},
        entityManagerFactoryRef = "lotteryEntityManager",
        transactionManagerRef = "lotteryTransactionManager")
//@Profile("test")
public class JpaConfig {

    private static final Logger logger = LoggerFactory.getLogger(JpaConfig.class);


    /**
     * test datasource.
     *
     * @return datasource
     */
    @Bean
    public DataSource getDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:h2:mem:testdb");
        hikariDataSource.setDriverClassName("org.h2.Driver");
        hikariDataSource.setUsername("t");
        hikariDataSource.setPassword("t");
        hikariDataSource.setMaximumPoolSize(2);


        // schema init
        Resource initSchema = new ClassPathResource("data.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, hikariDataSource);

        return hikariDataSource;
    }

    /**
     * test entityManager.
     *
     * @return factory of my test entity manager
     */
    @Bean(name = "lotteryEntityManager")
    public LocalContainerEntityManagerFactoryBean productEntityManager() {
        final LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDataSource());
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setPackagesToScan("com.xteamstudio.exam.oms.entity");
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        em.setJpaPropertyMap(properties);
        return em;
    }

    /**
     * transaction manager bean.
     *
     * @return transaction manager bean
     */
    @Bean(name = "lotteryTransactionManager")
    public PlatformTransactionManager productTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(productEntityManager().getObject());
        return transactionManager;
    }

    /**
     * webFlux transaction annotation is not work, use transaction template instead.
     *
     * @return my TransactionTemplate
     */
    @Bean(name = "lotteryTransactionTemplate")
    public TransactionTemplate newTransactionTemplate() {
        return new TransactionTemplate(productTransactionManager());
    }

}