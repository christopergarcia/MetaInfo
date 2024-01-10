package com.example.metainfo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

//import java.nio.file.Path;
//import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageProcessingException;

public interface MetaDataServiceImp {
	
	
	void init() throws IOException;
	
	String storeFile(MultipartFile file);
	
	Map<String,String> extractMetaData(File url) throws ImageProcessingException, IOException;
	
	void deleteFile(File url);
	
	List<Map<String, String>> getMetaData();

}
