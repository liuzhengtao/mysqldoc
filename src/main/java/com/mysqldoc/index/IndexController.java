package com.mysqldoc.index;

import java.util.List;

import com.google.gson.Gson;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.mysqldoc.common.controller.BaseController;
import com.mysqldoc.common.model.Configure;
import com.mysqldoc.common.model.Directinfo;
import com.mysqldoc.configure.ConfigureService;
import com.mysqldoc.directinfo.DirectInfoService;
import com.mysqldoc.parsedb.ParseDbService;
import org.apache.log4j.Logger;

public class IndexController extends BaseController {
	public Logger logger = Logger.getLogger(this.getClass());
	public Gson gson = new Gson();
	public void index() {
		List<Configure> list = ConfigureService.service.list();
		// 所有链接信息
		setAttr("links", list);
		// 当前链接
		Configure config = null;
		Integer id = getParaToInt(0);
		if (null != id) {
			for (Configure c : list)
				if (c.getId().equals(id)){
					config = c;
				}
		} else if (null != list && list.size() > 0){
			config = list.get(0);
		}
		setAttr("config", config);

		// 数据库信息
		List<Record> dbs = null;
		if (null != config && config.getDbtype()==0) {
			dbs = ParseDbService.service.listDatabases(config);
			setAttr("databases", dbs);
		}
		// 表结构信息
		String tableName;
		if (null != dbs && dbs.size() > 0 && config.getDbtype()==0) {
			 tableName = getPara(1);
			if (StrKit.isBlank(tableName))
				tableName = dbs.get(0).getStr("name");
			List<Record> tables = ParseDbService.service.listTable(config, tableName);
			setAttr("tables", tables);
			setAttr("tableName", tableName);
		} else if(config.getDbtype()==1){
			tableName = getPara(0);
			List<Record> tables = ParseDbService.service.listTable(config, tableName);
			setAttr("tables", tables);
			setAttr("tableName", tableName);
		}
	}

	public void getSalesman(){
		String idno = getPara("idNum");
		Directinfo directinfo= DirectInfoService.dservice.findByIdNo(idno!=null?idno:"");
		String  jsonText;
		logger.info("获取到的身份参数为："+idno);
		if(directinfo!=null&&idno.equals(directinfo.getIdno())){
			String  usercode = directinfo.getUsercode();
			logger.info("从数据库里取出的值为："+directinfo.getIdno());
			jsonText= "{\"flag\": \"Y\",\"userCode\": \""+usercode+"\" }";
		}else{
			jsonText= "{\"flag\": \"N\"}";
		}
		 renderJson(jsonText);

	}

	public void personValidate(){
		String idno = getPara("idNo");
		Directinfo directinfo= DirectInfoService.dservice.findByIdNo(idno!=null?idno:"");
		String  jsonText;
		logger.info("获取到的身份参数为："+idno);
		if(directinfo!=null&&idno.equals(directinfo.getIdno())){
			logger.info("从数据库里取出的值为："+directinfo.getIdno());
			jsonText = "{\"flag\":"+ true +"}";
		}else{
			jsonText = "{\"flag\":"+ false +"}";
		}
		renderJson(jsonText);
	}

}
