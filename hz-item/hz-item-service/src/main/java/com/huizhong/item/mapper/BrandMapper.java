package com.huizhong.item.mapper;

import com.huizhong.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author shkstart
 * @create 2019-07-25 20:40
 */
public interface BrandMapper extends Mapper<Brand>, IdListMapper<Brand, Long> {

    //通用Mapper只能处理单表，也就是Brand的数据，因此手动编写一个方法及sql，实现中间表的新增：
    @Insert("INSERT INTO tb_category_brand VALUES (#{cid}, #{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    //就一个参数时，@Param("cid") 可省，建议不省略。
    @Select("SELECT b.* FROM tb_brand b INNER JOIN tb_category_brand cb ON b.id = cb.brand_id WHERE cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);
}
