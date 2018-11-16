package com.nokor.common.app.content.service.article;

import java.util.List;

import org.seuksa.frmk.service.MainEntityService;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.common.app.content.model.article.ArtAttachment;
import com.nokor.common.app.content.model.article.ArtDependency;
import com.nokor.common.app.content.model.article.Article;
import com.nokor.common.app.content.model.article.ArticleAware;
import com.nokor.common.app.content.model.base.ContentGroup;
import com.nokor.common.app.content.model.base.ContentType;
import com.nokor.common.app.content.model.eref.EArticleDependencyType;
import com.nokor.common.app.content.model.file.FileData;

/**
 * 
 * @author prasnar
 *
 */
public interface ArticleService extends MainEntityService {
	public  List<ArtDependency> getParents(long artId);

	void createDependency(Article art, Article other, EArticleDependencyType type, Integer sortIndex);

	void createDependency(Article art, Article other, EArticleDependencyType type);

	List<Article> listArticles(Long parentId) throws DaoException;
		
	List<Article> listArticlesByParentId(Long[] parentId) throws DaoException;
	
	List<Article> listArticlesByArticleId(Long[] artId) throws DaoException;
	
	List<Article> listArticlesByTypeId(Long typeId) throws DaoException;
	
	List<ArtAttachment> getAttachmentsByArticleId(Long articleId) throws DaoException;
	
	FileData getFileByAttId(Long attId) throws DaoException;

	void throwArticleAwareIntoRecycleBin(ArticleAware entity);
	
	void throwArticleIntoRecycleBin(Article article);
	
	void restoreArticleAwareFromRecycleBin(ArticleAware entity, Boolean isRestoreToActive);
	
	void restoreArticleFromRecycleBin(Article article, Boolean isRestoreToActive);

	List<ContentType> findContentTypeAndChildren(ContentType typArt) ;

	List<Long> findContentTypeAndChildrenIds(ContentType typArt) ;

	Article findArticleByTopicContentGroup(ContentGroup topic);
	
	List<ContentType> buildComboContentTypeTree(ContentType typArtRoot);
	
	void saveAttachment(Article article);
	
	List<ContentGroup> findTopicRecursiveAndChildren(ContentGroup group) throws DaoException;
	
	List<Long> findContentGroupRecursiveAndChildrenIds(ContentGroup group) throws DaoException;
	
	List<Long> findParentContentGroupRecursiveChildrenIds(ContentGroup group) throws DaoException;
	
	List<ContentGroup> buildComboContentGroupsTree(ContentGroup grpRoot);
	
	Boolean isVideoReadyInUse(Long videoId);
}
