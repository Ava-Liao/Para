package com.graduate.para.utils;

import com.graduate.para.model.Enzyme;
import com.graduate.para.service.EnzymeService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV导入工具类，使用OpenCSV库简化导入过程
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CsvImportUtil {

    private final EnzymeService enzymeService;
    
    /**
     * 从CSV文件导入酶数据
     * @param filePath CSV文件路径
     * @return 导入的记录数
     */
    public int importEnzymesFromCsv(String filePath) {
        log.info("Importing enzymes from CSV file: {}", filePath);
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            return processEnzymeData(reader);
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
            return 0;
        }
    }
    
    /**
     * 从上传的CSV文件导入酶数据
     * @param file 上传的CSV文件
     * @return 导入的记录数
     */
    public int importEnzymesFromUpload(MultipartFile file) {
        log.info("Importing enzymes from uploaded file: {}", file.getOriginalFilename());
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return processEnzymeData(reader);
        } catch (IOException e) {
            log.error("Error reading uploaded file", e);
            return 0;
        }
    }
    
    /**
     * 处理CSV数据并导入到数据库
     * @param reader CSV文件读取器
     * @return 导入的记录数
     */
    private int processEnzymeData(Reader reader) {
        try (CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1) // 跳过标题行
                .build()) {
            
            String[] line;
            List<Enzyme> enzymeList = new ArrayList<>();
            int count = 0;
            
            while ((line = csvReader.readNext()) != null) {
                if (line.length >= 3) {
                    try {
                        Enzyme enzyme = new Enzyme();
                        enzyme.setEcNumber(line[0].trim());
                        enzyme.setProtId(line[1].trim());
                        enzyme.setKcat(Double.parseDouble(line[2].trim()));
                        enzymeList.add(enzyme);
                        count++;
                        
                        // 每1000条记录保存一次，避免内存溢出
                        if (count % 1000 == 0) {
                            enzymeService.saveBatch(enzymeList);
                            log.info("Saved {} enzyme records", count);
                            enzymeList.clear();
                        }
                    } catch (NumberFormatException e) {
                        log.warn("Invalid kcat value in line: {}", String.join(",", line));
                    }
                }
            }
            
            // 保存剩余的记录
            if (!enzymeList.isEmpty()) {
                enzymeService.saveBatch(enzymeList);
            }
            
            log.info("Successfully imported {} enzyme records", count);
            return count;
            
        } catch (IOException | CsvValidationException e) {
            log.error("Error processing CSV data", e);
            return 0;
        }
    }
}