package com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

import java.util.List;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.exceptions.EntityNotFoundException;
import com.foxminded.university.exceptions.NotUniqueEntityException;
import com.foxminded.university.model.Group;

@Service
public class GroupService {

    private static final Logger log = LoggerFactory.getLogger(GroupService.class);
    private GroupDao groupDao;

    public GroupService(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public void create(Group group) {
        log.debug("Checking group with name - '{}' for uniqueness before creating", group.getName());
        verifyUniqueness(group);
        groupDao.create(group);
    }

    public Group getById(int id) {
        log.debug("Retrieving group by id - '{}'", id);
        return groupDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Can't retrieve group by id - '%d'", id)));
    }
    
    public List<Group> getAll() {
        return groupDao.getAll();
    }

    public Page<Group> getAll(Pageable page) {
        log.debug("Retrieving all groups");
        return groupDao.getAll(page);
    }
    

    public void update(Group group) {
        log.debug("Checking group with id - '{}' for uniqueness before creating", group.getId());
        verifyUniqueness(group);
        groupDao.update(group);
    }

    public void delete(Group group) {
        groupDao.delete(group);
    }

    private void verifyUniqueness(Group group) {
        if (groupDao.getByName(group.getName())
                .filter(currentGroup -> currentGroup.getId() != group.getId())
                .isPresent()) {
            throw new NotUniqueEntityException(format("Name = '%s' not unique", group.getName()));
        }
    }
}
