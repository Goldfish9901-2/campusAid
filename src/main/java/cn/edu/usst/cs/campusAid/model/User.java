package cn.edu.usst.cs.campusAid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class User {
    /**
     * 用户教务系统id<br/>
     * 可通过 [id]@st.usst.edu.cn 发送验证码
     */
    @Id
    long id;
    /**
     * 用户真实姓名
     */
    String name;
    /**
     * 手机号
     */
    long phoneNumber;
    /**
     * 余额 初始为0
     */
    float balance;
}
