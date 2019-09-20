package com.oseasy.com.pcore.common.persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/4.
 */
public class PageMap {
    private Map<String, Object>  pageMap;

    public Map<String, Object> getPageMap(Page page) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", page.getList());
        map.put("pageNo", page.getPageNo());
        map.put("pageSize", page.getPageSize());
        map.put("footer", page.getFooter());
        map.put("total", page.getCount());
        return map;
    }
}
