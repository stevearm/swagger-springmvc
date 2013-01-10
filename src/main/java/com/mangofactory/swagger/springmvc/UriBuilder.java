package com.mangofactory.swagger.springmvc;

public class UriBuilder {

	private StringBuilder sb = new StringBuilder();
	public UriBuilder()
	{
	}
	public UriBuilder(String uri)
	{
		sb.append(uri);
	}
	public UriBuilder append(String segment)
	{
		if (!sb.toString().endsWith("/"))
		{
			sb.append("/");
		}
		if (segment.startsWith("/"))
		{
			sb.append(segment.substring(1));
		} else {
			sb.append(segment);
		}
		return this;
	}
	
	public String toString()
	{
		return sb.toString();
	}
	
	public static String removeStars(String string) {
		int i;
		while ((i = string.indexOf('*')) != -1) {
			if (string.length() == 1) {
				return "";
			}
			if (i == 0) {
				string = string.substring(1);
			} else if (i == string.length() - 1) {
				string = string.substring(0, i);
			} else {
				string = string.substring(0, i) + string.substring(i+1);
			}
		}
		return string;
	}
}
