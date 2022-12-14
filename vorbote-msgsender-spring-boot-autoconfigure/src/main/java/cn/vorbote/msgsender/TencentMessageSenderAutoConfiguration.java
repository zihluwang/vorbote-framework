package cn.vorbote.msgsender;

import cn.vorbote.message.auth.UserProfile;
import cn.vorbote.message.sender.tencent.TencentSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is a auto configurer to create a {@link TencentSender} instance.<br>
 * Created at 04/09/2022 18:47
 *
 * @author theod
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {TencentMessageSenderProperties.class})
@ConditionalOnClass(TencentSender.class)
@ConditionalOnProperty(value = "vorbote.msg-sender.tencent.enabled", havingValue = "true")
public class TencentMessageSenderAutoConfiguration {

    private final TencentMessageSenderProperties senderProperties;

    @Autowired
    public TencentMessageSenderAutoConfiguration(TencentMessageSenderProperties senderProperties) {
        this.senderProperties = senderProperties;
    }

    private ObjectMapper objectMapper;

    private OkHttpClient okHttpClient;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        log.debug("Setting ObjectMapper for Tencent Sender.");
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setOkHttpClient(OkHttpClient okHttpClient) {
        log.debug("Setting OkHttpClient for Tencent Sender.");
        this.okHttpClient = okHttpClient;
    }

    @Bean
    public TencentSender tencentSender() {
        return new TencentSender(
                senderProperties.getSign(),
                senderProperties.getAppId(),
                UserProfile.createProfile(senderProperties.getSecretId(), senderProperties.getSecretKey()),
                objectMapper,
                okHttpClient
        );
    }
}
