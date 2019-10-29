package com.huizhong.search.listener;

import com.huizhong.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 98050
 * @Time: 2018-10-21 12:57
 * @Feature: mq监听器，消费者
 */
@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;

    /**
     * 处理insert和update的消息
     *
     * // durable = "true" 是否持久化
     * // key = {"item.insert","item.update"} 路由键
     * @param id
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "hz.create.index.queue",durable = "true"), //队列持久化
            exchange = @Exchange(
                    value = "hz.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert","item.update"}
    ))
    public void listenCreate(Long id) throws Exception{
        if (id == null){
            return;
        }
        //创建或更新索引
        searchService.createOrUpdateIndex(id);
    }



    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "hz.delete.index.queue",durable = "true"), //队列持久化
            exchange = @Exchange(
                    value = "hz.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.delete"}
    ))
    public void listenDelete(Long spuId){
        if (spuId == null){
            return;
        }

        //删除索引
        searchService.deleteIndex(spuId);
    }
}
