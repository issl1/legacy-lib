package com.nokor.common.app.content.service.text;

import java.util.List;

import org.seuksa.frmk.service.MainEntityService;

import com.nokor.common.app.content.model.doc.Text;
import com.nokor.common.app.content.model.doc.TextDependency;
import com.nokor.common.app.content.model.eref.ETextDependencyType;

/**
 * 
 * @author prasnar
 * 
 */
public interface TextService extends MainEntityService {
	
	public  List<TextDependency> getParents(long texId);

	void createDependency(Text text, Text other, ETextDependencyType type, Integer sortIndex);

	void createDependency(Text text, Text other, ETextDependencyType type);
}
