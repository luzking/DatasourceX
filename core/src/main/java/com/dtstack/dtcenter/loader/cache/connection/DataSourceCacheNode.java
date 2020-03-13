package com.dtstack.dtcenter.loader.cache.connection;

import com.dtstack.dtcenter.common.exception.DtCenterDefException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @company: www.dtstack.com
 * @Author ：Nanqi
 * @Date ：Created in 16:27 2020/3/4
 * @Description：数据源具体节点信息
 */
@Data
@Builder
@Slf4j
public class DataSourceCacheNode {
    /**
     * 数据源类型
     */
    private Integer sourceType;

    /**
     * 连接信息
     */
    private Connection connection;

    /**
     * 关闭数据库连接
     */
    public void close() {
        if (connection != null) {
            try {
                log.info("close connection sourceType = {}", sourceType);
                connection.close();
            } catch (SQLException e) {
                throw new DtCenterDefException("缓存连接关闭失败", e);
            }
        }
    }
}