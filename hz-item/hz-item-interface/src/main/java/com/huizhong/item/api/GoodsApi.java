package com.huizhong.item.api;

import com.huizhong.common.dto.CartDTO;
import com.huizhong.common.vo.PageResult;
import com.huizhong.item.pojo.Sku;
import com.huizhong.item.pojo.Spu;
import com.huizhong.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: cuzz
 * @Date: 2018/11/9 16:03
 * @Description:
 */
public interface GoodsApi {
    /**
     * 分页查询spu
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);


    /**
     * 根据spu的id查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long id);


    /**
     * 根据spu商品id查询详情
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id") Long id);


    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);



    @GetMapping("sku/list/ids")
    List<Sku> querySkuByIds(@RequestParam("ids") List<Long> ids);



    /**
     * 减库存接口
     */
    @PostMapping("stock/decrease")
    void decreaseStock(@RequestBody List<CartDTO> carts);

}
