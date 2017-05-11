package com.diting.bootstrap;

import com.diting.core.SecurityFilter;
import com.diting.core.SessionFilter;
//import com.sun.jersey.api.core.ResourceConfig;
import io.dropwizard.Application;
import io.dropwizard.configuration.UrlConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.Path;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * DiTingApplication.
 */
public class DiTingApplication extends Application<DiTingConfiguration> {
    private static final String DEFAULT_SERVER_ROLE = "onebox";

    private static final Set<String> AVAILABLE_SERVER_ROLES = new HashSet<String>() {{
        add("onebox");
        add("cronbox");
        add("appbox");
    }};

    private static final Logger LOGGER = LoggerFactory.getLogger(DiTingApplication.class);

    @Override
    public void initialize(Bootstrap<DiTingConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new UrlConfigurationSourceProvider());
    }

    @Override
    public void run(DiTingConfiguration diTingConfiguration, Environment environment) throws Exception {
        // register CORS filter
        enableCors(environment);

        // load spring beans
        loadSpringBeans(diTingConfiguration, environment);

//        environment.servlets().addFilter("",new SessionFilter());
//        environment.jersey().getResourceConfig().getContainerRequestFilters().add(new SessionFilter());
        environment.jersey().getResourceConfig().register(SecurityFilter.class);
        environment.jersey().getResourceConfig().register(SessionFilter.class);
    }

    private ApplicationContext loadSpringBeans(DiTingConfiguration diTingConfiguration, Environment environment) {
        //init Spring context
        //before we init the app context, we have to create a parent context with all the config objects others rely on to get initialized
        AnnotationConfigWebApplicationContext parent = new AnnotationConfigWebApplicationContext();
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

        parent.refresh();
        parent.getBeanFactory().registerSingleton("configuration", diTingConfiguration);
        parent.registerShutdownHook();
        parent.start();

        //the real main app context has a link to the parent context
        ctx.setParent(parent);
        ctx.register(DiTingConfiguration.class);
        ctx.refresh();
        ctx.registerShutdownHook();
        ctx.start();

        //resources
        Map<String, Object> resources = ctx.getBeansWithAnnotation(Path.class);
        for (Map.Entry entry : resources.entrySet()) {
            environment.jersey().register(entry.getValue());
        }

        //last, but not least,let's link Spring to the embedded Jetty in Dropwizard
        environment.servlets().addServletListeners(new SpringContextLoaderListener(ctx));

        return ctx;
    }
    private void enableCors(Environment environment) {
        // Enable CORS headers
        FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    private static String parseServerRole(String[] args) {
        if (args == null || args.length == 0)
            return DEFAULT_SERVER_ROLE;

        String serverRole = args[0];
        if (!AVAILABLE_SERVER_ROLES.contains(serverRole)) {
            throw new IllegalArgumentException("Invalid server role [" + serverRole + "] specified.");
        }

        return serverRole;
    }

    public static void main(String[] args) throws Exception {
        String serverRole = parseServerRole(args);
        LOGGER.info("Server role is [" + serverRole + "].");

        System.setProperty("serverRole", serverRole);

        // changing from URL#getPath() to URL#toString(), together with UrlConfigurationSourceProvider ( default is FileConfigurationSourceProvider)
        // to allow read yaml from inside jar file
        String configFile = DiTingApplication.class.getResource("/diting_bootstrap.yaml").toString();
        args = new String[]{"server", configFile};

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.SLF4JLogger");
        System.setProperty("logback.configurationFile", "diting-logback.xml");
        System.setProperty("logDir", "/tmp");

        LOGGER.info("Start backend service...");
        new DiTingApplication().run(args);
        LOGGER.info("Backend service started successfully.");
    }
}
