package com.mw.omc.tool.ftp;

/**
 * Created by 00054054 on 2016/09/01.
 */
import java.io.*;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtil {
    private static Logger logger = Logger.getLogger(FTPUtil.class);

    public static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.info("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                logger.info("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.info("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }

    public static void getFile(FTPClient ftpClient, String ftpPath, String localPath) {
        try {
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);

            FileOutputStream outputStream = new FileOutputStream(localPath);
            ftpClient.retrieveFile(ftpPath, outputStream);
            outputStream.close();
            logger.info("下载文件成功: " + ftpPath);
        } catch (FileNotFoundException e) {
            logger.error("没有找到" + ftpPath + "文件");
            e.printStackTrace();
        } catch (SocketException e) {
            logger.error("连接FTP失败.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件读取错误。");
            e.printStackTrace();
        }
    }

    public static void putFile(FTPClient ftpClient, String ftpPath, String localFile) {
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 对远程目录的处理
//            if (ftpPath.contains("/")) {
//                ftpPath = ftpPath.substring(ftpPath.lastIndexOf("/") + 1);
//            }
            if(!ftpPath.contains("")){
                ftpPath = ftpPath + new File(localFile).getName();
            }

            InputStream in = new FileInputStream(new File(localFile));
            ftpClient.storeFile(ftpPath, new FileInputStream(new File(localFile)));
            in.close();
            logger.info("上传文件" + ftpPath + "到FTP成功!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String host = "10.86.95.16";
        String user = "mwCM";
        String pwd = "mwCM";
        int port = 21;
        String localFile = "D:\\0-ProjectCode\\mwtool\\omctool\\src\\main\\java\\com\\mw\\omc\\tool\\service\\ftp\\test2.txt";
        FTPClient ftpClient = FTPUtil.getFTPClient(host, user, pwd, port);
        FTPUtil.putFile(ftpClient, "", localFile);
//        FTPUtil.getFile(ftpClient, "", localFile);
        ftpClient.disconnect();
    }
}