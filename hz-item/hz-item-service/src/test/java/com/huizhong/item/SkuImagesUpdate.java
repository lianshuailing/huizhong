package com.huizhong.item;

import com.huizhong.HzItemService;
import com.huizhong.item.mapper.SkuMapper;
import com.huizhong.item.pojo.Sku;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shkstart
 * @create 2019-08-15 18:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HzItemService.class)
@EnableAutoConfiguration
public class SkuImagesUpdate {
    @Resource
    private SkuMapper skuMapper;

    @Test
    public void updateImages(){
        List<Sku> skus = skuMapper.selectAll();
        for (Sku sku : skus) {
            String image = sku.getImages().split(",")[0];
            image.replace("leyou","huizhong");
            sku.setImages(image);
            skuMapper.updateByPrimaryKeySelective(sku);
        }
    }
}
