package com.mysqldoc.directinfo;

import com.google.gson.Gson;
import com.jfinal.aop.Before;
import com.jfinal.kit.Ret;
import com.mysqldoc.common.controller.BaseController;
import com.mysqldoc.common.model.Configure;
import com.mysqldoc.common.model.Directinfo;
import com.mysqldoc.configure.ConfigureService;
import com.mysqldoc.configure.ConfigureValidator;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.util.List;

/**
 * Created by YM10095 on 2018/2/2.
 */
public class DirectInfoController extends BaseController {
    Gson gson = new Gson();
    protected Logger logger = Logger.getLogger(DirectInfoController.class);
    public void save() {
        Directinfo model = getModel(Directinfo.class, "");
        Directinfo directinfo=DirectInfoService.dservice.findByIdNo(model!=null?model.getIdno():"");
        if(directinfo!=null){
            renderJson(Ret.fail("error", "身份信息已存在库里"));
            logger.info("*************保存客户信息之前已经存在信息********************");
        }else{
            boolean bln = model.save();
            if (bln)
                renderJson(Ret.ok());
            else
                renderJson(Ret.fail("error", "失败"));
        }
    }
    public void index(){
        List<Directinfo> directinfos = (List<Directinfo>) DirectInfoService.dservice.list();
        setAttr("dirctinfos",directinfos);
    }



    public void delete() {
        boolean bln = DirectInfoService.dservice.delete(Integer.valueOf(getPara("cid")));
        if (bln){
            renderJson(Ret.ok());
        }
        else{
            logger.error("--------------删除失败的ID为："+getPara("cid")+"----------------------");
            renderJson(Ret.fail("" +
                    "", "删除信息失败"));
        }

    }

}
