package com.wh.springframework.beans.support;

import com.wh.springframework.annotation.Component;
import com.wh.springframework.beans.config.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BeanDefinitionReader {

    //配置文件
    private Properties config = new Properties();

    private List<String> registyBeanClass = new ArrayList<>();

    //配置文件中指定需要扫描的路径
    private final String SCAN_PACKAGE="scanPackage";

    public BeanDefinitionReader(String... locations){
        try {
            //1.定位，通过URL定位找到配置文件，然后转化为文件流
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));

            //2.加载，保存为properties
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3.扫描，扫描资源位文件(class),并保存到集合中
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for(File file:classPath.listFiles()){
            if(file.isDirectory()){
                //如果是目录则递归调用，直到直到class文件
                doScanner(scanPackage+"."+file.getName());
            }else{
                if(!file.getName().endsWith(".class")){
                    continue;
                }
                String className = (scanPackage + "." + file.getName().replace(".class",""));
                //className保存到集合
                registyBeanClass.add(className);
            }
        }
    }

    public Properties getConfig(){
        return config;
    }

    /**
     * 把配置文件中扫描到的所有配置信息转换为BeanDefinition对象
     */
    public List<BeanDefinition> loadBeanDefinitions() {
        List<BeanDefinition> result = new ArrayList<>();
        try {
            for(String className : registyBeanClass){
                Class<?> beanClass = Class.forName(className);
                //如果是一个接口，是不能实例化的，不需要封装
                if(beanClass.isInterface()){
                    continue;
                }

                Annotation[] annotations = beanClass.getAnnotations();
                if(annotations.length == 0){
                    continue;
                }

                for(Annotation annotation : annotations){
                    Class<? extends Annotation> annotationType = annotation.annotationType();
                    //只考虑被@Component注解的class
                    if(annotationType.isAnnotationPresent(Component.class)){
                        //beanName有三种情况
                        //1.默认是类名首字母小写
                        //2.自定义名字（这里暂不考虑）
                        //3.接口注入
                        result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));

                        Class<?>[] interfaces = beanClass.getInterfaces();
                        for(Class<?> i : interfaces){
                            //接口和实现类的关系也需要封装
                            result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 相关属性封装到BeanDefinition
     */
    private BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    /**
     * 将单词首字母变成小写
     */
    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
