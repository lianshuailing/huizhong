package com.huizhong.search.repository;

import com.huizhong.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: cuzz
 * @Date: 2018/11/10 15:47
 * @Description:
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long>{
}
