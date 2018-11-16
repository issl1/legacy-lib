/**
 * 
 */
package com.nokor.common.app.content.service.article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EntityStatusRecordAware;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service("articleService")
public class ArticleServiceImpl extends MainEntityServiceImpl implements ArticleService {
    /** */
	private static final long serialVersionUID = 1598992537218798205L;
	
	@Autowired
    private EntityDao dao;

    @Override
    public BaseEntityDao getDao() {
        return this.dao;
    } 
    
    @Override
    public void createProcess(MainEntity mainEntity)  {
    	Article art = (Article) mainEntity;

    	if (art.getStatusRecord() == null) {
    		processStatusRecord(art);
    	}
		getDao().create(art);
		
    	if (art.getAttachments() != null) {
	    	for (ArtAttachment att : art.getAttachments()) {
	    		att.setArticle(art);
	    		if (att.getFile() != null && att.getFile().getId() == null) {
	    			getDao().create(att.getFile());
	    		}
			}
    	}
    	saveOnAction(art.getAttachments());
	}
    
    @Override
    public void updateProcess(MainEntity mainEntity)  {
    	Article art = (Article) mainEntity;

    	if (art.getAttachments() != null) {
	    	for (ArtAttachment att : art.getAttachments()) {
				att.setArticle(art);
				if (att.getFile() != null && att.getFile().getId() == null) {
		    		getDao().saveOrUpdate(att.getFile());
				}
			}
    	}
    	saveOnAction(art.getAttachments());
		getDao().merge(art);
	}
    
    @Override
   	public void deleteProcess(MainEntity mainEntity)  {
    	Article art = (Article) mainEntity;

		getDao().delete(art);
		getDao().deleteList(art.getAttachments());
	}
    
    @Override
	public void throwArticleAwareIntoRecycleBin(ArticleAware entity) {
		if (entity instanceof Article) {
			throwArticleIntoRecycleBin((Article) entity);
		} else {
			throwIntoRecycledBin((EntityStatusRecordAware) entity);
			if (entity instanceof ArticleAware) {
				if (((ArticleAware) entity).getArticle() != null) {
					throwArticleIntoRecycleBin(((ArticleAware) entity).getArticle());
				}
			}
		}
	}
	
	@Override
	public void throwArticleIntoRecycleBin(Article article) {
		if (article != null) {
			throwIntoRecycledBin(article);
			// Attachment
			List<ArtAttachment> ArtAttachments = article.getAttachments();
			if (ArtAttachments != null && ArtAttachments.size() > 0) {
				for (ArtAttachment ArtAttachment : ArtAttachments) {
					if (ArtAttachment != null) {
						if (ArtAttachment.getFile() != null) {
							throwIntoRecycledBin(ArtAttachment.getFile());
						}
						
						throwIntoRecycledBin(ArtAttachment);
					}
				}
			}
		}
	}

	@Override
	public void restoreArticleAwareFromRecycleBin(ArticleAware entity, Boolean isRestoreToActive) {
		if (entity instanceof Article) {
			if (isRestoreToActive) {
				restoreFromRecycledBin((Article) entity);			
			} else {
				restoreFromRecycledBin((Article) entity);
			}
		} else {
			if (isRestoreToActive) {
				restoreFromRecycledBin((EntityStatusRecordAware) entity);			
			} else {
				restoreFromRecycledBin((EntityStatusRecordAware) entity);
			}
			if (entity instanceof ArticleAware) { 
				if (((ArticleAware) entity).getArticle() != null) {
					restoreArticleFromRecycleBin(((ArticleAware) entity).getArticle(), isRestoreToActive);
				}
			}
		}
	}
	
	@Override
	public void restoreArticleFromRecycleBin(Article article, Boolean isRestoreToActive) {
		if (article != null) {
			if (isRestoreToActive) {
				restoreFromRecycledBin(article);			
			} else {
				restoreFromRecycledBin(article);
			}
			// Attachment
			List<ArtAttachment> ArtAttachments = article.getAttachments();
			if (ArtAttachments != null && ArtAttachments.size() > 0) {
				for (ArtAttachment ArtAttachment : ArtAttachments) {
					if (ArtAttachment != null) {
						if (ArtAttachment.getFile() != null) {
							if (isRestoreToActive) {
								restoreFromRecycledBin(ArtAttachment.getFile());			
							} else {
								restoreFromRecycledBin(ArtAttachment.getFile());
							}
						}
						
						
						if (isRestoreToActive) {
							restoreFromRecycledBin(ArtAttachment);			
						} else {
							restoreFromRecycledBin(ArtAttachment);
						}
					}
				}
			}
		}
	}


