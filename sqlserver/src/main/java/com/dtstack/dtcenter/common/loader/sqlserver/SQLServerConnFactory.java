package com.dtstack.dtcenter.common.loader.sqlserver;

import com.dtstack.dtcenter.common.loader.common.ConnFactory;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import com.dtstack.dtcenter.loader.source.DataBaseType;

import java.sql.Connection;

/**
 * @company: www.dtstack.com
 * @Author ：Nanqi
 * @Date ：Created in 15:30 2020/1/7
 * @Description：连接器工厂类
 */
public class SQLServerConnFactory extends ConnFactory {
    public SQLServerConnFactory() {
        driverName = DataBaseType.SQLServer.getDriverClassName();
        testSql = DataBaseType.SQLServer.getTestSql();
    }

    /**
     * 不支持开启连接池
     * @param source
     * @return
     * @throws Exception
     */
    @Override
    protected Connection getCpConn(ISourceDTO source) throws Exception {
        return super.getSimpleConn(source);
    }
}
