/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.tool.IeAbsYw;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;

/**
 * .
 * @author chenhao
 *
 */
public class IeYws extends IeAbsYw{
    public IeYws() {
        super();
    }

    public IeYws(IepTpl iepTpl, HttpServletRequest request, HttpServletResponse response) {
        super(iepTpl, request, response);
    }
}
