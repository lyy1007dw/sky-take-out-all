package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author candong
 */
@Data
public class EmployeePageQueryDTO implements Serializable {

    /**
     * 用户名
     */
    private String name;

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页显示记录数
     */
    private int pageSize;

}
