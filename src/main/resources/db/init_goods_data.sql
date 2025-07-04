-- 删除现有商品数据
DELETE FROM good;

-- 插入商品信息
INSERT INTO good (id, name, description, shop, price) VALUES

-- 饮料类
(16, '农夫山泉矿泉水', '550ml 天然矿泉水', '教育超市', 2.00),
(17, '可口可乐', '330ml 碳酸饮料', '教育超市', 3.50),
(18, '百事可乐', '330ml 碳酸饮料', '教育超市', 3.50),
(19, '雪碧', '330ml 柠檬味汽水', '教育超市', 3.50),
(20, '芬达', '330ml 橙味汽水', '教育超市', 3.50),
(21, '红牛', '250ml 能量饮料', '教育超市', 6.00),
(22, '维他奶', '250ml 豆奶', '教育超市', 4.50),
(23, '统一冰红茶', '500ml 茶饮料', '教育超市', 4.00),
(24, '康师傅绿茶', '500ml 茶饮料', '教育超市', 4.00),
(25, '蒙牛纯牛奶', '250ml 纯牛奶', '教育超市', 5.00),

-- 零食类
(26, '乐事薯片', '104g 原味薯片', '教育超市', 6.50),
(27, '上好佳薯条', '80g 原味薯条', '教育超市', 4.50),
(28, '奥利奥', '116g 巧克力夹心饼干', '教育超市', 7.50),
(29, '好丽友派', '12枚装 巧克力派', '教育超市', 8.50),
(30, '德芙巧克力', '43g 丝滑牛奶巧克力', '教育超市', 9.90),
(31, '士力架', '51g 花生夹心巧克力', '教育超市', 5.50),
(32, '旺旺雪饼', '84g 原味雪饼', '教育超市', 4.50),
(33, '卫龙辣条', '100g 大面筋', '教育超市', 5.00),
(34, '三只松鼠坚果', '100g 混合坚果', '教育超市', 15.90),
(35, '百草味肉脯', '100g 猪肉脯', '教育超市', 12.90),

-- 日用品类
(36, '维达抽纸', '3层120抽 面巾纸', '教育超市', 12.90),
(37, '清风卷纸', '10卷装 卫生纸', '教育超市', 15.90),
(38, '黑人牙膏', '100g 薄荷味牙膏', '教育超市', 9.90),
(39, '舒肤佳香皂', '125g 清爽型香皂', '教育超市', 6.50),
(40, '海飞丝洗发水', '400ml 去屑洗发水', '教育超市', 39.90),
(41, '蓝月亮洗衣液', '1kg 洗衣液', '教育超市', 29.90),
(42, '佳洁士牙刷', '2支装 软毛牙刷', '教育超市', 12.90),
(43, '心相印湿巾', '80片装 湿纸巾', '教育超市', 9.90),
(44, '妮维雅洗面奶', '100g 控油洗面奶', '教育超市', 29.90),
(45, '护舒宝卫生巾', '10片装 日用卫生巾', '教育超市', 12.90),

-- 文具类
(46, '晨光中性笔', '0.5mm 黑色中性笔', '教育超市', 3.50),
(47, '得力笔记本', 'A5 100页 笔记本', '教育超市', 8.90),
(48, '真彩修正带', '5m 修正带', '教育超市', 4.50),
(49, '国誉文件夹', 'A4 文件袋', '教育超市', 5.90),
(50, '斑马荧光笔', '双头荧光笔', '教育超市', 6.90),

-- 暖屋商品
(51, '奶茶', '500ml 珍珠奶茶', '暖屋', 12.00),
(52, '咖啡', '350ml 美式咖啡', '暖屋', 15.00),
(53, '蛋糕', '6寸 奶油蛋糕', '暖屋', 68.00),
(54, '三明治', '火腿芝士三明治', '暖屋', 18.00),
(55, '沙拉', '水果沙拉', '暖屋', 22.00),
(56, '果汁', '鲜榨橙汁', '暖屋', 16.00),
(57, '甜点', '提拉米苏', '暖屋', 28.00),
(58, '面包', '法式牛角包', '暖屋', 8.00),
(59, '饼干', '曲奇饼干', '暖屋', 15.00),
(60, '冰淇淋', '双球冰淇淋', '暖屋', 20.00); 