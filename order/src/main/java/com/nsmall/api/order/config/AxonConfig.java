package com.nsmall.api.order.config;

import com.nsmall.api.order.interceptor.CommandDispatchInterceptor;

import org.springframework.context.annotation.Configuration;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@Configuration
public class AxonConfig {
    @Autowired
	public void registerCommandDispatchInterceptor(ApplicationContext context,	CommandBus commandBus){
		commandBus.registerDispatchInterceptor(context.getBean(CommandDispatchInterceptor.class));
	}

	@Bean
	public DeadlineManager deadlineManager(org.axonframework.config.Configuration configuration, SpringTransactionManager springTransactionManager){
		return SimpleDeadlineManager.builder()
				.scopeAwareProvider(new ConfigurationScopeAwareProvider(configuration))
				.transactionManager(springTransactionManager)
				.build();

	}
}