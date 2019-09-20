package com.oseasy.cms.modules.cms.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.cms.modules.cms.entity.FileTpl;

/**
 * User: songlai
 * Date: 13-8-27
 * Time: 下午4:56
 */
@Service
@Transactional(readOnly = true)
public class FileTplService {
    private ServletContext context;

    public List<String> getNameListByPrefix(String path) {
        List<FileTpl> list = getListByPath(path, false);
        List<String> result = new ArrayList<String>(list.size());
        for (FileTpl tpl : list) {
            result.add(tpl.getName());
        }
        return result;
    }

    public List<FileTpl> getListByPath(String path, boolean directory) {
   		File f = new File(getContext().getRealPath(path));
   		if (f.exists()) {
   			File[] files = f.listFiles();
   			if (files != null) {
   				List<FileTpl> list = new ArrayList<FileTpl>();
   				for (File file : files) {
                    if (file.isFile() || directory)
   					    list.add(new FileTpl(file, getContext().getRealPath("")));
   				}
   				return list;
   			} else {
   				return new ArrayList<FileTpl>(0);
   			}
   		} else {
   			return new ArrayList<FileTpl>(0);
   		}
   	}

    public List<FileTpl> getListForEdit(String path) {
        List<FileTpl> list = getListByPath(path, true);
        List<FileTpl> result = new ArrayList<FileTpl>();
        result.add(new FileTpl(new File(getContext().getRealPath(path)), getContext().getRealPath("")));
        getAllDirectory(result, list);
        return result;
    }

    private void getAllDirectory(List<FileTpl> result, List<FileTpl> list) {
        for (FileTpl tpl : list) {
            result.add(tpl);
            if (tpl.isDirectory()) {
                getAllDirectory(result, getListByPath(tpl.getName(), true));
            }
        }
    }

    public FileTpl getFileTpl(String name) {
   		File f = new File(getContext().getRealPath(name));
   		if (f.exists()) {
   			return new FileTpl(f, "");
   		} else {
   			return null;
   		}
   	}

    public ServletContext getContext() throws RuntimeException{
        if(this.context == null){
            throw new RuntimeException();
        }
        return context;
    }

    public void setContext(HttpServletRequest request) {
        this.context = request.getSession().getServletContext();
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }
}
