package com.xteamstudio.exam.oms.lottery.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class PlaceOrderDto {

    @In52Number
    @NotEmpty(message = "number must be less one")
    private List<Integer> numbers;

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }
}
