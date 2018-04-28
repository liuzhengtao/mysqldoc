package com.mysqldoc.parsedb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.cache.EhCache;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.mysqldoc.common.model.Configure;

public class ParseDbService {
    Gson gson = new Gson();
	public static final ParseDbService service = new ParseDbService();

	/**
	 * 验证连接是否有效
	 * 
	 * @return 有效 true
	 */
	public boolean validateConn(Configure config) {
		boolean result = false;
		DruidPlugin druidPlugin;
		// 验证数据源是否存在
		Config c = DbKit.getConfig("name".concat(config.getId().toString()));
		String driver= "oracle.jdbc.driver.OracleDriver";
		// 创建数据源
		if (null == c) {
			if(config.getDbtype()==1){
				 String url = "jdbc:oracle:thin:@//" + config.getIp()+":"+config.getPort() + "/"+config.getSID();
				 druidPlugin = new DruidPlugin(url, config.getUsername(), config.getPassword(),driver);
			}else{
				druidPlugin = new DruidPlugin(
						"jdbc:mysql://" + config.getIp()
								+ "/information_schema?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull",
						config.getUsername(), config.getPassword());
			}

			try {
				druidPlugin.start();
				DataSource dataSource = druidPlugin.getDataSource();
				Connection conn = dataSource.getConnection();
				conn.close();
				Config cf;
				// 添加链接
				if(config.getDbtype()==1){
					cf = new Config("name".concat(config.getId().toString()), dataSource, new OracleDialect(), false,
							false, DbKit.DEFAULT_TRANSACTION_LEVEL, new CaseInsensitiveContainerFactory(true),
							new EhCache());
				}else{
					cf = new Config("name".concat(config.getId().toString()), dataSource, new MysqlDialect(), false,
							false, DbKit.DEFAULT_TRANSACTION_LEVEL, new CaseInsensitiveContainerFactory(false),
							new EhCache());
				}
				DbKit.addConfig(cf);
				result = true;
			} catch (Exception e) {
				druidPlugin.stop();
				e.printStackTrace();
			}
		} else
			result = true;
		return result;
	}

	/**
	 * 数据库
	 * 
	 * @param config
	 * @return
	 */
	public List<Record> listDatabases(Configure config) {
		// 验证连接
		if (!validateConn(config)) {
			return null;
		}
		String configName = "name".concat(config.getId().toString());
		List<Record> list=null;
		// 数据库
		if(config.getDbtype()==0){
			list = Db.use(configName).find(
					"SELECT SCHEMA_NAME name,DEFAULT_CHARACTER_SET_NAME charset,DEFAULT_COLLATION_NAME sortRules FROM SCHEMATA");
		}

		return list;
	}

	/**
	 * 数据库表
	 * 
	 * @return
	 */
	public List<Record> listTable(Configure config, String tableName) {
		// 验证连接
		if (!validateConn(config)) {
			return null;
		}
		String configName = "name".concat(config.getId().toString());
		List<Record> columns;
		// 获取列
		if(config.getDbtype()==0){
			columns = Db.use(configName).find(
					"SELECT TABLE_NAME 'tableName',COLUMN_NAME 'name',COLUMN_DEFAULT 'dft',IS_NULLABLE 'isNull',COLUMN_TYPE 'dataType',COLUMN_COMMENT 'comment' FROM COLUMNS where TABLE_SCHEMA=?",
					tableName);
		}else{
			columns = Db.use(configName).find(
					"select DISTINCT b.table_name tableName,b.column_name name,a.DATA_TYPE dataType,b.comments \"comment\" from user_tab_columns a INNER JOIN user_col_comments b on a.table_name = b.table_name" +
							" where a.column_name=b.column_name");
		}

		Map<String, List<Record>> map = new HashMap<String, List<Record>>();
		List<Record> l;
		for (Record r : columns) {
			String key = r.getStr("tableName");
			if (map.containsKey(key)) {
				l = map.get(key);
			} else {
				l = new ArrayList<>();
			}
			l.add(r);
			map.put(key, l);
		}
		// 获取表
		List<Record> tables = null;
		if(config.getDbtype()==0){
			tables= Db.use(configName)
					.find("SELECT TABLE_NAME name,TABLE_COMMENT comment FROM TABLES WHERE TABLE_SCHEMA=?", tableName);
		}else{
			tables= Db.use(configName).find("select t.table_name name,t.comments \"comment\" from USER_TAB_COMMENTS t");
		}
		// 分配列
		for (Record t : tables) {
			t.set("list", map.get(t.getStr("name")));
		}
		return tables;
	}
}
