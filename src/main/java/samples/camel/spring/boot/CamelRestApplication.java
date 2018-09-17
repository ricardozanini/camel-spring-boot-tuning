package samples.camel.spring.boot;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "samples.camel.spring.boot")
public class CamelRestApplication {

    private static final String PROTOCOL = "AJP/1.3";
    private static final String MAX_CONN_PROP = "maxConnections";
    private static final String ACCEPT_COUNT_PROP = "acceptCount";

    @Value("${server.tomcat.ajp-port:#{null}}") // Defined on application.properties
    private Integer ajpPort;

    @Value("${server.tomcat.max-connections:#{null}}")
    private Integer maxConnections;

    @Value("${server.tomcat.accept-count:#{null}}")
    private Integer acceptCount;

    @Value("${server.tomcat.enable-apr:#{false}}")
    private Boolean enableApr;

    public static void main(String[] args) {
        SpringApplication.run(CamelRestApplication.class, args);
    }

    @Bean
    public TomcatConnectorCustomizer customizer(TomcatEmbeddedServletContainerFactory tomcat) {
        return new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                if (maxConnections != null) {
                    connector.setProperty(MAX_CONN_PROP, maxConnections.toString());
                }
                if (acceptCount != null) {
                    connector.setProperty(ACCEPT_COUNT_PROP, acceptCount.toString());
                }
            }
        };
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addConnectorCustomizers(this.customizer(tomcat));
        if (ajpPort != null) {
            Connector ajpConnector = new Connector(PROTOCOL);
            if (enableApr) {
                tomcat.addContextLifecycleListeners(new AprLifecycleListener());
                ajpConnector = new Connector("org.apache.coyote.ajp.AjpAprProtocol");
            }
            
            ajpConnector.setPort(ajpPort);
            tomcat.addAdditionalTomcatConnectors(ajpConnector);
        }

        return tomcat;
    }
}
