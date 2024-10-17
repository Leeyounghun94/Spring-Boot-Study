package org.zerock.memberboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {

    // 업로드 결과 처리용 DTO

    private String fileName ;

    private String uuid ;

    private String folderPath;

    private String getImageURL() {// 실제 파일과 관련된 모든 정보를 가지는데 나중에 전체 경로가 필요할 경우를 대비해서 제공하는 메서드

        try {
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL() {// getImgURL()과 동일하며 중간에 S_가 추가한 형태

        try {
            return URLEncoder.encode(folderPath + "/s_" + uuid + "_" + fileName, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "" ;
    }

}
