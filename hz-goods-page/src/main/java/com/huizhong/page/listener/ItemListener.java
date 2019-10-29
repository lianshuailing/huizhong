package com.huizhong.page.listener;

import com.huizhong.page.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListener {
    @Autowired
    private PageService pageService;

    // durable = "true" 是否持久化
    // key = { "item.insert", "item.update" } 路由键
    @RabbitListener(
            bindings = @QueueBinding(
                value = @Queue(name = "page.item.insert.queue", durable = "true"),
                exchange = @Exchange(name = "hz.item.exchange", type = ExchangeTypes.TOPIC),
                key = { "item.insert", "item.update" }
            )
    )
    public void listenInsertOrUpdate(Long spuId) {
        if (spuId == null) {
            return;
        }
        pageService.createHtml(spuId);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                value = @Queue(name = "page.item.delete.queue", durable = "true"),
                exchange = @Exchange(name = "hz.item.exchange", type = ExchangeTypes.TOPIC),
                key = { "item.delete" }
            )
    )
    public void listenDelete(Long spuId) {
        if (spuId == null) {
            return;
        }
        // 处理消息, 对静态页进行删除
        pageService.deleteHtml(spuId);
    }
}
