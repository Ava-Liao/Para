package com.graduate.para.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.graduate.para.model.Enzyme;
import com.graduate.para.service.EnzymeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/enzyme")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class EnzymeController {
    
    private final EnzymeService enzymeService;
    
    /**
     * 获取数据库中酶的总数量
     */
    @GetMapping("/count")
    public ResponseEntity<?> count() {
        log.info("Getting count of enzyme records");
        Map<String, Object> result = new HashMap<>();
        result.put("count", enzymeService.count());
        return ResponseEntity.ok(result);
    }


    /**
     * 查询酶数据（分页）
     */
    @GetMapping("/list")
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        log.info("Getting enzyme list with page={}, size={}", page, size);
        Page<Enzyme> pageData = enzymeService.page(new Page<>(page, size));
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotal());
        result.put("records", pageData.getRecords());
        result.put("current", pageData.getCurrent());
        result.put("size", pageData.getSize());
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 按EC号查询
     */
    @GetMapping("/findByEc")
    public ResponseEntity<?> findByEc(@RequestParam String ecNumber) {
        log.info("Searching enzymes by EC number: {}", ecNumber);
        LambdaQueryWrapper<Enzyme> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Enzyme::getEcNumber, ecNumber);
        List<Enzyme> enzymes = enzymeService.list(queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("count", enzymes.size());
        result.put("records", enzymes);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 按蛋白ID查询
     */
    @GetMapping("/findByProtId")
    public ResponseEntity<?> findByProtId(@RequestParam String protId) {
        log.info("Searching enzymes by protein ID: {}", protId);
        LambdaQueryWrapper<Enzyme> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Enzyme::getProtId, protId);
        List<Enzyme> enzymes = enzymeService.list(queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("count", enzymes.size());
        result.put("records", enzymes);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 按底物名称查询
     */
    @GetMapping("/findBySub")
    public ResponseEntity<?> findBySub(@RequestParam String sub) {
        log.info("Searching enzymes by substrate name: {}", sub);
        LambdaQueryWrapper<Enzyme> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Enzyme::getSub, sub);
        List<Enzyme> enzymes = enzymeService.list(queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("count", enzymes.size());
        result.put("records", enzymes);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 同时按EC号和蛋白ID查询kcat
     */
    @GetMapping("/findKcat")
    public ResponseEntity<?> findKcat(
            @RequestParam(required = false) String ecNumber,
            @RequestParam(required = false) String protId,
            @RequestParam(required = false) String sub) {
        
        log.info("Searching kcat by EC number: {}, protein ID: {}, and substrate: {}", ecNumber, protId, sub);
        
        LambdaQueryWrapper<Enzyme> queryWrapper = new LambdaQueryWrapper<>();
        boolean hasCondition = false;
        
        if (ecNumber != null && !ecNumber.isEmpty()) {
            queryWrapper.like(Enzyme::getEcNumber, ecNumber);
            hasCondition = true;
        }
        
        if (protId != null && !protId.isEmpty()) {
            queryWrapper.like(Enzyme::getProtId, protId);
            hasCondition = true;
        }
        
        if (sub != null && !sub.isEmpty()) {
            queryWrapper.like(Enzyme::getSub, sub);
            hasCondition = true;
        }
        
        if (!hasCondition) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "At least one parameter (ecNumber, protId, or sub) is required");
            return ResponseEntity.badRequest().body(response);
        }
        
        List<Enzyme> enzymes = enzymeService.list(queryWrapper);
        
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Enzyme enzyme : enzymes) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", enzyme.getId());
            item.put("ecNumber", enzyme.getEcNumber());
            item.put("protId", enzyme.getProtId());
            item.put("kcat", enzyme.getKcat());
            item.put("formattedKcat", String.format("%.4f", enzyme.getKcat()));
            item.put("sub", enzyme.getSub());
            item.put("smiles", enzyme.getSmiles());
            item.put("sequences", enzyme.getSequences());
            item.put("predicted", enzyme.getPredicted());
            resultList.add(item);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("count", enzymes.size());
        result.put("records", resultList);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 精确查询: 根据准确的EC号和蛋白ID查询kcat
     */
    @GetMapping("/getExactKcat")
    public ResponseEntity<?> getExactKcat(
            @RequestParam String ecNumber,
            @RequestParam String protId) {
        
        log.info("Searching exact kcat by EC number: {} and protein ID: {}", ecNumber, protId);
        
        LambdaQueryWrapper<Enzyme> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Enzyme::getEcNumber, ecNumber)
                   .eq(Enzyme::getProtId, protId);
        
        Enzyme enzyme = enzymeService.getOne(queryWrapper);
        
        if (enzyme == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No enzyme found with the given EC number and protein ID");
            return ResponseEntity.ok(response);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", enzyme.getId());
        result.put("ecNumber", enzyme.getEcNumber());
        result.put("protId", enzyme.getProtId());
        result.put("kcat", enzyme.getKcat());
        result.put("formattedKcat", String.format("%.4f", enzyme.getKcat()));
        result.put("sub", enzyme.getSub());
        result.put("smiles", enzyme.getSmiles());
        result.put("sequences", enzyme.getSequences());
        result.put("predicted", enzyme.getPredicted());
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 保存酶数据到数据库
     * 接收前端发送的预测结果
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveEnzyme(@RequestBody Enzyme enzyme) {
        log.info("Saving enzyme data: {}", enzyme);
        
        try {
            // 确保标记为预测值
            if (enzyme.getPredicted() == null) {
                enzyme.setPredicted(1);
            }
            
            // 为空的EC号和蛋白ID设置默认值
            if (enzyme.getEcNumber() == null || enzyme.getEcNumber().isEmpty()) {
                enzyme.setEcNumber("unknown");
            }
            
            if (enzyme.getProtId() == null || enzyme.getProtId().isEmpty()) {
                enzyme.setProtId("unknown");
            }
            
            // 保存到数据库
            boolean saved = enzymeService.save(enzyme);
            
            if (saved) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "Enzyme data saved successfully");
                result.put("id", enzyme.getId());
                return ResponseEntity.ok(result);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to save enzyme data");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        } catch (Exception e) {
            log.error("Error while saving enzyme data", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error while saving enzyme data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
} 