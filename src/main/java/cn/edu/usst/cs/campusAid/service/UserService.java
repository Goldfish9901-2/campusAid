package cn.edu.usst.cs.campusAid.service;



import cn.edu.usst.cs.campusAid.model.User;

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
}
