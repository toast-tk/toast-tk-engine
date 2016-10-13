package io.toast.tk.runtime.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.guice.MongoModule;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;

public abstract class EmbeddedMongoDBTestCase {

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;
    protected static Injector injector;
    protected static final String DEFAULT_DB = "test_project_db";

    @BeforeClass
    public static void beforeEach() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        int port = 27017;
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(port, Network.localhostIsIPv6()))
                .build();

		mongodExe = starter.prepare(mongodConfig);
        mongod = mongodExe.start();
        injector = Guice.createInjector(new MongoModule("localhost", port));
    }

    @AfterClass
    public static void afterClass() throws Exception {
        if (mongod != null) {
        	mongod.stop();
            mongodExe.stop();
        }
    }
    
    public <E extends AbstractMongoDaoService<?> ,F extends IServiceFactory<E>> E getService(Class<F> factoryClass){
    	F factory = injector.getInstance(factoryClass);
		return factory.create(DEFAULT_DB);
    }
}
