package com.bark.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * 基于环境变量的动态数据源配置
 * 检测 MYSQL_HOST_NAME 环境变量决定使用 MySQL 还是 SQLite
 */
@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment environment;

    /**
     * MySQL 数据源配置（当存在 MYSQL_HOST_NAME 环境变量时激活）
     */
    @Bean
    @ConditionalOnProperty(name = "MYSQL_HOST_NAME")
    public DataSource mysqlDataSource() {
        String host = environment.getProperty("MYSQL_HOST_NAME");
        String port = environment.getProperty("MYSQL_PORT", "3306");
        String username = environment.getProperty("MYSQL_USERNAME", "root");
        String password = environment.getProperty("MYSQL_PASSWORD", "");
        String database = "notice-api";

        // 构建 MySQL 连接 URL
        String url = String.format(
                "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true",
                host, port, database
        );

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // HikariCP 配置优化
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        // MySQL 特定配置
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        System.out.println("═══════════════════════════════════════════");
        System.out.println("✓ 使用 MySQL 数据库");
        System.out.println("  Host: " + host + ":" + port);
        System.out.println("  Database: " + database);
        System.out.println("  Username: " + username);
        System.out.println("═══════════════════════════════════════════");
        return new HikariDataSource(config);
    }

    /**
     * SQLite 数据源配置（默认，当不存在 MYSQL_HOST_NAME 时激活）
     */
    @Bean
    @ConditionalOnProperty(name = "MYSQL_HOST_NAME", havingValue = "", matchIfMissing = true)
    public DataSource sqliteDataSource() {
        String dbPath = environment.getProperty("SQLITE_DB_PATH", "./data/notice-api.db");

        // 自动创建数据库目录
        java.io.File dbFile = new java.io.File(dbPath);
        java.io.File dbDir = dbFile.getParentFile();
        if (dbDir != null && !dbDir.exists()) {
            dbDir.mkdirs();
            System.out.println("✓ 创建数据库目录: " + dbDir.getAbsolutePath());
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + dbPath);
        config.setDriverClassName("org.sqlite.JDBC");

        // SQLite 特定配置
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(20000);

        // SQLite 特定属性
        config.addDataSourceProperty("journal_mode", "WAL");
        config.addDataSourceProperty("synchronous", "NORMAL");
        config.addDataSourceProperty("cache_size", "-64000");
        config.addDataSourceProperty("temp_store", "MEMORY");
        config.addDataSourceProperty("mmap_size", "268435456");

        System.out.println("═══════════════════════════════════════════");
        System.out.println("✓ 使用 SQLite 数据库");
        System.out.println("  Database: " + dbPath);
        System.out.println("═══════════════════════════════════════════");
        return new HikariDataSource(config);
    }

    /**
     * Flyway 配置（自动适配 MySQL 或 SQLite）
     */
    @Bean
    @Autowired
    public Flyway flyway(DataSource dataSource) {
        // 检测数据库类型
        String dbType = detectDatabaseType(dataSource);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration/" + dbType)
                .baselineOnMigrate(true)
                .validateOnMigrate(false)
                .cleanDisabled(true)
                .load();

        System.out.println("✓ 启动 Flyway 迁移 (数据库类型: " + dbType + ")");
        flyway.migrate();

        return flyway;
    }

    /**
     * 检测数据库类型
     */
    private String detectDatabaseType(DataSource dataSource) {
        try {
            String url = dataSource.getConnection().getMetaData().getURL();
            if (url.contains("mysql")) {
                return "mysql";
            } else if (url.contains("sqlite")) {
                return "sqlite";
            }
        } catch (Exception e) {
            System.err.println("⚠ 无法检测数据库类型，使用 SQLite 脚本");
        }
        return "sqlite";
    }
}
