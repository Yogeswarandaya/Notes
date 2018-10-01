package com.wander.note.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.wander.note.web.AuthenticateFilter;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
@ComponentScan(basePackages = { "com.wander.note.controller", "com.wander.note.service", "com.wander.note.repository",
		"com.wander.note.web", "com.wander.note.validator"})
public class NoteConfiguration extends WebMvcConfigurerAdapter {
	
	@Value("${default.view.name}")
	private String defaultViewName;

	@Value("${viewresolver.prefix}")
	private String prefix;

	@Value("${viewresolver.suffix}")
	private String suffix;

	@Bean
	public ViewResolver internalViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix(prefix);
		viewResolver.setSuffix(suffix);
		return viewResolver;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName(defaultViewName);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/*").addResourceLocations("/WEB-INF/js/");
		registry.addResourceHandler("/css/*").addResourceLocations("/WEB-INF/css/");
		registry.addResourceHandler("/e/b-dist/css/*").addResourceLocations("/WEB-INF/external-plugin/bootstrap-3.3.7-dist/css/");
		registry.addResourceHandler("/e/b-dist/js/*").addResourceLocations("/WEB-INF/external-plugin/bootstrap-3.3.7-dist/js/");
		registry.addResourceHandler("/e/b-dist/fonts/*").addResourceLocations("/WEB-INF/external-plugin/bootstrap-3.3.7-dist/fonts/");
		registry.addResourceHandler("/e/f-awe/css/*").addResourceLocations("/WEB-INF/external-plugin/font-awesome/css/");
		registry.addResourceHandler("/e/f-awe/fonts/*").addResourceLocations("/WEB-INF/external-plugin/font-awesome/fonts/");
		registry.addResourceHandler("/e/f-awe/less/*").addResourceLocations("/WEB-INF/external-plugin/font-awesome/less/");
		registry.addResourceHandler("/e/f-awe/scss/*").addResourceLocations("/WEB-INF/external-plugin/font-awesome/scss/");
	}
	
	@Bean
	public FilterRegistrationBean authenticateFilter(){
		FilterRegistrationBean registrationBean=new FilterRegistrationBean();
		registrationBean.setFilter(new AuthenticateFilter());
	    registrationBean.setEnabled(true);
	    registrationBean.addUrlPatterns("/*");
	    registrationBean.setOrder(1);
	    return registrationBean;
	}

}
