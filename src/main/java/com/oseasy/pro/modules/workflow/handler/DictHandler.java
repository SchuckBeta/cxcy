/**
 * .
 */

package com.oseasy.pro.modules.workflow.handler;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;

/**
 * .
 * @author chenhao
 *
 */
public class DictHandler implements IExcelDictHandler{
    /* (non-Javadoc)
     * @see cn.afterturn.easypoi.handler.inter.IExcelDictHandler#toName(java.lang.String, java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public String toName(String dict, Object obj, String name, Object value) {
        // TODO Auto-generated method stub
        //System.out.println("toName:" + obj.getClass().getSimpleName() + "-name:" + name + "-value:" + value);
        return null;
    }

    /* (non-Javadoc)
     * @see cn.afterturn.easypoi.handler.inter.IExcelDictHandler#toValue(java.lang.String, java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public String toValue(String dict, Object obj, String name, Object value) {
        // TODO Auto-generated method stub
        //System.out.println("toValue:" + obj.getClass().getSimpleName() + "-name:" + name + "-value:" + value);
        return null;
    }

}
