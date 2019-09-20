/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.engine;

import com.oseasy.pie.modules.impdata.tool.IitEngine;
import com.oseasy.pie.modules.impdata.tool.IitService;

/**
 * 通用导入模板引擎.
 * @author chenhao
 *
 */
public abstract class IitAbsEngine<T extends IitService<?>> implements IitEngine{
    private T service;

    public IitAbsEngine() {
        super();
    }
    public IitAbsEngine(T service) {
        super();
        this.service = service;
    }
    public T service() {
        return service;
    }
    public void service(T service){
        this.service = service;
    }
    public T getService() {
        return service;
    }
    public void setService(T service) {
        this.service = service;
    }
}
