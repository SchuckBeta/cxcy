package com.oseasy.scr.modules.sco.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.scr.modules.sco.entity.ScoScore;

/**
 * 学分处理Vo类.
 * @author chenhao
 *
 * @param <T>
 */
public class ScoScoreVo<T extends IScoScore<T>>{
  /**
   * 日志对象
   */
  protected Logger logger = LoggerFactory.getLogger(ScoScoreVo.class);

  private T isco;
  private ScoScore scoScore;

  public ScoScoreVo() {
    super();
  }

  public ScoScoreVo(T isco, ScoScore scoScore) {
    super();
    this.isco = isco;
    this.scoScore = scoScore;
  }

  public T getIsco() {
    return isco;
  }
  public void setIsco(T isco) {
    this.isco = isco;
  }
  public ScoScore getScoScore() {
    return scoScore;
  }
  public void setScoScore(ScoScore scoScore) {
    this.scoScore = scoScore;
  }

  public ScoScore calculate(ScoType scoType) {
    if (scoType == null) {
      return null;
    }

    if ((scoType).equals(ScoType.ST_BUSINESS)) {
      scoScore.setBusinessProId(isco.getId(isco));
      scoScore.setBusinessScore(isco.calculate(isco));
    } else if ((scoType).equals(ScoType.ST_COURSE)) {
      scoScore.setCourseId(isco.getId(isco));
      scoScore.setCourseScore(isco.calculate(isco));
    } else if ((scoType).equals(ScoType.ST_CREDIT)) {
      scoScore.setCreditId(isco.getId(isco));
      scoScore.setCreditScore(isco.calculate(isco));
    } else if ((scoType).equals(ScoType.ST_INNOVATE)) {
      scoScore.setInnovateProId(isco.getId(isco));
      scoScore.setInnovateScore(isco.calculate(isco));
    } else if ((scoType).equals(ScoType.ST_SKILL)) {
      scoScore.setSkillId(isco.getId(isco));
      scoScore.setSkillScore(isco.calculate(isco));
    } else {
      logger.warn("ScoType 未定义！");
    }
    return scoScore;
  }
}
