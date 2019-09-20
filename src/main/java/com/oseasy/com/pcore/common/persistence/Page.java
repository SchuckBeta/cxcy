/**
 *
 */

package com.oseasy.com.pcore.common.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.util.common.utils.CookieUtils;


/**
 * 分页类
 *
 * @version 2013-7-2
 * @param <T>
 *          泛型参数.
 */
public class Page<T> implements Serializable {
    /**
     * 日志对象.
     */
    protected Logger logger = LoggerFactory.getLogger(Page.class);
  private static final long serialVersionUID = 1L;
  public static final String PAGE = "page";
  public static final String ORDER_ASC = "ASC";
  public static final String ORDER_DESC = "DESC";
  public static final int MAX_PAGE_SIZE = 10000;

  private int pageNo = 1; // 当前页码
  /**
   * 页面大小，设置为“-1”表示不进行分页（分页无效）.
   */
  private int pageSize = Integer.valueOf(CoreSval.getConfig("page.pageSize"));

  private long count;// 总记录数，设置为“-1”表示不查询总数

  private long todoCount;  //待审核任务数 ，addBy 张正

  private int first;// 首页索引
  private int last;// 尾页索引
  private int prev;// 上一页索引
  private int next;// 下一页索引

  private boolean firstPage;// 是否是第一页
  private boolean lastPage;// 是否是最后一页

  private int length = 8;// 显示页面长度
  private int slider = 1;// 前后显示页面长度

  private transient List<T> list;

  private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc

  private String orderByType ; // 排序类型 实例:desc,asc

  private String funcName = "page"; // 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用.

  private String funcParam = ""; // 函数的附加参数，第三个参数值.

  private String message = ""; // 设置提示消息，显示在“共n条”之后


  public Page() {
    this.pageSize = -1;
  }

  /**
   * 构造方法.
   *
   * @param request
   *          传递 repage 参数，来记住页码
   * @param response
   *          用于设置 Cookie，记住页码
   */
  public Page(HttpServletRequest request, HttpServletResponse response) {
    this(request, response, -2);
  }

  /**
   * 构造方法.
   *
   * @param request
   *          传递 repage 参数，来记住页码
   * @param response
   *          用于设置 Cookie，记住页码
   * @param defaultPageSize
   *          默认分页大小，如果传递 -1 则为不分页，返回所有数据
   */
  public Page(HttpServletRequest request, HttpServletResponse response, int defaultPageSize) {
// 设置页码参数（传递repage参数，来记住页码）
    String no = request.getParameter("pageNo");
    if (StringUtils.isEmpty(no)) {
      no = pageNo+"";
    }

    try {
        if (StringUtils.isNumeric(no)) {
//            logger.info("更新Cookie中的pageNo属性:["+no+"]");
          CookieUtils.setCookie(response, "pageNo", no);
//          logger.info("更新Cookie中的pageNo属性完成");
          this.setPageNo(Integer.parseInt(no));
        } else if (request.getParameter("repage") != null) {
//            logger.info("获取Cookie中的pageNo属性");
          no = CookieUtils.getCookie(request, "pageNo");
//          logger.info("获取Cookie中的pageNo属性:["+no+"]");
          if (StringUtils.isNumeric(no)) {
            this.setPageNo(Integer.parseInt(no));
          }
        }
    } catch (Exception e) {
        this.setPageNo(Integer.parseInt(no));
        logger.warn("创建Page时处理Page.pageNo属性失败！");
    }

    // 设置页面大小参数（传递repage参数，来记住页码大小）
    String size = request.getParameter("pageSize");
    if (StringUtils.isEmpty(size)) {
      size = pageSize+"";
    }

    try {
        if (StringUtils.isNumeric(size)) {
            logger.info("更新Cookie中的pageSize属性:["+pageSize+"]");
          CookieUtils.setCookie(response, "pageSize", size);
          logger.info("更新Cookie中的pageSize属性完成");
          this.setPageSize(Integer.parseInt(size));
        } else if (request.getParameter("repage") != null) {
            logger.info("获取Cookie中的pageSize属性");
          size = CookieUtils.getCookie(request, "pageSize");
          logger.info("获取Cookie中的pageSize属性:["+size+"]");
          if (StringUtils.isNumeric(size)) {
            this.setPageSize(Integer.parseInt(size));
          }
        } else if (defaultPageSize != -2) {
          this.pageSize = defaultPageSize;
        }
    } catch (Exception e) {
        this.setPageSize(Integer.parseInt(size));
        logger.warn("创建Page时处理Page.pageSize属性失败！");
    }

    // 设置页面分页函数
    String funcName = request.getParameter("funcName");
    try {
        if (StringUtils.isNotBlank(funcName)) {
            logger.info("更新Cookie中的funcName属性:["+funcName+"]");
          CookieUtils.setCookie(response, "funcName", funcName);
          logger.info("更新Cookie中的funcName属性完成");
          this.setFuncName(funcName);
        } else if (request.getParameter("repage") != null) {
            logger.info("获取Cookie中的funcName属性");
          funcName = CookieUtils.getCookie(request, "funcName");
          logger.info("获取Cookie中的funcName属性:["+funcName+"]");
          if (StringUtils.isNotBlank(funcName)) {
            this.setFuncName(funcName);
          }
        }
    } catch (Exception e) {
        this.setFuncName(funcName);
        logger.warn("创建Page时处理Page.funcName属性失败！");
    }

    // 设置排序参数
    String orderBy = request.getParameter("orderBy");
    if (StringUtils.isNotBlank(orderBy)) {
      this.setOrderBy(orderBy);
    }
    String orderByType = request.getParameter("orderByType");
    if (StringUtils.isNotBlank(orderByType)) {
      this.setOrderByType(orderByType);
    }
  }

