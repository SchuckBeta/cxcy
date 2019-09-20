/**
 *
 */
package com.oseasy.pw.modules.pw.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.modules.pw.service.PwDictService;

import net.sf.json.JSONObject;

/**
 * 字典Controller

 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class PwDictController extends BaseController {
	@Autowired
	private PwDictService pwDictService;

	/*删除字典类型或字典*/

	@RequestMapping(value = "delDictType")
	@ResponseBody
	public JSONObject delDictType(HttpServletRequest request, HttpServletResponse response) {
		String id=request.getParameter("id");
		return pwDictService.delDictType(id);
	}
}
