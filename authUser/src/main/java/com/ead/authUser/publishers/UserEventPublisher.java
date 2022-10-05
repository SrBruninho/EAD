package com.ead.authUser.publishers;

import com.ead.authUser.dtos.UserEventDTO;
import com.ead.authUser.enums.ActionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value(value = "${ead.broker.exchange.userEvent}")
    private String exchangeUserEvent;

    public void publishUserEvent( UserEventDTO userEventDTO, ActionType actionType ){
        userEventDTO.setActionType( actionType.toString() );
        rabbitTemplate.convertAndSend( exchangeUserEvent, "",userEventDTO );
    }
}
