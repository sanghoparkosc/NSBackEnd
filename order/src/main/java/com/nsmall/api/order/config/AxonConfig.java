package com.nsmall.api.order.config;

import com.nsmall.api.order.interceptor.CommandDispatchInterceptor;

import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Autowired
	public void registerCommandDispatchInterceptor(ApplicationContext context,	CommandBus commandBus){
		commandBus.registerDispatchInterceptor(context.getBean(CommandDispatchInterceptor.class));
	}
}