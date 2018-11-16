package com.nokor.common.app.content.service.doc;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.nokor.common.app.content.model.doc.DocContent;

/**
 * 
 * @author prasnar
 *
 */
public class DocumentDisplayComparator implements Comparator<DocContent> {
	private String searchedText;
	private static char SEP = ' ';

	/**
	 * 
	 * @param searchedText
	 */
	public DocumentDisplayComparator(String searchedText) {
		this.searchedText = searchedText;
	}

	@Override
	public int compare(DocContent doc1, DocContent doc2) {
		String trimText = "";
		if (StringUtils.isNotBlank(searchedText)) {
			trimText = searchedText.trim();
			if (trimText.charAt(0) == '"' && trimText.charAt(trimText.length() - 1) == '"') {
				trimText = trimText.substring(1, trimText.length() - 1);
			}
		}
				
		Integer count1 = countMatches(doc1.getTitleKh(), trimText) 
						+ countMatches(doc1.getTitleFr(), trimText)
						+ countMatches(doc1.getTitleEn(), trimText);
		Integer count2 = countMatches(doc2.getTitleKh(), trimText) 
						+ countMatches(doc2.getTitleFr(), trimText) 
						+ countMatches(doc2.getTitleEn(), trimText);

		Integer result = count2.compareTo(count1);
		if (result != 0) {
			return result;
		}
		
		count1 = 0;
		count2 = 0;
		count1 = countMatches(doc1.getKeywordsKh(), trimText) 
				+ countMatches(doc1.getKeywordsFr(), trimText) 
				+ countMatches(doc1.getKeywordsEn(), trimText);
		count2 = countMatches(doc2.getKeywordsKh(), trimText) 
				+ countMatches(doc2.getKeywordsFr(), trimText) 
				+ countMatches(doc2.getKeywordsEn(), trimText);

		result = count2.compareTo(count1);
		if (result != 0) {
			return result;
		}

		String[] words = StringUtils.split(trimText, SEP);
		count1 = 0;
		count2 = 0;
		for (String word : words) {
			if (StringUtils.isBlank(word)) {
				continue;
			}
			count1 += countMatches(doc1.getTitleKh(), word) 
					+ countMatches(doc1.getTitleFr(), word) 
					+ countMatches(doc1.getTitleEn(), word);
			count2 += countMatches(doc2.getTitleKh(), word) 
					+ countMatches(doc2.getTitleFr(), word) 
					+ countMatches(doc2.getTitleEn(), word);
		}
		
		result = count2.compareTo(count1);
		if (result != 0) {
			return result;
		}
		
		count1 = 0;
		count2 = 0;
		for (String word : words) {
			if (StringUtils.isBlank(word)) {
				continue;
			}
			count1 += countMatches(doc1.getKeywordsKh(), word) 
					+ countMatches(doc1.getKeywordsFr(), word) 
					+ countMatches(doc1.getKeywordsEn(), word);
			count2 += countMatches(doc2.getKeywordsKh(), word) 
					+ countMatches(doc2.getKeywordsFr(), word) 
					+ countMatches(doc2.getKeywordsEn(), word);
		}
		
		result = count2.compareTo(count1);
		if (result != 0) {
			return result;
		}

		return 0;
	}
	
	/**
	 * 
	 * @param str
	 * @param sub
	 * @return
	 */
	public int countMatches(String str, String sub) {
		if ((StringUtils.isEmpty(str)) || (StringUtils.isEmpty(sub))) {
			return 0;
		}
		
		str = str.toLowerCase();
		sub = sub.toLowerCase();
		
		int count = 0;
		int idx = 0;
		while ((idx = indexOf(str, sub, idx)) != -1) {
			++count;
			idx += sub.length();
		}
		return count;
	}
	
	/**
	 * 
	 * @param cs
	 * @param searchChar
	 * @param start
	 * @return
	 */
	private int indexOf(String cs, String searchChar, int start) {
		return cs.indexOf(searchChar, start);
	}
}
