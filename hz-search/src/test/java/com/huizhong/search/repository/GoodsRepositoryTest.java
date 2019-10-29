package com.huizhong.search.repository;

import com.huizhong.common.vo.PageResult;
import com.huizhong.item.pojo.Spu;
import com.huizhong.search.client.GoodsClient;
import com.huizhong.search.pojo.Goods;
import com.huizhong.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: cuzz
 * @Date: 2018/11/10 15:55
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    GoodsClient goodsClient;


    @Autowired
    private SearchService searchService;

    @Test
    public void testCreateIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }
    @Test
    public void loadData() {
        int page = 1;
        int size = 0;
        int rows = 100;
        do {//执行循环
            // 查询spu信息
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> spuList = result.getItems();
            // 构建成goods
            List<Goods> goodList = spuList.stream()
                    .map(searchService::buildGoods).collect(Collectors.toList());
            size = spuList.size();
            this.goodsRepository.saveAll(goodList);
            page++;
        } while (size == 100);//当不为100时即末页，结束
    }
}