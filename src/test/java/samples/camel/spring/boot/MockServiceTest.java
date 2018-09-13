package samples.camel.spring.boot;

import static org.junit.Assert.assertNotNull;
import samples.camel.spring.boot.model.ServiceInfo;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MockServiceTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void test() throws Exception {
		final ServiceInfo serviceInfo = restTemplate.getForObject("http://localhost:" + port + "/camel/mock", ServiceInfo.class);
		assertNotNull(serviceInfo);
		assertThat(serviceInfo.getPort()).isEqualTo(0);
	}

}
