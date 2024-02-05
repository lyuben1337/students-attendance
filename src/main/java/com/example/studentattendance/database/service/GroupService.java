package com.example.studentattendance.database.service;

import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing group-related operations.
 */
@Service
@AllArgsConstructor
public class GroupService {
    private GroupRepository groupRepository;

    /**
     * Retrieves a list of all groups.
     *
     * @return A list of Group entities representing all groups.
     */
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    /**
     * Deletes the specified group from the repository.
     *
     * @param group The group to be deleted.
     * @throws IllegalArgumentException if the provided group is null.
     */
    public void deleteGroup(Group group) {
        groupRepository.delete(group);
    }

    /**
     * Adds a new group to the repository if a group with the same name doesn't already exist.
     *
     * @param group The group to be added.
     * @throws RuntimeException if a group with the same name already exists.
     * @throws IllegalArgumentException if the provided group is null.
     */
    public void addGroup(Group group) {
        if (groupRepository.existsByName(group.getName())) {
            throw new RuntimeException(String.format("Група %s вже існує", group.getName()));
        }
        groupRepository.save(group);
    }
}
