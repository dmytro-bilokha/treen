define([
  'knockout',
  'appConstants',
  'appUtils'
],
  function (ko, AppConstants, AppUtils) {
    'use strict';

    class NotebookManager {

      buildNestedObservable(data) {
        //I want this method to work both for regular arrays and knockout observable arrays
        const dataArray = typeof data.map === 'function' ? data : data();
        return ko.observableArray(dataArray.map((node) => {
          const newNode = {
            id: node.id,
            parentId: node.parentId,
            title: node.title,
            link: node.link,
            description: node.description,
          };
          if (node.children) {
            newNode.children = this.buildNestedObservable(node.children);
          }
          return newNode;
        }));
      }

      findNodePlace(topObservableArray, id) {
        const arrayData = topObservableArray();
        for (let i = 0; i < arrayData.length; i += 1) {
          if (arrayData[i].id == id) {
            const target = {
              enclosingArray: topObservableArray,
              index: i,
            };
            return target;
          }
          else if (arrayData[i].children) {
            const target = this.findNodePlace(arrayData[i].children, id);
            if (target) {
              return target;
            }
          }
        }
      }

      init() {
        return $.ajax({
          url: `${AppConstants.CONTEXT_PATH}/api/notebook`,
        }).done((notebook) => {
          this.notebookVersion = notebook.version;
          this.notesTree(this.buildNestedObservable(notebook.notes)());
        });
      }

      getNodeSortingString(node) {
        if (AppUtils.isStringNotBlank(node.title)) {
          return node.title;
        }
        if (AppUtils.isStringNotBlank(node.link)) {
          return node.link;
        }
        if (AppUtils.isStringNotBlank(node.description)) {
          return node.description;
        }
        return '';
      }

      updateNote(note) {
        return $.ajax({
          url: `${AppConstants.CONTEXT_PATH}/api/notebook/note`,
          type: 'PUT',
          contentType: 'application/json',
          data: JSON.stringify({
            id: note.id,
            parentId: note.parentId,
            title: note.title,
            link: note.link,
            description: note.description,
            version: this.notebookVersion
          })
        }).done(() => {
          const targetLocation = this.findNodePlace(this.notesTree, note.id);
          const targetNode = targetLocation.enclosingArray()[targetLocation.index];
          const updatedNode = {
            id: targetNode.id,
            parentId: note.parentId,
            title: note.title,
            link: note.link,
            description: note.description,
          };
          if (targetNode.children) {
            updatedNode.children = this.buildNestedObservable(targetNode.children);
          }
          targetLocation.enclosingArray.splice(targetLocation.index, 1);
          this.insertNoteOrdered(targetLocation.enclosingArray, updatedNode);
          this.notebookVersion++;
        });
      }

      createNote(note) {
        return $.ajax({
          url: `${AppConstants.CONTEXT_PATH}/api/notebook/note`,
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({
            parentId: note.parentId,
            title: note.title,
            link: note.link,
            description: note.description,
            version: this.notebookVersion
          })
        }).done((noteResponse) => {
          const createdNote = {
            id: noteResponse.id,
            parentId: noteResponse.parentId,
            title: noteResponse.title,
            link: noteResponse.link,
            description: noteResponse.description,
          };
          let enclosingArray;
          if (createdNote.parentId === undefined || createdNote.parentId === null) {
            //Creating the root note
            enclosingArray = this.notesTree;
          } else {
            //Creating nested note
            const parentLocation = this.findNodePlace(this.notesTree, createdNote.parentId);
            const parentNote = parentLocation.enclosingArray()[parentLocation.index];
            if (!parentNote.children) {
              const updatedParent = {
                id: parentNote.id,
                parentId: parentNote.parentId,
                title: parentNote.title,
                link: parentNote.link,
                description: parentNote.description,
                children: ko.observableArray([createdNote])
              };
              this.replaceNote(parentLocation, updatedParent);
              this.notebookVersion++;
              return;
            }
            enclosingArray = parentNote.children;
          }
          this.insertNoteOrdered(enclosingArray, createdNote);
          this.notebookVersion++;
        });
      }

      replaceNote(targetLocation, updatedNode) {
        if (targetLocation.enclosingArray().length === 1) {
          //If the array has only one element, we don't simply replace it, but add new - remove old to avoid parent node to shrink
          targetLocation.enclosingArray.push(updatedNode);
          targetLocation.enclosingArray.shift();
        } else {
          targetLocation.enclosingArray.splice(targetLocation.index, 1, updatedNode);
        }
      }

      insertNoteOrdered(observableOrderedArray, note) {
        const innerArray = observableOrderedArray();
        if (innerArray.length === 0 || this.compareNotes(innerArray[innerArray.length - 1], note) <= 0) {
          observableOrderedArray.push(note);
          return;
        }
        if (this.compareNotes(innerArray[0], note) > 0) {
          observableOrderedArray.unshift(note);
          return;
        }
        let startIndex = 0;
        let endIndex = innerArray.length - 1;
        while (endIndex - startIndex > 1) {
          const midIndex = (startIndex + endIndex) >>> 1;
          const cmp = this.compareNotes(innerArray[midIndex], note);
          if (cmp === 0) {
            //This should never happen, because we don't insert the same note into array already having it
            observableOrderedArray.splice(midIndex, 0, note);
            return;
          }
          if (cmp > 0) {
            endIndex = midIndex;
          } else {
            startIndex = midIndex;
          }
        }
        observableOrderedArray.splice(endIndex, 0, note);
      }

      deleteNote(note) {
        return $.ajax({
          url: `${AppConstants.CONTEXT_PATH}/api/notebook/note`,
          type: 'DELETE',
          contentType: 'application/json',
          data: JSON.stringify({
            id: note.id,
            version: this.notebookVersion
          })
        }).done(() => {
          const targetLocation = this.findNodePlace(this.notesTree, note.id);
          if (targetLocation.enclosingArray().length === 1 && note.parentId) {
            //If we are removing the last child, we should set enclosing array to null for tree component to change the icon
            const parentLocation = this.findNodePlace(this.notesTree, note.parentId);
            const parentNote = parentLocation.enclosingArray()[parentLocation.index];
            const updatedParent = {
              id: parentNote.id,
              parentId: parentNote.parentId,
              title: parentNote.title,
              link: parentNote.link,
              description: parentNote.description,
            };
            this.replaceNote(parentLocation, updatedParent);
          } else {
            targetLocation.enclosingArray.splice(targetLocation.index, 1);
          }
          this.notebookVersion++;
        });
      }

      sortNotesArray(notesArray) {
        notesArray.sort(this.compareNotes);
      }

      compareNotes(left, right) {
        const leftString = this.getNodeSortingString(left);
        const rightString = this.getNodeSortingString(right);
        if (leftString < rightString) {
          return -1;
        }
        if (leftString > rightString) {
          return 1;
        }
        if (left.id < right.id) {
          return -1;
        }
        if (left.id > right.id) {
          return 1;
        }
        return 0;
      }

      constructor() {
        this.notesTree = ko.observableArray();
        this.notebookVersion = 0;
        this.init = this.init.bind(this);
        this.updateNote = this.updateNote.bind(this);
        this.createNote = this.createNote.bind(this);
        this.deleteNote = this.deleteNote.bind(this);
      }

    }

    return new NotebookManager();
  });