  /**
   * 构造方法.
   *
   * @param pageNo
   *          当前页码
   * @param pageSize
   *          分页大小
   */
  public Page(int pageNo, int pageSize) {
    this(pageNo, pageSize, 0);
  }

  /**
   * 构造方法.
   *
   * @param pageNo
   *          当前页码
   * @param pageSize
   *          分页大小
   * @param count
   *          数据条数
   */
  public Page(int pageNo, int pageSize, long count) {
    this(pageNo, pageSize, count, new ArrayList<T>());
  }

  /**
   * 构造方法.
   *
   * @param pageNo
   *          当前页码
   * @param pageSize
   *          分页大小
   * @param count
   *          数据条数
   * @param list
   *          本页数据对象列表
   */
  public Page(int pageNo, int pageSize, long count, List<T> list) {
    this.setCount(count);
    this.setPageNo(pageNo);
    this.pageSize = pageSize;
    this.list = list;
  }

  /**
   * 初始化参数.
   */
  public void initialize() {

    // 1
    this.first = 1;

    this.last = (int) (count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);

    if (this.count % this.pageSize != 0 || this.last == 0) {
      this.last++;
    }

    if (this.last < this.first) {
      this.last = this.first;
    }

    if (this.pageNo <= 1) {
      this.pageNo = this.first;
      this.firstPage = true;
    }

    if (this.pageNo >= this.last) {
      this.pageNo = this.last;
      this.lastPage = true;
    }

    if (this.pageNo < this.last - 1) {
      this.next = this.pageNo + 1;
    } else {
      this.next = this.last;
    }

    if (this.pageNo > 1) {
      this.prev = this.pageNo - 1;
    } else {
      this.prev = this.first;
    }

    // 如果当前页小于首页
    if (this.pageNo < this.first) {
      this.pageNo = this.first;
    }

    // 如果当前页大于尾页
    if (this.pageNo > this.last) {
      this.pageNo = this.last;
    }

  }


  public String getCourseFooter() {
    StringBuilder sb = new StringBuilder();
    sb.append("<div class=\"row pagination_num\">");
    sb.append("<ul class=\"pagination_list\">");

    // 如果是首页
    if (pageNo == first) {
      sb.append("<li class=\"disabled head_footer\"><a href=\"javascript:\">« 上一页</a></li>");
    } else {
      sb.append("<li class=\"head_footer\"><a href=\"javascript:\" onclick=\"" + funcName + "("
              + prev + "," + pageSize + ",'" + funcParam + "');\">&#171; 上一页</a></li>\n");
    }

    int begin = pageNo - (length / 2);

    if (begin < first) {
      begin = first;
    }

    int end = begin + length - 1;

    if (end >= last) {
      end = last;
      begin = end - length + 1;
      if (begin < first) {
        begin = first;
      }
    }

    if (begin > first) {
      int i = 0;
      for (i = first; i < first + slider && i < begin; i++) {
        sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize
                + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
      }
      if (i < begin) {
        sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
      }
    }

    for (int i = begin; i <= end; i++) {
      if (i == pageNo) {
        sb.append(
                "<li class=\"current\"><a href=\"javascript:\">" + (i + 1 - first) + "</a></li>\n");
      } else {
        sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize
                + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
      }
    }

    if (last - end > slider) {
      sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
      end = last - slider;
    }

    for (int i = end + 1; i <= last; i++) {
      sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize
              + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
    }

    if (pageNo == last) {
      sb.append("<li class=\"disabled head_footer\"><a href=\"javascript:\">下一页 »</a></li>\n");
    } else {
      sb.append("<li class=\"head_footer\"><a href=\"javascript:\" onclick=\"" + funcName + "("
              + next + "," + pageSize + ",'" + funcParam + "');\">" + "下一页 &#187;</a></li>\n");
    }
    sb.append("</ul>");
    sb.append("</div>");

    return sb.toString();
  }

