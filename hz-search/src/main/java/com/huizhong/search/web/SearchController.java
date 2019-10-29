package com.huizhong.search.web;

import com.huizhong.common.vo.PageResult;
import com.huizhong.search.pojo.Goods;
import com.huizhong.search.pojo.SearchRequest;
import com.huizhong.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: cuzz
 * @Date: 2018/11/12 15:05
 * @Description:
 */
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 搜索功能
     * @param request
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.search(request));
    }

}
