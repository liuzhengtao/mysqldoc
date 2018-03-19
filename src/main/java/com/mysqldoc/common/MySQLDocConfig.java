package com.mysqldoc.common;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.template.Engine;
import com.mysqldoc.common.handler.GlobalbHandler;
import com.mysqldoc.common.model._MappingKit;
import org.apache.log4j.Logger;

/**
 * 系统配置
 * 
 * @author vic-pc
 *
 */
public class MySQLDocConfig extends JFinalConfig {
	private static Logger logger = Logger.getLogger(MySQLDocConfig.class);
	private static Prop c = PropKit.use("config.ini");
	private static Prop p = loadConfig();
	private static Prop loadConfig() {
		Prop m = null;
		try {
			// 优先加载生产环境配置文件
			if("true".equals(c.get("devMode"))){
				m=PropKit.use("mysql_doc_config_dev.properties");
			}else{
				m=PropKit.use("mysql_doc_config_pro.properties");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return m;
	}

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(c.getBoolean("devMode", false));
		me.setJsonFactory(MixedJsonFactory.me());
	}

	@Override
	public void configEngine(Engine me) {
		if("true".equals(c.get("devMode"))){
			me.setDevMode(true);
		}else{
			me.setDevMode(false);
		}

		me.addSharedFunction("/WEB-INF/view/common/__layout.html");
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ctx"));
		me.add(new GlobalbHandler());
	}

	@Override
	public void configInterceptor(Interceptors me) {
	}

	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin druidPlugin = getDruidPlugin();
		me.add(druidPlugin);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setDialect(new Sqlite3Dialect());
		if (c.getBoolean("devMode", false)) {
			arp.setShowSql(true);
		}
		me.add(arp);
		_MappingKit.mapping(arp);
		RedisPlugin redisPlugin = new RedisPlugin("myredis","172.16.230.132");
		me.add(redisPlugin);
	}

	@Override
	public void configRoute(Routes me) {
		me.add(new FrontRoutes());
	}

	public static DruidPlugin getDruidPlugin() {
		return new DruidPlugin(p.get("jdbcUrl"), "1", "1");
	}

	/**
	 * 启动入口，运行此 main 方法可以启动项目
	 */
	public static void main(String[] args) {
		/**
		 * 特别注意：Eclipse 之下建议的启动方式
		 */
		JFinal.start("src/main/webapp", 8887, "/");
	}
}
