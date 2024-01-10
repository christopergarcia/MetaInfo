package com.example.metainfo.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageProcessingException;
import com.example.metainfo.service.MetaDataServiceImp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
@RequestMapping("metadata")
@AllArgsConstructor
public class MediaController {
	
	private MetaDataServiceImp storage;
	
	@PostMapping("upload")
	public Map<String,String> uploadFile(@RequestParam("file") MultipartFile file) throws ImageProcessingException, IOException{
		
		String path = storage.storeFile(file);
		String url = (("mediafiles/") + path).toString();
		File fileUrl = new File(url);
		
		return storage.extractMetaData(fileUrl);
	}
	
	@GetMapping("/getData")
	public List<Map<String, String>> getMetaData(){
		return storage.getMetaData();
	}
}
