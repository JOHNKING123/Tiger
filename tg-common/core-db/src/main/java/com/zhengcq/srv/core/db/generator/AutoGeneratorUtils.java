package com.zhengcq.srv.core.db.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.zhengcq.srv.core.common.utils.PropertiesLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by clude on 3/17/17.
 */
public class AutoGeneratorUtils {

    final static String SUPER_ENTITY_CLASS = "com.zhengcq.srv.core.db.base.BaseModel";
    final static String SUPER_MAPPER_CLASS = "com.zhengcq.srv.core.db.base.BaseDao";
    final static String SUPER_SERVICE_CLASS = "com.zhengcq.srv.core.db.base.BaseService";
    final static String SUPER_SERVICE_IMPL_CLASS = "com.zhengcq.srv.core.db.base.BaseServiceImpl";

    final static String[] TABLE_TO_GENERATE_ARRAY = new String[] {"ba_url"};



    //模块名称
    final static String MODULE_NAME = "client1";
    //公共包名称
    final static String PACKAGE_PARENT = "com.zhengcq.srv";

    public static void main (String [] args) {
        AutoGeneratorUtils.getGenerator();
    }

    public static AutoGenerator getGenerator(){
        AutoGenerator mpg = new AutoGenerator();

        PropertiesLoader properties = new PropertiesLoader("db/application.properties");

//        System.out.println( properties.getProperty("spring.datasource.url") );

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("./");
        gc.setFileOverride(true);
        gc.setActiveRecord(false);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setAuthor("Yoorstore");


        // 自定义文件命名，注意 %s 会自动填充表实体属性！
         gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        gc.setServiceImplName("%sService");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert(){
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName(properties.getProperty("spring.datasource.driver-class-name"));
        dsc.setUsername(properties.getProperty("spring.datasource.username"));
        dsc.setPassword(properties.getProperty("spring.datasource.password"));
        dsc.setUrl(properties.getProperty("spring.datasource.url"));
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setTablePrefix(new String[]{""});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(TABLE_TO_GENERATE_ARRAY); // 需要生成的表
        //strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 字段名生成策略
        strategy.setFieldNaming(NamingStrategy.underline_to_camel);
        // 自定义实体父类
        strategy.setSuperEntityClass(SUPER_ENTITY_CLASS);
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(new String[] { "id", "enable", "remark", "created_by", "created_time", "updated_by", "updated_time" });
        // 自定义 mapper 父类
        strategy.setSuperMapperClass(SUPER_MAPPER_CLASS);
        // 自定义 service 父类
         strategy.setSuperServiceClass(SUPER_SERVICE_CLASS);
        // 自定义 service 实现类父类
         strategy.setSuperServiceImplClass(SUPER_SERVICE_IMPL_CLASS);
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuliderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGE_PARENT);
        pc.setModuleName(MODULE_NAME);
        pc.setEntity("model");
        pc.setMapper("dao"); // 输出目录到Dao
        pc.setXml("mapper"); // 输出Xml目录到Mapper
        pc.setServiceImpl("service");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        // 自定义 xxList.jsp 生成
//        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
//        focList.add(new FileOutConfig("/template/list.jsp.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return "D://my_" + tableInfo.getEntityName() + ".jsp";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
//         tc.setController("...");
         tc.setEntity("/tpl/model.java.vm");
         tc.setMapper("/tpl/dao.java.vm");
        tc.setServiceImpl("/tpl/serviceImpl.java.vm");
         tc.setXml("/tpl/mapper.xml.vm");
//         tc.setService("...");
//         tc.setServiceImpl("...");
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

        // 打印注入设置
        //System.err.println(mpg.getCfg().getMap().get("abc"));

        return mpg;
    }
}
