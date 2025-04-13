package com.graduate.para.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnzymeDTO {
    private Long id;
    
    private String ecNumber;
    
    private String protId;
    
    private Double kcat;
    
    private String formattedKcat;
    
    private LocalDateTime createdTime;
    
    private LocalDateTime updatedTime;
    
    // 用于批量导入的构造函数
    public static EnzymeDTO fromImport(String ecNumber, String protId, Double kcat) {
        EnzymeDTO dto = new EnzymeDTO();
        dto.setEcNumber(ecNumber);
        dto.setProtId(protId);
        dto.setKcat(kcat);
        dto.setFormattedKcat(String.format("%.2e", kcat)); // 科学计数法格式
        return dto;
    }
} 