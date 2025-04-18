package com.graduate.para.controller.dto;

import lombok.Data;

public class PredictRequest {
    private String smiles;
    private String sequences;
    private Double kcat;
}
