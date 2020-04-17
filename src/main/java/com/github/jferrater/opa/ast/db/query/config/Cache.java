package com.github.jferrater.opa.ast.db.query.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "opa.authorization.datasource.hibernate.cache")
@Data
@NoArgsConstructor
public class Cache {

    /**
     * Enable Hibernate second level caching
     */
    private boolean useSecondLevelCache = true;
    /**
     * Enable Hibernate query caching
     */
    private boolean useQueryCache = true;
    private Region region;

    @Data
    public static class Region {
        /**
         * The Hibernate cache region factory class to use for caching
         */
        private String factoryClass = "org.hibernate.cache.ehcache.EhCacheRegionFactory";
    }
}
