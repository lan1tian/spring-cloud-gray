package cn.springlcoud.gray.event.server;

import cn.springlcoud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-01-30 12:49
 */
public interface GrayEventSender {

    void send(GrayEvent grayEvent);


    public static class Default implements GrayEventSender {

        @Override
        public void send(GrayEvent grayEvent) {

        }
    }

}
