package com.c1.donguri.util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@WebServlet(name = "s3 test", value = "/s3-test")
public class S3TestC extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String filePath = "C:\\Users\\soldesk\\Desktop\\upload\\test-img.png";


        String contentType = "image/png";

        String fileName = UUID.randomUUID() + "_" + "wow";

        File file = new File(filePath);

        S3Uploader uploader = new S3Uploader();


        try (InputStream inputStream = new FileInputStream(file)) {

            String fileUrl = uploader.upload(
                    inputStream,
                    fileName,
                    contentType,
                    file.length()
            );

            System.out.println("fileUrl: " + fileUrl);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void destroy() {
    }
}