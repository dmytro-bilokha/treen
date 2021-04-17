package com.dmytrobilokha.treen.notes;

import com.dmytrobilokha.treen.InternalApplicationException;
import com.dmytrobilokha.treen.OptimisticLockException;
import com.dmytrobilokha.treen.db.DbException;

import javax.annotation.CheckForNull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Transactional
public class NotebookService {

    private NoteNodeRepository noteNodeRepository;

    public NotebookService() {
        //CDI
    }

    @Inject
    public NotebookService(NoteNodeRepository noteNodeRepository) {
        this.noteNodeRepository = noteNodeRepository;
    }

    public List<NoteNodeDto> fetchUserNotebook(long userId) throws InternalApplicationException {
        List<NoteNodeEntity> userNotes;
        try {
            userNotes = noteNodeRepository.getUserNotes(userId);
        } catch (DbException e) {
            throw new InternalApplicationException("Failed to fetch user notes from the DB", e);
        }
        var nodeById = new HashMap<Long, NoteNodeDto>();
        var nodesByParentId = new HashMap<Long, List<NoteNodeDto>>();
        var rootNodes = new ArrayList<NoteNodeDto>();
        for (NoteNodeEntity nodeEntity : userNotes) {
            var nodeDto = new NoteNodeDto();
            nodeDto.setId(nodeEntity.getId());
            nodeDto.setTitle(nodeEntity.getTitle());
            nodeDto.setLink(nodeEntity.getLink());
            nodeDto.setDescription(nodeEntity.getDescription());
            nodeDto.setVersion(nodeEntity.getVersion());
            nodeById.put(nodeDto.getId(), nodeDto);
            var parentId = nodeEntity.getParentId();
            if (parentId == null) {
                rootNodes.add(nodeDto);
            } else {
                nodesByParentId.computeIfAbsent(parentId, (parent) -> new ArrayList<>()).add(nodeDto);
            }
        }
        for (Map.Entry<Long, List<NoteNodeDto>> entry : nodesByParentId.entrySet()) {
            var parentId = entry.getKey();
            var children = entry.getValue();
            Collections.sort(children);
            nodeById.get(parentId).setChildren(children);
        }
        return rootNodes;
    }

    public void updateNoteNode(NoteNodeEntity note) throws InternalApplicationException, OptimisticLockException {
        int count;
        try {
            count = noteNodeRepository.updateNoteNode(note);
        } catch (DbException e) {
            throw new InternalApplicationException("Failed to update note in the DB", e);
        }
        if (count == 0) {
            throw new OptimisticLockException("Unable to update, your note is outdated");
        }
    }

}
