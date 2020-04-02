package rabbit.provider;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * <p>
 *
 * </p>
 *
 * @author wanxf
 * @date 2020/04/02
 */
@EnableRabbit
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RabbitProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitProviderApplication.class, args);
    }
}
