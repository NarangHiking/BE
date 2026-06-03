package com.naranghiking.common.service;

import com.naranghiking.common.dto.ImageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class FileService {

    @Value("${file.images-dir}")
    private String imagesDir; // 게시글에 사용될 images 경로

    private final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    public List<ImageRequest> saveFiles(List<MultipartFile> files, String pivot) {
        List<ImageRequest> storedFilenames = new ArrayList<>();
        if(files == null || files.isEmpty()) return storedFilenames;

        String path = "";
        // pivot이 board인 경우에는 게시글에서 넘어온 이미지
        if(pivot.equals("board")) path = "boardImages";

        File dir = new File(imagesDir + path);
        if(!dir.exists()) dir.mkdirs(); // 경로에 폴더가 없으면 생성

        for(MultipartFile file : files) {
            if(file.isEmpty()) continue;
            // 원본 파일명
            String originalFilename = file.getOriginalFilename();
            // 확장자 추출
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            // 이상한 확장자는 차단
            if(!ALLOWED_EXTENSIONS.contains(extension)) throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
            // 새로운 파일명 생성
            String storedFilename = UUID.randomUUID() + extension;

            try { // 해당 경로에 파일 저장
                file.transferTo(new File(dir.getAbsolutePath(), storedFilename));
                storedFilenames.add(new ImageRequest(originalFilename, storedFilename));
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 에러 발생", e);
            }
        }

        return storedFilenames;
    }

    public void deleteFiles(List<String> deletedImages, String pivot) {
        if(deletedImages == null || deletedImages.isEmpty()) return;

        String path = "";
        // pivot이 board인 경우에는 게시글에서 넘어온 이미지
        if(pivot.equals("board")) path = "boardImages";

        File dir = new File(imagesDir + path);

        for(String image : deletedImages) { // 해당 image가 경로에 존재하면 삭제
            File file = new File(dir.getAbsolutePath(), image);
            if(file.exists()) file.delete();
        }
    }
}
