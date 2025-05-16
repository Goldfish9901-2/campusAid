package cn.edu.usst.cs.campusAid.service.auth;



import cn.edu.usst.cs.campusAid.model.User;
import jakarta.annotation.Nullable;

/**
 * User 服务接口，提供与用户相关的业务逻辑。
 */
public interface UserService {

    /**
     * 根据用户ID获取用户信息。
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);
    /**
     * 判断用户是否为管理员。
     *
     * @param userId 用户ID
     * @return 是否为管理员
     */
    boolean isAdmin(Long userId);

    /**
     * 判断当前用户就这个想查的ID能查到哪个用户的详细信息
     *
     * @param userId 当前用户ID
     * @param targetUserId 想查的ID
     * @return 能查到的ID
     */
    @Nullable
    Long getTargetUserId(Long userId,Long targetUserId);

    /**
     * 获取用户余额
     *
     * @param userId 用户ID
     * @return 用户余额
     * @see User#getBalance()
     */
    double getBalance(Long userId);
}
