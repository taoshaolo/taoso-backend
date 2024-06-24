package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;
import org.apache.poi.ss.formula.functions.T;

/**
 * 数据源接口（新接入的数据源必须实现）
 *
 * @Author taoshao
 * @Date 2024/5/24
 */
public interface DataSource<T> {

    /**
     * 搜索
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
