package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

/**
 * @Author taoshao
 * @Date 2024/5/24
 */
@Service
public class VideoDataSource implements DataSource<Object>{
    @Override
    public Page<Object> doSearch(String searchText, long pageNum, long pageSize) {
        return null;
    }
}
