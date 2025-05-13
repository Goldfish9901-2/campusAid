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
     * <h2>实际输出的余额应由以下几部分得出</h2>
     * <ul>
     *     <li>增值</li>
     *     <ul>
     *         <li>跑腿订单的跑腿费</li>
     *         <li>用户通过支付宝转入</li>
     *     </ul>
     *     <li>减少</li>
     *     <ul>
     *         <li>跑腿的发布</li>
     *         <li>商店下单</li>
     *     </ul>
     * </ul>
     */
    float balance;
}
