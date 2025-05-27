-- 删除数据（按照外键约束的顺序）
DELETE FROM like_blog WHERE blog_id IN (SELECT id FROM blog WHERE creator = 2235062111);
DELETE FROM reply WHERE blog_id IN (SELECT id FROM blog WHERE creator = 2235062111);
DELETE FROM blog WHERE creator = 2235062111;

-- 插入模拟帖子数据
INSERT INTO blog (creator, title, content, visibility, send_time) VALUES
(2235062111, '校园二手交易平台上线啦！', '大家好，我们的校园二手交易平台已经正式上线了！在这里你可以买卖二手教材、电子产品、生活用品等。平台支持在线支付，交易安全有保障。欢迎同学们来体验！', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2235062111, '寻找一起考研的研友', '本人准备报考计算机专业研究生，想找志同道合的研友一起复习。可以一起讨论问题，分享资料，互相督促。有意向的同学请私信我！', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(2235062111, '图书馆占座问题讨论', '最近图书馆占座现象越来越严重，很多同学用书占座但人不在。大家觉得有什么好的解决方案吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2235062111, '校园跑腿服务体验分享', '今天试用了校园跑腿服务，体验非常好！配送速度快，服务态度好，价格也很合理。推荐给需要的同学！', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2235062111, '关于校园网速的吐槽', '最近校园网速特别慢，看视频经常卡顿，下载文件也很慢。有没有同学遇到同样的问题？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2235062111, '校园美食分享', '#美食 #校园 #推荐\n今天在食堂发现了一家新开的麻辣香锅，味道真的很不错！价格也很实惠，推荐大家去尝尝。位置在第二食堂二楼，靠近东门的位置。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(2235062111, '求推荐笔记本电脑', '#电脑 #推荐 #学习\n准备买一台笔记本电脑，预算6000左右，主要用于编程和学习。有没有同学可以推荐一下性价比比较高的型号？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 10 HOUR)),
(2235062111, '校园社团招新', '#社团 #招新 #活动\n计算机协会开始招新啦！我们每周都有技术分享会，还有各种比赛和项目实践机会。欢迎对编程感兴趣的同学加入！', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
(2235062111, '校园招聘信息', '#招聘 #实习 #就业\n某知名互联网公司来校招聘实习生，主要面向计算机、软件工程等专业。要求熟悉Java开发，有项目经验优先。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(2235062111, '校园活动预告', '#活动 #校园 #文化节\n下周末将举办校园文化节，有才艺表演、美食节、科技展等多个活动。欢迎大家来参加！', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(2235062111, '求合租室友', '#租房 #合租 #生活\n本人男，计算机专业，想在学校附近找合租室友。要求作息规律，爱干净。有意向的同学请私信。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2235062111, '校园二手书交易', '#二手 #教材 #交易\n出售以下教材：\n1. 高等数学（第七版）\n2. 数据结构与算法\n3. 计算机网络\n价格可议，有意者私信。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
-- 新增20条帖子
(2235062111, '校园篮球赛报名', '#体育 #篮球 #比赛\n下个月将举办校园篮球赛，欢迎各院系组队参加。比赛分为男子组和女子组，前三名有丰厚奖品。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 29 MINUTE)),
(2235062111, '求推荐考研资料', '#考研 #学习 #资料\n准备考研，想找一些好的复习资料。有没有学长学姐可以推荐一下？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 28 MINUTE)),
(2235062111, '校园摄影展', '#摄影 #艺术 #展览\n本周末在图书馆举办校园摄影展，欢迎大家来参观。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 27 MINUTE)),
(2235062111, '求推荐实习机会', '#实习 #就业 #推荐\n想找一份Java开发相关的实习，有没有好的公司推荐？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 26 MINUTE)),
(2235062111, '校园歌手大赛', '#音乐 #比赛 #活动\n校园歌手大赛开始报名啦！欢迎有才艺的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
(2235062111, '求推荐学习网站', '#学习 #资源 #推荐\n想找一些好的编程学习网站，大家有什么推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 24 MINUTE)),
(2235062111, '校园创业大赛', '#创业 #比赛 #活动\n校园创业大赛开始报名，欢迎有创业想法的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 23 MINUTE)),
(2235062111, '求推荐考研院校', '#考研 #院校 #推荐\n想报考计算机专业研究生，有什么好的院校推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 22 MINUTE)),
(2235062111, '校园志愿者招募', '#志愿者 #公益 #活动\n校园志愿者协会开始招募新成员，欢迎热心公益的同学加入。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 21 MINUTE)),
(2235062111, '求推荐考研导师', '#考研 #导师 #推荐\n想找一位好的考研导师，有没有推荐的？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(2235062111, '校园创业讲座', '#创业 #讲座 #活动\n本周五将举办校园创业讲座，欢迎有兴趣的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 19 MINUTE)),
(2235062111, '求推荐考研资料', '#考研 #资料 #推荐\n想找一些好的考研资料，大家有什么推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 18 MINUTE)),
(2235062111, '校园创业比赛', '#创业 #比赛 #活动\n校园创业比赛开始报名，欢迎有创业想法的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 17 MINUTE)),
(2235062111, '求推荐考研院校', '#考研 #院校 #推荐\n想报考计算机专业研究生，有什么好的院校推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 16 MINUTE)),
(2235062111, '校园志愿者活动', '#志愿者 #公益 #活动\n校园志愿者协会开始招募新成员，欢迎热心公益的同学加入。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(2235062111, '求推荐考研导师', '#考研 #导师 #推荐\n想找一位好的考研导师，有没有推荐的？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 14 MINUTE)),
(2235062111, '校园创业讲座', '#创业 #讲座 #活动\n本周五将举办校园创业讲座，欢迎有兴趣的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 13 MINUTE)),
(2235062111, '求推荐考研资料', '#考研 #资料 #推荐\n想找一些好的考研资料，大家有什么推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 12 MINUTE)),
(2235062111, '校园创业比赛', '#创业 #比赛 #活动\n校园创业比赛开始报名，欢迎有创业想法的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 11 MINUTE)),
(2235062111, '求推荐考研院校', '#考研 #院校 #推荐\n想报考计算机专业研究生，有什么好的院校推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(2235062111, '校园志愿者活动', '#志愿者 #公益 #活动\n校园志愿者协会开始招募新成员，欢迎热心公益的同学加入。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 9 MINUTE)),
-- 再新增20条帖子
(2235062111, '校园电竞比赛', '#电竞 #比赛 #活动\n英雄联盟校园赛开始报名啦！欢迎各院系组队参加，冠军奖金5000元！', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 8 MINUTE)),
(2235062111, '求推荐健身计划', '#健身 #运动 #健康\n想开始健身，有没有适合新手的健身计划推荐？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 7 MINUTE)),
(2235062111, '校园话剧社招新', '#话剧 #艺术 #社团\n校园话剧社开始招新啦！欢迎对表演感兴趣的同学加入。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 6 MINUTE)),
(2235062111, '求推荐英语学习方法', '#英语 #学习 #方法\n想提高英语水平，有什么好的学习方法推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
(2235062111, '校园街舞比赛', '#街舞 #比赛 #活动\n校园街舞比赛开始报名，欢迎有才艺的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 4 MINUTE)),
(2235062111, '求推荐实习公司', '#实习 #就业 #推荐\n想找一份前端开发相关的实习，有什么好的公司推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 3 MINUTE)),
(2235062111, '校园创业讲座', '#创业 #讲座 #活动\n本周六将举办校园创业讲座，欢迎有兴趣的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 2 MINUTE)),
(2235062111, '求推荐考研资料', '#考研 #资料 #推荐\n想找一些好的考研资料，大家有什么推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 1 MINUTE)),
(2235062111, '校园创业比赛', '#创业 #比赛 #活动\n校园创业比赛开始报名，欢迎有创业想法的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 30 SECOND)),
(2235062111, '求推荐考研院校', '#考研 #院校 #推荐\n想报考计算机专业研究生，有什么好的院校推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 20 SECOND)),
(2235062111, '校园志愿者活动', '#志愿者 #公益 #活动\n校园志愿者协会开始招募新成员，欢迎热心公益的同学加入。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 10 SECOND)),
(2235062111, '求推荐考研导师', '#考研 #导师 #推荐\n想找一位好的考研导师，有没有推荐的？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 5 SECOND)),
(2235062111, '校园创业讲座', '#创业 #讲座 #活动\n本周五将举办校园创业讲座，欢迎有兴趣的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 4 SECOND)),
(2235062111, '求推荐考研资料', '#考研 #资料 #推荐\n想找一些好的考研资料，大家有什么推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 3 SECOND)),
(2235062111, '校园创业比赛', '#创业 #比赛 #活动\n校园创业比赛开始报名，欢迎有创业想法的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 2 SECOND)),
(2235062111, '求推荐考研院校', '#考研 #院校 #推荐\n想报考计算机专业研究生，有什么好的院校推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 1 SECOND)),
(2235062111, '校园志愿者活动', '#志愿者 #公益 #活动\n校园志愿者协会开始招募新成员，欢迎热心公益的同学加入。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 30 SECOND)),
(2235062111, '求推荐考研导师', '#考研 #导师 #推荐\n想找一位好的考研导师，有没有推荐的？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 20 SECOND)),
(2235062111, '校园创业讲座', '#创业 #讲座 #活动\n本周五将举办校园创业讲座，欢迎有兴趣的同学参加。', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 10 SECOND)),
(2235062111, '求推荐考研资料', '#考研 #资料 #推荐\n想找一些好的考研资料，大家有什么推荐吗？', 'VISIBLE', DATE_SUB(NOW(), INTERVAL 5 SECOND));

-- 获取新插入帖子的ID
SELECT @first_post_id := MIN(id) FROM blog WHERE creator = 2235062111;

-- 插入模拟回复数据
INSERT INTO reply (blog_id, sender, content, send_time, parent_id) VALUES
-- 校园二手交易平台
(@first_post_id, 2235062111, '太好了！正好想买二手教材，请问怎么使用？', DATE_SUB(NOW(), INTERVAL 4 DAY), NULL);

SET @first_reply_id = LAST_INSERT_ID();

INSERT INTO reply (blog_id, sender, content, send_time, parent_id) VALUES
(@first_post_id, 2235062111, '直接注册账号，然后就可以发布商品或浏览商品了。', DATE_SUB(NOW(), INTERVAL 4 DAY), @first_reply_id),
-- 寻找一起考研的研友
(@first_post_id + 1, 2235062111, '我也是计算机专业的，可以一起复习！', DATE_SUB(NOW(), INTERVAL 3 DAY), NULL),
-- 图书馆占座问题讨论
(@first_post_id + 2, 2235062111, '建议图书馆可以设置座位预约系统', DATE_SUB(NOW(), INTERVAL 2 DAY), NULL);

SET @second_reply_id = LAST_INSERT_ID();

INSERT INTO reply (blog_id, sender, content, send_time, parent_id) VALUES
(@first_post_id + 2, 2235062111, '这个建议不错，但是系统开发需要时间', DATE_SUB(NOW(), INTERVAL 2 DAY), @second_reply_id),
-- 校园跑腿服务体验分享
(@first_post_id + 3, 2235062111, '确实很好用，我已经用了好几次了', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),
-- 关于校园网速的吐槽
(@first_post_id + 4, 2235062111, '我这边网速还行，可能是区域问题', DATE_SUB(NOW(), INTERVAL 12 HOUR), NULL),
-- 校园美食分享
(@first_post_id + 5, 2235062111, '我也去吃过，确实不错！推荐他们家的麻辣香锅套餐。', DATE_SUB(NOW(), INTERVAL 11 HOUR), NULL),
-- 求推荐笔记本电脑
(@first_post_id + 6, 2235062111, '联想小新Pro14可以考虑一下，性价比很高。', DATE_SUB(NOW(), INTERVAL 9 HOUR), NULL),
-- 校园社团招新
(@first_post_id + 7, 2235062111, '请问需要什么基础吗？我是大一新生。', DATE_SUB(NOW(), INTERVAL 7 HOUR), NULL),
-- 校园招聘信息
(@first_post_id + 8, 2235062111, '请问具体是哪家公司？', DATE_SUB(NOW(), INTERVAL 5 HOUR), NULL),
-- 校园活动预告
(@first_post_id + 9, 2235062111, '文化节具体时间是什么时候？', DATE_SUB(NOW(), INTERVAL 3 HOUR), NULL),
-- 求合租室友
(@first_post_id + 10, 2235062111, '请问房子具体位置在哪里？', DATE_SUB(NOW(), INTERVAL 1 HOUR), NULL),
-- 校园二手书交易
(@first_post_id + 11, 2235062111, '数据结构与算法多少钱？', DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL);

-- 为新增的20条帖子添加回复
INSERT INTO reply (blog_id, sender, content, send_time, parent_id) VALUES
-- 校园篮球赛报名
(@first_post_id + 12, 2235062111, '请问具体比赛时间是什么时候？', DATE_SUB(NOW(), INTERVAL 28 MINUTE), NULL),
(@first_post_id + 12, 2235062111, '下个月15号开始，持续一周。', DATE_SUB(NOW(), INTERVAL 27 MINUTE), LAST_INSERT_ID()),
-- 求推荐考研资料
(@first_post_id + 13, 2235062111, '有什么好的数学资料推荐吗？', DATE_SUB(NOW(), INTERVAL 26 MINUTE), NULL),
(@first_post_id + 13, 2235062111, '张宇的数学复习全书不错。', DATE_SUB(NOW(), INTERVAL 25 MINUTE), LAST_INSERT_ID()),
-- 校园摄影展
(@first_post_id + 14, 2235062111, '需要门票吗？', DATE_SUB(NOW(), INTERVAL 24 MINUTE), NULL),
(@first_post_id + 14, 2235062111, '不需要，免费参观。', DATE_SUB(NOW(), INTERVAL 23 MINUTE), LAST_INSERT_ID()),
-- 求推荐实习机会
(@first_post_id + 15, 2235062111, '有什么好的公司推荐吗？', DATE_SUB(NOW(), INTERVAL 22 MINUTE), NULL),
(@first_post_id + 15, 2235062111, '可以考虑BAT等大厂。', DATE_SUB(NOW(), INTERVAL 21 MINUTE), LAST_INSERT_ID()),
-- 校园歌手大赛
(@first_post_id + 16, 2235062111, '请问报名截止时间是什么时候？', DATE_SUB(NOW(), INTERVAL 20 MINUTE), NULL),
(@first_post_id + 16, 2235062111, '本周五截止。', DATE_SUB(NOW(), INTERVAL 19 MINUTE), LAST_INSERT_ID()),
-- 求推荐学习网站
(@first_post_id + 17, 2235062111, '有什么好的编程学习网站推荐吗？', DATE_SUB(NOW(), INTERVAL 18 MINUTE), NULL),
(@first_post_id + 17, 2235062111, '推荐LeetCode和牛客网。', DATE_SUB(NOW(), INTERVAL 17 MINUTE), LAST_INSERT_ID()),
-- 校园创业大赛
(@first_post_id + 18, 2235062111, '请问比赛奖金是多少？', DATE_SUB(NOW(), INTERVAL 16 MINUTE), NULL),
(@first_post_id + 18, 2235062111, '一等奖5000元，二等奖3000元，三等奖1000元。', DATE_SUB(NOW(), INTERVAL 15 MINUTE), LAST_INSERT_ID()),
-- 求推荐考研院校
(@first_post_id + 19, 2235062111, '有什么好的院校推荐吗？', DATE_SUB(NOW(), INTERVAL 14 MINUTE), NULL),
(@first_post_id + 19, 2235062111, '可以考虑985/211院校。', DATE_SUB(NOW(), INTERVAL 13 MINUTE), LAST_INSERT_ID()),
-- 校园志愿者招募
(@first_post_id + 20, 2235062111, '请问需要什么条件？', DATE_SUB(NOW(), INTERVAL 12 MINUTE), NULL),
(@first_post_id + 20, 2235062111, '只要热心公益，有责任心就可以。', DATE_SUB(NOW(), INTERVAL 11 MINUTE), LAST_INSERT_ID()),
-- 求推荐考研导师
(@first_post_id + 21, 2235062111, '有什么好的导师推荐吗？', DATE_SUB(NOW(), INTERVAL 10 MINUTE), NULL),
(@first_post_id + 21, 2235062111, '建议先了解导师的研究方向。', DATE_SUB(NOW(), INTERVAL 9 MINUTE), LAST_INSERT_ID()),
-- 校园创业讲座
(@first_post_id + 22, 2235062111, '请问讲座地点在哪里？', DATE_SUB(NOW(), INTERVAL 8 MINUTE), NULL),
(@first_post_id + 22, 2235062111, '在图书馆报告厅。', DATE_SUB(NOW(), INTERVAL 7 MINUTE), LAST_INSERT_ID()),
-- 求推荐考研资料
(@first_post_id + 23, 2235062111, '有什么好的英语资料推荐吗？', DATE_SUB(NOW(), INTERVAL 6 MINUTE), NULL),
(@first_post_id + 23, 2235062111, '推荐新东方的考研英语。', DATE_SUB(NOW(), INTERVAL 5 MINUTE), LAST_INSERT_ID()),
-- 校园创业比赛
(@first_post_id + 24, 2235062111, '请问比赛形式是什么？', DATE_SUB(NOW(), INTERVAL 4 MINUTE), NULL),
(@first_post_id + 24, 2235062111, '需要提交商业计划书和路演。', DATE_SUB(NOW(), INTERVAL 3 MINUTE), LAST_INSERT_ID()),
-- 求推荐考研院校
(@first_post_id + 25, 2235062111, '有什么好的院校推荐吗？', DATE_SUB(NOW(), INTERVAL 2 MINUTE), NULL),
(@first_post_id + 25, 2235062111, '建议考虑专业排名。', DATE_SUB(NOW(), INTERVAL 1 MINUTE), LAST_INSERT_ID()),
-- 校园志愿者活动
(@first_post_id + 26, 2235062111, '请问活动时间是什么时候？', DATE_SUB(NOW(), INTERVAL 30 SECOND), NULL),
(@first_post_id + 26, 2235062111, '每周六下午。', DATE_SUB(NOW(), INTERVAL 20 SECOND), LAST_INSERT_ID()),
-- 求推荐考研导师
(@first_post_id + 27, 2235062111, '有什么好的导师推荐吗？', DATE_SUB(NOW(), INTERVAL 10 SECOND), NULL),
(@first_post_id + 27, 2235062111, '建议先了解导师的研究方向。', DATE_SUB(NOW(), INTERVAL 5 SECOND), LAST_INSERT_ID()),
-- 校园创业讲座
(@first_post_id + 28, 2235062111, '请问讲座主题是什么？', DATE_SUB(NOW(), INTERVAL 4 SECOND), NULL),
(@first_post_id + 28, 2235062111, '互联网创业机会与挑战。', DATE_SUB(NOW(), INTERVAL 3 SECOND), LAST_INSERT_ID()),
-- 求推荐考研资料
(@first_post_id + 29, 2235062111, '有什么好的政治资料推荐吗？', DATE_SUB(NOW(), INTERVAL 2 SECOND), NULL),
(@first_post_id + 29, 2235062111, '推荐肖秀荣的政治。', DATE_SUB(NOW(), INTERVAL 1 SECOND), LAST_INSERT_ID()),
-- 校园创业比赛
(@first_post_id + 30, 2235062111, '请问比赛评委是谁？', DATE_SUB(NOW(), INTERVAL 30 SECOND), NULL),
(@first_post_id + 30, 2235062111, '有创业成功的校友和投资人。', DATE_SUB(NOW(), INTERVAL 20 SECOND), LAST_INSERT_ID()),
-- 求推荐考研院校
(@first_post_id + 31, 2235062111, '有什么好的院校推荐吗？', DATE_SUB(NOW(), INTERVAL 10 SECOND), NULL),
(@first_post_id + 31, 2235062111, '建议考虑地理位置。', DATE_SUB(NOW(), INTERVAL 5 SECOND), LAST_INSERT_ID());

-- 为再新增的20条帖子添加回复
INSERT INTO reply (blog_id, sender, content, send_time, parent_id) VALUES
-- 校园电竞比赛
(@first_post_id + 32, 2235062111, '请问比赛时间是什么时候？', DATE_SUB(NOW(), INTERVAL 8 MINUTE), NULL),
(@first_post_id + 32, 2235062111, '下周六开始，持续两天。', DATE_SUB(NOW(), INTERVAL 7 MINUTE), LAST_INSERT_ID()),
(@first_post_id + 32, 2235062111, '好的，谢谢！', DATE_SUB(NOW(), INTERVAL 6 MINUTE), LAST_INSERT_ID()),
-- 求推荐健身计划
(@first_post_id + 33, 2235062111, '有什么适合新手的健身计划推荐吗？', DATE_SUB(NOW(), INTERVAL 7 MINUTE), NULL),
(@first_post_id + 33, 2235062111, '建议先从基础动作开始，每周3-4次。', DATE_SUB(NOW(), INTERVAL 6 MINUTE), LAST_INSERT_ID()),
(@first_post_id + 33, 2235062111, '好的，我会试试。', DATE_SUB(NOW(), INTERVAL 5 MINUTE), LAST_INSERT_ID()),
-- 校园话剧社招新
(@first_post_id + 34, 2235062111, '请问需要什么条件？', DATE_SUB(NOW(), INTERVAL 6 MINUTE), NULL),
(@first_post_id + 34, 2235062111, '只要对表演感兴趣就可以。', DATE_SUB(NOW(), INTERVAL 5 MINUTE), LAST_INSERT_ID()),
(@first_post_id + 34, 2235062111, '好的，我会去报名。', DATE_SUB(NOW(), INTERVAL 4 MINUTE), LAST_INSERT_ID()),
-- 求推荐英语学习方法
(@first_post_id + 35, 2235062111, '有什么好的英语学习方法推荐吗？', DATE_SUB(NOW(), INTERVAL 5 MINUTE), NULL),
(@first_post_id + 35, 2235062111, '建议每天坚持背单词，多听多说。', DATE_SUB(NOW(), INTERVAL 4 MINUTE), LAST_INSERT_ID()),
(@first_post_id + 35, 2235062111, '谢谢，我会试试。', DATE_SUB(NOW(), INTERVAL 3 MINUTE), LAST_INSERT_ID()),
-- 校园街舞比赛
(@first_post_id + 36, 2235062111, '请问比赛时间是什么时候？', DATE_SUB(NOW(), INTERVAL 4 MINUTE), NULL),
(@first_post_id + 36, 2235062111, '下周日开始，持续一天。', DATE_SUB(NOW(), INTERVAL 3 MINUTE), LAST_INSERT_ID()),
(@first_post_id + 36, 2235062111, '好的，我会去参加。', DATE_SUB(NOW(), INTERVAL 2 MINUTE), LAST_INSERT_ID()),
-- 求推荐实习公司
(@first_post_id + 37, 2235062111, '有什么好的前端实习公司推荐吗？', DATE_SUB(NOW(), INTERVAL 3 MINUTE), NULL),
(@first_post_id + 37, 2235062111, '可以考虑字节跳动、腾讯等公司。', DATE_SUB(NOW(), INTERVAL 2 MINUTE), LAST_INSERT_ID()),
(@first_post_id + 37, 2235062111, '谢谢，我会去投简历。', DATE_SUB(NOW(), INTERVAL 1 MINUTE), LAST_INSERT_ID()),
-- 校园创业讲座
(@first_post_id + 38, 2235062111, '请问讲座地点在哪里？', DATE_SUB(NOW(), INTERVAL 2 MINUTE), NULL),
(@first_post_id + 38, 2235062111, '在图书馆报告厅。', DATE_SUB(NOW(), INTERVAL 1 MINUTE), LAST_INSERT_ID()),
(@first_post_id + 38, 2235062111, '好的，我会去听。', DATE_SUB(NOW(), INTERVAL 30 SECOND), LAST_INSERT_ID()),
-- 求推荐考研资料
(@first_post_id + 39, 2235062111, '有什么好的考研资料推荐吗？', DATE_SUB(NOW(), INTERVAL 1 MINUTE), NULL),
(@first_post_id + 39, 2235062111, '推荐新东方的考研资料。', DATE_SUB(NOW(), INTERVAL 30 SECOND), LAST_INSERT_ID()),
(@first_post_id + 39, 2235062111, '谢谢，我会去买。', DATE_SUB(NOW(), INTERVAL 20 SECOND), LAST_INSERT_ID()),
-- 校园创业比赛
(@first_post_id + 40, 2235062111, '请问比赛形式是什么？', DATE_SUB(NOW(), INTERVAL 30 SECOND), NULL),
(@first_post_id + 40, 2235062111, '需要提交商业计划书和路演。', DATE_SUB(NOW(), INTERVAL 20 SECOND), LAST_INSERT_ID()),
(@first_post_id + 40, 2235062111, '好的，我会准备。', DATE_SUB(NOW(), INTERVAL 10 SECOND), LAST_INSERT_ID()),
-- 求推荐考研院校
(@first_post_id + 41, 2235062111, '有什么好的院校推荐吗？', DATE_SUB(NOW(), INTERVAL 20 SECOND), NULL),
(@first_post_id + 41, 2235062111, '建议考虑985/211院校。', DATE_SUB(NOW(), INTERVAL 10 SECOND), LAST_INSERT_ID()),
(@first_post_id + 41, 2235062111, '谢谢，我会考虑。', DATE_SUB(NOW(), INTERVAL 5 SECOND), LAST_INSERT_ID()),
-- 校园志愿者活动
(@first_post_id + 42, 2235062111, '请问活动时间是什么时候？', DATE_SUB(NOW(), INTERVAL 10 SECOND), NULL),
(@first_post_id + 42, 2235062111, '每周六下午。', DATE_SUB(NOW(), INTERVAL 5 SECOND), LAST_INSERT_ID()),
(@first_post_id + 42, 2235062111, '好的，我会去参加。', DATE_SUB(NOW(), INTERVAL 4 SECOND), LAST_INSERT_ID()),
-- 求推荐考研导师
(@first_post_id + 43, 2235062111, '有什么好的导师推荐吗？', DATE_SUB(NOW(), INTERVAL 5 SECOND), NULL),
(@first_post_id + 43, 2235062111, '建议先了解导师的研究方向。', DATE_SUB(NOW(), INTERVAL 4 SECOND), LAST_INSERT_ID()),
(@first_post_id + 43, 2235062111, '谢谢，我会去了解。', DATE_SUB(NOW(), INTERVAL 3 SECOND), LAST_INSERT_ID()),
-- 校园创业讲座
(@first_post_id + 44, 2235062111, '请问讲座主题是什么？', DATE_SUB(NOW(), INTERVAL 4 SECOND), NULL),
(@first_post_id + 44, 2235062111, '互联网创业机会与挑战。', DATE_SUB(NOW(), INTERVAL 3 SECOND), LAST_INSERT_ID()),
(@first_post_id + 44, 2235062111, '好的，我会去听。', DATE_SUB(NOW(), INTERVAL 2 SECOND), LAST_INSERT_ID()),
-- 求推荐考研资料
(@first_post_id + 45, 2235062111, '有什么好的考研资料推荐吗？', DATE_SUB(NOW(), INTERVAL 3 SECOND), NULL),
(@first_post_id + 45, 2235062111, '推荐新东方的考研资料。', DATE_SUB(NOW(), INTERVAL 2 SECOND), LAST_INSERT_ID()),
(@first_post_id + 45, 2235062111, '谢谢，我会去买。', DATE_SUB(NOW(), INTERVAL 1 SECOND), LAST_INSERT_ID()),
-- 校园创业比赛
(@first_post_id + 46, 2235062111, '请问比赛形式是什么？', DATE_SUB(NOW(), INTERVAL 2 SECOND), NULL),
(@first_post_id + 46, 2235062111, '需要提交商业计划书和路演。', DATE_SUB(NOW(), INTERVAL 1 SECOND), LAST_INSERT_ID()),
(@first_post_id + 46, 2235062111, '好的，我会准备。', DATE_SUB(NOW(), INTERVAL 30 SECOND), LAST_INSERT_ID()),
-- 求推荐考研院校
(@first_post_id + 47, 2235062111, '有什么好的院校推荐吗？', DATE_SUB(NOW(), INTERVAL 1 SECOND), NULL),
(@first_post_id + 47, 2235062111, '建议考虑985/211院校。', DATE_SUB(NOW(), INTERVAL 30 SECOND), LAST_INSERT_ID()),
(@first_post_id + 47, 2235062111, '谢谢，我会考虑。', DATE_SUB(NOW(), INTERVAL 20 SECOND), LAST_INSERT_ID()),
-- 校园志愿者活动
(@first_post_id + 48, 2235062111, '请问活动时间是什么时候？', DATE_SUB(NOW(), INTERVAL 30 SECOND), NULL),
(@first_post_id + 48, 2235062111, '每周六下午。', DATE_SUB(NOW(), INTERVAL 20 SECOND), LAST_INSERT_ID()),
(@first_post_id + 48, 2235062111, '好的，我会去参加。', DATE_SUB(NOW(), INTERVAL 10 SECOND), LAST_INSERT_ID()),
-- 求推荐考研导师
(@first_post_id + 49, 2235062111, '有什么好的导师推荐吗？', DATE_SUB(NOW(), INTERVAL 20 SECOND), NULL),
(@first_post_id + 49, 2235062111, '建议先了解导师的研究方向。', DATE_SUB(NOW(), INTERVAL 10 SECOND), LAST_INSERT_ID()),
(@first_post_id + 49, 2235062111, '谢谢，我会去了解。', DATE_SUB(NOW(), INTERVAL 5 SECOND), LAST_INSERT_ID()),
-- 校园创业讲座
(@first_post_id + 50, 2235062111, '请问讲座主题是什么？', DATE_SUB(NOW(), INTERVAL 10 SECOND), NULL),
(@first_post_id + 50, 2235062111, '互联网创业机会与挑战。', DATE_SUB(NOW(), INTERVAL 5 SECOND), LAST_INSERT_ID()),
(@first_post_id + 50, 2235062111, '好的，我会去听。', DATE_SUB(NOW(), INTERVAL 4 SECOND), LAST_INSERT_ID()),
-- 求推荐考研资料
(@first_post_id + 51, 2235062111, '有什么好的考研资料推荐吗？', DATE_SUB(NOW(), INTERVAL 5 SECOND), NULL),
(@first_post_id + 51, 2235062111, '推荐新东方的考研资料。', DATE_SUB(NOW(), INTERVAL 4 SECOND), LAST_INSERT_ID()),
(@first_post_id + 51, 2235062111, '谢谢，我会去买。', DATE_SUB(NOW(), INTERVAL 3 SECOND), LAST_INSERT_ID());

-- 插入模拟点赞数据
INSERT INTO like_blog (blog_id, liker, like_time) VALUES
(@first_post_id, 2235062111, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(@first_post_id + 1, 2235062111, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(@first_post_id + 2, 2235062111, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(@first_post_id + 3, 2235062111, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(@first_post_id + 4, 2235062111, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(@first_post_id + 5, 2235062111, DATE_SUB(NOW(), INTERVAL 11 HOUR)),
(@first_post_id + 6, 2235062111, DATE_SUB(NOW(), INTERVAL 9 HOUR)),
(@first_post_id + 7, 2235062111, DATE_SUB(NOW(), INTERVAL 7 HOUR)),
(@first_post_id + 8, 2235062111, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(@first_post_id + 9, 2235062111, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(@first_post_id + 10, 2235062111, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(@first_post_id + 11, 2235062111, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(@first_post_id + 12, 2235062111, DATE_SUB(NOW(), INTERVAL 28 MINUTE)),
(@first_post_id + 13, 2235062111, DATE_SUB(NOW(), INTERVAL 26 MINUTE)),
(@first_post_id + 14, 2235062111, DATE_SUB(NOW(), INTERVAL 24 MINUTE)),
(@first_post_id + 15, 2235062111, DATE_SUB(NOW(), INTERVAL 22 MINUTE)),
(@first_post_id + 16, 2235062111, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(@first_post_id + 17, 2235062111, DATE_SUB(NOW(), INTERVAL 18 MINUTE)),
(@first_post_id + 18, 2235062111, DATE_SUB(NOW(), INTERVAL 16 MINUTE)),
(@first_post_id + 19, 2235062111, DATE_SUB(NOW(), INTERVAL 14 MINUTE)),
(@first_post_id + 20, 2235062111, DATE_SUB(NOW(), INTERVAL 12 MINUTE)),
(@first_post_id + 21, 2235062111, DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(@first_post_id + 22, 2235062111, DATE_SUB(NOW(), INTERVAL 8 MINUTE)),
(@first_post_id + 23, 2235062111, DATE_SUB(NOW(), INTERVAL 6 MINUTE)),
(@first_post_id + 24, 2235062111, DATE_SUB(NOW(), INTERVAL 4 MINUTE)),
(@first_post_id + 25, 2235062111, DATE_SUB(NOW(), INTERVAL 2 MINUTE)),
(@first_post_id + 26, 2235062111, DATE_SUB(NOW(), INTERVAL 30 SECOND)),
(@first_post_id + 27, 2235062111, DATE_SUB(NOW(), INTERVAL 10 SECOND)),
(@first_post_id + 28, 2235062111, DATE_SUB(NOW(), INTERVAL 4 SECOND)),
(@first_post_id + 29, 2235062111, DATE_SUB(NOW(), INTERVAL 2 SECOND)),
(@first_post_id + 30, 2235062111, DATE_SUB(NOW(), INTERVAL 30 SECOND)),
(@first_post_id + 31, 2235062111, DATE_SUB(NOW(), INTERVAL 10 SECOND)),
(@first_post_id + 32, 2235062111, DATE_SUB(NOW(), INTERVAL 8 MINUTE)),
(@first_post_id + 33, 2235062111, DATE_SUB(NOW(), INTERVAL 7 MINUTE)),
(@first_post_id + 34, 2235062111, DATE_SUB(NOW(), INTERVAL 6 MINUTE)),
(@first_post_id + 35, 2235062111, DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
(@first_post_id + 36, 2235062111, DATE_SUB(NOW(), INTERVAL 4 MINUTE)),
(@first_post_id + 37, 2235062111, DATE_SUB(NOW(), INTERVAL 3 MINUTE)),
(@first_post_id + 38, 2235062111, DATE_SUB(NOW(), INTERVAL 2 MINUTE)),
(@first_post_id + 39, 2235062111, DATE_SUB(NOW(), INTERVAL 1 MINUTE)),
(@first_post_id + 40, 2235062111, DATE_SUB(NOW(), INTERVAL 30 SECOND)),
(@first_post_id + 41, 2235062111, DATE_SUB(NOW(), INTERVAL 20 SECOND)),
(@first_post_id + 42, 2235062111, DATE_SUB(NOW(), INTERVAL 10 SECOND)),
(@first_post_id + 43, 2235062111, DATE_SUB(NOW(), INTERVAL 5 SECOND)),
(@first_post_id + 44, 2235062111, DATE_SUB(NOW(), INTERVAL 4 SECOND)),
(@first_post_id + 45, 2235062111, DATE_SUB(NOW(), INTERVAL 3 SECOND)),
(@first_post_id + 46, 2235062111, DATE_SUB(NOW(), INTERVAL 2 SECOND)),
(@first_post_id + 47, 2235062111, DATE_SUB(NOW(), INTERVAL 1 SECOND)),
(@first_post_id + 48, 2235062111, DATE_SUB(NOW(), INTERVAL 30 SECOND)),
(@first_post_id + 49, 2235062111, DATE_SUB(NOW(), INTERVAL 20 SECOND)),
(@first_post_id + 50, 2235062111, DATE_SUB(NOW(), INTERVAL 10 SECOND)),
(@first_post_id + 51, 2235062111, DATE_SUB(NOW(), INTERVAL 5 SECOND)); 