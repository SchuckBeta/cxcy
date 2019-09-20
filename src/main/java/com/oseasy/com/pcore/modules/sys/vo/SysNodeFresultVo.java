package com.oseasy.com.pcore.modules.sys.vo;

import java.util.List;

public class SysNodeFresultVo {
  private SysNodeFresult formatResult;
  private List<SysNodeFresult> formatResults;
  public SysNodeFresultVo() {
    super();
  }
  public SysNodeFresultVo(SysNodeFresult formatResult) {
    super();
    this.formatResult = formatResult;
  }
  public SysNodeFresultVo(SysNodeFresult formatResult, List<SysNodeFresult> formatResults) {
    super();
    this.formatResult = formatResult;
    this.formatResults = formatResults;
  }
  public SysNodeFresult getFormatResult() {
    return formatResult;
  }
  public void setFormatResult(SysNodeFresult formatResult) {
    this.formatResult = formatResult;
  }
  public List<SysNodeFresult> getFormatResults() {
    return formatResults;
  }
  public void setFormatResults(List<SysNodeFresult> formatResults) {
    this.formatResults = formatResults;
  }
}
