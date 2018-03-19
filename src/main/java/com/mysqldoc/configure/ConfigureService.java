package com.mysqldoc.configure;

import java.util.List;

import com.google.gson.Gson;
import com.mysqldoc.common.model.Configure;

/**
 * 数据库连接配置
 * 
 * @author vic-pc
 *
 */
public class ConfigureService {
    public Gson gson = new Gson();
	public static final ConfigureService service = new ConfigureService();
	private final Configure dao = new Configure();

	public List<Configure> list() {
		List<Configure> configures = dao.find("select * from configure");
		return configures;

	}

	public Configure findById(Integer id) {
		return dao.findById(id);
	}
	
	public boolean delete(Integer id){
		return dao.deleteById(id);
	}
}
