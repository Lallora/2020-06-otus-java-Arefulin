package ru.otus;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    //https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html

    @Override
    protected Class<?>[] getRootConfigClasses() {
        // определяет бины ответственные за бизнес слой и слой хранения данных
        // return new Class<?>[]{RootConfig.class}
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        // определяет бины работающие веб
        // (если DispatcherServlet один, то getServletConfigClasses может отвечать за всё,
        // поэтому getRootConfigClasses у нас будет возвращать null)
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        // ссылка на корень нашего приложения
        return new String[]{"/"};
    }



    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return new Filter[]{encodingFilter};
    }

    @Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        final DispatcherServlet dispatcherServlet = (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }
}