package com.huizhong.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.common.vo.PageResult;
import com.huizhong.item.mapper.BrandMapper;
import com.huizhong.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author shkstart
 * @create 2019-07-25 20:42
 */
@Service
public class BrandServiceImpl {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //开始分页 该pagehelper 也是tk.mapper通用mapper作者写的，挺智能，可以独立使用，不必非得跟tk.mapper一起。
        //不用再写分页sql语句，
        PageHelper.startPage(page, rows);
        //brandMapper.selectAll(); //假如该处是查询单表所有记录，pagehelper会利用mybatis的拦截器自动在seleall 后面补充limit ...语句完成分页。

        //过滤
        /**
         * select * from xx where name like "%zhang%" or letter == 'x' order by id desc;
         */
        Example example = new Example(Brand.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("name","%" + key +"%").orEqualTo("letter", key.toUpperCase());
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            //orderByClause 排序子句（也就是sql子句）
            example.setOrderByClause(orderByClause);
        }

//        //查询方式1 不用强转
//        List<Brand> list = brandMapper.selectByExample(example);
//        if(CollectionUtils.isEmpty(list)){
//            throw new HzException(ExceptionEnum.BRAND_NOT_FOUND);
//        }
//        //解析查询结果  PageInfo看源码
//        PageInfo<Brand> info = new PageInfo<>(list);
//        // 返回结果
//        return new PageResult<>(info.getTotal(), list);

        // 查询方式2
        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }


    /**
     * 新增品牌，这里注意：不仅要新增品牌，还要维护品牌和商品分类的中间表。代码逻辑来维护。
     * // 传入 "1,2,3"的字符串可以解析为列表
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {

//        //方式1：
//        //新增品牌
//        brand.setId(null);
//        int count = brandMapper.insert(brand);
//        if(count != 1){
//            throw new HzException(ExceptionEnum.BRAND_XINZENG_FAILD);
//        }
//        //新增品牌分类中间表
//        for(Long cid : cids){
//            count = brandMapper.insertCategoryBrand(cid, brand.getId());
//            if(count != 1){
//                throw new HzException(ExceptionEnum.BRAND_XINZENG_FAILD);
//            }
//        }

        //方式2：
        //新增品牌信息
        brandMapper.insertSelective(brand);
        //新增品牌和分类中间表
        for(Long cid : cids){
            brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    public Brand queryById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null) {
            throw new HzException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }


    /**
     * 查询品牌by category ID
     * @param cid
     * @return
     */
    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> list = brandMapper.queryByCategoryId(cid);
        if (CollectionUtils.isEmpty(list)) {
            throw new HzException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;

    }

    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> list = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)) {
            throw new HzException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }
    
    
    
}
