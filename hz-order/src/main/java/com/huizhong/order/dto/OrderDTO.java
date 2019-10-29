package com.huizhong.order.dto;

import com.huizhong.common.dto.CartDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull
    private Long addressId;
    @NotNull
    private Integer paymentType;
    @NotNull
    private List<CartDTO> carts;
}
