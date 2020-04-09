package com.dtstack.dtcenter.common.loader.mq.emq;

import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 2020/2/25
 * Company: www.dtstack.com
 *
 * @author xiaochen
 */
public class EMQUtils {
    private static Logger logger = LoggerFactory.getLogger(EMQUtils.class);


    public static Boolean checkConnection(String address, String userName, String password) {

        String clientId = "DTSTACK_" + System.currentTimeMillis();
        try (MemoryPersistence persistence = new MemoryPersistence();
             MqttClient sampleClient = new MqttClient(address, clientId, persistence);
        ) {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            if (StringUtils.isNotBlank(userName)) {
                connOpts.setUserName(userName);
            }
            if (StringUtils.isNotBlank(password)) {
                connOpts.setPassword(password.toCharArray());
            }
            sampleClient.connect(connOpts);
            sampleClient.disconnect();
            return true;
        } catch (MqttException e) {
            logger.error("connect to emq error", e);
        }
        return false;
    }


//    public static void main(String[] args) {
//        System.out.println(checkConnection("tcp://172.16.8.197:1883", null, null));
//
//    }
}
