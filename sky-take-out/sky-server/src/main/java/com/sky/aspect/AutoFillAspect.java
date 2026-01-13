package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 切面类，用于实现自动填充功能
 * @author can dong
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点 表示 com.sky.mapper 包下的所有方法，并且该方法有 AutoFill 注解
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPt(){}

    /**
     * 前置通知
     */
    @Before("autoFillPt()")
    public void autoFill(JoinPoint joinPoint){
        log.info("公共字段自动填充开始...");
        // 获取当前方法操作数据库的类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType value = autoFill.value();

        // 获取方法参数 -- 实体对象（约定方法的第一个参数为实体参数）
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];

        // 要赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 根据对应的数据库操作类型，为对应的字段赋值
        if(value == OperationType.INSERT){
            // 插入
            // setCreateTime setUpdateTime setCreateUser setUpdateUser
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            // 更新
            // setUpdateTime setUpdateUser
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 赋值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);

            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        log.info("公共字段填充完成...");
    }
}
