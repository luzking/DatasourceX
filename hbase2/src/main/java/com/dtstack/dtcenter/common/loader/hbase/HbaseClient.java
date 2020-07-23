package com.dtstack.dtcenter.common.loader.hbase;

import com.dtstack.dtcenter.common.enums.DataSourceType;
import com.dtstack.dtcenter.common.loader.common.AbsRdbmsClient;
import com.dtstack.dtcenter.common.loader.common.ConnFactory;
import com.dtstack.dtcenter.loader.dto.ColumnMetaDTO;
import com.dtstack.dtcenter.loader.dto.SqlQueryDTO;
import com.dtstack.dtcenter.loader.dto.source.HbaseSourceDTO;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import com.dtstack.dtcenter.loader.exception.DtLoaderException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @company: www.dtstack.com
 * @Author ：Nanqi
 * @Date ：Created in 17:06 2020/7/9
 * @Description：Hbase 客户端
 */
@Slf4j
public class HbaseClient extends AbsRdbmsClient {
    @Override
    protected ConnFactory getConnFactory() {
        return new HbaseConnFactory();
    }

    @Override
    protected DataSourceType getSourceType() {
        return DataSourceType.HBASE;
    }

    @Override
    public List<String> getTableList(ISourceDTO iSource, SqlQueryDTO queryDTO) throws Exception {
        HbaseSourceDTO hbaseSourceDTO = (HbaseSourceDTO) iSource;
        Connection hConn = null;
        Admin admin = null;
        List<String> tableList = new ArrayList<>();
        try {
            hConn = HbaseConnFactory.getHbaseConn(hbaseSourceDTO);
            admin = hConn.getAdmin();
            TableName[] tableNames = admin.listTableNames();
            if (tableNames != null) {
                for (TableName tableName : tableNames) {
                    tableList.add(tableName.getNameAsString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("获取 hbase table list 异常", e);
        } finally {
            closeAdmin(admin);
            closeConnection(hConn);
        }
        return tableList;
    }

    private static void closeConnection(Connection hConn) {
        if (hConn != null) {
            try {
                hConn.close();
            } catch (IOException e) {
                log.error("hbase 关闭连接异常", e);
            }
        }
    }

    private static void closeAdmin(Admin admin) {
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                log.error("hbase 关闭连接异常", e);
            }
        }
    }

    @Override
    public List<ColumnMetaDTO> getColumnMetaData(ISourceDTO iSource, SqlQueryDTO queryDTO) throws Exception {
        HbaseSourceDTO hbaseSourceDTO = (HbaseSourceDTO) iSource;
        Connection hConn = null;
        Table tb = null;
        List<ColumnMetaDTO> cfList = new ArrayList<>();
        try {
            hConn = HbaseConnFactory.getHbaseConn(hbaseSourceDTO);
            TableName tableName = TableName.valueOf(queryDTO.getTableName());
            tb = hConn.getTable(tableName);
            HTableDescriptor hTableDescriptor = tb.getTableDescriptor();
            HColumnDescriptor[] columnDescriptors = hTableDescriptor.getColumnFamilies();
            for(HColumnDescriptor columnDescriptor: columnDescriptors) {
                ColumnMetaDTO columnMetaDTO = new ColumnMetaDTO();
                columnMetaDTO.setKey(columnDescriptor.getNameAsString());
                cfList.add(columnMetaDTO);
            }
        } catch (IOException e) {
            throw new RuntimeException("hbase list column families error", e);
        } finally {
            closeTable(tb);
            closeConnection(hConn);
        }
        return cfList;
    }

    public static void closeTable(Table table) {
        if(table != null) {
            try {
                table.close();
            } catch (IOException e) {
                throw new RuntimeException("hbase can not close table error", e);
            }
        }
    }

    /******************** 未支持的方法 **********************/
    @Override
    public java.sql.Connection getCon(ISourceDTO source) throws Exception {
        throw new DtLoaderException("Not Support");
    }

    @Override
    public List<Map<String, Object>> executeQuery(ISourceDTO source, SqlQueryDTO queryDTO) throws Exception {
        throw new DtLoaderException("Not Support");
    }

    @Override
    public Boolean executeSqlWithoutResultSet(ISourceDTO source, SqlQueryDTO queryDTO) throws Exception {
        throw new DtLoaderException("Not Support");
    }

    @Override
    public List<String> getColumnClassInfo(ISourceDTO source, SqlQueryDTO queryDTO) throws Exception {
        throw new DtLoaderException("Not Support");
    }

    @Override
    public List<List<Object>> getPreview(ISourceDTO iSource, SqlQueryDTO queryDTO) throws Exception {
        throw new DtLoaderException("Not Support");
    }
}