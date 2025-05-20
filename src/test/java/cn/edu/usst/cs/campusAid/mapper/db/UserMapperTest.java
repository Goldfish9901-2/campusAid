package cn.edu.usst.cs.campusAid.mapper.db;

import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.model.User;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(UserMapperTest.class);

    @Autowired
    private UserMapper userMapper;

    private static final String TEST_USER_NAME = "test_user";
    private static final long TEST_PHONE_NUMBER = 13800138000L;
    private static final Random random = new Random();

    private Long generateTestUserId() {
        // 生成一个随机的9位数ID
        return 1000000000L + random.nextInt(900000000);
    }

    @Test
    @DisplayName("测试用户注册和查询")
    void testRegisterAndQuery() {
        // 1. 构建测试用户
        Long testUserId = generateTestUserId();
        User testUser = User.builder()
                .id(testUserId)
                .name(TEST_USER_NAME)
                .phoneNumber(TEST_PHONE_NUMBER)
                .build();

        // 2. 插入用户
        userMapper.insertUser(testUser);
        logger.info("插入测试用户成功，ID: {}", testUserId);

        // 3. 通过ID查询
        User foundById = userMapper.getUserById(testUserId);
        assertNotNull(foundById, "通过ID查询用户不应返回null");
        assertEquals(TEST_USER_NAME, foundById.getName(), "用户名不匹配");
        assertEquals(TEST_PHONE_NUMBER, foundById.getPhoneNumber(), "手机号不匹配");
    }

    @Test
    @DisplayName("测试重复注册")
    void testDuplicateRegistration() {
        // 1. 插入测试用户
        Long testUserId = generateTestUserId();
        User testUser = User.builder()
                .id(testUserId)
                .name(TEST_USER_NAME)
                .phoneNumber(TEST_PHONE_NUMBER)
                .build();
        userMapper.insertUser(testUser);
        logger.info("插入测试用户成功，ID: {}", testUserId);

        // 2. 尝试重复插入
        assertThrows(Exception.class, () -> userMapper.insertUser(testUser),
                "重复插入用户应抛出异常");
    }

    @Test
    @DisplayName("测试查询不存在的用户")
    void testQueryNonExistentUser() {
        // 查询不存在的用户
        Long nonExistentId = generateTestUserId();
        User user = userMapper.getUserById(nonExistentId);
        assertNull(user, "查询不存在的用户应返回null");
    }
} 