	@Override
	public List<Article> listArticles(Long parentId) throws DaoException {
        final Criteria criteria = createCriteria(Article.class);
        criteria.add(Restrictions.eq("parentId", parentId));
        return criteria.list();
	}

	@Override
	public List<ArtAttachment> getAttachmentsByArticleId(Long articleId) throws DaoException {
		 final Criteria criteria = createCriteria(ArtAttachment.class);
	        criteria.add(Restrictions.eq("articleParent.id", articleId));
	        return criteria.list();
	}
	
	@Override
	public FileData getFileByAttId(Long attId) throws DaoException {
		final Criteria criteria = createCriteria(FileData.class);
        criteria.add(Restrictions.eq("attId", attId));
        return (FileData) criteria.list().get(0);
	}
	
	@Override
	public List<Article> listArticlesByParentId(Long[] parentId) throws DaoException {
		final Criteria criteria = createCriteria(Article.class);
        criteria.add(Restrictions.in("parentId", parentId));
        return criteria.list();
	}

	@Override
	public List<Article> listArticlesByArticleId(Long[] artId) throws DaoException {
		final Criteria criteria = createCriteria(Article.class);
        criteria.add(Restrictions.in("article.id", artId));
        return criteria.list();
	}

	@Override
	public List<Article> listArticlesByTypeId(Long typeId) throws DaoException {
		final Criteria criteria = createCriteria(Article.class);
        criteria.add(Restrictions.eq("ContentType.id", typeId));
        return criteria.list();
	}
	
	@Override
    public List<ContentType> findContentTypeAndChildren(ContentType typArt)  {
    	try {
	    	List<Long> idList = findContentTypeAndChildrenIds(typArt);
	    	if (idList.size() > 0) {
	    		return getDao().listByIds(ContentType.class, idList);
	    	}
    	} catch (Exception e) {
    		String errMsg = "Error has occured in findTopicAndChildren(" + (typArt != null ? typArt.getId() : "<empty id>" + ")");
    		logger.error(errMsg, e);
    	}
		return new ArrayList<ContentType>();
    }
    	
    @Override
    public List<Long> findContentTypeAndChildrenIds(ContentType typArt)  {
    	List<Long> idList = new ArrayList<Long>();
        idList.add(typArt.getId());
        logger.debug("==[" + typArt.getCode() + "] [" + typArt.getDesc() + "]");

        getSubContentTypeIds(idList, typArt.getChildren(), 1);
    
    	return idList;
    }
    
    /**
	 * 
	 * @param children
	 * @param level
	 */
	private void getSubContentTypeIds(List<Long> idList, List<ContentType> children, int level) {
        for (ContentType typArt : children) {
            String prefixSpacing = "";
            for (int i = 0; i < level; i++) {
            	prefixSpacing += "______";
            }
            idList.add(typArt.getId());
            logger.debug(prefixSpacing + "> [" + typArt.getCode() + "] [" + typArt.getDesc() + "]");
            getSubContentTypeIds(idList, typArt.getChildren(), level + 1);
        }
	}
	
	/**
	 * 
	 * @param children
	 * @param level
	 */
	private void getSubTopicIds(List<Long> idList, List<ContentGroup> children, int level) {
		for (ContentGroup topic : children) {
			String prefixSpacing = "";
			for (int i = 0; i < level; i++) {
				prefixSpacing += "______";
			}
			idList.add(topic.getId());
			logger.debug(prefixSpacing + "> [" + topic.getCode() + "] ["
					+ topic.getDesc() + "]");
			getSubTopicIds(idList, topic.getChildren(), level + 1);
		}
	}
	
	/**
     * 
     */
	@Override
    public List<ContentType> buildComboContentTypeTree(ContentType typArtRoot) {
    	List<ContentType> cbTypArt = new ArrayList<ContentType>();
        ContentType typ = new ContentType();
        typ.setId(typArtRoot.getId());
        typ.setDesc(typArtRoot.getDesc());
        cbTypArt.add(typ);
        buildComboSubContentTypeTree(cbTypArt, typArtRoot.getChildren(), 1);
        return cbTypArt;
	}
    
    /**
	 * 
	 * @param children
	 * @param level
	 */
	private void buildComboSubContentTypeTree(List<ContentType> cbContentGroups, List<ContentType> children, int level) {
        for (ContentType type : children) {
            String prefixSpacing = "";
            for (int i = 0; i < level; i++) {
            	prefixSpacing += "______";
            }
            ContentType typ = new ContentType();
            typ.setId(type.getId());
            typ.setDesc(prefixSpacing + type.getDesc());
            cbContentGroups.add(typ);
            buildComboSubContentTypeTree(cbContentGroups, type.getChildren(), level + 1);
        }
	}
	
    
    
    

    
    @Override
    public void saveAttachment(Article article) {
    	saveOnAction(article.getAttachments());
    }

