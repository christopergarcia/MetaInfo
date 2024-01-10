package com.example.metainfo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.example.metainfo.model.MetaData;
import com.example.metainfo.repository.MetaDataRepository;

import jakarta.annotation.PostConstruct;

@Service
public class MetaDataService implements MetaDataServiceImp {
	
	private MetaDataRepository metaDataRepository;
	
	@Value("${media.location}")
	private String mediaLocation;
	
	private Path rootLocation;
	
	@Autowired
	public MetaDataService(MetaDataRepository metaDataRepository) {
		this.metaDataRepository = metaDataRepository;
	}

	@Override
	@PostConstruct
	public void init() throws IOException {
		rootLocation = Paths.get(mediaLocation);
		Files.createDirectories(rootLocation);
	}

	@Override
	public String storeFile(MultipartFile file) {
		
		try {
			if(file.isEmpty())
				throw new RuntimeException("Archivo vacio.");
			
			String fileName = file.getOriginalFilename();
			Path destinationFile = rootLocation.resolve(Paths.get(fileName))
					.normalize().toAbsolutePath();
			
			try (InputStream inputStream = file.getInputStream()){
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
			
			return fileName;
		} catch (IOException e) {
			throw new RuntimeException("Fallo en cargar el archivo ", e);
		}
	}
	
	@Override
	public Map<String, String> extractMetaData(File url) throws ImageProcessingException, IOException {
		
		Metadata metaDataLibrary = ImageMetadataReader.readMetadata(url);
		Map<String,String> storeMetaData = new HashMap<>();
		
		for(Directory directory : metaDataLibrary.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				storeMetaData.put(tag.getTagName(), tag.getDescription());
			}
		}
		
		MetaData metaDataEntity = new MetaData();
		metaDataEntity.setData(storeMetaData);
		
		metaDataRepository.save(metaDataEntity);
		
		deleteFile(url);
		return storeMetaData;
	}
	
	public List<Map<String, String>> getMetaData(){
		return metaDataRepository.findAll().stream().map(MetaData::getData).toList();
	}

	@Override
	public void deleteFile(File url) {
		FileSystemUtils.deleteRecursively(url);
	}

}
