/**
 * .
 */

package com.oseasy.cas.modules.cas.utils;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.client.authentication.AttributePrincipal;

import com.oseasy.cas.common.config.CasSval;
import com.oseasy.cas.modules.cas.entity.SysCasUser;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.util.common.utils.StringUtil;

/**
 * CasUtils.
 * @author chenhao
 *
 */
public class CasUtils {
    private static final String RP_OU = "ou";
    private static final String RP_CN = "cn";
    private static final String RP_UID = "uid";
    private static final String RP_USER_NAME = "user_name";
    private static final String RP_USER_TYPE = "user_type";
    private static final String RP_REQ_UTYPE = "req_utype";//要求用户类型
    private static final String RP_CONTAINER_ID = "containerId";

    public static SysCasUser getSysCasUser(HttpServletRequest request) {
        SysCasUser user = new SysCasUser();
        user.setRip(request.getRemoteUser());
        String rutype = request.getParameter(RP_USER_TYPE);
        String requtypeStr = request.getParameter(RP_REQ_UTYPE);
        requtypeStr = (StringUtil.isNumeric(requtypeStr)) ? requtypeStr : Const.YES;
        Boolean requtype = ((Const.YES).equals(requtypeStr) ? true : false);
        Principal principal = request.getUserPrincipal();
        if(principal !=null && principal instanceof AttributePrincipal){
            AttributePrincipal aPrincipal = (AttributePrincipal)principal;
            //获取用户信息中公开的Attributes部分
            Map<String, Object> map = aPrincipal.getAttributes();
            user.setRuid((String)map.get(RP_UID));
            user.setRcname((String)map.get(RP_CN));
            user.setRname((String)map.get(RP_USER_NAME));
            String utype = (String)map.get(RP_USER_TYPE);
            utype = StringUtil.isEmpty(rutype) ? utype : rutype;
            user.setRutype(utype);
            user.setRcontainerId((String)map.get(RP_CONTAINER_ID));
            user.setRou((String)map.get(RP_OU));
            user.setCheckUtype(requtype);
        }
        return user;
    }
}