  /**
   * 国创平台分页html代码 addBy zhangzheng
   *
   * @return
   *
   *
   *         <li class="disabled head_footer"><a href="javascript:">« 上一页</a></li>
   *         <li class="current"><a href="javascript:">1</a></li>
   *         <li><a href="javascript:" onclick="page(2,2,'');">2</a></li>
   *         <li><a href="javascript:" onclick="page(3,2,'');">3</a></li>
   *         <li><a href="javascript:;">...</a></li>
   *         <li class="head_footer"><a href="javascript:" onclick="page(2,2,'');">下一页 »</a></li>
   */
  public String getFooter() {

    StringBuilder sb = new StringBuilder();
    sb.append("<div class=\"row pagination_num\">");
    sb.append("<ul class=\"pagination_list\">");

    // 如果是首页
    if (pageNo == first) {
      sb.append("<li class=\"disabled head_footer\"><a href=\"javascript:\">« 上一页</a></li>");
    } else {
      sb.append("<li class=\"head_footer\"><a href=\"javascript:\" onclick=\"" + funcName + "("
          + prev + "," + pageSize + ",'" + funcParam + "');\">&#171; 上一页</a></li>\n");
    }

    int begin = pageNo - (length / 2);

    if (begin < first) {
      begin = first;
    }

    int end = begin + length - 1;

    if (end >= last) {
      end = last;
      begin = end - length + 1;
      if (begin < first) {
        begin = first;
      }
    }

    if (begin > first) {
      int i = 0;
      for (i = first; i < first + slider && i < begin; i++) {
        sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize
            + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
      }
      if (i < begin) {
        sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
      }
    }

    for (int i = begin; i <= end; i++) {
      if (i == pageNo) {
        sb.append(
            "<li class=\"current\"><a href=\"javascript:\">" + (i + 1 - first) + "</a></li>\n");
      } else {
        sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize
            + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
      }
    }

    if (last - end > slider) {
      sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
      end = last - slider;
    }

    for (int i = end + 1; i <= last; i++) {
      sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize
          + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
    }

    if (pageNo == last) {
      sb.append("<li class=\"disabled head_footer\"><a href=\"javascript:\">下一页 »</a></li>\n");
    } else {
      sb.append("<li class=\"head_footer\"><a href=\"javascript:\" onclick=\"" + funcName + "("
          + next + "," + pageSize + ",'" + funcParam + "');\">" + "下一页 &#187;</a></li>\n");
    }
    sb.append("</ul>");
    sb.append("<div class=\"num_page\">");
    sb.append("<p class=\"page_tips\">当前第" + pageNo + "页 &nbsp;总记录" + count + "条&nbsp;&nbsp;</p>");
    sb.append("<select id=\"ps\"  class=\"per_page_count\"  onchange=\"page(1,this.value)\" >");
    sb.append("<option value=\"5\">5</option>");
    sb.append("<option value=\"10\">10</option>");
    sb.append("<option value=\"20\">20</option>");
    sb.append("<option value=\"50\">50</option>");
    sb.append("<option value=\"100\">100</option>");
    sb.append("</select>");
    sb.append("</div>");
    sb.append("</div>");

    return sb.toString();
  }

  /**
   * 默认输出当前分页标签 <div class="page">${page}</div>.
   */
  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();

