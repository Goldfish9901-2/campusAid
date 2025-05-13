package cn.edu.usst.cs.campusAid.model.forum;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 回复树形结构节点封装类
 * @author zyp
 * @version 1.0
 */
@Data
public class ReplyTreeNode {
    private Reply reply;
    private List<ReplyTreeNode> children = new ArrayList<>();

    public ReplyTreeNode(Reply reply) {
        this.reply = reply;
    }
}
