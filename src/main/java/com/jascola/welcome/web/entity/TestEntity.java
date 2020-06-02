package com.jascola.welcome.web.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.money.Money;

@Getter
@Setter
@ToString
public class TestEntity {
    private Long id;
    private Money myMoney;
    private String userName;
}
