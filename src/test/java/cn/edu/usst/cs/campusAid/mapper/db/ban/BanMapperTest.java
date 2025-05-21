package cn.edu.usst.cs.campusAid.mapper.db.ban;

import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.model.complaint.Ban;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
@DisplayName("BanMapper 测试")
public class BanMapperTest {

    @Autowired
    private BanMapper banMapper;

    @Test
    @DisplayName("测试插入 Ban 记录")
    public void testInsertBan() {
        // 构建测试数据
        Ban ban = Ban.builder()
                .block(BanBlock.ERRAND)
                .userId(2235062128L)
                .releaseTime(LocalDateTime.now().plusDays(7))
                .lengthByDay(7)
                .reason("单元测试插入")
                .build();

        // 调用插入方法
        banMapper.insert(ban);

        // 验证对象是否构建成功（可选：查询数据库验证插入）
        assertNotNull(ban, "Ban 对象不应为 null");
    }
}