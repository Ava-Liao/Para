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
    
    private String sub;
    
    private String smiles;
    
    private String sequences;
    
    private Integer predicted;
    
    private LocalDateTime createdTime;
    
    private LocalDateTime updatedTime;
    
    // 用于批量导入的构造函数
    public static EnzymeDTO fromImport(String ecNumber, String protId, Double kcat) {
        EnzymeDTO dto = new EnzymeDTO();
        dto.setEcNumber(ecNumber);
        dto.setProtId(protId);
        dto.setKcat(kcat);
        dto.setFormattedKcat(String.format("%.4f", kcat)); // 修改为保留4位小数的普通数值格式
        dto.setPredicted(0); // 默认为实验值
        return dto;
    }
    
    // 更新的批量导入构造函数，包含新字段
    public static EnzymeDTO fromImport(String ecNumber, String protId, Double kcat, 
                                      String sub, String smiles, String sequences) {
        EnzymeDTO dto = fromImport(ecNumber, protId, kcat);
        dto.setSub(sub);
        dto.setSmiles(smiles);
        dto.setSequences(sequences);
        return dto;
    }
    
    // 用于预测值的构造函数
    public static EnzymeDTO fromPrediction(String smiles, String sequences, Double kcat) {
        EnzymeDTO dto = new EnzymeDTO();
        dto.setSmiles(smiles);
        dto.setSequences(sequences);
        dto.setKcat(kcat);
        dto.setFormattedKcat(String.format("%.4f", kcat)); // 修改为保留4位小数的普通数值格式
        dto.setPredicted(1); // 设置为预测值
        return dto;
    }
} 