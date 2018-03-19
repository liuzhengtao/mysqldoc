package com.mysqldoc.common;

import com.jfinal.config.Routes;
import com.mysqldoc.configure.ConfigureController;
import com.mysqldoc.directinfo.DirectInfoController;
import com.mysqldoc.index.IndexController;

public class FrontRoutes extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/WEB-INF/view");
		add("/", IndexController.class, "/index");
		add("/configure", ConfigureController.class);
		add("/direct", DirectInfoController.class);
	}
}
