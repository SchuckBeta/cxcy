package com.oseasy.dr.modules.dr.vo;

import java.util.List;

import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.pw.modules.pw.entity.PwEnterDetail;

/**
 * 入驻用户.
 *
 * @author chenhao
 */
public class PweUser extends DrCard {
    private static final long serialVersionUID = 1L;
    private List<PwEnterDetail> enterDetails;
    private User user;

    public PweUser() {
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PwEnterDetail> getEnterDetails() {
        return enterDetails;
    }

    public void setEnterDetails(List<PwEnterDetail> enterDetails) {
        this.enterDetails = enterDetails;
    }
}