package com.sample.Service;

import com.sample.Dao.SampleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleServiceImpl implements SampleService{

    @Autowired
    SampleDao sampleDao;
}
