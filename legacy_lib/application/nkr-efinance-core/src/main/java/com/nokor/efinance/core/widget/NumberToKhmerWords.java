package com.nokor.efinance.core.widget;

import java.math.BigDecimal;

public class NumberToKhmerWords

{

      static public boolean HelperConvertNumberToText(int num, String[] result)

      {

          String [] strones = {

                  "មួយ", "ពីរ", "បី", "បួន", "ប្រាំ", "ប្រាំមួយ", "ប្រាំពីរ", "ប្រាំបី",

                  "ប្រាំបួន", "ដប់", "ដប់មួយ", "ដប់ពីរ", "ដប់បី", "ដប់បួន",

                  "ដប់ប្រាំ", "ដប់ប្រាំមួយ", "ដប់ប្រាំពីរ", "ដប់ប្រាំបី", "ដប់ប្រាំបួន",

                };
	
       

          String [] strtens = {

              "ដប់", "ម្ភៃ", "សាមសឺប", "សែសឺប", "ហាសឺប", "ហុកសឺប",

              "ចិតសឺប", "ពែតសឺប", "កៅសឺប", "មួយរយ"

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

             result[0] += "រយ";

          }

          if(tens > 0)

          {

             result[0] += strtens[tens - 1];

             result[0] += "";

          }

          if(single > 0)

          {

             result[0] += strones[single - 1];

             result[0] += "";

          }

          return true;

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

             System.out.println("សូន្យ");

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

              result[0] += "ពាន់";             

              HelperConvertNumberToText(temp, tempString);

              result[0] += tempString[0];

          }

          return true;

      }
    public static String[] convertToString(double value){
    	  BigDecimal bd = new BigDecimal(Double.toString(value));
    	  String [] rawSplit = bd.toPlainString().split("\\.");
    	  return rawSplit;
      }
  	public static String getDoubleToword(double value) {
		String afterPoint = "";
		String strWord = "";
		String[] strArray = new String[1];
		strArray = NumberToKhmerWords.convertToString(value);
		String[] result = new String[1];
		result[0] = "";
		String zero = "";
		if (!strArray[1].equals("0")) {
			if (NumberToKhmerWords.ConvertNumberToText(
					Integer.parseInt(strArray[1].substring(0, 2)), result)) {
				if (Integer.parseInt(strArray[1].substring(0, 1)) == 0) {
					zero = "សូន្យ";
				}
				afterPoint = "ក្បៀស​" + zero + result[0];
			}

		}
		if (NumberToKhmerWords.ConvertNumberToText(Integer.parseInt(strArray[0]),
				result)) {
			strWord = result[0] + afterPoint;
		}
		return strWord;
	}
}