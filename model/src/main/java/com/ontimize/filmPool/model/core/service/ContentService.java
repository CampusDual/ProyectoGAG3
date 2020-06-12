package com.ontimize.filmPool.model.core.service;

import com.ontimize.db.EntityResult;
import com.ontimize.filmPool.api.core.service.IContentService;
import com.ontimize.filmPool.model.core.dao.ContentDao;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ContentService")
@Lazy

public class ContentService implements IContentService {

    @Autowired
    private ContentDao contentDao;
    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;
    private ContentService contentService;

    public EntityResult contentQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.contentDao, keyMap, attrList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntityResult contentInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        Map<String, Object> nonContentData = removeNonRelatedData(attrMap, ContentDao.CONTENT_NAME,
                ContentDao.CONTENT_NACIONALITY, ContentDao.CONTENT_POSTER_PATH, ContentDao.CONTENT_PLOT,
                ContentDao.CONTENT_DURATION, ContentDao.CONTENT_RELEASE_DATE);
        this.insertNonRelatedData(nonContentData);
        attrMap.putAll(nonContentData);
        return this.daoHelper.insert(this.contentDao, attrMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntityResult contentUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<String, Object> nonContentData = removeNonRelatedData(attrMap, ContentDao.CONTENT_NAME,
                ContentDao.CONTENT_NACIONALITY, ContentDao.CONTENT_POSTER_PATH, ContentDao.CONTENT_PLOT,
                ContentDao.CONTENT_DURATION, ContentDao.CONTENT_RELEASE_DATE);
        this.insertNonRelatedData(nonContentData);
        attrMap.putAll(nonContentData);
        return this.daoHelper.update(this.contentDao, attrMap, keyMap);
    }

    @Override
    public EntityResult contentDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.delete(this.contentDao, keyMap);
    }

    public EntityResult latestMovies(List<String> columns)
            throws OntimizeJEERuntimeException {
        Map<String, Object> keyMap = new HashMap<String, Object>();
        return this.daoHelper.query(this.contentDao, keyMap, columns, "newMovies");
    }

    public EntityResult latestShows(List<String> columns)
            throws OntimizeJEERuntimeException {
        Map<String, Object> keyMap = new HashMap<String, Object>();
        return this.daoHelper.query(this.contentDao, keyMap, columns, "newShows");
    }


    private Map<String, Object> insertNonRelatedData(Map<String, Object> nonContentData) throws OntimizeJEERuntimeException {
        //TODO implementar esta funcionalidad
        return nonContentData;
    }

    private Map<String, Object> removeNonRelatedData(Map<String, Object> attrMap, String... attrToExclude) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        for (String attr : attrToExclude) {
            if (attrMap.containsKey(attr) && attrMap.get(attr) instanceof String) {
                data.put(attr, attrMap.remove(attr));
            }
        }
        return data;
    }

}