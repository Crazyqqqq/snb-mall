package com.mmall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @program: mmall
 * @description: 高精度测试类
 * @author: snb
 * @create: 2019-02-06 22:02
 */
public class BigDecimalTest {

    @Test
    public void test1(){
        System.out.println(0.05+0.01);//0.060000000000000005
        System.out.println(1.0-0.42);//0.5800000000000001
    }

    @Test
    public void test2(){
        BigDecimal  b1=new BigDecimal(0.05);
        BigDecimal  b2=new BigDecimal(0.01);
        System.out.println(b1.add(b2));//06000000000000000298372437868010820238851010799407958984375
    }

    @Test
    public void test3(){
        BigDecimal  b1=new BigDecimal("0.05");
        BigDecimal  b2=new BigDecimal("0.01");
        System.out.println(b1.add(b2));//0.06
    }
}
