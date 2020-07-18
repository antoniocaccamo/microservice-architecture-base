package microservices.base.proxy;

import lombok.extern.slf4j.Slf4j;
import microservices.base.proxy.security.DefaultProfileUtil;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;

@SpringBootApplication
@EnableZuulProxy
@EnableSwagger2
@ComponentScan @Slf4j
public class ProxyApplication {

	public static void main(String[] args) throws Exception {
		//SpringApplication.run(ProxyApplication.class, args);

		SpringApplication app = new SpringApplication(ProxyApplication.class);
		app.setBannerMode(Banner.Mode.CONSOLE);
		DefaultProfileUtil.addDefaultProfile(app);
		Environment env = app.run(args).getEnvironment();
		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}
		log.info("\n----------------------------------------------------------\n\t" +
						"Application '{}' is running! Access URLs:\n\t" +
						"Local: \t\t{}://localhost:{}\n\t" +
						"External: \t{}://{}:{}\n\t" +
						"Profile(s): \t{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				protocol,
				env.getProperty("server.port"),
				protocol,
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"),
				env.getActiveProfiles()
		);
	}
	
}