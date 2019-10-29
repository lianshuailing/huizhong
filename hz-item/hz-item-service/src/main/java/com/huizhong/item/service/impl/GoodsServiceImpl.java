package com.huizhong.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huizhong.common.dto.CartDTO;
import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.common.vo.PageResult;
import com.huizhong.item.mapper.SkuMapper;
import com.huizhong.item.mapper.SpuDetailMapper;
import com.huizhong.item.mapper.SpuMapper;
import com.huizhong.item.mapper.StockMapper;
import com.huizhong.item.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: cuzz
 * @Date: 2018/11/6 19:46
 * @Description: 商品货物
 */
@Slf4j
@Service
public class GoodsServiceImpl {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private BrandServiceImpl brandService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;


    public PageResult<Spu> querySpuPage(Integer page, Integer rows, Boolean saleable, String key) {
        // 1、查询SPU
        // 分页,最多允许查100条
        PageHelper.startPage(page, Math.min(rows, 200));

        // 创建查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        // 是否过滤上下架
        if (saleable != null) {
            criteria.orEqualTo("saleable", saleable);
        }

        // 是否模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        // 默认排序
        example.setOrderByClause("last_update_time DESC");

        // 查询
        List<Spu> spus = spuMapper.selectByExample(example);

        // 判断
        if (CollectionUtils.isEmpty(spus)) {
            throw new HzException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        // 解析分类和
        loadCategoryAndBrandName(spus);

        // 解析分页的结果
        PageInfo<Spu> info = new PageInfo<>(spus);
        return new PageResult<>(info.getTotal(), spus);
    }

    private void loadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
            // 处理分类名称
            //List<Category> categories = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            // 1 处理分类名称  这里用java8的新特性，利用流来 获得名称流  再利用map reduce 最终处理为我们想要的List<String> 但是用reduce的话会产生大量字符串
            // 2 处理分类名称  这里用java8的新特性，利用流来 获得名称流  再利用collect(Collectors.toList()) 转为List<String>
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());

            // String工具类用“/” 把内容分类名字拼接起来 实现页面效果
            spu.setCname(StringUtils.join(names, "/"));
            // 处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        }
    }

    /**
     * 新增商品----废弃
     * @param spu
     */
    @Transactional
    public void saveGoods0(Spu spu) {
        // 新增spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        int count = spuMapper.insert(spu);
        if (count != 1) {
            throw new HzException(ExceptionEnum.GOODS_SAVE_ERROR);
        }

        // 新增spu detail
        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        count = spuDetailMapper.insert(detail);
        if (count != 1) {
            throw new HzException(ExceptionEnum.GOODS_SAVE_ERROR);
        }

        // 新增sku
        // 新增库存
        List<Stock> list = new ArrayList<>();
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            // 保存sku
            sku.setSpuId(spu.getId());
            // 初始化时间
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            count = skuMapper.insert(sku);
            if (count != 1) {
                throw new HzException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
            // 保存库存信息  得写到这个for里面是因为，在这个里面sku新增完，下面sku.getId()才能获得id。
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            list.add(stock);
        }
        // 批量新增库存
        count = stockMapper.insertList(list);
        if (count != list.size()) {
            throw new HzException(ExceptionEnum.GOODS_SAVE_ERROR);
        }

    }

    /**
     * 新增商品
     * @param spu
     */
    @Transactional
    public void saveGoods(Spu spu) {
        // 新增spu
        saveSpu(spu);
        // 新增detail
        saveSpuDetail(spu);
        // 新增sku和库存
        saveSkuAndStock(spu);

        // 发送消息
        this.sendMessage(spu.getId(), "insert");
    }

    private void saveSkuAndStock(Spu spu) {
        List<Stock> list = new ArrayList<>();
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            // 保存sku
            sku.setSpuId(spu.getId());
            // 初始化时间
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            //sku为什么不批量新增？    因为下面要用新增完sku后返回的id。若像下面先加入到list再批量新增，下面保存库存用id还得遍历list。
            int count = skuMapper.insert(sku);
            if (count != 1) {
                throw new HzException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
            // 保存库存信息  得写到这个for里面是因为，在这个里面sku新增完，下面sku.getId()才能获得id。
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            list.add(stock);
        }
        // 批量新增库存
        int count = stockMapper.insertList(list);
        if (count != list.size()) {
            throw new HzException(ExceptionEnum.GOODS_SAVE_ERROR);
        }

    }

    private void saveSpuDetail(Spu spu) {
        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        int count = spuDetailMapper.insert(detail);
        if (count != 1) {
            throw new HzException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }

    private void saveSpu(Spu spu) {
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        int count = spuMapper.insert(spu);
        if (count != 1) {
            throw new HzException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }


    public List<Sku> querySkusBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skus)) {
            throw new HzException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        return skus;
    }

    /**
     * @param spuId SPU的id
     * @return
     */
    public SpuDetail querySpuDetailById(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if (spuDetail == null) {
            throw new HzException(ExceptionEnum.SPU_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    public Spu querySpuByid(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if (spu == null) {
            throw new HzException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        // 查询sku
        spu.setSkus(querySkusBySpuId(spuId));
        // 查询detail
        spu.setSpuDetail(querySpuDetailById(spuId));
        return spu;
    }


    // 封装一个发送消息的方法
    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            log.error("{}商品消息发送异常，商品id：{}", type, id, e);
        }
    }


    /**
     * 购物车，根据集合ids查询所有sku信息，并查询最新的相关库存。
     * @param ids
     * @return
     */
    public List<Sku> querySkuListByIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)) {
            throw new HzException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        // 查询库存
        List<Stock> stocks = stockMapper.selectByIdList(ids);
        Map<Long, Integer> map = stocks.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skus.forEach(sku -> sku.setStock(map.get(sku.getId())));
        return skus;
    }


    /**
     *  扣库存
     * @param carts
     */
    @Transactional
    public void decreaseStock(List<CartDTO> carts) {
        for (CartDTO cart : carts) {
            int count = stockMapper.decreaseStock(cart.getSkuId(), cart.getNum());
            if (count != 1) {
                throw new HzException(ExceptionEnum.STOCK_NOT_ENOUGH);
            }
        }
    }
}
