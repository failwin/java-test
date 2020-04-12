package home.yura.pivot_test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.Callable;

@SpringBootApplication
@EnableAsync
@EnableWebMvc
@EnableWebSocketMessageBroker
@EnableConfigurationProperties
public class Main implements WebSocketMessageBrokerConfigurer, AsyncConfigurer {

    private final Logger log = LoggerFactory.getLogger(Main.class);

    @Bean
    public HealthContributor rabbitHealthContributor(Map<String, RabbitTemplate> rabbitTemplates) {
        return null;
    }

//    @Bean
//    Queue queue() {
//        return new Queue("hello", false);
//    }
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost("127.0.0.1");
//        connectionFactory.setPort(5672);
//        return connectionFactory;
//    }
//
//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueues(queue());
//        container.setMessageListener(yuraMessageListener());
//        return container;
//    }
//
//    @Bean
//    MessageListener yuraMessageListener() {
//        return new YuraMessageListener();
//    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    // EnableAsync
    @Bean
    public AsyncTaskExecutor myTaskExecutor(TaskExecutorBuilder builder) {
        return builder.build();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurerConfigurer(AsyncTaskExecutor myTaskExecutor, CallableProcessingInterceptor callableProcessingInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
                configurer.setDefaultTimeout(-1);
                configurer.setTaskExecutor(myTaskExecutor);
                configurer.registerCallableInterceptors(callableProcessingInterceptor);
                WebMvcConfigurer.super.configureAsyncSupport(configurer);
            }
        };
    }

    @Bean
    public CallableProcessingInterceptor callableProcessingInterceptor() {
        return new TimeoutCallableProcessingInterceptor() {
            @Override
            public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
                log.error("timeout!");
                return super.handleTimeout(request, task);
            }
        };
    }

    // EnableWebSocketMessageBroker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/api");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("*")
            .addInterceptors(new HandshakeInterceptor() {
                @Override
                public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
                    if (serverHttpRequest instanceof ServletServerHttpRequest) {
                        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
                        HttpSession session = servletRequest.getServletRequest().getSession();
                        map.put("sessionId", session.getId());
                    }
                    return true;
                }

                @Override
                public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

                }
            })
            .withSockJS();
    }



    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
