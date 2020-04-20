package cho.carbon.hc.copframe.utils.qrcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cho.carbon.hc.copframe.utils.FormatUtils;
import cho.carbon.hc.copframe.utils.TextUtils;

public class QrCodeUtils {
	static Logger logger = LoggerFactory.getLogger(QrCodeUtils.class);
	
	/**
     * 根据编码和配置生成二维码并写入输出流
     * @param content 二维码内容
     * @param charset 内容编码格式
     * @param outputStream 输出流
     * @param format 图片格式
     * @param width 图片宽度
     * @param height 图片高度
	public static boolean encodeQRCodeImage(String content, String charset, OutputStream outputStream, String format, int width, int height){
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        //æŒ‡å®šç¼–ç æ ¼å¼  
        //hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
        //æŒ‡å®šçº é”™çº§åˆ«(L--7%,M--15%,Q--25%,H--30%)  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.MARGIN, 0);
        //ç¼–ç å†…å®¹,ç¼–ç ç±»åž‹(è¿™é‡ŒæŒ‡å®šä¸ºäºŒç»´ç ),ç”Ÿæˆå›¾ç‰‡å®½åº¦,ç”Ÿæˆå›¾ç‰‡é«˜åº¦,è®¾ç½®å‚æ•°  
        BitMatrix bitMatrix = null;  
        try {  
            bitMatrix = new MultiFormatWriter().encode(new String(content.getBytes(charset==null?"UTF-8":charset), "ISO-8859-1"), BarcodeFormat.QR_CODE, width, height, hints);  
        } catch (Exception e) {  
            System.out.println("ç¼–ç å¾…ç”ŸæˆäºŒç»´ç å›¾ç‰‡çš„æ–‡æœ¬æ—¶å‘ç”Ÿå¼‚å¸¸,å †æ ˆè½¨è¿¹å¦‚ä¸‹");  
            e.printStackTrace();  
            return false;  
        }  
          //生成的二维码图片默认背景为白色,前景为黑色,但是在加入logo图像后会导致logo也变为黑白色,至于是什么原因还没有仔细去读它的源码  
        //所以这里对其第一个参数黑色将ZXing默认的前景色0xFF000000稍微改了一下0xFF000001,最终效果也是白色背景黑色前景的二维码,且logo颜色保持原有不变  
        try {  
        	MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream, config);
            //MatrixToImageWriter.writeToFile(bitMatrix, imagePath.substring(imagePath.lastIndexOf(".") + 1), new File(imagePath), config);  
        } catch (IOException e) { 
            return false; 
        }  
        return true;
	}
	
	   /** 
     * 生成二维码 
     * @param content   二维码内容 
     * @param charset   编码二维码内容时采用的字符集(传null时默认采用UTF-8编码) 
     * @param imagePath 二维码图片存放路径(含文件名) 

     * @param width     生成的二维码图片宽度 
     * @param height    生成的二维码图片高度 
     * @param logoPath  logo头像存放路径(含文件名,若不加logo则传null即可) 
     * @return 生成二维码结果(true or false) 
     */  
	public static boolean encodeQRCodeImage(String content, String charset, String imagePath, int width, int height) {
    	File file = new File(imagePath);
    	if(!file.exists()){
    		try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("创建文件时失败", e);
			}
    	}
    	try {
			FileOutputStream outputStream = new FileOutputStream(file);
			return encodeQRCodeImage(content, charset, outputStream, imagePath.substring(imagePath.lastIndexOf(".") + 1), width, height);
		} catch (FileNotFoundException e) {
		}
    	return false;
    }  
   
    public static void main(String[] args) {
    	long result = ChronoUnit.DAYS.between((new Date(0l)).toInstant(), Instant.now());
    	String dateCode = TextUtils.convert(FormatUtils.toInteger(result));
    	for (int i = 1; i <= 10; i++) {
    		for (int j = 0; j < 10; j++) {
    			StringBuffer code = new StringBuffer();
    			code.append("HS");
    			code.append("0" + TextUtils.convert(i));
    			code.append("00");
    			code.append(dateCode);
    			code.append(TextUtils.uuid(4, 62));
    			System.out.println(code);
    			QrCodeUtils.encodeQRCodeImage(code.toString(), "utf-8", "d://qrcodes1/" + code.toString() + ".png", 400, 400);
    		}
		}
		
	}
    
}
