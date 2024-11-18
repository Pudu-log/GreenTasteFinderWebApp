package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActDto {
    private String id;
    private String store_id;
    private String act_dt;
    private String gubn;

    ActDto(String id, String store_id, String gubn) {
        this.id = id;
        this.store_id = store_id;
        this.gubn = gubn;
    }
}
