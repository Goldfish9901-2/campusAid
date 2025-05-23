package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.mapper.db.errand.ErrandMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.OrderMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.ShopMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.TransactionMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.model.charge.Charge;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import cn.edu.usst.cs.campusAid.service.charge.ChargeService;
import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private final UserMapper userMapper;
    private final AdminConfig adminConfig;
    private final ChargeService chargeService;
    private final ErrandMapper errandMapper;
    private final ShopMapper shopMapper;
    private final OrderMapper orderMapper;
    private final TransactionMapper transactionMapper;

    public UserServiceImpl(UserMapper userMapper, AdminConfig adminConfig, ChargeService chargeService, ErrandMapper errandMapper, ShopMapper shopMapper, OrderMapper orderMapper, TransactionMapper transactionMapper) {
        this.userMapper = userMapper;
        this.adminConfig = adminConfig;
        this.chargeService = chargeService;
        this.errandMapper = errandMapper;
        this.shopMapper = shopMapper;
        this.orderMapper = orderMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public User getUserById(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null)
            throw new CampusAidException("用户不存在");
        return user;
    }

    /**
     * 判断用户是否为管理员
     *
     * @param userId 用户id
     * @return 是否为管理员
     */
    @Override
    public boolean isAdmin(Long userId) {
        User user = userMapper.getUserById(userId);
        return user != null && adminConfig.getAdmin().equals(userId.toString()); // 管理员id为2235062128
    }

    @Override
    public Long getTargetUserId(Long userId, Long targetUserId) {
        try {
            adminConfig.verifyIsAdmin(userId);
//            return targetUserId;
            return userId;
        } catch (CampusAidException e) {
            // 不是管理员 只能看自己的
            return userId;
        }
    }

    @Override
    public double getBalance(Long userId) {
        AtomicDouble
                chargeAmount = new AtomicDouble(),
                errandIncome = new AtomicDouble(),
                errandCost = new AtomicDouble(),
                shopCost = new AtomicDouble();
        List<Charge> charges = chargeService.getHistory(userId);
        charges.forEach(charge -> chargeAmount.addAndGet( charge.getAmount()));
        List<Errand> acceptedErrands = errandMapper.selectErrandsAcceptedByUser(userId);
        acceptedErrands.forEach(errand -> errandIncome.addAndGet(errand.getFee()));
        List<Errand> publishedErrands = errandMapper.selectErrandsPublishedByUser(userId);
        publishedErrands.forEach(errand -> errandCost.addAndGet(errand.getFee()));
        List<ProductTransaction> purchases = transactionMapper.getHistory(userId);
        purchases.forEach(purchase -> shopCost.addAndGet(purchase.getPrice() * purchase.getAmount()));
        return chargeAmount.get() + errandIncome.get()
                - errandCost.get() - shopCost.get();

    }
}
