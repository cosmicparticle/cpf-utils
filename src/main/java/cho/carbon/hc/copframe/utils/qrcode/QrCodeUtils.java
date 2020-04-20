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
	 * @return 成功返回true，失败返回false
	 */
	public static boolean encodeQRCodeImage(String content, String charset, OutputStream outputStream, String format, int width, int height){
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        //指定编码格式  
        //hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
        //指定纠错级别(L--7%,M--15%,Q--25%,H--30%)  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.MARGIN, 0);
        //编码内容,编码类型(这里指定为二维码),生成图片宽度,生成图片高度,设置参数  
        BitMatrix bitMatrix = null;  
        try {  
            bitMatrix = new MultiFormatWriter().encode(new String(content.getBytes(charset==null?"UTF-8":charset), "ISO-8859-1"), BarcodeFormat.QR_CODE, width, height, hints);  
        } catch (Exception e) {  
            System.out.println("编码待生成二维码图片的文本时发生异常,堆栈轨迹如下");  
            e.printStackTrace();  
            return false;  
        }  
        //生成的二维码图片默认背景为白色,前景为黑色,但是在加入logo图像后会导致logo也变为黑白色,至于是什么原因还没有仔细去读它的源码  
        //所以这里对其第一个参数黑色将ZXing默认的前景色0xFF000000稍微改了一下0xFF000001,最终效果也是白色背景黑色前景的二维码,且logo颜色保持原有不变  
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);  
        //这里要显式指定MatrixToImageConfig,否则还会按照默认处理将logo图像也变为黑白色(如果打算加logo的话,反之则不须传MatrixToImageConfig参数)  
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
