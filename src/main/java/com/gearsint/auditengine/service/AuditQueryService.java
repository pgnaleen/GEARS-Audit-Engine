package com.gearsint.auditengine.service;

import com.gearsint.auditengine.entity.CustomRevisionEntity;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gearsint.auditengine.entity.AllReportColumn;
import com.gearsint.auditengine.entity.ReportJoin;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Service
public class AuditQueryService {
    Logger log = LoggerFactory.getLogger(AuditQueryService.class);

    @Autowired
    EntityManager em;

    public String getAuditLogs(String tableId, Integer id, String timestamp){
        String response = "";
        try {
            switch (tableId){
                case "all_report_columns":{
                    AllReportColumn entity = new AllReportColumn();
                    if (id == null)
                        response = getAuditInfoFromDB(entity, "columnName");
                    else {
                        if (timestamp == null)
                            response = getAuditInfoFromDBGivenId(entity, id);
                        else
                            response = getAuditInfoFromDBGivenIdAndDate(entity, id, timestamp);
                    }
                }
                break;
                case "report_joins":{
                    ReportJoin entity = new ReportJoin();
                    if (id == null)
                        response = getAuditInfoFromDB(entity, "reportTableColumn");
                    else {
                        if (timestamp == null)
                            response = getAuditInfoFromDBGivenId(entity, id);
                        else
                            response = getAuditInfoFromDBGivenIdAndDate(entity, id, timestamp);
                    }
                }
                break;
            }
        } catch (Exception e) {
                log.error(e.getMessage(), e);
        }

        return response;
    }

    private <T> String getAuditInfoFromDB(T entity, String columnName){
        JSONObject jsonResponse = new JSONObject();
        JSONArray auditRowsJsonArray = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();

        AuditReader auditReader = AuditReaderFactory.get(em);
        // here selectEntitiesOnly false means it will give Revision info as well. ex. revision date
        AuditQuery q = auditReader.createQuery().forRevisionsOfEntity(entity.getClass()
                , false, true);
        q.add(AuditEntity.property(columnName).like("%", MatchMode.ANYWHERE));

        List revisions = q.getResultList();
        log.debug("Number of revisions fetched === " + revisions.size());

        revisions.forEach( auditRow -> {
            Object[] auditDetailsObjArrayEntry = (Object[]) auditRow;// as selectEntitiesOnly false few objets
            String json = "";

            try {
                json = objectMapper.writeValueAsString((T)auditDetailsObjArrayEntry[0]);//entity
                CustomRevisionEntity revisionEntity
                        = (CustomRevisionEntity) auditDetailsObjArrayEntry[1];//revision details. add username to this class

                // need to convert to JSONObject otherwise JSONArray.put consider this as string and add " and \"
                JSONObject temp = new JSONObject(json);
                temp.put("revision_date", revisionEntity.getRevisionDate());
                temp.put("user", revisionEntity.getUsername());
                auditRowsJsonArray.put(temp);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        jsonResponse.put("audit_entries", auditRowsJsonArray);
        return jsonResponse.toString();
    }

    private <T> String getAuditInfoFromDBGivenId(T entity, Integer id){
        JSONObject jsonResponse = new JSONObject();
        JSONArray auditRowsJsonArray = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();
        AuditReader auditReader = AuditReaderFactory.get(em);
        List<Number> revisionNumbers = auditReader.getRevisions(entity.getClass(), id);

        for (Number rev : revisionNumbers) {
            Object auditRow = auditReader.find(entity.getClass(), id, rev);
            String json = "";

            try {
                json = objectMapper.writeValueAsString((T)auditRow);
                // need to convert to JSONObject otherwise JSONArray.put consider this as string and add " and \"
                JSONObject temp = new JSONObject(json);
                temp.put("revision_date", auditReader.getRevisionDate(rev));

                // find audit user and put in the response. find table entity, audit entity using revision number
                AuditQuery q = auditReader.createQuery().forRevisionsOfEntity(entity.getClass(), false, true);
                q.add(AuditEntity.property("id").eq(id));// id should be used on all the entities as id field

                List revisions = q.getResultList();
                log.debug("Number of revisions fetched === " + revisions.size());

//                // get the audit revision entity, get user from it and stamp into json response
                revisions.forEach( entityArray -> {
                    Object[] auditDetailsObjArrayEntry = (Object[]) entityArray;// as selectEntitiesOnly false few objets
                    try {
                        CustomRevisionEntity revisionEntity
                                = (CustomRevisionEntity) auditDetailsObjArrayEntry[1];//revision details. add username to this class
                        if (revisionEntity.getId() == rev.intValue())
                            temp.put("user", revisionEntity.getUsername());

                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                auditRowsJsonArray.put(temp);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            log.info("Revision Date: " + auditReader.getRevisionDate(rev) + " Revision: " + json);
        }

        jsonResponse.put("audit_entries", auditRowsJsonArray);
        return jsonResponse.toString();
    }

    private <T> String getAuditInfoFromDBGivenIdAndDate(T entity, Integer id, String timeStamp){
        JSONObject jsonResponse = new JSONObject();
        JSONArray auditRowsJsonArray = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();
        AuditReader auditReader = AuditReaderFactory.get(em);
        Date requiredDate = new Date(Long.parseLong(timeStamp));
        log.info(requiredDate.toString());

        try {
            T auditRow = (T)auditReader.find(entity.getClass(), id, requiredDate);
            String jsonOfAuditRow = objectMapper.writeValueAsString(auditRow);
            JSONObject temp = new JSONObject(jsonOfAuditRow);
            temp.put("revision_date", requiredDate.toString());
            auditRowsJsonArray.put(temp);
        } catch (JsonProcessingException jpex) {
            jpex.printStackTrace();
        } catch (RevisionDoesNotExistException rneex) {
            JSONObject temp = new JSONObject();
            temp.put("revision_date", requiredDate.toString());
            temp.put("revision_status", "no revision");
            auditRowsJsonArray.put(temp);

            rneex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        jsonResponse.put("audit_entries", auditRowsJsonArray);
        return jsonResponse.toString();
    }
}