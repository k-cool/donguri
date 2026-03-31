package com.c1.donguri.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    public static Map<String, String> loadEnv(String fileName) {
        final Map<String, String> envMap = new HashMap<>();

        try (
                InputStream is = EnvLoader.class
                        .getClassLoader()
                        .getResourceAsStream(fileName);

                BufferedReader br = new BufferedReader(new InputStreamReader(is))
        ) {

            if (is == null) {
                throw new RuntimeException(fileName + " 파일을 classpath에서 찾을 수 없음");
            }
            
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // 빈 줄 또는 주석(#) 무시
                if (line.isEmpty() || line.startsWith("#")) continue;

                // key=value 분리
                int idx = line.indexOf("=");
                if (idx == -1) continue; // 잘못된 라인 무시

                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();

                // 따옴표 제거 가능 ("value" → value)
                if ((value.startsWith("\"") && value.endsWith("\"")) ||
                        (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }

                envMap.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return envMap;
    }
}
