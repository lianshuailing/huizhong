package com.huizhong.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author shkstart
 * @create 2019-08-09 9:08
 */
@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    @Id
    private Long id; // spuId

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all; // 所有需要被搜索的信息，包含标题，分类，甚至品牌

    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;// 卖点

    private Long brandId;// 品牌id  @Field 可以不写，默认spring data elasticsearch会给补上默认的@Filed，有特殊要求设置，则必须写
    private Long cid1;// 1级分类id
    private Long cid2;// 2级分类id
    private Long cid3;// 3级分类id
    private Date createTime;// 创建时间  参考京东，按新品排序   也是一个过滤条件
    private Set<Long> price;// 价格

    @Field(type = FieldType.Keyword, index = false)
    private String skus;// sku信息的json结构

    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
}
