package cn.edu.usst.cs.campusAid.controller.shop;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.shop.StockService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 *
 */
@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final UploadFileSystemService uploadFileSystemService;
    private final ExceptionService exceptionService;
    private final StockService stockService;

    public StockController(UploadFileSystemService uploadFileSystemService, ExceptionService exceptionService, StockService stockService) {
        this.uploadFileSystemService = uploadFileSystemService;
        this.exceptionService = exceptionService;
        this.stockService = stockService;
    }


    /**
     * 商户登录
     *
     * @param shopName 商户名
     * @param password 密码(商户暂时没有办法通过邮箱验证)
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String shopName,
            @RequestParam String password,
            HttpSession session
    ) {
        session.setAttribute(
                SessionKeys.SHOP_NAME,
                stockService.verify(shopName, password)
        );
        return ResponseEntity.ok("登录成功");
    }

    /**
     * <p>商家上架或补给商品</p>
     *
     * @param shopName           商户名，从会话中获取
     * @param productTransaction 包含商品信息和数量的交易数据对象
     * @return 响应实体表示操作结果
     */
    @PostMapping("/supply")
    public ResponseEntity<String> supply(
            @SessionAttribute(SessionKeys.SHOP_NAME) String shopName,
            @RequestBody ProductTransaction productTransaction
    ) {
        var productId = stockService.addProductToShop(shopName, productTransaction);
        return ResponseEntity.ok("商品添加成功 编号:" + productId);
    }

    /**
     * 上传商品图片
     */
    @PostMapping("/extra/upload")
    public ResponseEntity<String> uploadPost(
            @RequestParam String goodName,
            @RequestParam("file") MultipartFile file,
            @SessionAttribute(SessionKeys.SHOP_NAME) String shopName
    ) {
        File dir = uploadFileSystemService.getProductDir(
                shopName, goodName
        );
        var loc = uploadFileSystemService.uploadFile(dir, file);
        return ResponseEntity.ok(loc);
    }

}
