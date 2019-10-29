package com.huizhong.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author shkstart
 * @create 2019-07-20 17:17
 */

@Table(name="tb_category")
@Data
public class Category  implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;
}
