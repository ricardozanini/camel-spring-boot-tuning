package samples.camel.spring.boot;

import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.language.groovy.GroovyLanguage;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import samples.camel.spring.boot.model.ServiceInfo;

@Component
public class RestServiceRoute extends RouteBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceRoute.class);
	private static final String SERVICE_NAME = "mock";
		
	@Value("${server.port}")
	private Integer serverPort;
	
	@Value("${camelsvc.sleepSecOrigin}")
	private Integer sleepSecOrigin;
	
	@Value("${camelsvc.sleepSecBound}")
	private Integer sleepSecBound;
	
	@Value("${mockservice.server.port}")
	private Integer mockLocalServerPort;
	

	@Override
	public void configure() throws Exception {
		LOGGER.info("Setting up route");
		
		rest("/" + SERVICE_NAME).description("A small mock service with random response time")
			.consumes("application/json")
			.produces("application/json")
				.get()
					.description("Service information")
				.to("direct:get-service-info");
		
		from("direct:get-service-info")
			.routeId("get-service-info-route")
			.tracing()
			.log("Received request: ${in.headers}, trying to reply")
			.log("Calling mock server")
			.toF("http4://localhost:%s/data.json?bridgeEndpoint=true", mockLocalServerPort)
			.convertBodyTo(String.class)
			.log("Received data ${in.body}")
			.process(e -> {
				final ServiceInfo serviceInfo = new ServiceInfo();
				serviceInfo.setTransactionId(UUID.randomUUID().toString());
				serviceInfo.setPort(serverPort);
				serviceInfo.setServiceName(SERVICE_NAME);
				serviceInfo.setSleepSecs(ThreadLocalRandom.current().nextInt(sleepSecOrigin, sleepSecBound));
				serviceInfo.setIp(InetAddress.getLocalHost().getHostAddress());
				serviceInfo.setData(e.getIn().getBody(String.class));
				
				e.getIn().setBody(serviceInfo, ServiceInfo.class);
			})
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
			//.log("waiting ${in.body.sleepSecs} seconds to reply")
			//.delay(GroovyLanguage.groovy("exchange.in.body.sleepSecs * 1000"))
			.marshal().json(JsonLibrary.Jackson, true)
			.log("Replying with body: ${in.body}");
	}

}
