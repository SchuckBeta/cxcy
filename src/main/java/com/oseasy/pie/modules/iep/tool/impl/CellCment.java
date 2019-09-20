/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.impl;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellCment {
    private XSSFWorkbook wb;
    private CreationHelper factory;
    private ClientAnchor anchor;
    private Drawing drawing;
    private IeDmap edmap;
    private String sheetIndex;

    public CellCment(XSSFWorkbook wb) {
        super();
        this.wb = wb;
        this.factory = wb.getCreationHelper();
        this.anchor = this.factory.createClientAnchor();
    }
    public CellCment(XSSFWorkbook wb, CreationHelper factory, ClientAnchor anchor, Drawing drawing, IeDmap edmap) {
        super();
        this.wb = wb;
        this.factory = factory;
        this.anchor = anchor;
        this.drawing = drawing;
        this.edmap = edmap;
    }
    public String getSheetIndex() {
        return sheetIndex;
    }
    public void setSheetIndex(String sheetIndex) {
        this.sheetIndex = sheetIndex;
    }
    public XSSFWorkbook getWb() {
        return wb;
    }
    public void setWb(XSSFWorkbook wb) {
        this.wb = wb;
    }
    public CreationHelper getFactory() {
        return factory;
    }
    public void setFactory(CreationHelper factory) {
        this.factory = factory;
    }
    public ClientAnchor getAnchor() {
        return anchor;
    }
    public void setAnchor(ClientAnchor anchor) {
        this.anchor = anchor;
    }
    public Drawing getDrawing() {
        return drawing;
    }
    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }
    public IeDmap getEdmap() {
        return edmap;
    }
    public void setEdmap(IeDmap edmap) {
        this.edmap = edmap;
    }
}