	@Override
	public boolean checkBeforeActive(EntityStatusRecordAware entity) {
		return true;
	}


	@Override
	public List<ContentGroup> findTopicRecursiveAndChildren(ContentGroup group) throws DaoException {
		try {
			List<Long> idList = findContentGroupRecursiveAndChildrenIds(group);
			if (idList.size() > 0) {
				return getDao().listByIds(ContentGroup.class, idList);
			}
		} catch (Exception e) {
			String errMsg = "Error has occured in findTopicAndChildren("
					+ (group != null ? group.getId() : "<empty id>" + ")");
			logger.error(errMsg, e);
		}
		return new ArrayList<ContentGroup>();
	}

	@Override
	public List<Long> findContentGroupRecursiveAndChildrenIds(ContentGroup group) throws DaoException {
		List<Long> idList = new ArrayList<Long>();
		idList.add(group.getId());
		logger.debug("==[" + group.getCode() + "] [" + group.getDesc() + "]");

		getSubTopicIds(idList, group.getChildren(), 1);

		return idList;
	}

	@Override
	public List<Long> findParentContentGroupRecursiveChildrenIds(ContentGroup group) throws DaoException {
		List<Long> idList = new ArrayList<Long>();
		logger.debug("==[" + group.getCode() + "] [" + group.getDesc() + "]");

		getSubTopicIds(idList, group.getChildren(), 1);

		return idList;
	}
	
	/**
     * 
     */
	@Override
	public List<ContentGroup> buildComboContentGroupsTree(ContentGroup grpRoot) {
		List<ContentGroup> cbTopics = new ArrayList<ContentGroup>();
		ContentGroup group = new ContentGroup();
		group.setId(grpRoot.getId());
		group.setDesc(grpRoot.getDesc());
		cbTopics.add(group);
		buildComboSubContentGroupsTree(cbTopics, grpRoot.getChildren(), 1);
		return cbTopics;
	}

	/**
	 * 
	 * @param children
	 * @param level
	 */
	private void buildComboSubContentGroupsTree(List<ContentGroup> cbGroups, List<ContentGroup> children, int level) {
		for (ContentGroup grp : children) {
			String prefixSpacing = "";
			for (int i = 0; i < level; i++) {
				prefixSpacing += "______";
			}
			ContentGroup group = new ContentGroup();
			group.setId(grp.getId());
			group.setDesc(prefixSpacing + grp.getDesc());
			cbGroups.add(group);
			buildComboSubContentGroupsTree(cbGroups, grp.getChildren(), level + 1);
		}
	}

	



	@Override
	public Article findArticleByTopicContentGroup(ContentGroup grp) {
		logger.debug("findArticleByTopicArticle");

		if (grp != null) {
			ArticleRestriction restriction = new ArticleRestriction();
			restriction.setTopArtId(grp.getId());
			List<Article> lstArt = list(restriction);
			if (lstArt != null && lstArt.size() > 0) {
				return lstArt.get(0);
			}
		}
		return null;
	}


	@Override
	public Boolean isVideoReadyInUse(Long videoId) {
		
		final Criteria criteria = createCriteria(ArtAttachment.class);
	    criteria.createAlias("video", "vid", JoinType.INNER_JOIN);
	    criteria.add(Restrictions.eq("vid.id", videoId));
	    
	    List<ArtAttachment> listAtts = criteria.list();
	    
	    return listAtts != null && listAtts.size() > 0 ? true : false;
	}

	@Override
	public List<ArtDependency> getParents(long artId) {
		BaseRestrictions<ArtDependency> restriction = new BaseRestrictions<>(ArtDependency.class);
		restriction.addCriterion(Restrictions.eq("other.id", artId));
		List<ArtDependency> lst = dao.list(restriction);
		return lst;
	}


	@Override
	public void createDependency(Article art, Article other, EArticleDependencyType type) {
		createDependency(art, other, type, 1);
	}
	
	@Override
	public void createDependency(Article art, Article other, EArticleDependencyType type, Integer sortIndex) {
		ArtDependency dep = new ArtDependency();
		dep.setDependencyDate(new Date());
		dep.setSortIndex(sortIndex);

		dep.setType(type.getEntity());

		dep.setArticle(art);
		dep.setOther(other);
		dao.create(dep);
	
	}	
}
