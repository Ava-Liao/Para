package com.graduate.para.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.graduate.para.model.Enzyme;
import com.graduate.para.service.EnzymeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    
    @PostMapping("/import")
    public ResponseEntity<?> importData(@RequestParam("file") MultipartFile file) {
        log.info("Received file upload request: {}", file.getOriginalFilename());
        try {
            List<Enzyme> enzymeList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            // Skip header
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                if (data.length >= 3) {
                    Enzyme enzyme = new Enzyme();
                    enzyme.setEcNumber(data[0].trim());
                    enzyme.setProtId(data[1].trim());
                    enzyme.setKcat(Double.parseDouble(data[2].trim()));
                    enzymeList.add(enzyme);
                }
            }
            
            enzymeService.saveBatch(enzymeList);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Successfully imported " + enzymeList.size() + " records");
            response.put("count", enzymeList.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error importing enzyme data", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error importing data: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
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
     * 同时按EC号和蛋白ID查询kcat
     */
    @GetMapping("/findKcat")
    public ResponseEntity<?> findKcat(
            @RequestParam(required = false) String ecNumber,
            @RequestParam(required = false) String protId) {
        
        log.info("Searching kcat by EC number: {} and protein ID: {}", ecNumber, protId);
        
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
        
        if (!hasCondition) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "At least one parameter (ecNumber or protId) is required");
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
            item.put("formattedKcat", String.format("%.2e", enzyme.getKcat()));
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
        result.put("formattedKcat", String.format("%.2e", enzyme.getKcat()));
        
        return ResponseEntity.ok(result);
    }
} 