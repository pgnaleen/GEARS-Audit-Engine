package com.gearsint.auditengine.config;

import com.gearsint.auditengine.entity.CustomRevisionEntity;
import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Configurable
public class CustomRevisionListener implements RevisionListener {


    public void newRevision(Object revisionEntity){

        CustomRevisionEntity revision = (CustomRevisionEntity) revisionEntity;
        String username = "user1";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        revision.setUsername(username);
    }
}
