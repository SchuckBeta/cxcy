package com.oseasy.pro.modules.cert.entity;

public class CertElement implements Comparable<CertElement>{
	private String id;//元素id
	private Integer x;//
	private Integer y;//
	private String width;//
	private String height;//
	private String angle;//
	private String matrix;//
	private String elementType;//元素类型 区分主图或者水印其它类型
	private Float fillOpacity;//
	private Integer sort;//层级，亦作生成证书图片的元素排序用
	private String text;//文本内容
	private String url;//图片ftp地址
	private String varCol;//当元素类型是变量类型关联的字段值
	private String repeat;//
	private String color;//字体颜色
	private Integer fontSize;//字体大小
	private String fontFamily;
	private String fontStyle;//字体斜体
	private String fontWeight;//字体加粗
	private String textDecoration;//字体下划线
	
	
	public String getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}
	public String getFontWeight() {
		return fontWeight;
	}
	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}
	public String getTextDecoration() {
		return textDecoration;
	}
	public void setTextDecoration(String textDecoration) {
		this.textDecoration = textDecoration;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getAngle() {
		return angle;
	}
	public void setAngle(String angle) {
		this.angle = angle;
	}
	public String getMatrix() {
		return matrix;
	}
	public void setMatrix(String matrix) {
		this.matrix = matrix;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public Float getFillOpacity() {
		return fillOpacity;
	}
	public void setFillOpacity(Float fillOpacity) {
		this.fillOpacity = fillOpacity;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVarCol() {
		return varCol;
	}
	public void setVarCol(String varCol) {
		this.varCol = varCol;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getFontSize() {
		return fontSize;
	}
	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}
	@Override
	public int compareTo(CertElement o) {
		return this.getSort() - o.getSort();
	}
	
}
