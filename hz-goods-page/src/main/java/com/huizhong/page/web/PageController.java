package com.huizhong.page.web;

import com.huizhong.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @Author: cuzz
 * @Date: 2018/11/15 12:32
 * @Description:
 */
@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long spuId, Model model) {
        // 查询模型数据
        Map<String, Object> attributes = pageService.loadModel(spuId);
        // 准备模型数据
        model.addAllAttributes(attributes);
        // 返回视图
        return "item";
    }

}
