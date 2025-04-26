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
     * 按底物结构查询
     */
    @GetMapping("/findBySmiles")
    public ResponseEntity<?> findBySmiles(@RequestParam String smiles) {
        log.info("Searching enzymes by substrate name: {}", smiles);
        LambdaQueryWrapper<Enzyme> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Enzyme::getSmiles, smiles);
        List<Enzyme> enzymes = enzymeService.list(queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("count", enzymes.size());
        result.put("records", enzymes);
        
        return ResponseEntity.ok(result);
    }

    
    /**
     * 通过底物名称和底物结构(SMILES)查询kcat
     */
    @GetMapping("/findKcat")
    public ResponseEntity<?> findKcat(
            @RequestParam(required = false) String sub,
            @RequestParam(required = false) String smiles) {
        
        log.info("Searching kcat by substrate: {}, SMILES: {}", 
                sub, smiles);
        
        LambdaQueryWrapper<Enzyme> queryWrapper = new LambdaQueryWrapper<>();
        boolean hasCondition = false;
    
        
        if (sub != null && !sub.isEmpty()) {
            queryWrapper.like(Enzyme::getSub, sub);
            hasCondition = true;
        }
        
        if (smiles != null && !smiles.isEmpty()) {
            queryWrapper.like(Enzyme::getSmiles, smiles);
            hasCondition = true;
        }
        
        if (!hasCondition) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "At least one parameter (sub or smiles) is required");
            return ResponseEntity.badRequest().body(response);
        }
        
        List<Enzyme> enzymes = enzymeService.list(queryWrapper);
        
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Enzyme enzyme : enzymes) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", enzyme.getId());
            item.put("kcat", enzyme.getKcat());
            item.put("formattedKcat", String.format("%.4f", enzyme.getKcat()));
            item.put("sub", enzyme.getSub());
            item.put("smiles", enzyme.getSmiles());
            item.put("sequences", enzyme.getSequences());
            item.put("predicted", enzyme.getPredicted());
            item.put("temperature", enzyme.getTemperature());
            resultList.add(item);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("count", enzymes.size());
        result.put("records", resultList);
        
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
            // 确保标记为预测值，如果为空设置默认值为1
            if (enzyme.getPredicted() == null) {
                enzyme.setPredicted(1);
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