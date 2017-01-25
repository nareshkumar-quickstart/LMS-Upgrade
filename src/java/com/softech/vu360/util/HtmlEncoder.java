package com.softech.vu360.util;

import org.apache.log4j.Logger;

public class HtmlEncoder {

	private static Logger log = Logger.getLogger(HtmlEncoder.class.getName());
	
	public static StringBuilder escapeHtmlFull(String s)
	 {
	     StringBuilder b = new StringBuilder(s.length());
	     for (int i = 0; i < s.length(); i++)
	     {
	       char ch = s.charAt(i);
	       if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9')
	       {
	         // safe
	         b.append(ch);
	       }
	       else if (Character.isWhitespace(ch))
	       {
	         // paranoid version: whitespaces are unsafe - escape
	         // conversion of (int)ch is naive
	    	   //// safe to me
	    	   b.append(ch);
	       }
	       else if (ch == '-' || ch == '#' || ch == '%' || ch == '@' ||ch == '.' || ch == ',' || ch == '!' || ch == '$' || ch == '&' || ch == '(' || ch == ')' || ch == '_' || ch == ';' || ch == ':'|| ch == '/') {
	    	   b.append(ch);
	       }
	       else if (Character.isISOControl(ch))
	       {
	         // paranoid version:isISOControl which are not isWhitespace removed !
	         // do nothing do not include in output !
	       }else if(ch == '"'){
	    	   b.append("\"");
	       }else if(ch == '>'){
	    	   b.append("&gt;");
	       }else if(ch == '<'){
	    	   b.append("&lt;");
	       }
	       else if (Character.isHighSurrogate(ch))
	       {
	         int codePoint;
	         if (i + 1 < s.length() && Character.isSurrogatePair(ch, s.charAt(i + 1))
	           && Character.isDefined(codePoint = (Character.toCodePoint(ch, s.charAt(i + 1)))))
	         {
	            b.append("&#").append(codePoint).append(";");
	         }
	         else
	         {
	           log.debug("bug:isHighSurrogate");
	         }
	         i++; //in both ways move forward
	       }
	       else if(Character.isLowSurrogate(ch))
	       {
	         // wrong char[] sequence, //TODO: LOG !!!
	         log.debug("bug:isLowSurrogate");
	         i++; // move forward,do nothing do not include in output !
	       }
	       else
	       {
	         if (Character.isDefined(ch))
	         {
	           // paranoid version
	           // the rest is unsafe, including <127 control chars
	           b.append("&#").append((int) ch).append(";");
	         }
	         //do nothing do not include undefined in output!
	       }
	    }
	     
	     return b;
	 }

}
