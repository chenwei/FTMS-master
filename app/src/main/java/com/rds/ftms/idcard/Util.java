package com.rds.ftms.idcard;

import java.text.SimpleDateFormat;
import java.util.Date;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.rds.ftms.R;

public class Util {


	public static SoundPool sp ;
	public static SparseIntArray suondMap;
	public static Context context;

	//初始化声音池
	public static void initSoundPool(Context context){
		Util.context = context;
		sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
		suondMap = new SparseIntArray();
		suondMap.put(1, sp.load(context, R.raw.msg, 1));
	}

	//播放声音池声音
	public static  void play(int sound, int number){
		AudioManager am = (AudioManager)Util.context.getSystemService(Context.AUDIO_SERVICE);
		//返回当前AlarmManager最大音量

		//返回当前AudioManager对象的音量值
		float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		sp.play(
				suondMap.get(sound), //播放的音乐Id
				audioCurrentVolume, //左声道音量
				audioCurrentVolume, //右声道音量
				1, //优先级，0为最低
				number, //循环次数，0无不循环，-1无永远循环
				1);//回放速度，值在0.5-2.0之间，1为正常速度
	}
	@SuppressLint("SimpleDateFormat")
	public static String getTime(){
		String model = "yyyy-MM-dd HH:mm:ss";
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(model);
		String dateTime = format.format(date);
		return  dateTime;
	}

}
