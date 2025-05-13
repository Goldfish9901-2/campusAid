package cn.edu.usst.cs.campusAid.util;

import cn.edu.usst.cs.campusAid.dto.forum.ReplyView;
import cn.edu.usst.cs.campusAid.model.forum.Reply;
import cn.edu.usst.cs.campusAid.mapper.ReplyMapperStruct;
import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 回复树结构转换工具类
 */
public class ReplyTreeConverter {

    private static final ReplyMapperStruct replyMapper = ReplyMapperStruct.INSTANCE;

    @Data
    public static class Tree {
        public interface Filter<T> {
            boolean filter(T element);
        }

        ReplyView element;
        List<Tree> children = new ArrayList<>();

        public ReplyView findElement(Long id) {
            if (element != null && Objects.equals(element.getId(), id)) return element;
            Queue<Tree> layer = new LinkedList<>(children);
            while (!layer.isEmpty()) {
                Tree node = layer.poll();
                if (node.element != null
                        && Objects.equals(node.element.getId(), id)
                )
                    return element;
                layer.addAll(node.children);
            }
            throw new NoSuchElementException("No element found.");
        }

        private String show(int index) {
            StringBuilder sb = new StringBuilder();
            sb.append("  ".repeat(Math.max(0, index)));
            sb.append(element);
            if (children != null) {
                for (Tree child : children) {
                    sb.append("\n").append(child.show(index + 1));
                }
            }
            return sb.toString();
        }

        @Override
        public String toString() {
            return show(0);
        }
    }

    public static Tree buildTree(List<Reply> replies) {
        final Queue<Reply> replyCopy = new LinkedList<>(replies);
        Tree root = new Tree();
        if (replies.isEmpty()) return root;

        // 使用 iterator 显式遍历
        Iterator<Reply> iterator = replyCopy.iterator();
        while (iterator.hasNext()) {
            Reply reply = iterator.next();
            if (reply.getParentId() == null) {
                Tree firstLevelChildren = new Tree();
                firstLevelChildren.element = replyMapper.toView(reply);
                root.children.add(firstLevelChildren);
                iterator.remove(); // ✅ 安全删除
                System.out.println(root);
                System.out.println("***");
            }
        }

        return root;


//        // 将所有回复转为 View 并放入 Map
//        Map<Long, ReplyView> viewMap = replies.stream()
//                .collect(Collectors.toMap(Reply::getId, replyMapper::toView));
//        System.err.println("---BUILD STARTED---");
//        System.out.println(viewMap);
//        System.err.println("--**main*--");
//        List<ReplyView> rootNodes = new ArrayList<>();
//
//
//        for (Reply r : replies) {
//            ReplyView view = viewMap.get(r.getId());
//            if (r.getParentId() == null) {
//                System.out.println("---" + view);
//                rootNodes.add(view);
//            } else {
//                ReplyView parent = viewMap.get(r.getParentId());
//                if (parent != null) {
//                    if (parent.getReplies() == null) {
//                        parent.setReplies(new ArrayList<>());
//                    }
//                    parent.getReplies().add(view);
//                }
//            }
//        }
//
//        System.err.println("---BUILD FINISHED---");
//        return rootNodes;
    }
}
