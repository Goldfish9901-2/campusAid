package cn.edu.usst.cs.campusAid.util;

import cn.edu.usst.cs.campusAid.dto.forum.ReplyView;
import cn.edu.usst.cs.campusAid.model.forum.Reply;
import cn.edu.usst.cs.campusAid.mapper.ReplyMapperStruct;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 回复树结构转换工具类
 */
public class ReplyTreeConverter {

    private static final ReplyMapperStruct replyMapper = ReplyMapperStruct.INSTANCE;

    public static List<ReplyView> buildTree(List<Reply> replies) {
        if (replies == null || replies.isEmpty()) return Collections.emptyList();

        // 将所有回复转为 View 并放入 Map
        Map<Long, ReplyView> viewMap = replies.stream()
                .collect(Collectors.toMap(Reply::getId, replyMapper::toView));

        List<ReplyView> rootNodes = new ArrayList<>();

        for (Reply r : replies) {
            ReplyView view = viewMap.get(r.getId());
            if (r.getParentId() == null) {
                rootNodes.add(view);
            } else {
                ReplyView parent = viewMap.get(r.getParentId());
                if (parent != null) {
                    if (parent.getReplies() == null) {
                        parent.setReplies(new ArrayList<>());
                    }
                    parent.getReplies().add(view);
                }
            }
        }

        return rootNodes;
    }
}
