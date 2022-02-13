package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.exceptions.NotUniqueEntityException;
import com.foxminded.university.model.Group;
import static com.foxminded.university.service.GroupServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupDao groupDao;
    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private GroupService groupService;

    @Test
    void givenGroup_whenCreate_thenCreate() {
        when(groupDao.getByName(group1.getName())).thenReturn(Optional.empty());

        groupService.create(group1);

        verify(groupDao).create(group1);
    }

    @Test
    void givenAlreadyExistGroup_whenCreate_thenNotCreate() {
        when(groupDao.getByName(group1.getName())).thenReturn(Optional.of(group2));
        Exception exception = assertThrows(NotUniqueEntityException.class, () -> groupService.create(group1));

        assertEquals("Name = 'group1' not unique", exception.getMessage());
    }

    @Test
    void givenCorrectId_whenGetById_thenGetById() {
        Group expected = group2;
        int id = expected.getId();
        when(groupDao.getById(id)).thenReturn(Optional.ofNullable(expected));

        Group actual = groupService.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    void givenTableGroups_whenGetAll_thenGetAll() {
        List<Group> expected = new ArrayList<>();
        expected.add(group2);
        when(groupDao.getAll()).thenReturn(expected);

        List<Group> actual = groupService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableGroups_whenGetAll_thenGetAllByPages() {
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        Page<Group> expected = new PageImpl<Group>(groups);
        when(groupDao.getAll(PageRequest.of(1, 1))).thenReturn(expected);

        assertEquals(expected, groupService.getAll(PageRequest.of(1, 1)));
    }

    @Test
    void givenGroup_whenUpdate_thenUpdate() {
        when(groupDao.getByName(group2.getName())).thenReturn(Optional.of(group2));

        groupService.update(group2);

        verify(groupDao).update(group2);
    }

    @Test
    void givenGroup_whenDelete_thenDelete() {
        groupService.delete(group2);

        verify(groupDao).delete(group2);
    }
  
    interface TestData{
        Group group1 = new Group.Builder()
                .name("group1")
                .build();
        
        Group group2 = new Group.Builder()
                .id(2)
                .name("group2")
                .build();
        
        Group group3 = new Group.Builder()
                .id(3)
                .name("group3")
                .build();
    }
}
