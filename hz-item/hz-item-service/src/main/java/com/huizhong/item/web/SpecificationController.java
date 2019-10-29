package com.huizhong.item.web;

import com.huizhong.item.pojo.SpecGroup;
import com.huizhong.item.pojo.SpecParam;
import com.huizhong.item.service.impl.SpecificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: shkstart
 * @Date: 2018/11/5 13:58
 * @Description:
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationServiceImpl specificationService;



    /**
     * 根据分类cid查询规格组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specificationService.queryGroupByCid(cid));
    }

    /**
     * 查询规格 @RequestParam从请求中获得参数，不是从url中获得参数。
     * @param gid 组ID  可不传
     * @param cid 分类ID  可不传
     * @param searching 是否用于搜索 可不传
     * @param generic 是否通用的   可不传
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParams(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic){
        return ResponseEntity.ok(specificationService.querySpecParams(gid, cid, searching, generic));
    }

    /**
     * 根据分类查询规格组及组内分类
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specificationService.querySpecsByCid(cid));
    }

    /**
     * 根据分类查询规格组及组内参数
     * @param cid
     * @return
     */
    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid) {
        return ResponseEntity.ok(specificationService.queryListByCid(cid));
    }
}
