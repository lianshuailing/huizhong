package com.huizhong.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.common.utils.JsonUtils;
import com.huizhong.common.utils.NumberUtils;
import com.huizhong.common.vo.PageResult;
import com.huizhong.item.pojo.*;
import com.huizhong.item.vo.SkuVo;
import com.huizhong.search.client.BrandClient;
import com.huizhong.search.client.CategoryClient;
import com.huizhong.search.client.GoodsClient;
import com.huizhong.search.client.SpecificationClient;
import com.huizhong.search.pojo.Goods;
import com.huizhong.search.pojo.SearchRequest;
import com.huizhong.search.pojo.SearchResult;
import com.huizhong.search.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: cuzz
 * @Date: 2018/11/10 16:37
 * @Description:
 */
@Slf4j
@Service
public class SearchService {
    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    public Goods buildGoods(Spu spu) {

        // 获取all字段的拼接
        String all = getAll(spu);

        // 需要对sku过滤把不需要的数据去掉
        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        List<SkuVo> skuVoList = getSkuVo(skus);

        // 获取sku的价格列表
        Set<Long> prices = getPrices(skus);

        // 获取specs
        HashMap<String, Object> specs = getSpecs(spu); //  数据不全导致的 bug

        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        // 搜索条件 拼接：标题、分类、品牌
        goods.setAll(all);
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(skuVoList));
        goods.setSpecs(specs); // 数据不全导致的 bug

