package cn.vorbote.msgsender;

import cn.vorbote.message.sender.aliyun.AliyunSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MessageSenderAutoConfigure<br>
 * Created at 04/09/2022 14:32
 *
 * @author theod
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {AliyunMessageSenderProperties.class})
@ConditionalOnClass(AliyunSender.class)
@ConditionalOnProperty(value = "vorbote.msg-sender.aliyun.enabled", havingValue = "true")
public class AliyunMessageSenderAutoConfiguration {

    private final AliyunMessageSenderProperties senderProperties;

    @Autowired
    public AliyunMessageSenderAutoConfiguration(AliyunMessageSenderProperties senderProperties) {
        this.senderProperties = senderProperties;
    }

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        log.debug("Setting object mapper for Aliyun Sender");
        this.objectMapper = objectMapper;
    }

    @Bean
    public AliyunSender aliyunSender() {
        return new AliyunSender(
                senderProperties.getRegion(),
                senderProperties.getSign(),
                senderProperties.getSecretId(),
                senderProperties.getSecretKey(),
                objectMapper);
    }
}
