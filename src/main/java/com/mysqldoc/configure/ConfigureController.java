package com.mysqldoc.configure;

import com.jfinal.aop.Before;
import com.jfinal.kit.Ret;
import com.mysqldoc.common.controller.BaseController;
import com.mysqldoc.common.model.Configure;

public class ConfigureController extends BaseController {

	public void add() {
	}

	@Before(ConfigureValidator.class)
	public void save() {
		Configure model = getModel(Configure.class, "");
		boolean bln = model.save();
		if (bln)
			renderJson(Ret.ok());
		else
			renderJson(Ret.fail("error", "失败"));
	}

	public void edit() {
		setAttr("model", ConfigureService.service.findById(getParaToInt()));
	}

	@Before(ConfigureValidator.class)
	public void update() {
		Configure model = getModel(Configure.class, "");
		boolean bln = model.update();
		if (bln)
			renderJson(Ret.ok());
		else
			renderJson(Ret.fail());
	}

	public void delete() {
		boolean bln = ConfigureService.service.delete(getParaToInt());
		if (bln)
			renderJson(Ret.ok());
		else
			renderJson(Ret.fail());
	}
}
