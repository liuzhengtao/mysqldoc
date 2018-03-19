package com.mysqldoc.directinfo;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.validate.Validator;

public class DirectInfoValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateRequiredString("name","name","请输入客户姓名");
		validateRequiredString("id_no","id_no","请输入客户身份证号码");
		validateRequiredString("usrecode","usercode","请输入分配人员code");
	}

	@Override
	protected void handleError(Controller c) {
		c.renderJson(Ret.fail("error", c.getAttr("error")));
	}
}
