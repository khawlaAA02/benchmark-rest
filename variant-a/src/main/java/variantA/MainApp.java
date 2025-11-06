package variantA;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import variantA.config.ObjectMapperProvider;
import variantA.config.LoggingExceptionMapper;
import variantA.config.ObjectMapperProvider;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class MainApp {
    public static final String BASE_URI = "http://0.0.0.0:8080/";

    public static void main(String[] args) throws Exception {
        ResourceConfig rc = new ResourceConfig()
                .packages("variantA.rest")
                .register(JacksonFeature.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdownNow();
            latch.countDown();
        }));

        System.out.println("Variant A running at " + BASE_URI + " (Ctrl+C to stop)");
        // Rester bloqué jusqu'au Ctrl+C
        latch.await();
    }
    ResourceConfig rc = new ResourceConfig()
            .packages("variantA.rest")
            .register(JacksonFeature.class)
            .register(ObjectMapperProvider.class)
            .register(LoggingExceptionMapper.class);

}