    // 如果是首页
    if (pageNo == first) {
      sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; 上一页</a></li>\n");
    } else {
      sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + prev + "," + pageSize
          + ",'" + funcParam + "');\">&#171; 上一页</a></li>\n");
    }

    int begin = pageNo - (length / 2);

    if (begin < first) {
      begin = first;
    }

    int end = begin + length - 1;

    if (end >= last) {
      end = last;
      begin = end - length + 1;
      if (begin < first) {
        begin = first;
      }
    }

    if (begin > first) {
      int i = 0;
      for (i = first; i < first + slider && i < begin; i++) {
        sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize
            + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
      }
      if (i < begin) {
        sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
      }
    }

    for (int i = begin; i <= end; i++) {
      if (i == pageNo) {
        sb.append(
            "<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first) + "</a></li>\n");
      } else {
        sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize
            + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
      }
    }

    if (last - end > slider) {
      sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
      end = last - slider;
    }

    for (int i = end + 1; i <= last; i++) {
      sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize
          + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
    }

    if (pageNo == last) {
      sb.append("<li class=\"disabled\"><a href=\"javascript:\">下一页 &#187;</a></li>\n");
    } else {
      sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + next + "," + pageSize
          + ",'" + funcParam + "');\">" + "下一页 &#187;</a></li>\n");
    }

    sb.append("<li class=\"disabled controls\"><a href=\"javascript:\">当前 ");
    sb.append("<input type=\"text\" value=\"" + pageNo
        + "\" onkeypress=\"var e=window.event||event;var c=e.keyCode||e.which;if (c==13)");
    sb.append(funcName + "(this.value," + pageSize + ",'" + funcParam
        + "');\" onclick=\"this.select();\"/> / ");
    sb.append("<input type=\"text\" value=\"" + pageSize
        + "\" onkeypress=\"var e=window.event||event;var c=e.keyCode||e.which;if (c==13)");
    sb.append(funcName + "(" + pageNo + ",this.value,'" + funcParam
        + "');\" onclick=\"this.select();\"/> 页，");
    sb.append("共 " + count + " 条" + (message != null ? message : "") + "</a></li>\n");

    sb.insert(0, "<ul>\n").append("</ul>\n");

    sb.append("<div style=\"clear:both;\"></div>");

    // sb.insert(0,"<div class=\"page\">\n").append("</div>\n");

    return sb.toString();
  }

  /**
   * 获取分页HTML代码.
   *
   * @return String
   */
  public String getHtml() {
    return toString();
  }

  // public static void main(String[] args) {
  // Page<String> p = new Page<String>(3, 3);
  // System.out.println(p);
  // System.out.println("首页："+p.getFirst());
  // System.out.println("尾页："+p.getLast());
  // System.out.println("上页："+p.getPrev());
  // System.out.println("下页："+p.getNext());
  // }

  /**
   * 获取设置总数.
   *
   * @return count 数据总数
   */
  public long getCount() {
    return count;
  }

  /**
   * 设置数据总数.
   *
   * @param count
   *          数据总数
   */
  public void setCount(long count) {
    this.count = count;
    if (pageSize >= count) {
      pageNo = 1;
    }
  }

  /**
   * 获取当前页码.
   *
   * @return pageNo 页号
   */
  public int getPageNo() {
    return pageNo;
  }

  /**
   * 设置当前页码.
   *
   * @param pageNo
   *          页号
   */
  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  /**
   * 获取页面大小.
   *
   * @return pageSize
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * 设置页面大小（最大500）.
   *
   * @param pageSize
   *          每页显示数
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize <= 0 ? 10 : pageSize;// > 500 ? 500 : pageSize;
  }

  /**
   * 首页索引.
   *
   * @return first
   */
  @JsonIgnore
  public int getFirst() {
    return first;
  }

  /**
   * 尾页索引.
   *
   * @return last
   */
  @JsonIgnore
  public int getLast() {
    return last;
  }

  /**
   * 获取页面总数.
   *
   * @return getLast();
   */
  @JsonIgnore
  public int getTotalPage() {
    return getLast();
  }

  /**
   * 是否为第一页.
   *
   * @return firstPage 是否为第一页
   */
  @JsonIgnore
  public boolean isFirstPage() {
    return firstPage;
  }

  /**
   * 是否为最后一页.
   *
   * @return boolean 是否最后一页
   */
  @JsonIgnore
  public boolean isLastPage() {
    return lastPage;
  }

  /**
   * 上一页索引值.
   *
   * @return int 上一页页号
   */
  @JsonIgnore
  public int getPrev() {
    if (isFirstPage()) {
      return pageNo;
    } else {
      return pageNo - 1;
    }
  }

  /**
   * 下一页索引值.
   *
   * @return int 下一页页号
   */
  @JsonIgnore
  public int getNext() {
    if (isLastPage()) {
      return pageNo;
    } else {
      return pageNo + 1;
    }
  }

  /**
   * 获取本页数据对象列表.
   *
   * @return list 数据列表
   */
  public List<T> getList() {
    if (list == null) {
      list = new ArrayList<T>();
    }
    return list;
  }

  /**
   * 设置本页数据对象列表.
   *
   * @param list
   *          数据列表
   */
  public Page<T> setList(List<T> list) {
    this.list = list;
    initialize();
    return this;
  }

  /**
   * 获取查询排序字符串.
   *
   * @return String 排序字符串
   */
  @JsonIgnore
  public String getOrderBy() {
    // SQL过滤，防止注入
    String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
        + "(\\b(select|update|and|or|delete|insert|trancate|char|into|"
        + "substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
    Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
    if (sqlPattern.matcher(orderBy).find()) {
      return "";
    }
    return orderBy;
  }

  /**
   * 设置查询排序，标准查询有效， 实例： updatedate desc, name asc.
   */
  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  /**
   * 获取点击页码调用的js函数名称 function.
   * ${page.funcName}(pageNo) {location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+i;}
   *
   * @return funcName 函数名称
   */
  @JsonIgnore
  public String getFuncName() {
    return funcName;
  }

  /**
   * 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用.
   *
   * @param funcName
   *          默认为page
   */
  public void setFuncName(String funcName) {
    this.funcName = funcName;
  }

  /**
   * 获取分页函数的附加参数.
   *
   * @return funcParam 分页的附加参数
   */
  @JsonIgnore
  public String getFuncParam() {
    return funcParam;
  }

  /**
   * 设置分页函数的附加参数.
   */
  public void setFuncParam(String funcParam) {
    this.funcParam = funcParam;
  }

  /**
   * 设置提示消息，显示在“共n条”之后.
   *
   * @param message
   *          提示消息
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * 分页是否有效.
   *
   * @return this.pageSize==-1
   */
  @JsonIgnore
  public boolean isDisabled() {
    return this.pageSize == -1;
  }

  /**
   * 是否进行总数统计.
   *
   * @return this.count==-1
   */
  @JsonIgnore
  public boolean isNotCount() {
    return this.count == -1;
  }

  /**
   * 获取 Hibernate FirstResult.
   */
  public int getFirstResult() {
    int firstResult = (getPageNo() - 1) * getPageSize();
    if (firstResult >= getCount()) {
      firstResult = 0;
    }
    return firstResult;
  }

  /**
   * 获取 Hibernate MaxResults.
   */
  public int getMaxResults() {
    return getPageSize();
  }

  public long getTodoCount() {
    return todoCount;
  }

  public void setTodoCount(long todoCount) {
    this.todoCount = todoCount;
  }

  public String getOrderByType() {
    return orderByType;
  }

  public void setOrderByType(String orderByType) {
    this.orderByType = orderByType;
  }


// /**
  // * 获取 Spring data JPA 分页对象
  // */
  // public Pageable getSpringPage() {
  // List<Order> orders = new ArrayList<Order>();
  // if (orderBy!=null) {
  // for (String order : StringUtils.split(orderBy, ",")) {
  // String[] o = StringUtils.split(order, " ");
  // if (o.length==1) {
  // orders.add(new Order(Direction.ASC, o[0]));
  // }else if (o.length==2) {
  // if ("DESC".equals(o[1].toUpperCase())) {
  // orders.add(new Order(Direction.DESC, o[0]));
  // }else{
  // orders.add(new Order(Direction.ASC, o[0]));
  // }
  // }
  // }
  // }
  // return new PageRequest(this.pageNo - 1, this.pageSize, new Sort(orders));
  // }
  //
  // /**
  // * 设置 Spring data JPA 分页对象，转换为本系统分页对象
  // */
  // public void setSpringPage(org.springframework.data.domain.Page<T> page) {
  // this.pageNo = page.getNumber();
  // this.pageSize = page.getSize();
  // this.count = page.getTotalElements();
  // this.list = page.getContent();
  // }

}
