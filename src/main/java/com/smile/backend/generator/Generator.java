// package com.xp.practice.generator;

package com.smile.backend.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class Generator {
    public static void main(String[] args) {

        FastAutoGenerator.create("jdbc:mysql://localhost:3306/smile","root","331506")
                .globalConfig(builder -> {
                    builder.outputDir(System.getProperty("user.dir")+"/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.smile.backend");  // 设置父包名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("specific_libs");  // 设置需要生成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}

