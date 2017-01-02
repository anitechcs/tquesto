package com.anitech.tquesto;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import com.anitech.tquesto.util.TquestoProperties;

/**
 * This is the entry point of the application
 * 
 * @author Tapas
 */
@SpringBootApplication
@EnableConfigurationProperties({ TquestoProperties.class })
public class TquestoApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(TquestoApplication.class);
	

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
	public static void main(String[] args) throws UnknownHostException {
		LOG.info("tQuesto Application Started!");
		SpringApplication application = new SpringApplication(TquestoApplication.class);
		Environment env = application.run(args).getEnvironment();
		LOG.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:{}\n\t" +
                "External: \thttp://{}:{}\n----------------------------------------------------------",
            env.getProperty("tquesto.application.name"),
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"));
	}
	
}
