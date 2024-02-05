package com.example.studentattendance.database.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * Configuration class for managing multitenancy in a Spring Boot application with dynamic DataSource provisioning.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DatabaseConfiguration {
    private final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private final Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();
    private final DataSourceProperties properties;

    private AbstractRoutingDataSource multiTenantDataSource;
    private Function<String, DataSourceProperties> tenantResolver;

    @Bean
    public DataSource dataSource() {
        multiTenantDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return currentTenant.get();
            }
        };
        multiTenantDataSource.setTargetDataSources(tenantDataSources);
        multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
        multiTenantDataSource.afterPropertiesSet();
        return multiTenantDataSource;
    }

    /**
     * Adds a new tenant with the specified DataSource properties.
     *
     * @param tenantId The identifier of the new tenant.
     * @param url      The JDBC URL for the new tenant's DataSource.
     * @param username The username for connecting to the new tenant's DataSource.
     * @param password The password for connecting to the new tenant's DataSource.
     * @throws SQLException If there is an issue establishing a connection to the new DataSource.
     */
    public void addTenant(String tenantId, String url, String username, String password) throws SQLException {

        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(properties.getDriverClassName())
                .url(url)
                .username(username)
                .password(password)
                .build();

        // Check that new connection is 'live'. If not - throw exception
        try(Connection c = dataSource.getConnection()) {
            tenantDataSources.put(tenantId, dataSource);
            multiTenantDataSource.afterPropertiesSet();
        }
    }

    private DriverManagerDataSource defaultDataSource() {
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setDriverClassName(properties.getDriverClassName());
        defaultDataSource.setUrl(properties.getUrl());
        defaultDataSource.setUsername(properties.getUsername());
        defaultDataSource.setPassword(properties.getPassword());
        return defaultDataSource;
    }

    /**
     * Sets the current tenant for the current thread and dynamically adds the tenant's DataSource if it does not exist.
     *
     * @param tenantId The identifier of the tenant to set as current.
     * @throws SQLException                 If there is an issue establishing a connection to the new DataSource.
     * @throws TenantNotFoundException      If the specified tenant is not found, and no resolver is provided.
     * @throws TenantResolvingException      If there is an issue resolving DataSourceProperties for the tenant.
     */
    public void setCurrentTenant(String tenantId) throws SQLException, TenantNotFoundException, TenantResolvingException {
        if (tenantIsAbsent(tenantId)) {
            if (tenantResolver != null) {
                DataSourceProperties properties;
                try {
                    properties = tenantResolver.apply(tenantId);
                    log.debug("[d] Datasource properties resolved for tenant ID '{}'", tenantId);
                } catch (Exception e) {
                    throw new TenantResolvingException(e, "Could not resolve the tenant!");
                }

                String url = properties.getUrl();
                String username = properties.getUsername();
                String password = properties.getPassword();

                addTenant(tenantId, url, username, password);
            } else {
                throw new TenantNotFoundException(format("Tenant %s not found!", tenantId));
            }
        }
        currentTenant.set(tenantId);
        log.debug("[d] Tenant '{}' set as current.", tenantId);
    }

    /**
     * Checks if a tenant is absent in the configuration.
     *
     * @param tenantId The identifier of the tenant to check.
     * @return True if the tenant is absent, false otherwise.
     */
    public boolean tenantIsAbsent(String tenantId) {
        return !tenantDataSources.containsKey(tenantId);
    }
}
