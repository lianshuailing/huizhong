package com.huizhong.item.service.impl;

import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.item.mapper.SpecGroupMapper;
import com.huizhong.item.mapper.SpecParamMapper;
import com.huizhong.item.pojo.SpecGroup;
import com.huizhong.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: cuzz
 * @Date: 2018/11/5 13:55
 * @Description:
 */
@Service
public class SpecificationServiceImpl {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(group);
        if (CollectionUtils.isEmpty(list)) {
            throw new HzException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }


    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        List<SpecParam> list = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)) {
            throw new HzException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

    public List<SpecGroup> queryListByCid(Long cid) {
        // 查询规格组
        List<SpecGroup> specGroups = queryGroupByCid(cid);
        // 查询当前分类下的参数
        List<SpecParam> specParams = querySpecParams(null, cid, null, null);

        //map的key为group_id
        Map<Long, List<SpecParam>> map = new HashMap<>();
        for (SpecParam param : specParams) {
            Long groupId = param.getGroupId();
            if (!map.containsKey(groupId)) {
                // 这个组id在map中不存在, 新增一个list
                List<SpecParam> list = new ArrayList<>();
                map.put(groupId, list);
            }
            //map.get(param.getGroupId()).add(param);
            List<SpecParam> list = map.get(groupId);
            list.add(param);
        }

        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }

        return specGroups;
    }

    public List<SpecGroup> querySpecsByCid(Long cid) {
        // 查询规格组
        List<SpecGroup> groups = this.queryGroupByCid(cid);
        if (CollectionUtils.isEmpty(groups)) {
            throw new HzException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        SpecParam param = new SpecParam();
        groups.forEach(g -> {
            // 查询组内参数
            g.setParams(this.querySpecParams(g.getId(), null, null, null));
        });
        return groups;
    }
}
