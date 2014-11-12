package com.doschool.methods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.component.atemotion.AtSpan;
import com.doschool.component.atemotion.ExpressionUtil;
import com.doschool.component.atemotion.AtLayout.ListAdapter;
import com.doschool.entity.SimplePerson;

public class ConvertMethods {

	/**
	 * 去除微博/评论等内容中的@标签和表情标签 保证我们的标签形式不被暴露
	 */
	public static String removeTextTag(String input) {
		input = input.replaceAll("\\[a=.*?\\].*?\\[\\/a\\]", "");
		input = input.replaceAll("\\[e\\]\\d{4}\\[\\/e\\]", "");
		return input;
	}

	/**
	 * dp转px的方法
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 给定一个人名，通过与名片列表的比对，返回显示的名字
	 * 如果有名片：则显示真实姓名
	 * 否则：显示昵称
	 */
	public static String getShowName(SimplePerson sp) {
//		if (DoschoolApp.cardsList != null) {
//			for (int i = 0; i < DoschoolApp.cardsList.size(); i++) {
//				if (sp.personId == DoschoolApp.cardsList.get(i).personId) {
//					return sp.trueName;
//				}
//			}
//		}
//		return sp.nickName;
		return sp.trueName;
	}

	/**
	 * 把yyyy-MM-dd HH:mm:ss格式的日期，转成long型的
	 */
	public static long dateStrToLong(String str) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str).getTime();
		} catch (ParseException e) {
			return new Date().getTime();
		}
	}

	/**
	 * 把long型的日期，转成MM-dd HH:mm型的
	 */
	public static String longToMDHM(long time) {
		return new SimpleDateFormat("M月d日 H:m").format(new Date(time));
	}

	/**
	 * 把long型的日期，转成智能型的日期
	 */
	public static String dateLongToDiyStr(long time) {
		Calendar changeC = Calendar.getInstance();
		changeC.setTimeInMillis(time);
		Calendar currentC = Calendar.getInstance();
		if (currentC.getTimeInMillis() - changeC.getTimeInMillis() < 0) {
			return "来自未来";
		} else if (changeC.get(Calendar.YEAR) < currentC.get(Calendar.YEAR) - 2) {
			return new SimpleDateFormat("yyyy年M月d日 H:mm").format(changeC.getTime());
		} else if (changeC.get(Calendar.YEAR) == currentC.get(Calendar.YEAR) - 2) {
			return new SimpleDateFormat("前年M月d日 H:mm").format(changeC.getTime());
		} else if (changeC.get(Calendar.YEAR) < currentC.get(Calendar.YEAR) - 1) {
			return new SimpleDateFormat("去年M月d日 H:mm").format(changeC.getTime());
		} else if (changeC.get(Calendar.DAY_OF_YEAR) < currentC.get(Calendar.DAY_OF_YEAR) - 2) {
			return new SimpleDateFormat("M月d日 H:mm").format(changeC.getTime());
		} else if (changeC.get(Calendar.DAY_OF_YEAR) == currentC.get(Calendar.DAY_OF_YEAR) - 2) {
			return new SimpleDateFormat("前天 H:mm").format(changeC.getTime());
		} else if (changeC.get(Calendar.DAY_OF_YEAR) == currentC.get(Calendar.DAY_OF_YEAR) - 1) {
			return new SimpleDateFormat("昨天 H:mm").format(changeC.getTime());
		} else if (changeC.get(Calendar.DAY_OF_YEAR) == currentC.get(Calendar.DAY_OF_YEAR)) {
			return new SimpleDateFormat("今天 H:mm").format(changeC.getTime());
		}
		return "";
	}

	/**
	 * 把SpannableString转成String
	 */
	public static SpannableString stringToSpannableString(String str, Context context) {
		return stringToSpannableString(str, context, null, null);
	}

	/**
	 * 把String转成SpannableString
	 */
	public static SpannableString stringToSpannableString(String str, Context context, ArrayList<AtSpan> at3spanList, ListAdapter listAdapter) {
		Drawable drawableAt = context.getResources().getDrawable(R.drawable.img_at_dark);
		drawableAt.setBounds(0, 0, DoschoolApp.widthPixels / 20, DoschoolApp.widthPixels / 20);

		Drawable drawableAtEnd = context.getResources().getDrawable(R.drawable.img_transparent);
		drawableAtEnd.setBounds(0, 0, DoschoolApp.widthPixels / 100, DoschoolApp.widthPixels / 100);

		SpannableString spannableString = new SpannableString(str);
		String zhengze = "\\[a=.*?\\].*?\\[\\/a\\]";

		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		Matcher matcher = sinaPatten.matcher(spannableString);
		if (at3spanList != null)
			at3spanList.clear();

		while (matcher.find()) {

			String key = matcher.group();

			Pattern patternUid = Pattern.compile("\\[a=\\d*?\\]@", Pattern.CASE_INSENSITIVE);
			Matcher matcherUid = patternUid.matcher(key);
			String keyUid = "";
			while (matcherUid.find()) {
				keyUid = matcherUid.group();

				ImageSpan imageSpanAt = new ImageSpan(drawableAt);
				spannableString.setSpan(imageSpanAt, matcher.start() + matcherUid.start(), matcher.start() + matcherUid.start() + keyUid.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			int id = Integer.valueOf(keyUid.substring(3, keyUid.length() - 2));
			if (at3spanList != null)
				at3spanList.add(new AtSpan(matcher.start(), matcher.end() - 1, id));
			Pattern patternNick = Pattern.compile("]@.*?\\[/a\\]", Pattern.CASE_INSENSITIVE);
			Matcher matcherNick = patternNick.matcher(key);
			String keyNick = null;
			while (matcherNick.find()) {
				keyNick = matcherNick.group();
				keyNick = keyNick.substring(2, keyNick.length() - 4);
				ForegroundColorSpan sp = new ForegroundColorSpan(context.getResources().getColor(R.color.at_color));
				spannableString.setSpan(sp, matcher.start() + matcherNick.start() - 2, matcher.start() + matcherNick.end() - 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				break;
			}

			ImageSpan imageSpanAt = new ImageSpan(drawableAtEnd);
			spannableString.setSpan(imageSpanAt, matcher.end() - 4, matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

		}

		String zhengze2 = "\\[e\\]\\d{4}\\[\\/e\\]";
		try {
			spannableString = ExpressionUtil.getExpressionString(context, spannableString, zhengze2);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		if (listAdapter != null) {
			listAdapter.notifyDataSetChanged();
			listAdapter.notifyDataSetInvalidated();
		}

		return spannableString;
	}

}
