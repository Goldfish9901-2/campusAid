package cn.edu.usst.cs.campusAid.integration;

import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected UserMapper userMapper;

    private static final AtomicLong counter = new AtomicLong(0);

    public String getBaseUrl() {
        return "http://localhost:" + port;
    }

    public Long generateUniqueId() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8)) * 1000000 + 
               counter.incrementAndGet();
    }

    public String loginAndGetSessionCookie(String phoneNumber, String password) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("phoneNumber", phoneNumber);
        params.add("password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var request = new org.springframework.http.HttpEntity<>(params, headers);
        var response = restTemplate.postForEntity(
            getBaseUrl() + "/api/auth/login", request, String.class
        );
        List<String> cookies = response.getHeaders().get("Set-Cookie");
        assertNotNull(cookies);
        return cookies.stream().filter(c -> c.startsWith("JSESSIONID")).findFirst().orElse(null);
    }

    public HttpHeaders createSessionHeaders(String sessionCookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionCookie);
        return headers;
    }

    public User createTestUser(String name, Long phoneNumber) {
        User user = User.builder()
                .id(generateUniqueId())
                .name(name)
                .phoneNumber(phoneNumber)
                .build();
        userMapper.insertUser(user);
        return user;
    }
} 