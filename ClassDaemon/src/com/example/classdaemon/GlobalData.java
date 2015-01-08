package com.example.classdaemon;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

public class GlobalData {
	private static String UserName;
	private static String currentCourse;
	public static String cookie;
	public static String sessionid;
	public static String baseUrl = "http://182.92.169.74:8000";
	public static String testName;
	public static int success = 0x123;
	public static String test;
	public static int testnum;
	
	public static void setUserName(String userName)
	{
		UserName = userName;
	}
	public static void setCourse(String course)
	{
		currentCourse = course;
	}
	public static String getUserName()
	{
		return UserName;
	}
	public static String getCourse()
	{
		return currentCourse;
	}
	public static void setTest(String testValue)
	{
		test = testValue;
	}
	public static void setTestNum(int testNum)
	{
		testnum = testNum;
	}
	public static String getTest()
	{
		return test;
	}
	public static int getTestNum()
	{
		return testnum;
	}
}
