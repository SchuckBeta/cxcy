/**
 * .
 */

package com.oseasy.pie.modules.iep.tool;

/**
 * 通用导入模板引擎.
 * @author chenhao
 *
 */
public abstract class IeAbsEngine<T extends IeYwEser> implements IeYwEge{
    private T service;

    public IeAbsEngine() {
        super();
    }
    public IeAbsEngine(T service) {
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
