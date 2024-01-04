package kr.co.market.common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {

    /**
     * 오늘날짜를 지정한 패턴에 맞게 가져오기
     * @param datePattern (yyyy-MM-dd : 2023-01-16 , yyyy-MM-dd HH:mm:ss.SSS : 2023-01-16 21:38:02.077
     */
    public static String getDatePattern(String datePattern) {
        String returnTiem = "";
        try {
            SimpleDateFormat currentTime = new java.text.SimpleDateFormat(datePattern, Locale.KOREA);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            returnTiem = currentTime.format(ts.getTime());
        } catch (IllegalArgumentException e) {
            System.out.println("예외발생::"+e);
        }
        return returnTiem;
    }

    public static HashMap fileUpload(Map<String, MultipartFile> files, String rootPath) throws Exception {
        HashMap map = new HashMap();
        //storePath::저장경로
        String storePath = rootPath + System.getProperty("file.separator");
        //특정 문자가 들어올 경우 공백으로 replace
        storePath = getFilePathReplace(storePath);
        //저장폴더 권한 설정
        File saveFolder = new File(storePath);
        if (!saveFolder.exists() || saveFolder.isFile()) {
            saveFolder.setExecutable(false, true);
            saveFolder.setReadable(true);
            saveFolder.setWritable(true, true);
            saveFolder.mkdirs();
        }

        Iterator<Map.Entry<String, MultipartFile>> itr = files.entrySet().iterator();
        MultipartFile file;
        String filePath = "";

        while (itr.hasNext()) {
            Map.Entry<String, MultipartFile> entry = itr.next();

            file = entry.getValue();
            //이용자가 등록한 파일 원본명
            String originalFileName = file.getOriginalFilename();
            if ("".equals(originalFileName)) {
                continue;
            }
            //파일 이름 길이
            int index = originalFileName.lastIndexOf(".");
            //파일 확장자
            String fileExt = originalFileName.substring(index + 1);
            //변환된 파일명
            String newName = getUniqueFileName(storePath, fileExt); //날짜 20121011+랜덤8자리
            //파일크기(KB)
            long _size = file.getSize();
            //저장경로+변환된파일명+확장자
            filePath = storePath + File.separator + newName + "." + fileExt;
            //파일 풀경로 내 특수문자 들어갈 경우 replace
            filePath = getFilePathReplace(filePath);
            //파일업로드
            file.transferTo(new File(filePath));
            map.put("fileSaveName", newName + "." + fileExt);
            map.put("filePath", filePath);
            map.put("fileExt", fileExt);
            map.put("fileSize", _size);
            map.put("fileOrgName", originalFileName);
        }

        return map;
    }

    public static String getFilePathReplace(String str) {
        str = str.replace("?", "");
        str = str.replace("[*]", "");
        str = str.replace("[\"]", "");
        return str;
    }

    /**
     * 해당 경로에 중복되지 않는 파일명을 구한다.
     * @param path 전체 풀 경로
     * @param fileExt 첨부파일의 호
     * @return
     */
    public static String getUniqueFileName(String path, String fileExt) {
        String filename = "";
        while(true) {
            filename = "uploadFile" + getDatePattern("yyyyMMdd")+getRandomString(4);
            String fullFileName = getFilePathReplace(path + System.getProperty("file.separator") + filename + "." + fileExt);
            if(!new File(fullFileName).exists()) {
                break;
            }
        }
        return filename;
    }

    /**
     * 영문 대, 소문자, 숫자가 조합된 랜덤한 문자열을 구한다.
     * @param length 구하고자 하는 문자열 갯수
     * @return 입력된 길이의 랜덤 문자열
     */
    public static String getRandomString(int length) {

        String[] arrString = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","x","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","X"};
        java.util.Random rnd = new java.util.Random();
        String returnValue = "";
        for(int i =0; i< length; i++) {
            returnValue+= arrString[rnd.nextInt(arrString.length-1)];
        }
        return returnValue;

    }

    public static List<Map<String, Object>> youtubeClient(int count, String qTitle) throws Exception {
        WebClient webClient = CommonUtil.getBaseUrl("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults="+count+"&q="+qTitle+"&key=AIzaSyDBu3opwfLYQFX_n5amH7YYJhb6A9IQeCc");
        String token = webClient.get()
                .uri("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults="+count+"&q="+qTitle+"&key=AIzaSyDBu3opwfLYQFX_n5amH7YYJhb6A9IQeCc")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObj = (JSONObject) jsonParser.parse(token);
        JSONArray memberArray = (JSONArray) jsonObj.get("items");
        List<Map<String, Object>> infoList = new ArrayList<>();
        for(int i=0; i<memberArray.size(); i++){
            Map<String,Object> hMap = new HashMap<>();
            JSONObject tempObj = (JSONObject) memberArray.get(i);
            JSONObject memberArray1 = (JSONObject) tempObj.get("id");
            JSONObject title = (JSONObject) tempObj.get("snippet");
            hMap.put("videoId",memberArray1.get("videoId"));
            hMap.put("title",title.get("title"));
            hMap.put("channelTitle",title.get("channelTitle"));
            infoList.add(i,hMap);
        }
        return infoList;
    }

    public static WebClient getBaseUrl(final String uri) {
        return WebClient.builder()
                .baseUrl(uri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .mutate()
                .build();
    }
}
