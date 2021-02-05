package com.nisum.springwebflux.config;

import com.datastax.driver.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;
import uk.sky.cqlmigrate.CassandraClusterFactory;
import uk.sky.cqlmigrate.CassandraLockConfig;
import uk.sky.cqlmigrate.CqlMigrator;
import uk.sky.cqlmigrate.CqlMigratorFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

import static java.util.Objects.nonNull;

@Configuration
@EnableReactiveCassandraRepositories(basePackages = "com.nisum.springwebflux")
@Slf4j
@Profile("!test")
public class CassandraConfig extends AbstractReactiveCassandraConfiguration {

    @Value("${spring.data.cassandra.contact-points}")
    private String cassandraHost;

    @Value("${spring.data.cassandra.native_transport_port}")
    private String cassandraPort;

    @Value("${spring.data.cassandra.local-datacenter}")
    private String dc;

    @Value("${spring.data.cassandra.username}")
    private String cassandraUserName;

    @Value("${spring.data.cassandra.password}")
    private String cassandraPassword;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String cassandraKeyspace;

    @Value("${spring.data.cassandra.keyspace-replication-strategy}")
    private String cassandraKeyspaceReplicationStrategy;

    @PostConstruct
    public void migrateDbChanges(){
        if (null != getClass().getResource("/cql")) {
            debugQueryLog();
            URI uri;
            FileSystem fileSystem = null;
            Path cqlScriptsFolder;
            try {
                uri = getClass().getResource("/cql").toURI();
                if ("jar".equals(uri.getScheme())) {
                    fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    cqlScriptsFolder = fileSystem.getPath("BOOT-INF/classes/cql");
                } else {
                    cqlScriptsFolder = Paths.get(uri);
                }
                List<Path> cqlScriptsFoldersList = new ArrayList<>();
                cqlScriptsFoldersList.add(cqlScriptsFolder);
                cqlMigratePrerequisite();
                CassandraLockConfig lockConfig = CassandraLockConfig.builder()
                        .withTimeout(Duration.ofSeconds(10)).withPollingInterval(Duration.ofMillis(500)).build();
                CqlMigrator migrator = CqlMigratorFactory.create(lockConfig);

                migrator.migrate(this.cassandraHost.split(","), Integer.parseInt(this.cassandraPort),
                        this.cassandraUserName, this.cassandraPassword, this.cassandraKeyspace,
                        cqlScriptsFoldersList);
            } catch (URISyntaxException | IOException ex) {
                log.error("unable to access the filesystem", ex);
            } finally {
                if (nonNull(fileSystem))
                    try{
                        fileSystem.close();
                    }
                    catch (IOException ioe){
                        log.error("unable to close the filesystem", ioe);
                    }
            }
        }
    }

    private void debugQueryLog() {
        if (log.isDebugEnabled()) {
            try (Cluster cluster = CassandraClusterFactory.createCluster(this.cassandraHost.split(","),
                    Integer.parseInt(this.cassandraPort), this.cassandraUserName, this.cassandraPassword)) {
                QueryLogger queryLogger = QueryLogger.builder().withConstantThreshold(500).build();
                cluster.register(queryLogger);
            }
        }
    }

    private void cqlMigratePrerequisite() {
        Cluster cluster = null;
        Session session = null;
        try {
            cluster = CassandraClusterFactory.createCluster(this.cassandraHost.split(","),
                    Integer.parseInt(this.cassandraPort), this.cassandraUserName, this.cassandraPassword);
            session = cluster.connect();
            session.execute(
                    "CREATE TABLE IF NOT EXISTS cqlmigrate.locks (name text PRIMARY KEY, client text)");
            session.execute(
                    "CREATE keyspace if not exists nisum WITH replication = { 'class':'SimpleStrategy', 'replication_factor': '1' }");
        } catch (Exception e) {
            log.error("Truncate lock failed", e);
        } finally {
            if (nonNull(session))
                session.close();
            if (nonNull(cluster))
                cluster.close();
        }
    }

    @Override
    protected String getKeyspaceName() {
        return this.cassandraKeyspace;
    }

    @Override
    protected String getContactPoints() {
        return this.cassandraHost;
    }

    @Override
    protected String getLocalDataCenter() {
        return "datacenter1";
    }

    @Override
    protected int getPort() {
        return new Integer(this.cassandraPort);
    }

}