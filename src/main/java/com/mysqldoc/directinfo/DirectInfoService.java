package com.mysqldoc.directinfo;

import com.google.gson.Gson;
import com.mysqldoc.common.model.Configure;
import com.mysqldoc.common.model.Directinfo;
import com.mysqldoc.configure.ConfigureService;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YM10095 on 2018/2/2.
 */
public class DirectInfoService {

    public Gson gson = new Gson();
    public static final DirectInfoService dservice = new DirectInfoService();
    private final Directinfo dao = new Directinfo();
    public Logger logger = Logger.getLogger(this.getClass());
    public List<Directinfo> list() {
        List<Directinfo> directinfos = dao.find("select * from directinfo");
        return directinfos;

    }

    public Directinfo findById(Integer id) {
        return dao.findById(id);
    }

    public boolean delete(Integer id){
        return dao.deleteById(id);
    }

    public Directinfo findByIdNo(String idno) {
        Directinfo directinfo = null;
        List<String> idnos = new ArrayList<>();
        List<Directinfo> directinfos = dao.find("select * from directinfo where idno=?",idno);
        if (directinfos!=null&&directinfos.size()>0){
            directinfo = directinfos.get(0);
        }
        return directinfo;
    }
}
