package org.zerock.memberboard.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.memberboard.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {


    @Value("${org.zerock.upload.path}") //import org.springframework.beans.factory.annotation.Value;
    private String uploadPath ; // application.properties 추가 필수


    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
        // MultipartFile[] -> 배열을 사용하면 여러 개 파일 정보 처리 할 수 있고 화면에서 여러개의 파일을 동시에 업로드 할수 있다.

        List<UploadResultDTO> resultDTOList = new ArrayList<>(); // 399p.
        for (MultipartFile uploadFile : uploadFiles) {


            if (uploadFile.getContentType().startsWith("image") == false) {
                log.warn("이 파일은 이미지 형식에 맞지 않습니다.");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름 IE, Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("UploadController.uploadFile 메서드 실행중 . .. ");
            log.info("파일 이름 : "+ fileName);

            // 날짜 폴더 생성
            String folderPath = makeFoler();

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 "_" 구분하기
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);    // 원본 파일 저장

                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName ; // 섬네일 생성

                File thumnailFile = new File(thumbnailSaveName);    // 섬네일 파일 이름은 중간에 s_로 시작하게

                Thumbnailator.createThumbnail(savePath.toFile(), thumnailFile,100,100);// 섬네일 생성하기

                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    private String makeFoler() {

        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folerPath = str.replace("/", File.separator);

        // 폴더 만들기   - - -
        File uploadPathFoler = new File(uploadPath, folerPath);

        if (uploadPathFoler.exists() == false) {
            uploadPathFoler.mkdirs();

        }
        return folerPath;
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("파일 이름 : " +  srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("파일 : " + file);

            HttpHeaders headers = new HttpHeaders();

            //MIME 타입 처리
            headers.add("Content type", Files.probeContentType(file.toPath()));

            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    return result ;
    }


    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {
        // 주의 할 점 : 원본파일과 함께 섬네일 파일도 같이 삭제해야 한다는 점!, 원본 파일의 이름을 파라미터로 전송 받은 후 파일 객체를 이용하여 원본과 섬네일을 같이 삭제..

        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");

            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
