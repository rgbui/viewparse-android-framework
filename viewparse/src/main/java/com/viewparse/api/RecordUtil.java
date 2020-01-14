package com.viewparse.api;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 录制音频
 */
public class RecordUtil {
    private static MediaRecorder mr = null;
    private static String soundPath = "";
    private static File soundFile = null;

    //文件夹根路径
    private static final String dirPath = "viewparse/sounds";

    public static void startRecord() {
        if (mr == null) {
            File dir = new File(Environment.getExternalStorageDirectory(), dirPath);
            if (!dir.exists()) dir.mkdirs();
            soundFile = new File(dir, System.currentTimeMillis() + ".amr");
            if (!soundFile.exists()) {
                try {
                    soundFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            soundPath = soundFile.getPath();
            mr = new MediaRecorder();
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);  //音频输入源
            mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);   //设置输出格式
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);   //设置编码格式
            mr.setOutputFile(soundFile.getAbsolutePath());
            try {
                mr.prepare();
                mr.start();  //开始录制
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Object> stopRecord() {
        if (mr != null) {
            mr.stop();
            mr.release();
            mr = null;
        }
        String data = "";
        try {
            data = FileUtil.encodeBase64File(soundPath);
        } catch (Exception e) {
            LogUtil.i(e.getMessage());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("path", soundPath);
        map.put("data", data);
        return map;
    }

}
