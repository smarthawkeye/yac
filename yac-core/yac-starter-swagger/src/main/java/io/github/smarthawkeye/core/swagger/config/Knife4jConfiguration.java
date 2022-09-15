package io.github.smarthawkeye.core.swagger.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.github.smarthawkeye.core.swagger.core.GroupInfo;
import io.github.smarthawkeye.core.swagger.props.YaKnife4jProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Knife4jConfiguration
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/24 18:29
 * @Version V0.1.0
 */
@Configuration
@EnableSwagger2WebMvc
@EnableConfigurationProperties(YaKnife4jProperties.class)
public class Knife4jConfiguration implements BeanFactoryAware {
    private static final String AUTH_KEY = "token";
    /*引入Knife4j提供的扩展类*/
    private final OpenApiExtensionResolver openApiExtensionResolver;
    private BeanFactory beanFactory;
    @Autowired
    @SuppressWarnings("all")
    private YaKnife4jProperties properties;

    @Autowired
    public Knife4jConfiguration(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean
    @ConditionalOnMissingBean
    public List<Docket> createRestApi() {
        List<GroupInfo> group = properties.getGroup();
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> dockets = new ArrayList<>();
        for (GroupInfo groupInfo : group) {
            Docket docket=new Docket(DocumentationType.SWAGGER_2)
                    .host(groupInfo.getHost())
                    .apiInfo(apiInfo(groupInfo))
                    .groupName(groupInfo.getGroupName())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(groupInfo.getBasepackage()))
                    .paths(PathSelectors.any())
                    .build()
                    //赋予插件体系
                    .extensions(openApiExtensionResolver.buildExtensions(groupInfo.getGroupName()));
            configurableBeanFactory.registerSingleton(groupInfo.getGroupName(), docket);
            dockets.add(docket);
        }


        return dockets;
    }
    public ApiInfo apiInfo(GroupInfo groupInfo){
        return new ApiInfoBuilder()
                .title(groupInfo.getTitle())
                .description(groupInfo.getDescription())
                .contact(getAuthorInfo(groupInfo))
                .version(groupInfo.getVersion())
                .build();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    private Contact getAuthorInfo(GroupInfo groupInfo) {
        return new Contact(groupInfo.getAuthor(), groupInfo.getAuthorUrl(), groupInfo.getAuthorEmail());
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> list = new ArrayList();
        list.add( new ApiKey(AUTH_KEY, AUTH_KEY, "header"));
        return list;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> list = new ArrayList();
        list.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                // 所有包含"auth"的接口不需要使用securitySchemes
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build());
        return list;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> list = new ArrayList();
        list.add(new SecurityReference("token", authorizationScopes));
        return list;
    }
}
