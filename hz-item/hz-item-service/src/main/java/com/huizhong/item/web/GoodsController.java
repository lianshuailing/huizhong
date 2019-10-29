package com.huizhong.item.web;

import com.huizhong.common.dto.CartDTO;
import com.huizhong.common.vo.PageResult;
import com.huizhong.item.pojo.Sku;
import com.huizhong.item.pojo.Spu;
import com.huizhong.item.pojo.SpuDetail;
import com.huizhong.item.service.impl.GoodsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: cuzz
 * @Date: 2018/11/6 19:50
 * @Description:
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsServiceImpl goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {

        return ResponseEntity.ok(goodsService.querySpuPage(page, rows, saleable, key));
    }

    /**
     * 新增商品
     * @param spu
     * @return
     */
    @PostMapping("goods")   //前端传的json对象，所以后台得加上@RequestBody自动映射数据到spu对象上
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 根据id查询商品细节的方法
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(goodsService.querySpuDetailById(id));

    }

    /**
     * 根据spuId查询所有sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuList(@RequestParam("id") Long id){
        return ResponseEntity.ok(goodsService.querySkusBySpuId(id));
    }

    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(goodsService.querySpuByid(id));
    }


    /**
     * 根据集合ids查询所有sku信息
     * @param ids
     * @return
     */
    @GetMapping("sku/list/ids")
    public ResponseEntity<List<Sku>> querySkuListByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(goodsService.querySkuListByIds(ids));
    }

    /**
     * 减库存
     * @param carts
     * @return
     */
    @PostMapping("stock/decrease")
    public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDTO> carts) {
        goodsService.decreaseStock(carts);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