        return goods;
    }

    private HashMap<String, Object> getSpecs(Spu spu) {
        // 获取规格参数
        List<SpecParam> params = specificationClient.querySpecParams(null, spu.getCid3(), true, null);
        if (CollectionUtils.isEmpty(params)) {
            throw new HzException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        // 查询商品详情
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spu.getId());
        // 获取通用规格参数
        //获取通用规格参数
        Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        //获取特有规格参数
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        //定义spec对应的map
        HashMap<String, Object> map = new HashMap<>();
        //对规格进行遍历，并封装spec，其中spec的key是规格参数的名称，值是商品详情中的值
        for (SpecParam param : params) {
            //key是规格参数的名称
            String key = param.getName();
            Object value = "";

            if (param.getGeneric()) {
                //参数是通用属性，通过规格参数的ID从商品详情存储的规格参数中查出值
                value = genericSpec.get(param.getId());
                if (param.getNumeric()) {
                    //参数是数值类型，处理成段，方便后期对数值类型进行范围过滤
                    value = chooseSegment(value.toString(), param);
                }
            } else {
                //参数不是通用类型
                value = specialSpec.get(param.getId());
            }
            value = (value == null ? "其他" : value);
            //存入map
            map.put(key, value);
        }
        return map;
    }

    private List<SkuVo> getSkuVo(List<Sku> skus) {
        List<SkuVo> skuVoList = new ArrayList<>();
        for (Sku sku : skus) {
            SkuVo skuVo = new SkuVo();
            skuVo.setId(sku.getId());
            skuVo.setPrice(sku.getPrice());
            skuVo.setTitle(sku.getTitle());
            skuVo.setImage(StringUtils.substringBefore(sku.getImages(), ","));
            skuVoList.add(skuVo);
        }
        return skuVoList;

    }

    private Set<Long> getPrices(List<Sku> skuList) {
        // 查询sku
        if (CollectionUtils.isEmpty(skuList)) {
            throw new HzException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        return skuList.stream().map(Sku::getPrice).collect(Collectors.toSet());
    }

    private String getAll(Spu spu) {
        // 查询分类
        List<Category> categories = categoryClient.queryCategoryListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)) {
            throw new HzException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        // 查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new HzException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
        // 搜索字段
        String all = spu.getTitle() + StringUtils.join(names, ",") + brand.getName();
        return all;
    }



    /**
     * 参数是数值类型，处理成段，方便后期对数值类型进行范围过滤
     *
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }



    // 搜索
    public PageResult<Goods> search(SearchRequest request) {
        int page = request.getPage() - 1; // elasicsearch默认page是从0开始
        int size = request.getSize();
        // 查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        // 分页
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 过滤
        QueryBuilder query = buildBaseQuery(request);
        queryBuilder.withQuery(query);

        // 聚合分类、品牌
        String categoryAggName = "category_agg";
        String brandAggName = "brand_agg";

        // 对商品分类进行聚合  addAggregation 添加聚合
        // AggregationBuilders.terms(categoryAggName) 聚合起的名字   后面还得告知聚合的字段
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));

        // 对品牌进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));


        // 查询
        // 简单查询，不能查询聚合结果--废弃
        // Page<Goods> result = repository.search(queryBuilder.build());

        // 查询结果聚合
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        long total = result.getTotalElements();
        long totalPage = result.getTotalPages();
        List<Goods> content = result.getContent();

        // 解析聚合结果
        Aggregations aggs = result.getAggregations();
        List<Category> categories = getCategoryAggResult(aggs.get(categoryAggName));
        List<Brand> brands = getBrandAggResult(aggs.get(brandAggName));

        // 完成规格参数聚合
        // 判断商品分类数量，看是否需要对规格参数进行聚合
        List<Map<String, Object>> specs = null;
        if (categories != null && categories.size() == 1) {
            // 如果分类只剩下一个，才进行规格参数过滤
            specs = getSpecs(categories.get(0).getId(), query);
        }


        // 返回结果
        return new SearchResult(total, totalPage, result.getContent(), categories, brands, specs);
    }

    private QueryBuilder buildBaseQuery(SearchRequest request) {
        // 创建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all",request.getKey()));
        // 过滤条件
        Map<String, String> map = request.getFilter();
        for (Map.Entry<String, String> entry : map.entrySet()) {

            String key = entry.getKey();
            // 处理key
            if (!"cid3".equals(key) && ! "brandId".equals(key)) {
                key = "spec." + key + ".keyword";
            }
            queryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }
        return queryBuilder;
    }

    // 解析品牌聚合结果
    private List<Brand> getBrandAggResult(LongTerms terms) {
        try {

            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());

            List<Brand> brands = brandClient.queryBrandByIds(ids);
            return brands;
        } catch (Exception e) {
            log.error("[搜索服务]查询品牌异常", e);
            return null;
        }
    }

    // 解析商品分类聚合结果
    private List<Category> getCategoryAggResult(LongTerms terms) {
        try {

            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());

            List<Category> categories = categoryClient.queryCategoryListByIds(ids);
            return categories;
        } catch (Exception e) {
            log.error("[搜索服务]查询分类异常", e);
            return null;
        }
    }

    // 聚合规格参数
    private List<Map<String, Object>> getSpecs(Long cid, QueryBuilder query) {
        try {
            // 根据分类查询规格
            List<SpecParam> params = specificationClient.querySpecParams(null, cid, true, null);

            // 创建集合，保存规格过滤条件
            List<Map<String, Object>> specs = new ArrayList<>();

            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(query);

            // 聚合规格参数
            params.forEach(p -> {
                String key = p.getName();
                queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs." + key + ".keyword"));

            });

            // 查询
            Map<String, Aggregation> aggs = template.query(queryBuilder.build(), SearchResponse::getAggregations).asMap();

            // 解析聚合结果
            params.forEach(param -> {
                Map<String, Object> spec = new HashMap<>();
                String key = param.getName();
                spec.put("k", key);
                StringTerms terms = (StringTerms) aggs.get(key);
                spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
                specs.add(spec);
            });

            return specs;
        }catch (Exception e){
            log.error("[搜索服务]规格聚合出现异常：", e);
            return null;
        }
    }

    /**
     * 创建索引
     * @param spuId
     */
    public void createOrUpdateIndex(Long spuId) {

        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //构建goods
        Goods goods = buildGoods(spu);
        //保存数据到elasticsearch的索引库中
        // ps：在执行save保存操作过程中可能会发生(运行时)异常，第一感觉应try catch，但是抛出后到listener中再抛出会被spring捕获转换到控制台、日志里更好。
        goodsRepository.save(goods);
    }

    /**
     * 删除索引
     * @param spuId
     */
    public void deleteIndex(Long spuId) {

        goodsRepository.deleteById(spuId);
    }

}
