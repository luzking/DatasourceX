/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.dtcenter.common.loader.hbase;

import com.dtstack.dtcenter.common.loader.hbase.pool.HbasePoolManager;
import com.dtstack.dtcenter.loader.dto.SqlQueryDTO;
import com.dtstack.dtcenter.loader.dto.source.HbaseSourceDTO;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;

/**
 * @company: www.dtstack.com
 * @Author ：Nanqi
 * @Date ：Created in 17:08 2020/7/9
 * @Description：Hbase 连接工厂
 */
@Slf4j
public class HbaseConnFactory {
    public Boolean testConn(ISourceDTO iSource) {
        HbaseSourceDTO hbaseSourceDTO = (HbaseSourceDTO) iSource;
        boolean check = false;
        Connection hConn = null;
        try {
            hConn = getHbaseConn(hbaseSourceDTO, null);
            hConn.getAdmin().getClusterStatus();
            check = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if ((hbaseSourceDTO.getPoolConfig() == null || MapUtils.isNotEmpty(hbaseSourceDTO.getKerberosConfig())) && hConn != null) {
                try {
                    hConn.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return check;
    }

    public static Connection getHbaseConn(HbaseSourceDTO source, SqlQueryDTO queryDTO) {
        if (source.getPoolConfig() == null || MapUtils.isNotEmpty(source.getKerberosConfig())) {
            return HbasePoolManager.initHbaseConn(source, queryDTO);
        }
        return HbasePoolManager.getConnection(source, queryDTO);
    }


}
