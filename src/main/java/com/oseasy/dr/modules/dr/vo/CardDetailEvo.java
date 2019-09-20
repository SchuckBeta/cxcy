package com.oseasy.dr.modules.dr.vo;

import com.oseasy.dr.modules.dr.entity.DrCard;

import Net.PC15.FC8800.Command.Data.CardDetail;

public class CardDetailEvo {
    public DrCard card;
    public CardDetail cardDetail;
    public String epmentId;
    public CardDetailEvo() {
        super();
    }
    public CardDetailEvo(CardDetail cardDetail, String epmentId) {
        super();
        this.cardDetail = cardDetail;
        this.epmentId = epmentId;
    }
    public CardDetailEvo(DrCard card, String epmentId) {
        super();
        this.card = card;
        this.epmentId = epmentId;
    }

    public DrCard getCard() {
        return card;
    }
    public void setCard(DrCard card) {
        this.card = card;
    }
    public CardDetail getCardDetail() {
        return cardDetail;
    }
    public void setCardDetail(CardDetail cardDetail) {
        this.cardDetail = cardDetail;
    }
    public String getEpmentId() {
        return epmentId;
    }
    public void setEpmentId(String epmentId) {
        this.epmentId = epmentId;
    }
}