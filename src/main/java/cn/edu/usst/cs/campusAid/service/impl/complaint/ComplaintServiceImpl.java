package cn.edu.usst.cs.campusAid.service.impl.complaint;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintRequest;
import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.mapper.db.complaint.ComplaintMapper;
import cn.edu.usst.cs.campusAid.mapper.db.errand.ErrandMapper;
import cn.edu.usst.cs.campusAid.mapper.db.forum.BlogMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.OrderMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.ShopMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.TransactionMapper;
import cn.edu.usst.cs.campusAid.mapper.mapstruct.ComplaintDTOMapper;
import cn.edu.usst.cs.campusAid.model.complaint.Ban;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.complaint.ComplaintService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ComplaintServiceImpl
        implements ComplaintService {
    private final ComplaintDTOMapper complaintDTOMapper;
    private final ComplaintMapper complaintMapper;
    private final AdminConfig adminConfig;
    private final ErrandMapper errandMapper;
    private final ShopMapper shopMapper;
    private final OrderMapper orderMapper;
    private final TransactionMapper transactionMapper;
    private final BlogMapper blogMapper;
    private BanMapper banMapper;

    public ComplaintServiceImpl(BanMapper banMapper,
                                BlogMapper blogMapper,
                                TransactionMapper transactionMapper,
                                OrderMapper orderMapper,
                                ShopMapper shopMapper,
                                ErrandMapper errandMapper,
                                AdminConfig adminConfig,
                                ComplaintMapper complaintMapper,
                                ComplaintDTOMapper complaintDTOMapper) {
        this.banMapper = banMapper;
        this.blogMapper = blogMapper;
        this.transactionMapper = transactionMapper;
        this.orderMapper = orderMapper;
        this.shopMapper = shopMapper;
        this.errandMapper = errandMapper;
        this.adminConfig = adminConfig;
        this.complaintMapper = complaintMapper;
        this.complaintDTOMapper = complaintDTOMapper;
    }

    @Override
    public Long postComplaint(ComplaintRequest complaintRequest) {
        Complaint complaint = complaintDTOMapper.toDetail(complaintRequest);
        Long complaintId = complaintMapper.minFreeId();
        complaint.setId(complaintId);
        complaintMapper.insert(complaint);
        return complaintId;
    }

    @Override
    public List<Complaint> listComplaints(Long userId) {
        adminConfig.verifyIsAdmin(userId);
        return complaintMapper.listAllComplaints();
    }

    @Override
    public String processComplaint(Long adminId, Long complaintId, String result, int banLength) {
        adminConfig.verifyIsAdmin(adminId);
        Complaint complaint = complaintMapper.getComplaintById(complaintId);
        complaint.setResult(result);
        complaintMapper.submitResult(complaintId, result);
        if (banLength < 0)
            throw new IllegalArgumentException("封禁时长不能小于0");
        if (banLength == 0)
            return "不封禁";
        Ban.BanBuilder banBuilder = Ban.builder();
        Long complaintSourceId = complaint.getComplainedID();
        Long targetUserId;
        switch (complaint.getBlock()) {
            case ERRAND:
                try {
                    targetUserId = errandMapper.selectById(complaintSourceId).getAcceptorId();
                    Objects.requireNonNull(targetUserId);
                } catch (NullPointerException runtimeException) {
                    throw new CampusAidException("跑腿订单不存在");
                }
                break;
            case FORUM_BLOG:
                try {
                    targetUserId = blogMapper.selectById(complaintSourceId).getCreator();
                    Objects.requireNonNull(targetUserId);
                } catch (NullPointerException runtimeException) {
                    throw new CampusAidException("论坛帖子不存在");
                }
                break;
            case FORUM_REPLY:
                try {
                    targetUserId = blogMapper.selectByReplyId(complaintSourceId).getCreator();
                    Objects.requireNonNull(targetUserId);
                } catch (NullPointerException runtimeException) {
                    throw new CampusAidException("论坛帖子不存在");
                }
            default:
                throw new CampusAidException("未知投诉类型");
        }
        LocalDateTime banReleaseTime = LocalDateTime.now().plusDays(banLength);
        BanBlock banBlock =
                switch (complaint.getBlock()) {
                    case ERRAND -> BanBlock.ERRAND;
                    case FORUM_BLOG, FORUM_REPLY -> BanBlock.FORUM;
                    default -> null;
                };
        if (banBlock == null)
            throw new CampusAidException("未知投诉类型");
        banBuilder
                .userId(targetUserId)
                .lengthByDay(banLength)
                .reason(result)
                .block(banBlock)
                .releaseTime(banReleaseTime);
        banMapper.insert(banBuilder.build());
        return String.format(
                "封禁用户%s，封禁时长为%d天，封禁原因为%s，待%s解封",
                targetUserId, banLength, result, banReleaseTime
        );

    }
}
