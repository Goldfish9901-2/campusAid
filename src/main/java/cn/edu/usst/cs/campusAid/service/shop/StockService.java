package cn.edu.usst.cs.campusAid.service.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;

public interface StockService {
    /** 比对系统内部（数据库）是否含有相应商家信息，验证商家身份
     * @param name 商家姓名
     * @param password 密码
     */
    String verify(String name, String password);

    /** <h5>商家添加商品到店铺</h5>
     * <p>如果先前没有该产品，则创建</p>
     * <p>如果有的话，则将数目相加</p>
     * <p>如果描述不为空，则更新描述</p>
     * @param shopName 商家名称
     * @param productTransaction 产品
     */
    Long addProductToShop(String shopName, ProductTransaction productTransaction);

}
