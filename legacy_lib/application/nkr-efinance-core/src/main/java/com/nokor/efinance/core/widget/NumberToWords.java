package com.nokor.efinance.core.widget;

import java.math.BigDecimal;

public class NumberToWords

{

      static public boolean HelperConvertNumberToText(int num, String[] result)

      {

          String [] strones = {

            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",

            "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",

            "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen",

          };

       

          String [] strtens = {

              "Ten", "Twenty", "Thirty", "Fourty", "Fifty", "Sixty",

              "Seventy", "Eighty", "Ninety", "Hundred"

          };

 

          result[0] = "";

          int single, tens, hundreds;

       

          if(num > 1000)

              return false;

       

          hundreds = num / 100;

          num = num - hundreds * 100;

          if( num < 20)

          {

              tens = 0; // special case

              single = num;

          }

          else

          {

              tens = num / 10;

              num = num - tens * 10;

              single = num;

          }

         

          if(hundreds > 0)

          {

             result[0] += strones[hundreds-1];

             result[0] += " Hundred ";

          }

          if(tens > 0)

          {

             result[0] += strtens[tens - 1];

             result[0] += " ";

          }

          if(single > 0)

          {

             result[0] += strones[single - 1];

             result[0] += " ";

          }

          return true;

      }

      public static String[] convertToString(double value){
    	  BigDecimal bd = new BigDecimal(Double.toString(value));
    	  String [] rawSplit = bd.toPlainString().split("\\.");
    	  return rawSplit;
      }

      static public boolean ConvertNumberToText(int num, String[] result)

      {

          String tempString[] = new String[1];

          tempString[0] = "";

          int thousands;

          int temp;

          result[0] = "";

          if(num < 0 || num > 100000)

          {

              System.out.println(num + " \tNot Supported");

              return false;

          }

       

          if( num == 0)

          {

             System.out.println("Zero");

             return false;

          }

               

          if(num < 1000)

          {  

              HelperConvertNumberToText(num, tempString);

              result[0] += tempString[0];

          }

          else

          {

              thousands = num / 1000;

              temp = num - thousands * 1000;

              HelperConvertNumberToText(thousands, tempString);

              result[0] += tempString[0];

              result[0] += "Thousand ";             

              HelperConvertNumberToText(temp, tempString);

              result[0] += tempString[0];

          }

          return true;

      }

	public static String getDoubleToword(double value) {
		String afterPoint = "";
		String strWord = "";
		String[] strArray = new String[1];
		strArray = NumberToWords.convertToString(value);
		String[] result = new String[1];
		String zero = "";
		result[0] = "";
		if (!strArray[1].equals("0")) {
			
			if (NumberToWords.ConvertNumberToText(
					getTextToInteger(strArray[1]), result)) {
				if (Integer.parseInt(strArray[1].substring(0, 1)) == 0) {
					zero = "Zero ";
				}
				afterPoint = "point " + zero + result[0];
			}

		}
		if (NumberToWords.ConvertNumberToText(Integer.parseInt(strArray[0]),
				result)) {
			strWord = result[0] + "" + afterPoint;
		}
		return strWord;
	}
	
	/**
	 * 
	 * @param textValue
	 * @return
	 */
	private static Integer getTextToInteger(String textValue)
	{
		switch (textValue.length())
		{
		case 1:
			return Integer.parseInt(textValue.substring(0, 1));
		case 2:
			return Integer.parseInt(textValue.substring(0, 2));
		default:
			return 0;
		}
	}
}