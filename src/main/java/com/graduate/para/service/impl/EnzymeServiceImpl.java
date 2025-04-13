package com.graduate.para.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduate.para.model.Enzyme;
import com.graduate.para.mapper.EnzymeMapper;
import com.graduate.para.service.EnzymeService;
import org.springframework.stereotype.Service;

@Service
public class EnzymeServiceImpl extends ServiceImpl<EnzymeMapper, Enzyme> implements EnzymeService {
} 